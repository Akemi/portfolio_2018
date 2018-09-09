<iframe name="preview-iframe" id="preview-iframe"> 
</iframe>

<?php
	if ($edit==1){
		$sql_content="SELECT * FROM matheeintraege WHERE id =".$artikelid;
		$db_erg_content = mysql_query($sql_content);
  								
		while ($zeile_content = mysql_fetch_array( $db_erg_content, MYSQL_ASSOC)){
			echo '<form name="form" method="post" onsubmit="return submitFormEditArtikel('.$zeile_content['id'].');">';
			echo '<p><b>Artikel-Name:</b><br><input name="title" type="text" value="'.$zeile_content['name'].'"></p>';
			echo '<p><b>Artikel-Inhalt:</b><br><textarea name="inhalt">'.$zeile_content['inhalt'].'</textarea></p>';
			echo '<input type="submit" name="Speichern" value="Speichern" style="width: 200px;" onclick="document.pressed=this.value"> ';
			echo '<input type="submit" name="Vorschau" value="Vorschau" style="width: 200px;" onclick="document.pressed=this.value"> ';
			echo '<input type="submit" name="Löschen" value="Löschen" style="width: 200px;" onclick="document.pressed=this.value">';
			echo '</form>';
		}
	
		mysql_free_result($db_erg_content);
	}
	else if ($edit==2){
		$mysql = array();
		$mysql['title'] = mysql_real_escape_string($_POST['title'] );
		$mysql['inhalt'] = mysql_real_escape_string($_POST['inhalt'] );
		
		$sql_update="UPDATE matheeintraege SET name = '".$mysql['title']."',inhalt = '".$mysql['inhalt']."' WHERE id = ".$artikelid." LIMIT 1";
		
		mysql_query($sql_update);
		echo 'Der Artikel <a href="index.php?artikelid='.$artikelid.'">'.$mysql['title'].'</a> mit der ID <b>'.$artikelid.'</b> wurde geändert.';
	}
	else if ($edit==3){
		$sql_delete="DELETE FROM matheeintraege WHERE id = ".$artikelid." LIMIT 1";
		$title = $_POST['title'];
		mysql_query($sql_delete);
		echo 'Der Artikel <b>'.$title.'</b> mit der ID <b>'.$artikelid.'</b> wurde gelöscht.';
	}

	
?>