<?php
	$delupload = "uploads/".$delupload;
  	
	if(file_exists($delupload)) {
		unlink($delupload);
		echo "Die Datei <b>".$delupload."</b> wurde gelöscht.";
	}
	else {
		echo "Die Datei existiert nicht.";
	}
?>