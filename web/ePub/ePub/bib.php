<?php 
	include('auth.php'); 
	require_once('ebookFkt.php');
	require_once('ebookRead.php');
	require_once('ebookData.php');
	
	if(isset($_GET["delete"])){
		$delete = $_GET["delete"];
	}
	else {
		$delete = "";
	}
?>
<!DOCTYPE html>
<html lang="de">
<head>
	<meta charset="utf-8" />
	<title>Admin Verwaltung - Upload</title>
	<link rel="stylesheet" type="text/css" href="content/style/style-admin.css" />

	<script type="text/javascript" src="content/scripts/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="content/scripts/epub-scripts-bib.js"></script>
</head>
<body>
	<?php 
		//abfrage ob buch gelöscht werden soll, falls ja ruft funktion zum löschen auf
		if($delete != ""){
			$dir = "books/".$delete;
			if (is_dir($dir)) {
				rrmdir($dir);
			}
		}	
	?>
	<a href="admin-panel.php" id="back-to-admin">Zurück zur Admin-Verwaltung</a>
	<div id="bib-frame">
		<div id="bib-books">
			<div id="bib-arrowleft"></div>
			<div id="bib-arrowright"></div>
			<ul id="bib-booklist">
				<?php 
					readBib();	
				?>
			</ul>
		</div>
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
