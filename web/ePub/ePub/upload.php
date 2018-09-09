<?php include('auth.php'); ?>
<!DOCTYPE html>
<html lang="de">
<head>
	<meta charset="utf-8" />
	<title>Admin Verwaltung - Upload</title>
	<link rel="stylesheet" type="text/css" href="content/style/style-admin.css" />
	<script type="text/javascript" src="content/scripts/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="content/scripts/jquery.form.js"></script>
</head>
<body>
	<a href="admin-panel.php" id="back-to-admin">Zurück zur Admin-Verwaltung</a>
	<div id="upload-wrapper">
		<form id="upload-form" action="upload_send.php" method="post" enctype="multipart/form-data" name="upload-form">
			Wähle eine ePub Datei: <input name="epub_files[]" type="file" id="epub_files[]" size="50" />
			<br>
			Wähle ein Bild: <input name="epub_files[]" type="file" id="epub_files[]" size="50" />
			<br>
			<input type="submit" name="Submit" value="Upload" />
		</form>
		<div class="progress">
			<div class="bar"></div >
			<div class="percent">0%</div >
		</div>
			
		<div id="status"></div>
		
		<script type="text/javascript" src="content/scripts/epub-scripts-admin.js"></script>
	</div>
	
	<footer id="footer">
		&copy; 2012 
		<a href="mailto:carina.hoffmann@iem.de">Carina Hoffmann</a>, 
		<a href="mailto:simone.d.weil@iem.thm.de">Simone Weil</a>, 
		<a href="mailto:anna.faerber@iem.thm.de">Anna Färber</a>, 
		<a href="mailto:franz.weisflug@gmx.de">Franz Weisflug</a>
	</footer>
</body>
</html>
