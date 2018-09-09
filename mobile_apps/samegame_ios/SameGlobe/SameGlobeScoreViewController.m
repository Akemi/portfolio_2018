//
//  SameGlobeScoreViewController.m
//  SameGlobe
//
//  Created by Akemi on 10/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "SameGlobeScoreViewController.h"
#import "SameGlobeGlobVars.h"

@interface SameGlobeScoreViewController ()

@end

@implementation SameGlobeScoreViewController

NSMutableArray *scoreData;      //score table
SameGlobeGlobVars *glob;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    glob = [SameGlobeGlobVars sharedManager];
    
    NSLog(@"Your Score %d", _score);
    
    //set score title
    _scoreLabel.text = [NSString stringWithFormat:NSLocalizedString(@"your points %d", nil), _score];
    
    //get readable directory
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *libPath = [paths objectAtIndex:0];
    //set right filename
    NSString *filePath =  [libPath stringByAppendingPathComponent:[NSString stringWithFormat:@"sameGlobeScoreColors%dDiffifcultiy%d.plist", [glob NumOfColours], [glob Difficulty]]];
    NSLog(@"Score saved at: %@", filePath);
    
    //Reading from File
    scoreData = [NSMutableArray arrayWithContentsOfFile:filePath];
    //if not readable new file else init old file
    if(!scoreData) scoreData = [[NSMutableArray alloc] init];
    else scoreData = [[NSMutableArray alloc] initWithArray:scoreData];
    
    //insert new score
    [scoreData insertObject:[NSNumber numberWithInteger:_score] atIndex:0];
    
    //sort score table descending
    NSSortDescriptor *sortOrder = [NSSortDescriptor sortDescriptorWithKey: @"self" ascending: NO];
	[scoreData sortUsingDescriptors:[NSArray arrayWithObject: sortOrder]];

    //write score to file
    if(![scoreData writeToFile:filePath atomically:NO]) {
        NSLog(@"Array wasn't saved properly");
    };
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [scoreData count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //fill table
    static NSString *simpleTableIdentifier = @"SimpleTableItem";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    //cell formating
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
        cell.textLabel.textColor = [UIColor colorWithRed:(95/255.0) green:(95/255.0) blue:(95/255.0) alpha:1];
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
    }
    
    //format how to write data
    cell.textLabel.text = [NSString stringWithFormat: @"%ld. %@",(long)indexPath.row+1 ,[[scoreData objectAtIndex:indexPath.row] stringValue]];
    return cell;
}

@end
