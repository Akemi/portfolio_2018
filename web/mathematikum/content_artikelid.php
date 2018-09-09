<?php
	$sql_content="SELECT * FROM  `matheeintraege` WHERE `matheeintraege`.`id` =".$artikelid;
	$db_erg_content = mysql_query($sql_content);
  								
	while ($zeile_content = mysql_fetch_array( $db_erg_content, MYSQL_ASSOC)){
		$title_content = $zeile_content['name'];
		$inhalt_content = nl2br($zeile_content['inhalt']);
			
		include 'content_parser.php';
	}
  								
	mysql_free_result($db_erg_content);
?>