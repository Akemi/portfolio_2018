//
//  BlockArray.m
//  SameGlobe
//
//  Created by Akemi on 06/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "BlockArray.h"
#import "Block.h"

@implementation BlockArray

//init array
-(id) initWithX:(int)x andY:(int) y{
    
    if (self = [super init]) {
        _array = [[NSMutableArray alloc] init];
        _y = y;
        _x = x;
        
        //fill array
        for (int i = 0; i < _x*_y; i++) {
            [_array insertObject:[[Block alloc] init] atIndex:(i)];
        }
    }
    return self;
}

-(id) getBlockAtX:(int) x andY:(int)y{
    return [_array objectAtIndex: (y*_x + x)];
}

-(void) setBlock:(Block*) anObject atX:(int) x andY:(int)y{
    [_array replaceObjectAtIndex:(y*_x + x) withObject:anObject];
}

-(void) exchangeObjectAtX:(int)x andY:(int)y withObjectAtX:(int)x2 andY:(int)y2{
    [_array exchangeObjectAtIndex:(y*_x + x) withObjectAtIndex:(y2*_x + x2)];
}

-(int) getYCount{
    return _y;
}
-(int) getXCount{
    return _x;
}

-(void) dealloc{
    
}

@end
