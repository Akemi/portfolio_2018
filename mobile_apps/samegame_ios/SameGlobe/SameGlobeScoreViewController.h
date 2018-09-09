//
//  SameGlobeScoreViewController.h
//  SameGlobe
//
//  Created by Akemi on 10/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SameGlobeScoreViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *scoreLabel;
@property(nonatomic) int score;

@end
