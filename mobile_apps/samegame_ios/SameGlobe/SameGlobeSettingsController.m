//
//  SameGlobeSettingsController.m
//  SameGlobe
//
//  Created by Akemi on 14/05/2013.
//  Copyright (c) 2013 Akemi. All rights reserved.
//

#import "SameGlobeSettingsController.h"
#import "SameGlobeAppDelegate.h"
#import "SameGlobeGlobVars.h"

@interface SameGlobeSettingsController ()

@property (nonatomic) NSUInteger color;
@property (nonatomic) NSUInteger dificulty;

#define SECTION_COLOR   0
#define SECTION_DIFFICULTY  1

@end

@implementation SameGlobeSettingsController

    SameGlobeGlobVars *glob;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    glob = [SameGlobeGlobVars sharedManager];
    
    //load defaults
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.color = [defaults integerForKey:kUYLSettingsColorKey]-3;
    self.dificulty = [defaults integerForKey:kUYLSettingsDifficultyKey]-2;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{    
    UITableViewCell *cell = [super tableView:tableView cellForRowAtIndexPath:indexPath];
    
    //reset checkmark
    cell.accessoryType = UITableViewCellAccessoryNone;
    
    //which section and row
    NSUInteger section = [indexPath section];
    NSUInteger row = [indexPath row];
    
    //set the right checkmark
    switch (section)
    {
        case SECTION_COLOR:
            if (row == self.color)
            {
                cell.accessoryType = UITableViewCellAccessoryCheckmark;
            }
            break;
            
        case SECTION_DIFFICULTY:
            if (row == self.dificulty)
            {
                cell.accessoryType = UITableViewCellAccessoryCheckmark;
            }
            break;
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //user defaults
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    //get section and row
    NSUInteger section = [indexPath section];
    NSUInteger row = [indexPath row];
    
    //set default and glob var
    switch (section)
    {
        case SECTION_COLOR:
            self.color = row;
            [defaults setInteger:row+3 forKey:kUYLSettingsColorKey];
            [glob setNumOfColours:[defaults integerForKey:kUYLSettingsColorKey]];
            break;
        case SECTION_DIFFICULTY:
            self.dificulty = row;
            [defaults setInteger:row+2 forKey:kUYLSettingsDifficultyKey];
            [glob setDifficulty:[defaults integerForKey:kUYLSettingsDifficultyKey]];
            break;
    }
    [self.tableView reloadData];
}

@end
