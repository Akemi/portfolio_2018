//
//  BlockArray.h
//  SameGlobe
//
//  Created by Akemi on 06/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Block.h"

@interface BlockArray : NSObject {
    
    @package
    NSMutableArray* _array;     //block array
    int _y, _x;                 //array dimensions
}

-(id) initWithX:(int)x andY:(int) y;
-(id) getBlockAtX:(int) x andY:(int)y;
-(void) setBlock:(Block*) anObject atX:(int) x andY:(int)y;
-(void) exchangeObjectAtX:(int)x andY:(int)y withObjectAtX:(int)x2 andY:(int)y2;
-(int) getYCount;
-(int) getXCount;

@end
