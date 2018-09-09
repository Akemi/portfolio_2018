//
//  Block.h
//  SameGlobe
//
//  Created by Akemi on 06/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Block : NSObject {
    int Colour;                                         //colour of block
    UIImage *bitmap;									//image of Block
    BOOL accessible;                                    //destroey indicator
}

@property (nonatomic, assign) int Colour;
@property (nonatomic, retain) UIImage *bitmap;
@property (nonatomic, assign) BOOL accessible;

@end
