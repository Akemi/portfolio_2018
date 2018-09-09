//
//  UILabelTest.m
//  SameGlobe
//
//  Created by Akemi on 16/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "UILabelMargin.h"

@implementation UILabelMargin

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}


// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    UIEdgeInsets insets = UIEdgeInsetsMake(0, 5, 0, 0);
    //rect.size.width += 20;
    //rect.size.height += 20;
    //rect.origin.x += 10;
    //rect.origin.y += 10;
    
    /*NSLog(@"%f - %f", self.frame.size.width, self.frame.size.height);
    self.frame = CGRectMake(
                                    self.frame.origin.x,
                                    self.frame.origin.y,
                                    self.frame.size.width+20,
                                    self.frame.size.height+20
                                    );*/
    //[super drawRect:rect];
    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, insets)];
    //[super drawRect:rect];
}

@end
