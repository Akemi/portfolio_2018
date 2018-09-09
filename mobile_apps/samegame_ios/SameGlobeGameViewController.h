//
//  SameGlobeGameViewController.h
//  SameGlobe
//
//  Created by Akemi on 03/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SameGlobeGameView.h"

@class SameGlobeGameView;
@interface SameGlobeGameViewController : UIViewController

@property (strong, nonatomic) IBOutlet SameGlobeGameView *gameView;

- (void)changeViewControllerWithScore:(int) scoreSent;
- (void)pan:(UIPanGestureRecognizer *) gesture;

@end
