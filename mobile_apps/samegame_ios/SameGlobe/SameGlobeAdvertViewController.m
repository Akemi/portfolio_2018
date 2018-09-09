//
//  SameGlobeAdvertViewController.m
//  SameGlobe
//
//  Created by Akemi on 13/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import "SameGlobeAdvertViewController.h"
#import "SameGlobeScoreViewController.h"

@interface SameGlobeAdvertViewController ()

@end

@implementation SameGlobeAdvertViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.    
    //sauto resizing of label
    [[self sloganLabel] setPreferredMaxLayoutWidth:350.f];
    [[self sloganLabel] removeConstraints:_sloganLabel.constraints];
    
    /*
    NSLog(@"%f", _sloganLabel.frame.size.width);
    
    _sloganLabel.frame = CGRectMake(
                             _sloganLabel.frame.origin.x,
                             _sloganLabel.frame.origin.y,
                             _sloganLabel.frame.size.width+20,
                             _sloganLabel.frame.size.height+20
    );*/
    
    //random slogan
    int rnd = (arc4random() % 3);
    if (rnd == 0) {
        _sloganLabel.text = [NSString stringWithFormat:NSLocalizedString(@"slogan fb", nil)];
    }
    else if (rnd == 1){
        _sloganLabel.text = [NSString stringWithFormat:NSLocalizedString(@"slogan twitter", nil)];
    }
    else if (rnd == 2){
        _sloganLabel.text = [NSString stringWithFormat:NSLocalizedString(@"slogan gsc", nil)];
    }
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//open site functions
- (IBAction)openFacebook:(id)sender {
    NSURL *url = [NSURL URLWithString:@"http://facebook.com/GSCFrankfurt"];
    
    if (![[UIApplication sharedApplication] openURL:url])
        NSLog(@"%@%@",@"Failed to open url:",[url description]);
}

- (IBAction)openTwitter:(id)sender {
    NSURL *url = [NSURL URLWithString:@"https://www.twitter.com/GSCFrankfurt"];
    
    if (![[UIApplication sharedApplication] openURL:url])
        NSLog(@"%@%@",@"Failed to open url:",[url description]);
}

- (IBAction)openGSC:(id)sender {
    NSURL *url = [NSURL URLWithString:@"https://www.globalsuccess-club.net"];
    
    if (![[UIApplication sharedApplication] openURL:url])
        NSLog(@"%@%@",@"Failed to open url:",[url description]);
}

//segue send, non working indentifier
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    NSLog(@"segue sent %@", segue.identifier);
    if([segue.identifier isEqualToString:@"scoreSegue"]){
        //SameGlobeScoreViewController *scoreView = [self.storyboard instantiateViewControllerWithIdentifier:@"ScoreView"];
        NSLog(@"segue sent");
        
    }
    SameGlobeScoreViewController *controller = (SameGlobeScoreViewController *)segue.destinationViewController;
    controller.score = _score;
}

@end
