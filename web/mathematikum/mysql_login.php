<?php
	//Adresse ihres Webservers
	$dbhost = 'localhost';
	//LogIn Name ihrer Datenbank
	$dbuser = 'root';
	//LogIn Passwort ihrer Datenbank
	$dbpass = '';
	//Name der Datenbank
	$dbname = 'projekt';
	
	$con = mysql_connect($dbhost, $dbuser, $dbpass) or die('Could not connect to DB: ' . mysql_error() );
	mysql_select_db($dbname, $con);
	
?>