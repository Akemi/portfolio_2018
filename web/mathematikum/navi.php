				<ul>
					<?php     
					    $sql_navi="select * from navi";
					    $sql_artikel="select * from matheeintraege ORDER BY name";
					    $sql_test="select * from tests ORDER BY name";
    
						$db_erg_navi = mysql_query($sql_navi);
						$db_erg_test = mysql_query($sql_test);
						$db_erg_artikel = mysql_query($sql_artikel);
						
						if (! $db_erg_navi){
							die('Ungültige Abfrage: ' . mysql_error());
						}

						while ($zeile_navi = mysql_fetch_array( $db_erg_navi, MYSQL_ASSOC)){
							echo "<li>";
							
							if($zeile_navi['name']=='Artikel' || $zeile_navi['id']=='2'){
								echo "<span class='nolink'>".$zeile_navi['name']."</span>";
								echo "<ul>";
								while ($zeile_artikel = mysql_fetch_array( $db_erg_artikel, MYSQL_ASSOC)){
									echo "<li>";
									echo "<a href='index.php?artikelid=".$zeile_artikel['id']."'>";
									echo $zeile_artikel['name'];
									echo "</a>";
									echo "</li>";
								}
								echo "</ul>";
							}
							
							else if ($zeile_navi['name']=='Quiz' || $zeile_navi['id']=='3'){
								echo "<span class='nolink'>".$zeile_navi['name']."</span>";
								echo "<ul>";
								echo "<li><a href='index.php?testid=rnd'>Zufalls-Quiz</a></li>";
								while ($zeile_test = mysql_fetch_array( $db_erg_test, MYSQL_ASSOC)){
									echo "<li>";
									echo "<a href='index.php?testid=".$zeile_test['id']."'>";
									echo $zeile_test['name'];
									echo "</a>";
									echo "</li>";
								}
								echo "</ul>";
							}
							
							else {
								echo "<a href='index.php?naviid=".$zeile_navi['id']."'>";
								echo $zeile_navi['name'];
								echo "</a>";
							}
							echo "</li>";
						}

						mysql_free_result($db_erg_navi);
						mysql_free_result($db_erg_test);
						mysql_free_result($db_erg_artikel);
					?>
				</ul>