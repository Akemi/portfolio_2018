//
//  SameGlobeViewController.m
//  SameGlobe
//
//  Created by Akemi on 03/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "SameGlobeViewController.h"
#import "SameGlobeGlobVars.h"
#import "SameGlobeAppDelegate.h"

@interface SameGlobeViewController ()

@end

@implementation SameGlobeViewController

    SameGlobeGlobVars *glob;

- (void)viewDidLoad
{
    [super viewDidLoad];
    glob = [SameGlobeGlobVars sharedManager];
    
    //set initial variables for the singleton pattern
    //VERY IMPORTANT, SEE DESCRIPTION IN GlobVars SINGLETON PATTER
    [glob setBlock1Image:[UIImage imageNamed:@"block1.png"]];
    [glob setBlock2Image:[UIImage imageNamed:@"block2.png"]];
    [glob setBlock3Image:[UIImage imageNamed:@"block3.png"]];
    [glob setBlock4Image:[UIImage imageNamed:@"block4.png"]];
    [glob setBlock5Image:[UIImage imageNamed:@"block5.png"]];
    
    //loads and sets defficulty from defaults
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [glob setNumOfColours:[defaults integerForKey:kUYLSettingsColorKey]];
    [glob setDifficulty:[defaults integerForKey:kUYLSettingsDifficultyKey]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewWillAppear:(BOOL)animated{
    [[self navigationController] setNavigationBarHidden:YES animated:YES];
}

-(void)viewWillDisappear:(BOOL)animated{
    [[self navigationController] setNavigationBarHidden:NO animated:YES];
}
@end
