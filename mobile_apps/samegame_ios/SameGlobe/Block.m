//
//  Block.m
//  SameGlobe
//
//  Created by Akemi on 06/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "Block.h"
#import "SameGlobeGlobVars.h"

@implementation Block

SameGlobeGlobVars *glob;

@synthesize Colour;									//colour of block
@synthesize bitmap;									//image of Block
@synthesize accessible;                             //destroey indicator

- (id)init {
    if (self = [super init]) {
        glob = [SameGlobeGlobVars sharedManager];
        
        //random block image
        Colour = (arc4random() % [glob NumOfColours]) + 1;
        accessible = YES;
        
        //sets right image
        if (Colour == [glob BLOCK_COLOUR1]) bitmap = [glob block1Image];
        if (Colour == [glob BLOCK_COLOUR2]) bitmap = [glob block2Image];
        if (Colour == [glob BLOCK_COLOUR3]) bitmap = [glob block3Image];
        if (Colour == [glob BLOCK_COLOUR4]) bitmap = [glob block4Image];
        if (Colour == [glob BLOCK_COLOUR5]) bitmap = [glob block5Image];
    }
    return self;
}

@end
