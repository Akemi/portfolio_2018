//
//  SameGlobeGameViewController.m
//  SameGlobe
//
//  Created by Akemi on 03/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "SameGlobeGameViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "SameGlobeAdvertViewController.h"

@interface SameGlobeGameViewController ()

@end

@implementation SameGlobeGameViewController

CGPoint startLocation;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)initWithCoder:(NSCoder *) coder {
    
    if(self = [super initWithCoder:coder]) {
        
    }
    
    return self;
}

- (void)encodeWithCoder:(NSCoder *)enCoder {
    [super encodeWithCoder:enCoder];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIPanGestureRecognizer *panRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(pan:)];
    [[self gameView] addGestureRecognizer:panRecognizer];
    
    //makes the contoller known to thw view
    [self.gameView initController:self];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)changeViewControllerWithScore:(int) scoreSent
{
    //sends the score to the next screen
    SameGlobeAdvertViewController *svc = [self.storyboard instantiateViewControllerWithIdentifier:@"AdvertView"];
    svc.score = scoreSent;
    //changes controller view
    [self.navigationController pushViewController:svc animated:YES];
}

- (void)pan:(UIPanGestureRecognizer *)gesture {
    //touch start
    if (gesture.state == UIGestureRecognizerStateBegan) {
        startLocation = [gesture locationInView:[self gameView]];
    }
    //touch end
    else if (gesture.state == UIGestureRecognizerStateEnded) {
        CGPoint stopLocation = [gesture locationInView:[self gameView]];
        CGFloat distance = stopLocation.x - startLocation.x;
        NSLog(@"Distance: %f", distance);
        
        //change view when distance big enough
        if (distance >= 300) {
            //define tansition
            CATransition *transition = [CATransition animation];
            transition.duration = 0.3f;
            transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
            transition.type = kCATransitionPush;
            transition.subtype = kCATransitionFromTop;
            [self.navigationController.view.layer addAnimation:transition forKey:nil];
            
            SameGlobeAdvertViewController *svc = [self.storyboard instantiateViewControllerWithIdentifier:@"StartView"];
            //changes controller view
            [self.navigationController pushViewController:svc animated:NO];
        }
    }
}


@end
