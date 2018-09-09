<?php
	//simples login script mit redirect falls einglogt
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		session_start();
		
		$passwort = $_POST['passwort'];
		
		$hostname = $_SERVER['HTTP_HOST'];
		$path = dirname($_SERVER['PHP_SELF']);
		
		//Passwort abfrage - Passwort kann hier geändert werden
		if ($passwort == 'admin') {
			$_SESSION['angemeldet'] = true;
			
			if ($_SERVER['SERVER_PROTOCOL'] == 'HTTP/1.1') {
				if (php_sapi_name() == 'cgi') {
					header('Status: 303 See Other');
				}
				else {
					header('HTTP/1.1 303 See Other');
				}
			}
			header('Location: http://'.$hostname.($path == '/' ? '' : $path).'/admin-panel.php');
			exit;
		}
	}
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title>Admin Verwaltung</title>
	<link rel="stylesheet" type="text/css" href="content/style/style-admin.css" />
</head>
<body>
	<form id="passwort-form" action="admin.php" method="post">
		Passwort <input id="password-input" type="password" name="passwort" /><br />
		<input id="password-submit" type="submit" value="Anmelden" />
	</form>
	
	<footer id="footer">
		&copy; 2012 
		<a href="mailto:carina.hoffmann@iem.de">Carina Hoffmann</a>, 
		<a href="mailto:simone.d.weil@iem.thm.de">Simone Weil</a>, 
		<a href="mailto:anna.faerber@iem.thm.de">Anna Färber</a>, 
		<a href="mailto:franz.weisflug@gmx.de">Franz Weisflug</a>
	</footer>
</body>
</html>
