//
//  SameGlobeGameView.m
//  SameGlobe
//
//  Created by Akemi on 03/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "SameGlobeGameView.h"
#import "SameGlobeGlobVars.h"
#import "Block.h"
#import "BlockArray.h"
#import <AudioToolbox/AudioServices.h>
#import "SameGlobeAppDelegate.h"
#import "SameGlobeScoreViewController.h"
#import "SameGlobeGameViewController.h"

@implementation SameGlobeGameView

    SameGlobeGlobVars *glob;                    //signleton pattern vars
    BlockArray *blocks;                         //block array
    int blockcount;								//current number of blocks
    int touchCorX = -1;							//touched point x coordinate
    int touchCorY = -1;                         //touched point y coordinate
    int score = 0;                              //score count
    SameGlobeGameViewController *gameViewCont;  //our view controller	

//init method called by programatically creation
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
    }
    return self;
}

//init method called by NIB
- (id)initWithCoder:(NSCoder *) coder {
    
    if(self = [super initWithCoder:coder]) {
        // Initialization code
        NSLog(@"Game View Init");
        glob = [SameGlobeGlobVars sharedManager];
        
        //Frame Size
        float width = [[UIScreen mainScreen] applicationFrame].size.height;
        float height = [[UIScreen mainScreen] applicationFrame].size.width;
        NSLog(@"My view's frame is: %f - %f", width, height);
        
        //set initial variables for the singleton pattern
        //VERY IMPORTANT, SEE DESCRIPTION IN GlobVars SINGLETON PATTER
        if(width > height) [glob setPixelScaleFac:((double)width/(double)568)];     //check right scaling
        else [glob setPixelScaleFac:((double)height/(double)568)];
        
        [glob setScreenDimX:width];                                                 //canvas size x
        [glob setScreenDimY:height];                                                //canvas size y
        
        
        //dimension of blocks, depends on difficulty and screen size
        if([glob ScreenDimX]/([glob RES_X_BLOCKS]*[glob Difficulty]) <= ([glob ScreenDimY]-[glob scoreBarHeight])/([glob RES_Y_BLOCKS]*[glob Difficulty])){
            [glob setBlockDim:floor([glob ScreenDimX]/([glob RES_X_BLOCKS]*[glob Difficulty]))];
        }
        else {
            [glob setBlockDim:floor(([glob ScreenDimY]-[glob scoreBarHeight])/([glob RES_Y_BLOCKS]*[glob Difficulty]))];
        }
        
        //set the offset for centered display
        [glob setScreenOffsetX:([glob ScreenDimX] - [glob BlockDim]*[glob Difficulty]*[glob RES_X_BLOCKS])/2];
        [glob setScreenOffsetY:([glob ScreenDimY] - [glob scoreBarHeight] - [glob BlockDim]*[glob Difficulty]*[glob RES_Y_BLOCKS])];
        
        //sets background
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            [glob setBackground:[UIImage imageNamed:@"background_ipad.jpg"]];
        }
        else {
            [glob setBackground:[UIImage imageNamed:@"background.jpg"]];
        }

        //Init blocks of not already init
        if(blocks == nil){
            //create block array with right dimension
            blocks = [[BlockArray alloc] initWithX:[glob RES_X_BLOCKS]*[glob Difficulty] andY:[glob RES_Y_BLOCKS]*[glob Difficulty]];
            
            //block count for score calculation
            blockcount = [blocks getXCount]*[blocks getYCount];
        }
    }
    
    return self;
}

- (void)encodeWithCoder:(NSCoder *)enCoder {
    [super encodeWithCoder:enCoder];
}

