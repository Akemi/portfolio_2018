<?php
	if ($testid == rnd){
		$sql_content="SELECT * FROM  `tests` ORDER BY RAND() LIMIT 1";
	}
	else {
		$sql_content="SELECT * FROM  `tests` WHERE `tests`.`id` =".$testid;
	}
	
	$db_erg_content = mysql_query($sql_content);
  								
	while ($zeile_content = mysql_fetch_array( $db_erg_content, MYSQL_ASSOC)){
		$title_content = $zeile_content['name'];
		$inhalt_content = nl2br($zeile_content['inhalt']);
		$losung_content = $zeile_content['ergebnis'];
		
		include 'test_parser.php';
	}
  								
	mysql_free_result($db_erg_content);
?>