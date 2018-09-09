<!DOCTYPE html>
<html lang="de">

<?php
	include 'mysql_login.php';
    
    error_reporting(4);
    
    $naviid = $_GET["naviid"];
    $testid = $_GET["testid"];
    $artikelid = $_GET["artikelid"];
	$edit = $_GET["edit"];
	$add = $_GET["add"];
	$search = $_GET["search"];
	$delupload = $_GET["delupload"];
	
	/*if(isset($naviid)){$naviid = $_GET["naviid"];} else {$naviid = "";}
    if(isset($testid)){$testid = $_GET["testid"];} else {$testid = "";}
    if(isset($artikelid)){$artikelid = $_GET["artikelid"];} else {$artikelid = "";}
	if(isset($edit)){$edit = $_GET["edit"];} else {$edit = "";}
	if(isset($add)){$add = $_GET["add"];} else {$add = "";}
	if(isset($search)){$search = $_GET["search"];} else {$search = "";}
	if(isset($delupload)){$delupload = $_GET["delupload"];} else {$delupload = "";}*/
    
    if (!($naviid || $testid || $artikelid || $edit || $add || $search || $delupload)) {
    	$naviid = 1;
    }
    
    if ($testid == 'rnd'){
		$sql_content="SELECT * FROM  `tests` ORDER BY RAND() LIMIT 1";
		$db_erg_content = mysql_query($sql_content);
  								
		while ($zeile_content = mysql_fetch_array( $db_erg_content, MYSQL_ASSOC)){
			$testid = $zeile_content['id'];
		}						
		mysql_free_result($db_erg_content);
	}
	
	include 'titel.php';

?>

<head>
	<meta charset="utf-8" />
	<title>Mathematikum - <?php echo $titel; ?></title>
	
	<link rel="stylesheet" type="text/css" href="content/stylesheet/style.css">
	
	<script src="content/scripts/mainscripts.js"></script>
	<script src="content/scripts/testscripts.js"></script>
</head>

<body onscroll="toolbarscroll();">

	<div id="frame">
		<!-- BEGIN - LOGO -->
		<div id="logo">
			<a href="index.php" id="logo-click"></a>
			<div id="search-admin-bar">
				<div id="search-admin-bar-login-wrapper">
					Login <input class="search-admin-bar-login-input" type="text" name="username" value="" /> 
					PW <input class="search-admin-bar-login-input" type="password" name="password" value="" /> 
					<input type="submit" id="search-admin-bar-login-button" value="LogIn"/>
				</div>
				<div id="search-admin-bar-search-wrapper">
					<form action="index.php?search=1" method="post">
						<input type="text" name="search" id="search-admin-bar-search" value="Suche" onblur="if(this.value=='')this.value='Suche';" onclick="if(this.value=='Suche')this.value='';">
					</form>
				</div>
			</div>
		</div>
		<div id="logo-bg"></div>
		<!-- END - LOGO -->
		
		<div id="navi-content-wrapper">
			
			
			<div id="content-wrapper">
			
			<!-- BEGIN - Admin Toolbar -->
				<div id="admin-toolbar">
					<ul>
						<li>Hinzufügen
							<ul>
								<li><a href="index.php?add=1">Artikel</a></li>
								<li><a href="index.php?add=2">Quiz</a></li>
								<li><a href="index.php?add=3">Upload</a></li>
							</ul>
						</li>
						<li>
							<?php
								if ($naviid){
  									echo "<a href='index.php?naviid=".$naviid."&amp;edit=1"."'>";
  								}
  								else if ($testid){
  									echo "<a href='index.php?testid=".$testid."&amp;edit=1"."'>";
  								}
  								else if ($artikelid){
  									echo "<a href='index.php?artikelid=".$artikelid."&amp;edit=1"."'>";
  								}
  								else {
  									echo "<a href=''>";
  								}
  								echo "Bearbeiten</a>"
							?>
							
						</li>
					</ul>
				</div>
			<!-- END - Admin Toolbar -->
			
			
				<div id="content">
				
					<?php

  						if ($edit){
  							if ($naviid){
  								include 'edit_navi.php';
  							}
  							else if ($testid){
  								include 'edit_test.php';
  							}
  							else if ($artikelid){
  								include 'edit_artikel.php';
  							}
  						}
  						else {
  							if ($naviid){
  								include 'content_naviid.php';
  							}
  							else if ($testid){
  								include 'content_testid.php';
  							}
  							else if ($artikelid){
  								include 'content_artikelid.php';
  							}
  							else if ($add){
  								include 'add.php';
  							}
  							else if ($search){
  								include 'search.php';
  							}
  							else if ($delupload){
  								include 'del_upload.php';
  							}
  						}
	
					?>

				</div>
			</div>
			
			
			<!-- BEGIN - NAVI -->
			<div id="navi">
				<!-- BEGIN - NAVI-LIST -->
				<?php
					include 'navi.php';
					include 'mysql_close.php';
				?>
				<!-- END - NAVI-LIST -->
			</div>
			<!-- END - NAVI -->
			
			
		</div>
		<div style="clear: both;"></div>
		
		<div id="footer">
				Dieses Projekt wurde erstellt von <a href="index.php?naviid=5">Dominic Linke</a> und <a href="index.php?naviid=5">Franz Weisflug</a>. <br>
				&copy; 2011 <a href="index.php?naviid=5">Dominic Linke</a>, <a href="index.php?naviid=5">Franz Weisflug</a>.
		</div>
		
	</div>
	
</body>

</html>
