<?php
	echo '<div class="headline-big">'.$title_content.'</div>';
?>
<script src="content/scripts/testscripts.js"></script>
<form id="testForm" name="testForm">
<?php

	function linkLenght($treffer) { 
		// $treffer[1] ist die URL 
		$url = trim($treffer[1]); 
		if(substr($url,0,7)!= 'http://' && substr($url,0,7)!= 'uploads' && substr($url,0,7)!= 'mailto:' && substr($url,0,5)!= 'index') 
			$url = "http://".$url;
		// $treffer[2] ist der Ausgabename 
		// wurde kein Name angegeben, wird die URL als Name gewählt 
		if(strlen(trim($treffer[2]))!=0) 
			$linkname = $treffer[2]; 
		else 
			$linkname = $treffer[1]; 
		// legt eine maximale Länge von 50 Zeichen fest 
		// Ausnahme bei [img]-Tags 
		if(strlen($linkname)>100 AND !substr_count(strtolower($linkname), '[bild]') AND !substr_count(strtolower($linkname), '[/bild]'))
			$linkname = substr($linkname, 0, 45-3)."...".substr($linkname, -5);
		// Rückgabelink 
		$ergebnis = "<a href=\"".$url."\" target=\"_blank\">".$linkname."</a>";
		return $ergebnis; 
	} 
	

	
	function picSize($treffer) { 
		return " <img src=\"".$treffer[1]."\" alt=\"".$treffer[1]."\">"; 
	}
	
	function src_umwandeln($a){
		return "[source]uploads/".rawurlencode($a[1])."[/source]";
	}
	
	function source_type($b) {
		if($b[2]==wav) {
			return '<source src="'.$b[1].'.'.$b[2].'" type="audio/'.$b[2].'" />';
		}
		else {
			return '<source src="'.$b[1].'.'.$b[2].'" type="video/'.$b[2].'" />';
		}
	}
	
	$inhalt_content = preg_replace_callback('/\[source\]uploads\/(.*)\[\/source\]/Usi', 'src_umwandeln', $inhalt_content);

	$inhalt_content = preg_replace("/\[fett\](.*)\[\/fett\]/Usi", "<b>\\1</b>", $inhalt_content); 
	$inhalt_content = preg_replace("/\[kursiv\](.*)\[\/kursiv\]/Usi", "<i>\\1</i>", $inhalt_content); 
	$inhalt_content = preg_replace("/\[unterstrichen\](.*)\[\/unterstrichen\]/Usi", "<u>\\1</u>", $inhalt_content); 
	$inhalt_content = preg_replace("/\[farbe=(.*)\](.*)\[\/farbe\]/Usi", "<span style=\"color:\\1;\">\\2</span>", $inhalt_content); 
	$inhalt_content = preg_replace_callback("/\[link=(.*)\](.*)\[\/link\]/Usi", 'linkLenght', $inhalt_content); 
	$inhalt_content = preg_replace_callback("/\[bild\](.*)\[\/bild\]/Usi", 'picSize', $inhalt_content);
	
	$inhalt_content = preg_replace("/\[uberschrift\](.*)\[\/uberschrift\]/Usi", "<div class=\"headline-small\">\\1</div>", $inhalt_content);

	
	$inhalt_content = preg_replace("/\[absatz\](.*)\[\/absatz\]/Usi", "<div class=\"absatz\">\\1<div style=\"clear: both;\"></div></div>", $inhalt_content);
	$inhalt_content = preg_replace("/\[links\](.*)\[\/links\]/Usi", "<div class=\"links\">\\1</div>", $inhalt_content);
	$inhalt_content = preg_replace("/\[rechts\](.*)\[\/rechts\]/Usi", "<div class=\"rechts\">\\1</div>", $inhalt_content);
	$inhalt_content = preg_replace("/\[mitte\](.*)\[\/mitte\]/Usi", "<div class=\"mitte\">\\1</div>", $inhalt_content); 
	
	$inhalt_content = preg_replace("/\[quellen\](.*)\[\/quellen\]/Usi", "<div class=\"headline-small\">Quellen</div><ul id=\"quellen\">\\1</ul>", $inhalt_content); 
	$inhalt_content = preg_replace("/\[eintrag\](.*)\[\/eintrag\]/Usi", "<li>\\1</li>", $inhalt_content); 
	$inhalt_content = preg_replace("/\<\/li\>\<br \/\>/Usi", "</li>", $inhalt_content); 

	$inhalt_content = preg_replace("/\[video\](.*)\[\/video\]/Usi", "<video class=\"video\" controls=\"controls\">\\1 Browser unterstützt Video tag nicht.</video>", $inhalt_content);
	$inhalt_content = preg_replace("/\[audio\](.*)\[\/audio\]/Usi", "<audio class=\"audio\" controls=\"controls\">\\1 Browser unterstützt Audio tag nicht.</audio>", $inhalt_content);
	$inhalt_content = preg_replace_callback("/\[source\](.*)\.(.*)\[\/source\]/Usi", 'source_type', $inhalt_content); 
		
	$inhalt_content = preg_replace("/\<\/div\>\<br \/\>/Usi", "</div>", $inhalt_content);
	
	$inhalt_content = preg_replace("/†/Usi", "", $inhalt_content);
		
		
	
	$inhalt_content = preg_replace("/\[frage=(.*)\](.*)\[\/frage\]/Usi", "<div id=\"\\1\">\\2<div style=\"clear: both;\"></div></div>", $inhalt_content);
	$inhalt_content = preg_replace("/\[antwort=(.*)\,(.*)\](.*)\[\/antwort\]/Usi", "<input type=\"radio\" name=\"r\\1\" value=\"\\2\" style=\"width: 20px;\" /> \\3", $inhalt_content);
	
	echo $inhalt_content;
	
?>
	<input type="text" name="ergebnis" value="<?php echo $losung_content ?>" style="display: none;" /><br />
	<input type="button" name="button" value="Abschicken" style="width: 150px;" onClick="testResults(this.form)" />
</form>


