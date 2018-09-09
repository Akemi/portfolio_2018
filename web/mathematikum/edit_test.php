<iframe name="preview-iframe" id="preview-iframe"> 
</iframe>

<?php
	if ($edit==1){
		$sql_content="SELECT * FROM tests WHERE id =".$testid;
		$db_erg_content = mysql_query($sql_content);
  								
		while ($zeile_content = mysql_fetch_array( $db_erg_content, MYSQL_ASSOC)){
			echo '<form name="form" method="post" onsubmit="return submitFormEditTest('.$zeile_content['id'].');">';
			echo '<p><b>Test-Name:</b><br><input name="title" type="text" value="'.$zeile_content['name'].'"></p>';
			echo '<p><b>Test-Inhalt:</b><br><textarea name="inhalt">'.$zeile_content['inhalt'].'</textarea></p>';
			echo '<p><b>Lösung:</b><br><input name="ergebnis" type="text" value="'.$zeile_content['ergebnis'].'"></p>';
			echo '<input type="submit" name="Speichern" value="Speichern" style="width: 200px;" onclick="document.pressed=this.value"> ';
			echo '<input type="submit" name="Vorschau" value="Vorschau" style="width: 200px;" onclick="document.pressed=this.value"> ';
			echo '<input type="submit" name="Löschen" value="Löschen" style="width: 200px;" onclick="document.pressed=this.value">';
			echo '</form>';
		}
	
		mysql_free_result($db_erg_content);
	}
	else if ($edit==2){
		$mysql = array();
		$mysql['title'] = mysql_real_escape_string($_POST['title']);
		$mysql['inhalt'] = mysql_real_escape_string($_POST['inhalt']);
		$mysql['ergebnis'] = mysql_real_escape_string($_POST['ergebnis']);
		
		$sql_update="UPDATE tests SET name = '".$mysql['title']."',inhalt = '".$mysql['inhalt']."',ergebnis = '".$mysql['ergebnis']."' WHERE id = ".$testid." LIMIT 1";
		
		mysql_query($sql_update);
		echo 'Das Quiz <a href="index.php?testid='.$testid.'">'.$mysql['title'].'</a> mit der ID <b>'.$testid.'</b> wurde geändert.';
	}
	else if ($edit==3){
		$sql_delete="DELETE FROM tests WHERE id = ".$testid." LIMIT 1";
		$title = $_POST['title'];
		mysql_query($sql_delete);
		echo 'Das Quiz <b>'.$title.'</b> mit der ID <b>'.$testid.'</b> wurde gelöscht.';
	}

	
?>