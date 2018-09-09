<?php

if ($edit){
  		if ($naviid){
  			$sql_navi="select * from navi WHERE id =".$naviid;
			$db_erg_navi = mysql_query($sql_navi);
  			while ($zeile_navi = mysql_fetch_array( $db_erg_navi, MYSQL_ASSOC)){
  				$titel = "Bearbeiten von Navi-Eintrag: ".$zeile_navi['name'];
  			}
  			mysql_free_result($db_erg_navi);
  		}
  		else if ($testid){
  			$sql_test="SELECT * FROM  tests WHERE id =".$testid;
			$db_erg_test = mysql_query($sql_test);
  			while ($zeile_test = mysql_fetch_array( $db_erg_test, MYSQL_ASSOC)){
  				$titel = "Bearbeiten von Quiz: ".$zeile_test['name'];
  			}
  			mysql_free_result($db_erg_test);
  		}
  		else if ($artikelid){
  			$sql_artikel="select * from matheeintraege WHERE id =".$artikelid;
			$db_erg_artikel = mysql_query($sql_artikel);
  			while ($zeile_artikel = mysql_fetch_array( $db_erg_artikel, MYSQL_ASSOC)){
  				$titel = "Bearbeiten von Artikel: ".$zeile_artikel['name'];
  			}
  			mysql_free_result($db_erg_artikel);
  		}
  	}
  	else {
  		if ($naviid){
  			$sql_navi="select * from navi WHERE id =".$naviid;
			$db_erg_navi = mysql_query($sql_navi);
  			while ($zeile_navi = mysql_fetch_array( $db_erg_navi, MYSQL_ASSOC)){
  				$titel = $zeile_navi['name'];
  			}
  			mysql_free_result($db_erg_navi);
  		}
  		else if ($testid){
  			$sql_test="SELECT * FROM  tests WHERE id =".$testid;
			$db_erg_test = mysql_query($sql_test);
  			while ($zeile_test = mysql_fetch_array( $db_erg_test, MYSQL_ASSOC)){
  				$titel = $zeile_test['name'];
  			}
  			mysql_free_result($db_erg_test);
  		}
  		else if ($artikelid){
  			$sql_artikel="select * from matheeintraege WHERE id =".$artikelid;
			$db_erg_artikel = mysql_query($sql_artikel);
  			while ($zeile_artikel = mysql_fetch_array( $db_erg_artikel, MYSQL_ASSOC)){
  				$titel = $zeile_artikel['name'];
  			}
  			mysql_free_result($db_erg_artikel);
  		}
  		else if ($add){
  			if ($add==1) {
  				$titel = 'Artikel hinzufügen';
  			}
  			else if ($add==2) {
  				$titel = 'Quiz hinzufügen';
  			}
  			else if ($add==3) {
  				$titel = 'Datei Upload';
  			}
  			
  		}
  		else if ($search){
  			$titel = "Suche nach ".$_POST['search'];
  		}
  		else if ($delupload){
  			$titel = "Lösche: ".$delupload;
  		}
  	}

?>