//function to make controller know tow view
- (void)initController:(SameGlobeGameViewController *)cont{
    gameViewCont = cont;
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    //get context
    CGContextRef  context = UIGraphicsGetCurrentContext();
    
    //calculate right dimesnion for background image
    float imageAspect = [[glob background] size].width/[[glob background] size].height;
    float screenAspect = (float)[glob ScreenDimX]/(float)[glob ScreenDimY];
    
    if (imageAspect <= screenAspect) {
        [[glob background] drawInRect:CGRectMake(0,([glob ScreenDimY]-(float)[glob ScreenDimX]/imageAspect)/2,[glob ScreenDimX], (float)[glob ScreenDimX]/imageAspect)];
    }
    else{
        [[glob background] drawInRect:CGRectMake([glob ScreenDimX]-(float)[glob ScreenDimY]*imageAspect,0,(float)[glob ScreenDimY]*imageAspect, [glob ScreenDimY])];
    }
    
    //draw the blocks
    for(int i=0; i < [blocks getYCount]; i++){
        for(int j=0; j < [blocks getXCount]; j++){
            if([[blocks getBlockAtX:j andY:i] accessible] == YES) {
                UIImage *image = [[blocks getBlockAtX:j andY:i] bitmap];
                [image drawInRect:CGRectMake(j*[glob BlockDim]+[glob ScreenOffsetX],i*[glob BlockDim]+[glob getRealOffsetY],[glob BlockDim],[glob BlockDim])];
            }
        }
    }
    
    //drawUI
    CGContextSelectFont(context, "Arial", [glob scoreBarFontSize], kCGEncodingMacRoman);
    CGContextSetTextMatrix(context, CGAffineTransformMake(1.0,0.0, 0.0, -1.0, 0.0, 0.0));
    const char *buffer = [[NSString stringWithFormat:@"%d", score] UTF8String];
    CGSize size = [[NSString stringWithFormat:@"%d", score] sizeWithFont:[UIFont fontWithName:@"Arial" size:[glob scoreBarFontSize]]];
    CGContextShowTextAtPoint(context, [glob ScreenDimX]-10-size.width, [glob scoreBarFontSize], buffer, strlen(buffer));
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    //location of touch
    UITouch *touch = [[event allTouches] anyObject];
    CGPoint location = [touch locationInView: touch.view];;
    
    //location.x is actually my y location, calculates touch array index
    touchCorX = (int)floor((location.x-[glob ScreenOffsetX])/[glob BlockDim]);
    touchCorY = (int)floor((location.y-[glob getRealOffsetY])/[glob BlockDim]);

    NSLog(@"Touch start at: %d - %d", touchCorX, touchCorY);
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    //location of touch
    UITouch *touch = [[event allTouches] anyObject];
    CGPoint location = [touch locationInView: touch.view];
    
    //log where touch ended
    NSLog(@"Touch end at: %i - %i", (int)floor((location.x-[glob ScreenOffsetX])/[glob BlockDim]), (int)floor((location.y-[glob getRealOffsetY])/[glob BlockDim]));
    
    //touch in bounds, block existent
    if(touchCorX >= 0 && touchCorX < [glob RES_X_BLOCKS]*[glob Difficulty] && touchCorY >= 0 && touchCorY < [glob RES_Y_BLOCKS]*[glob Difficulty] &&
        [[blocks getBlockAtX:touchCorX andY:touchCorY] accessible] == YES){
        //finger same place before and after
        if(touchCorX == (int)floor((location.x-[glob ScreenOffsetX])/[glob BlockDim]) &&
           touchCorY == (int)floor((location.y-[glob getRealOffsetY])/[glob BlockDim])){
            //check the block, if hit vibrate
            if([self checkBlockAtX:touchCorX andY:touchCorY withColor:[[blocks getBlockAtX:touchCorX andY:touchCorY] Colour] inDirection:[glob DIRECTION_ALL]] == YES){
                [self vibrate];
            }
            
            //counts destroyed blocks and calculates points
            NSLog(@"Points earned: %i", [self setNewScore]);
            
            //fill empty spaces
            [self shrinkBlocksDownwards];
            [self shrinkBlocksSidewards];
            
            //render screen
            [self setNeedsDisplay];
            
            //are there still moves left
            //end of the game
            NSLog([self checkMovesLeft] ? @"Still Moves Left" : @"No Moves Left");
            
            if([self checkMovesLeft] == NO){
                blocks = nil;
                [gameViewCont changeViewControllerWithScore:score];
                score = 0;
            }
        }
    }
}

