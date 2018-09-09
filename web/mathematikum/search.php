<?php	
	$search_input = $_POST['search'];
	$sql="select * from matheeintraege where inhalt like '%$search_input%' or name like '%$search_input%'";
    
    echo '<div class="headline-big">Suche</div>';
    
    echo '<div class="headline-small">Artikel</div>';
	$db_erg = mysql_query( $sql );
	if ( ! $db_erg )
	{
		die('Ungültige Abfrage: ' . mysql_error());
	}
	
	while ($zeile = mysql_fetch_array( $db_erg, MYSQL_ASSOC))
	{
		echo '<a href="index.php?artikelid='.$zeile['id'].'">'.$zeile['name'].'</a><br>';
	}
	
	
	
	
	
	$sql="select * from tests where inhalt like '%$search_input%' or name like '%$search_input%'";
    
    echo '<br><div class="headline-small">Tests</div>';
	$db_erg = mysql_query( $sql );
	if ( ! $db_erg )
	{
		die('Ungültige Abfrage: ' . mysql_error());
	}
	
	while ($zeile = mysql_fetch_array( $db_erg, MYSQL_ASSOC))
	{
		echo '<a href="index.php?testid='.$zeile['id'].'">'.$zeile['name'].'</a><br>';
	}
	
	
	
	
	
	$sql="select * from navi where inhalt like '%$search_input%' or name like '%$search_input%'";
    
    echo '<br><div class="headline-small">Navi</div>';
	$db_erg = mysql_query( $sql );
	if ( ! $db_erg )
	{
		die('Ungültige Abfrage: ' . mysql_error());
	}
	
	while ($zeile = mysql_fetch_array( $db_erg, MYSQL_ASSOC))
	{
		echo '<a href="index.php?naviid='.$zeile['id'].'">'.$zeile['name'].'</a><br>';
	}
?>