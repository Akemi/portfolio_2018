//
//  SameGlobeAdvertViewController.h
//  SameGlobe
//
//  Created by Akemi on 13/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SameGlobeAdvertViewController : UIViewController 

@property (weak, nonatomic) IBOutlet UILabel *sloganLabel;
@property (nonatomic) int score;

- (IBAction)openFacebook:(id)sender;
- (IBAction)openTwitter:(id)sender;
- (IBAction)openGSC:(id)sender;
@end
