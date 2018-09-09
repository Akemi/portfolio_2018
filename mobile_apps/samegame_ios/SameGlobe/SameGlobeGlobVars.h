//
//  SameGlobeGlobVars.h
//  SameGlobe
//
//  Created by Akemi on 06/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SameGlobeGlobVars : NSObject {
    //variables
    int BLOCK_COLOUR1;					//block colour 1
    int BLOCK_COLOUR2;					//block colour 2
    int BLOCK_COLOUR3;					//block colour 3
    int BLOCK_COLOUR4;					//block colour 4
    int BLOCK_COLOUR5;					//block colour 5
    int DIF_EASY;						//difficulty easy
    int DIF_NORMAL;						//difficulty easy
    int DIF_HARD;						//difficulty easy
    int DIRECTION_ALL;					//check direction all
    int DIRECTION_RIGHT;				//check direction right
    int DIRECTION_DOWN;					//check direction down
    int DIRECTION_LEFT;					//check direction left
    int DIRECTION_UP;					//check direction up
    int RES_X_BLOCKS;                   //number of blocks x resolution
    int RES_Y_BLOCKS;					//number of blocks y resolution
    int BONUS_POINTS_CLEAR;             //bonus points for level clear
    
    //variables in pixel for
    int SCORE_BAR_HEIGHT;				//height of score bar
    int SCORE_BAR_FONT_SIZE;			//font size in scorebar
    
    //scaled variables for destination resolution
    double pixelScaleFac;				//scale factor for target device resolution
    int scoreBarHeight;					//scaled bar height for scorebar
    int scoreBarFontSize;				//scaled font size for scorebar
    
    
    //screen dimensions and off-screen borders
    int ScreenDimX;						//screen dim x
    int ScreenDimY;						//screen dim y
    int ScreenOffsetX;					//offset of screen x direction
    int ScreenOffsetY;					//offset of screen y direction
    double BlockDim;					//dimension if blocks
    UIImage *background;				//background of game
    
    //game settings
    int NumOfColours;					//Number of Colours
    int Difficulty;						//difficulty of the game
    
    //images
    UIImage *block1Image;				//block images
    UIImage *block2Image;
    UIImage *block3Image;
    UIImage *block4Image;
    UIImage *block5Image;
}
@property (nonatomic, assign) int BLOCK_COLOUR1;
@property (nonatomic, assign) int BLOCK_COLOUR2;
@property (nonatomic, assign) int BLOCK_COLOUR3;
@property (nonatomic, assign) int BLOCK_COLOUR4;
@property (nonatomic, assign) int BLOCK_COLOUR5;
@property (nonatomic, assign) int DIF_EASY;
@property (nonatomic, assign) int DIF_NORMAL;
@property (nonatomic, assign) int DIF_HARD;
@property (nonatomic, assign) int DIRECTION_ALL;
@property (nonatomic, assign) int DIRECTION_RIGHT;
@property (nonatomic, assign) int DIRECTION_DOWN;
@property (nonatomic, assign) int DIRECTION_LEFT;
@property (nonatomic, assign) int DIRECTION_UP;
@property (nonatomic, assign) int RES_X_BLOCKS;
@property (nonatomic, assign) int RES_Y_BLOCKS;
@property (nonatomic, assign) int BONUS_POINTS_CLEAR;
@property (nonatomic, assign) int SCORE_BAR_HEIGHT;
@property (nonatomic, assign) int SCORE_BAR_FONT_SIZE;
@property (nonatomic, assign) double pixelScaleFac;
@property (nonatomic, assign) int scoreBarHeight;
@property (nonatomic, assign) int scoreBarFontSize;
@property (nonatomic, assign) int ScreenDimX;
@property (nonatomic, assign) int ScreenDimY;
@property (nonatomic, assign) int ScreenOffsetX;
@property (nonatomic, assign) int ScreenOffsetY;
@property (nonatomic, assign) double BlockDim;
@property (nonatomic, retain) UIImage *background;
@property (nonatomic, assign) int NumOfColours;
@property (nonatomic, assign) int Difficulty;
@property (nonatomic, retain) UIImage *block1Image;
@property (nonatomic, retain) UIImage *block2Image;
@property (nonatomic, retain) UIImage *block3Image;
@property (nonatomic, retain) UIImage *block4Image;
@property (nonatomic, retain) UIImage *block5Image;


+ (id)sharedManager;
- (int)getRealOffsetY;
- (void)setPixelScaleFac:(double) pSF;
- (double)getPixelScaleFac;

@end