//fills blank spaces with remaining blocks downwards
- (void)shrinkBlocksDownwards{
    
    for(int i=0; i < [blocks getXCount]; i++){				//x direction from left
        for(int j=[blocks getYCount]-1; j >= 0 ; j--){      //y direction from bottom            
            int count = 0;
            while([[blocks getBlockAtX:i andY:j] accessible] == NO && count <= j){	//move blocks while null and not more moves then blocks
                for(int k=j; k > 0; k--){
                    //swap block and the one above
                    [blocks exchangeObjectAtX:i andY:k withObjectAtX:i andY:k-1];
                }
                count++;
            }
        }
    }
}

//fills blank spaces with remaining blocks sidewards
- (void)shrinkBlocksSidewards{
    for(int i=0; i < [blocks getXCount]-1; i++){					//x direction from left
        int count = 0;
        while([[blocks getBlockAtX:i andY:[blocks getYCount]-1] accessible] == NO && count < [blocks getXCount]){	//move blocks while null and not more moves then blocks
            for(int k=i; k < [blocks getXCount]-1; k++){			//x direction from left, beginning from first loop
                for(int j=0; j < [blocks getYCount]; j++){          //y direction from top
                    //swap block column and the next one
                    [blocks exchangeObjectAtX:k andY:j withObjectAtX:k+1 andY:j];
                }
            }
            count++;
        }
    }
}

//check the surrounding blocks, recursive function
- (BOOL) checkBlockAtX:(int)x andY:(int)y withColor:(int)colour inDirection:(int)direction{
    BOOL hit = NO;		//flag when colours are the same, hit flag
    
    //check for direction, check if in level bounds, check if block still exists, check if new block has same colour as old
    //check to the right
    if(direction != [glob DIRECTION_LEFT] && x+1 < [glob RES_X_BLOCKS]*[glob Difficulty] && [[blocks getBlockAtX:x+1 andY:y] accessible] == YES && [[blocks getBlockAtX:x+1 andY:y] Colour] == colour){
        [self checkBlockAtX:x+1 andY:y withColor:[[blocks getBlockAtX:x+1 andY:y] Colour] inDirection:[glob DIRECTION_RIGHT]];
        hit = YES;
    }
    //check to the bottom
    if(direction != [glob DIRECTION_UP] && y+1 < [glob RES_Y_BLOCKS]*[glob Difficulty] && [[blocks getBlockAtX:x andY:y+1] accessible] == YES && [[blocks getBlockAtX:x andY:y+1] Colour] == colour){
        [self checkBlockAtX:x andY:y+1 withColor:[[blocks getBlockAtX:x andY:y+1] Colour] inDirection:[glob DIRECTION_DOWN]];
        hit = YES;
    }
    //check to the left
    if(direction != [glob DIRECTION_RIGHT] && x-1 >= 0 && [[blocks getBlockAtX:x-1 andY:y] accessible] == YES && [[blocks getBlockAtX:x-1 andY:y] Colour] == colour){
        [self checkBlockAtX:x-1 andY:y withColor:[[blocks getBlockAtX:x-1 andY:y] Colour] inDirection:[glob DIRECTION_LEFT]];
        hit = YES;
    }
    //check to the top
    if(direction != [glob DIRECTION_DOWN] && y-1 >= 0 && [[blocks getBlockAtX:x andY:y-1] accessible] == YES && [[blocks getBlockAtX:x andY:y-1] Colour] == colour){
        [[blocks getBlockAtX:x andY:y] setAccessible:NO];
        [self checkBlockAtX:x andY:y-1 withColor:[[blocks getBlockAtX:x andY:y-1] Colour] inDirection:[glob DIRECTION_UP]];
        hit = YES;
    }
    //if something was hit
    if(direction != [glob DIRECTION_ALL ] || hit == YES){
        [[blocks getBlockAtX:x andY:y] setAccessible:NO];
    }
    return hit;
}

