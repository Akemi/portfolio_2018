<iframe name="preview-iframe" id="preview-iframe"> 
</iframe>

<?php
	if ($add==1){
		echo '<form name="form" method="post" onsubmit="return submitFormAddArtikel();">';
		echo '<p><b>Artikel-Name:</b><br><input name="title" type="text" value=""></p>';
		echo '<p><b>Artikel-Inhalt:</b><br><textarea name="inhalt"></textarea></p>';
		echo '<input type="submit" name="Abschicken" value="Abschicken" style="width: 200px;" onclick="document.pressed=this.value"> ';
		echo '<input type="submit" name="Vorschau" value="Vorschau" style="width: 200px;" onclick="document.pressed=this.value">';
		echo '</form>';
	}
	else if ($add==2){
		echo '<form name="form" method="post" onsubmit="return submitFormAddTest();">';
		echo '<p><b>Test-Name:</b><br><input name="title" type="text" value=""></p>';
		echo '<p><b>Test-Inhalt:</b><br><textarea name="inhalt"></textarea></p>';
		echo '<p><b>Lösung:</b><br><input name="ergebnis" type="text" value="0"></p>';
		echo '<input type="submit" name="Abschicken" value="Abschicken" style="width: 200px;" onclick="document.pressed=this.value"> ';
		echo '<input type="submit" name="Vorschau" value="Vorschau" style="width: 200px;" onclick="document.pressed=this.value">';
		echo '</form>';
	}
	else if ($add==3){
		echo "Unterstützte Dateitypen sind: wav, png, gif, jpg, mp4, webm, ogg, mp3.<br><br>";
		echo '<form enctype="multipart/form-data" action="index.php?add=31" method="POST">';
		echo 'Bitte Datei auswählen: <input name="uploaded-file" type="file" /><br />';
		echo '<input type="submit" value="Upload" style="width: 200px;" />';
		echo '</form>';
	}
	else if ($add==11) {
		$mysql = array();
		$mysql['title'] = mysql_real_escape_string($_POST['title'] );
		$mysql['inhalt'] = mysql_real_escape_string($_POST['inhalt'] );
		
		if(!($mysql['title'])){
			$mysql['title'] = "nix";
		}
		
		$sql_add_artikel = "INSERT INTO matheeintraege (id, name, inhalt) VALUES (NULL, '".$mysql['title']."','".$mysql['inhalt']."')";

		mysql_query($sql_add_artikel);
		$idl = mysql_insert_id();
		
		echo 'Der Artikel <a href="index.php?artikelid='.$idl.'">'.$mysql['title'].'</a> mit der neuen ID '.$idl.' wurde hinzugefügt.';
	}
	else if ($add==21) {
		$mysql = array();
		$mysql['title'] = mysql_real_escape_string($_POST['title'] );
		$mysql['inhalt'] = mysql_real_escape_string($_POST['inhalt'] );
		$mysql['ergebnis'] = mysql_real_escape_string($_POST['ergebnis']);
		
		if(!($mysql['title'])){
			$mysql['title'] = "nix";
		}
		
		$sql_add_artikel = "INSERT INTO tests (id, name, inhalt, ergebnis) VALUES (NULL, '".$mysql['title']."','".$mysql['inhalt']."','".$mysql['ergebnis']."')";

		mysql_query($sql_add_artikel);
		$idl = mysql_insert_id();
		
		echo 'Das Quiz <a href="index.php?testid='.$idl.'">'.$mysql['title'].'</a> mit der neuen ID '.$idl.' wurde hinzugefügt.';
	}
	else if ($add==31) {
		ini_set('max_execution_time', 0);
		ini_set('max_input_time', 0);
		set_time_limit(0);
		$file = basename($_FILES['uploaded-file']['name']);
		$path = "uploads/".basename($_FILES['uploaded-file']['name']);
		$path_info = pathinfo($path);
		$extension = $path_info['extension'];
	
		if($extension == wav || $extension == png || $extension == gif || $extension == jpg || $extension == mp4 || $extension == webm|| $extension == ogg|| $extension == mp3 ) {
			if(file_exists($path)) {
				echo "Die Datei existiert bereits. Bitte Datei umbenennen.";
			}
			else if(move_uploaded_file($_FILES['uploaded-file']['tmp_name'], $path)) {
				echo "Die Datei <a href='uploads/".rawurlencode($file)."'>".basename($_FILES['uploaded-file']['name'])."</a> wurde erfolreich hochgeladen.<br>";
				echo "Sie kann in ihren Artikeln/Tests unter folgendem Link aufgerufen werden: <b>".$path."</b>";
			} 
			else {
				echo "Ein Fehler ist beim hochladen aufgetreten. Bitte versuch es noch einmal.";
			}
		}
		else {
			echo "Dieser Dateityp kann nicht hochgeladen werden.<br>";
			echo "Erlaubt sind: wav, png, gif, jpg, mp4, webm, ogg, mp3.";
		}
	}
					  
?>