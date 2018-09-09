<!DOCTYPE html>
<html lang="de">
<head>
	<meta charset="utf-8" />
	<title>Preview</title>
	
	<script src="content/scripts/mainscripts.js"></script>
	
	<link rel="stylesheet" type="text/css" href="content/stylesheet/style_preview.css">
</head>
<body onload="resizePreviewIFrame();">
	<div id="resizer">
	<?php 
		error_reporting(4);
	
		$title_content = $_POST['title'];
		$inhalt_content = nl2br($_POST['inhalt']);
		$losung_content = $_POST['ergebnis'];
		
		if ($losung_content){
			include 'test_parser.php';
		}
		else {
			include 'content_parser.php';
		}
		
	?>
	</div>

</body>
</html>