//vibration when blocks hit
- (void) vibrate{
    NSMutableDictionary* dict = [NSMutableDictionary dictionary];
    NSMutableArray* arr = [NSMutableArray array ];
    
    //vibration pattern
    [arr addObject:[NSNumber numberWithBool:YES]];
    [arr addObject:[NSNumber numberWithInt:200]];

    [dict setObject:arr forKey:@"VibePattern"];
    [dict setObject:[NSNumber numberWithInt:1] forKey:@"OnDuration"];
    
    //AudioServicesPlaySystemSoundWithVibration(kSystemSoundID_Vibrate,nil,dict);
}


//counts the destroyed blocks, for point calculation
- (int) setNewScore{
    int oldBlockcount = blockcount;
    blockcount = 0;
    
    //counts new number of blocks
    for(int i=0; i < [blocks getYCount]; i++){
        for(int j=0; j < [blocks getXCount]; j++){
            if([[blocks getBlockAtX:j andY:i] accessible] == YES){
                blockcount++;
            }
        }
    }
    //only calculate when blocks are destroyed
    if(oldBlockcount - blockcount != 0) score = score + (int)pow(oldBlockcount - blockcount - 1, 2);
    
    //check for bonus points when all blocks are destroyed
    BOOL blocksLeft = NO;
    for(int i=0; i < [blocks getYCount]; i++){
        for(int j=0; j < [blocks getXCount]; j++){
            if([[blocks getBlockAtX:j andY:i] accessible] == YES){
                blocksLeft = YES;
            }
        }
    }
    if(blocksLeft == NO) score = score + [glob BONUS_POINTS_CLEAR];
    
    return (int)pow(oldBlockcount - blockcount - 1, 2);
}

//checks if still blocks left
- (BOOL) checkMovesLeft{
    //check array, besides last row and column
    for(int i=0; i < [blocks getYCount]-1; i++){
        for(int j=0; j < [blocks getXCount]-1; j++){
            //right block
            if([[blocks getBlockAtX:j andY:i] accessible] == YES && [[blocks getBlockAtX:j+1 andY:i] accessible] == YES){
                if([[blocks getBlockAtX:j andY:i] Colour] == [[blocks getBlockAtX:j+1 andY:i] Colour]){
                    return YES;
                }
            }
            //bottom block
            if([[blocks getBlockAtX:j andY:i] accessible] == YES && [[blocks getBlockAtX:j andY:i+1] accessible] == YES){
                if([[blocks getBlockAtX:j andY:i] Colour] == [[blocks getBlockAtX:j andY:i+1] Colour]){
                    return YES;
                }
            }
        }
    }
    //check last row
    for(int i=0; i < [blocks getXCount]; i++){
        if([[blocks getBlockAtX:i andY:[blocks getYCount]-1] accessible] == YES && [[blocks getBlockAtX:i+1 andY:[blocks getYCount]-1] accessible] == YES){
            if([[blocks getBlockAtX:i andY:[blocks getYCount]-1] Colour] == [[blocks getBlockAtX:i+1 andY:[blocks getYCount]-1] Colour]){
                return YES;
            }
        }
    }
    //check last column
    for(int j=0; j < [blocks getYCount]-1; j++){
        if([[blocks getBlockAtX:[blocks getXCount]-1 andY:j] accessible] == YES && [[blocks getBlockAtX:[blocks getXCount]-1 andY:j+1] accessible] == YES){
            if([[blocks getBlockAtX:[blocks getXCount]-1 andY:j] Colour] == [[blocks getBlockAtX:[blocks getXCount]-1 andY:j+1] Colour]){
                return YES;
            }
        }
    }
    
    return NO;
}

@end
