//
//  SameGlobeGlobVars.m
//  SameGlobe
//
//  Created by Akemi on 06/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "SameGlobeGlobVars.h"

@implementation SameGlobeGlobVars

static id sharedManager = nil;

//static variables
@synthesize BLOCK_COLOUR1;					//block colour 1
@synthesize BLOCK_COLOUR2;					//block colour 2
@synthesize BLOCK_COLOUR3;					//block colour 3
@synthesize BLOCK_COLOUR4;					//block colour 4
@synthesize BLOCK_COLOUR5;					//block colour 5
@synthesize DIF_EASY;						//difficulty easy
@synthesize DIF_NORMAL;						//difficulty easy
@synthesize DIF_HARD;						//difficulty easy
@synthesize DIRECTION_ALL;					//check direction all
@synthesize DIRECTION_RIGHT;				//check direction right
@synthesize DIRECTION_DOWN;					//check direction down
@synthesize DIRECTION_LEFT;					//check direction left
@synthesize DIRECTION_UP;					//check direction up
@synthesize RES_X_BLOCKS;					//number of blocks x resolution
@synthesize RES_Y_BLOCKS;					//number of blocks y resolution
@synthesize BONUS_POINTS_CLEAR;             //bonus points for level clear

//static variables in pixel for
@synthesize SCORE_BAR_HEIGHT;				//height of score bar
@synthesize SCORE_BAR_FONT_SIZE;			//font size in scorebar

//scaled variables for destination resolution
@synthesize pixelScaleFac;					//scale factor for target device resolution
@synthesize scoreBarHeight;					//scaled bar height for scorebar
@synthesize scoreBarFontSize;				//scaled font size for scorebar


//screen dimensions and off-screen borders
@synthesize ScreenDimX;						//screen dim x
@synthesize ScreenDimY;						//screen dim y
@synthesize ScreenOffsetX;					//offset of screen x direction
@synthesize ScreenOffsetY;					//offset of screen y direction
@synthesize BlockDim;						//dimension if blocks
@synthesize background;                     //background of game

//game settings
@synthesize NumOfColours;					//Number of Colours
@synthesize Difficulty;						//difficulty of the game

//images
@synthesize block1Image;                    //block images
@synthesize block2Image;
@synthesize block3Image;
@synthesize block4Image;
@synthesize block5Image;

+ (id)sharedManager {
    if (sharedManager == nil) {
        sharedManager = [[self alloc] init];
    }
    
    return sharedManager;
}

//sets right scaling
- (void)setPixelScaleFac:(double) pSF {
    pixelScaleFac = pSF;
    
    scoreBarHeight = (int)(pSF * SCORE_BAR_HEIGHT);
    scoreBarFontSize = (int)(pSF * SCORE_BAR_FONT_SIZE);
}

- (double)getPixelScaleFac {
    return pixelScaleFac;
}

- (int)getRealOffsetY {
    return ScreenOffsetY + scoreBarHeight;
}

//init values
- (id)init {
    if (self = [super init]) {
        BLOCK_COLOUR1 = 1;
        BLOCK_COLOUR2 = 2;
        BLOCK_COLOUR3 = 3;
        BLOCK_COLOUR4 = 4;
        BLOCK_COLOUR5 = 5;
        DIF_EASY = 2;
        DIF_NORMAL = 3;
        DIF_HARD = 4;
        DIRECTION_ALL = 0;
        DIRECTION_RIGHT = 1;
        DIRECTION_DOWN = 2;
        DIRECTION_LEFT = 3;
        DIRECTION_UP = 4;
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            RES_X_BLOCKS = 10;
            RES_Y_BLOCKS = 7;
        }
        else {
            if ([[UIScreen mainScreen] applicationFrame].size.height > 480.0f) {
                RES_X_BLOCKS = 9;
                RES_Y_BLOCKS = 5;
            } else {
                RES_X_BLOCKS = 9;
                RES_Y_BLOCKS = 6;
            }
            
            
        }
        
        BONUS_POINTS_CLEAR = 10000;
        
        SCORE_BAR_HEIGHT = 20;
        SCORE_BAR_FONT_SIZE = 16;
    }
    return self;
}


@end
