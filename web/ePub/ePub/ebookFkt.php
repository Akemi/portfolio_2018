<?php
	//funktion zum erstellen des Inhaltsverzeichnisses
	function buildToc($ebook, $folder){
		$found = false;
		$spineInfo = $ebook->getSpine();
		
		//geht inhaltsverzeichnis daten durch, mit hilfe der lib
		//prüft auf vorhanden-sein
		for($x = 0;$x < count($spineInfo);$x+=1){		
			$content = $ebook->getContentById($spineInfo[$x]);		
			if($found)
				break;			
			for($y = 0;$y < count($spineInfo);$y+=1){
				$manItem = $ebook->getManifestById($spineInfo[$y]);				
				//echo $content."<br>";
				//if(strpos($manItem->href, $content)){
				//	$found = true;
				//	break;				
				//}					
			}		
		}
		//falls alles korrekt
		if(!$found){
			//erstellt das inhaltsverzeichnis als ul
			$content = "";
			$content = $content."<ul>\n";
			$toc = $ebook->getTOC();
			//geht alle elemente durch und fügt sie als li an
			for($x = 0;$x < count($spineInfo);$x+=1){
				if(isset($toc)){
					$cToc = $toc[$x];
					
					//formatiert tag geignet
					$tag = str_replace("/", "-", substr($cToc->fileName, 0, strrpos($cToc->fileName, '.')));
					$tag = str_replace("@", "-", $tag);
					$tag = str_replace(".", "-", $tag);	
					//teste ob tag leer
					if($tag != ""){
						$content = $content."<li class=\"toc-item\" name=\"".$tag."\">".$cToc->name."</li>\n";
					}
					else{
						$content = $content."<br>";
					}
				}else{
					//alternative weg
					$manItem = $ebook->getManifestById($spineInfo[$x]);
					$tag = str_replace("/", "-", substr($manItem->href, 0,strrpos($manItem->href, '.')));
					$tag = str_replace("@", "-", $tag);
					$tag = str_replace(".", "-", $tag);			
					$content = $content."<li>".$tag." - ".$cToc->name."</li>\n";
				}
			}
			//schließt liste
			$content = $content."</ul>\n";
			//echo $content;
			
			//schreibt inhaltsverzeichnis in extra datei um unabhängig vom epub zu sein und
			//die voran gegangen funktionen nicht wieder aufrufen zu müssen.
			$tocContFile = $folder."/toc.html";
			$tocContFileHandler = fopen($tocContFile, 'w') or die("can't open file");
			fclose($tocContFileHandler);
			$current = file_get_contents($tocContFile);
			file_put_contents($tocContFile, $content);
		}
	}
	
	//ließt das inhaltsverzeichnis und schreibt is and gegeben position.
	function readToc($md5_hash){
		if(is_file("books/".$md5_hash."/toc.html")){
			$ebookContent = file_get_contents("books/".$md5_hash."/toc.html");
			echo $ebookContent;
		}
	
	}
	
	//hilfsfunktion für die benutzte lib
	function editToc($content, $ebook){
		$spineInfo = $ebook->getSpine();	
		for($x = 0;$x < count($spineInfo);$x+=1){
			$manItem = $ebook->getManifestById($spineInfo[$x]);
			$tag = substr($manItem->href, 0,strrpos($manItem->href, '.'));		
			$content = str_replace($manItem->href, "#".$tag, $content);
		}
		return $content;
	}
	
	//gibt die gewollte information aus dem epub data objekt zurück
	//hilfsfunktion zum erstellen der unabhängigen daten
	//zum vereinfachen der epubData() funktion die von der lib vorgegeben war
	function getEpubData($ebook, $data){
		$returnData = "";
		
		if($data == "title"){
			$returnData = epubData($ebook->getDcTitle());
		}
		else if($data == "creator"){
			$returnData = epubData($ebook->getDcCreator());
		}
		else if($data == "desc"){
			$returnData = epubData($ebook->getDcDescription());
		}
		else if($data == "isbn"){
			$returnData = epubData($ebook->getDcIdentifier());
		}
		else if($data == "lang"){
			$returnData = epubData($ebook->getDcLanguage());
		}
		else if($data == "publisher"){
			$returnData = epubData($ebook->getDcPublisher());
		}
		else if($data == "date"){
			$returnData = epubData($ebook->getDcDate());
		}
		
		return $returnData;
	}
	
	//ließt die unabhängige xml datei mit allen wichtigen datein
	//und gibt die gewollte information zurück
	function getEpubDataXML($md5_hash, $data){
		$returnData = "";
		
		if(is_dir("books/".$md5_hash)){
		
			$doc = new DOMDocument();
			$doc->load('books/'.$md5_hash.'/info.xml');
			
			$books = $doc->getElementsByTagName("book");
			foreach($books as $book){
				$titles = $book->getElementsByTagName("title");
				$title = $titles->item(0)->nodeValue;
				
				$authors = $book->getElementsByTagName("author");
				$author = $authors->item(0)->nodeValue;
				
				$descs = $book->getElementsByTagName("desc");
				$desc = $descs->item(0)->nodeValue;
				
				$isbns = $book->getElementsByTagName("isbn");
				$isbn = $isbns->item(0)->nodeValue;
				
				$langs = $book->getElementsByTagName("lang");
				$lang = $langs->item(0)->nodeValue;
				
				$publishers = $book->getElementsByTagName("publisher");
				$publisher = $publishers->item(0)->nodeValue;
				
				$dates = $book->getElementsByTagName("date");
				$date = $dates->item(0)->nodeValue;
			}

			if($data == "title"){
				$returnData = $title;
			}
			else if($data == "creator"){
				$returnData = $author;
			}
			else if($data == "desc"){
				$returnData = $desc;
			}
			else if($data == "isbn"){
				$returnData = $isbn;
			}
			else if($data == "lang"){
				$returnData = $lang;
			}
			else if($data == "publisher"){
				$returnData = $publisher;
			}
			else if($data == "date"){
				$returnData = $date;
			}
		}
		return $returnData;
	}
	
	//hilfsfunktion von der lib und epub daten zu extrahieren.
	function epubData($data){
		$info = "";
		if(is_array($data)){
			foreach($data as $element){
				if($info == "")
					$info = $element;
				else
					$info = $info.", ".$element;
			}
			$data = $info;
		}
			return $data;
	}
	
	//funktion um das epub als selbständige datei und formatiert zu speichern
	//erstellt unabhängigkeit vom epub file
	function writeEpub($ebook, $spineInfo, $folder){
		//erstellt inhaltsverzeichnis
		buildToc($ebook, $folder);
		
		//erstellt den content als einen großen string um ihn als eine datei zu speichern
		//fügt hilfs-divs für spätere positionsbestimmung hinzu
		$epubCont = "";
		for($x = 0;$x < count($spineInfo);$x+=1){		
			$manItem = $ebook->getManifestById($spineInfo[$x]);				
			$epubCont .= "<div class=\"chapter\">";
			$secname = str_replace("/", "-", substr($manItem->href, 0,strrpos($manItem->href, '.')));
			$secname = str_replace("@", "-", $secname);
			$secname = str_replace(".", "-", $secname);	
			$epubCont .= "<div id=\"".$secname."\" class=\"book-chapter-helper\"></div>\n";
			$epubCont .= editToc($content = $ebook->getContentById($spineInfo[$x]), $ebook);
			$epubCont .= "</div>\n";
		}
		
		//hilfs-div zur positionsbestimmung des endes
		$epubCont .= "<div id=\"book-end\"></div>\n";
		
		//testet ob epub 2.0, daten struktur des jeweiligen falls wird gespeichert
		if(is_dir($folder."/epub/OEBPS")){
			$resourceFolder = $folder."/epub/OEBPS/";
		}
		else if(is_dir($folder."/epub/OPS")){
			$resourceFolder = $folder."/epub/OPS/";
		}
		//testet ob kompatible ePub 1.0 datei
		if($resourceFolder == ""){
			$dirs = scandir($folder."/epub");
			$count = 0;
			$changeDir = "";
			foreach($dirs as $dir)  {
				if(is_dir($folder."/epub/".$dir) && $dir != "." && $dir != ".." && $dir != "META-INF"){
					$count = $count + 1;
					$changeDir = $dir;
				}
			}
			
			if($count == 1){
				echo "Es wurde eine kompatible ePub 1.0 Datei erkannt. Sie konnte gelesen werden aber es kann zu Problemen kommen.<br>";
				rename($folder."/epub/".$changeDir, $folder."/epub/OEBPS");
				$resourceFolder = $folder."/epub/OEBPS/";
			}
			else{
				echo "Es wurde keine kompatible ePub 1.0/2.0 Datei erkannt, oder die Datei ist nicht Standard-Conform.<br>";
				echo "Es wird nur OPS und OEBPS unterstützt.<br>";
				echo "ePub 1.0 Dateien werden eingeschränkt unterstützt und es könnte zu Problemen kommen.<br>";
				return;
			}
			
		}
		
		//recursive iteration für alles extrahierten daten des epubs
		//es wird nach allen .css dateien gesucht da diese geändert werden müssen
		//ansonsten könnte es zu komplikation und style überschreibungen kommen
		$resourceFolderRec = new RecursiveDirectoryIterator($resourceFolder);
		$resourceFolderIter = new RecursiveIteratorIterator($resourceFolderRec);
		$regex1    = new RecursiveRegexIterator($resourceFolderRec,'%([^0-9]|^)(?<!/.Trash-)[0-9]*$%');
		$resourceFolderIter2 = new RecursiveIteratorIterator($regex1); 
		$regex2    = new RegexIterator($resourceFolderIter2,'/\.css$/i');
		
		//geht alle gefunden .css dateien druch
		foreach($regex2 as $cssFiles){
			//öffnet datei lesend
			$cssFilesHandle = fopen($cssFiles,"r");
			$cssFilesContent = fread($cssFilesHandle, filesize($cssFiles));
			
			//hier werden classen, id, usw. des epub stylesheets mit anderen id ersetzt um überschreibung vor zu beugen. 
			$cssFilesContent = str_replace("body\n\r", "#book-content", $cssFilesContent);				//ersetzt body mit #book-content
			$cssFilesContent = str_replace("body\n", "#book-content", $cssFilesContent);				//folgende funktionen decken	
			$cssFilesContent = str_replace("body\r", "#book-content", $cssFilesContent);				//verschiedene fälle ab
			$cssFilesContent = str_replace("body ", "#book-content", $cssFilesContent);					//
			$cssFilesContent = str_replace("body{", "#book-content", $cssFilesContent);					//
			$cssFilesContent = str_replace("body,", "#book-content", $cssFilesContent);
			$cssFilesContent = str_replace("body.", "#book-content", $cssFilesContent);
			$cssFilesContent = str_replace("h1", "#book-content h1", $cssFilesContent);					//ersetzt h1 mit #book-content h1 (nested in neuem 'body' element)
			$cssFilesContent = str_replace("h2", "#book-content h2", $cssFilesContent);					//für h2
			$cssFilesContent = str_replace("h3", "#book-content h3", $cssFilesContent);					//h3
			$cssFilesContent = str_replace("h4", "#book-content h4", $cssFilesContent);					//h4
			$cssFilesContent = str_replace("h5", "#book-content h5", $cssFilesContent);					//h5
			$cssFilesContent = str_replace("h6", "#book-content h6", $cssFilesContent);					//h6
			$cssFilesContent = str_replace("blockquote", "#book-content blockquote", $cssFilesContent);	//ersetzt blockquote mit nested blockquote
			$cssFilesContent = str_replace("sup", "#book-content sup", $cssFilesContent);				//gleiche für sup
			$cssFilesContent = str_replace("sc", "#book-content sc", $cssFilesContent);					//gleiche für sc
			
			//öffnet datei schreibend und ändert diese datei
			$cssFilesHandle = fopen($cssFiles, "w");
			fwrite($cssFilesHandle, $cssFilesContent);
			fclose($cssFilesHandle);
		}
		
		function linkReplace($treffer) { 
			//echo $treffer[1].strpos($treffer[1], "http").strpos($treffer[1], "www")."<br>";
			if(strpos($treffer[1], "http") === false && strpos($treffer[1], "www") === false && strpos($treffer[1], "mailto:") === false ){
				return "href=\"".$treffer[1]."\"";
			}
			else {
				return "href-tmp=\"".$treffer[1]."\"";
			}
		}
		
		//da der content bis jetzt noch unnötige, doppelte und obsolete tags enthält, da
		//einfach nur mehrere html dokumente zusammengefügt wurden, müssen diese entfernt werden
		//macht das dokument valide, lesbar and schön formatiert
		//auserdem müssen links neu gesetzt werden da sich das neu gespeicherte dokument
		//von der relativen position der vorherigen dokumente unterscheidet
		$epubCont = preg_replace_callback("/href=\"(.*)\"/Usi", 'linkReplace', $epubCont);
		$epubCont = str_replace('href="', 'href="'.$resourceFolder, $epubCont);				//ersetzt href mit dem richtigen pfad zu den extrahierten daten
		$epubCont = str_replace('href-tmp="', 'href="', $epubCont);
		$epubCont = str_replace('src="', 'src="'.$resourceFolder, $epubCont);				//das gleiche für src
		$epubCont = str_replace($resourceFolder.'../', $resourceFolder, $epubCont);		//ersetzt den relativen resource link zum richtigen epub 1.0/2.0 pfad
		$epubCont = str_replace('<head>', '', $epubCont);									//löscht das head tag da nicht benötitgt
		$epubCont = str_replace('</head>', '', $epubCont);									//
		$epubCont = str_replace('</html>', '', $epubCont);									//löscht clse tag da nicht benötigt
		$epubCont = str_replace('<body>', '', $epubCont);									//löscht body tag da nicht benötigt
		$epubCont = str_replace('</body>', '', $epubCont);									//
		
		//komplizierte ersetzungen durch reguläre ausrdücke da tags untershiedliche
		//attribute haben können
		$epubCont = preg_replace("/\<a[[:blank:]]class(.*)\/\>/Usi", "", $epubCont);
		$epubCont = preg_replace("/\<body(.*)\"\>/Usi", "", $epubCont); 
		$epubCont = preg_replace("/\<\?xml(.*)\?\>[\n\r]/Usi", "", $epubCont); 				//löscht xml tag da nicht benötigt
		$epubCont = preg_replace("/\<\!DOCTYPE(.*)\"\>[\n\r]/Usi", "", $epubCont);  		//löscht doctype da nicht benötigt
		$epubCont = preg_replace("/\<title(.*)\<\/title\>[\n\r]/Usi", "", $epubCont);  		//löscht title tag da nicht benötigt
		$epubCont = preg_replace("/\<meta(.*)\/\>/Usi", "", $epubCont); 				//löscht meta daten da nicht benötigt
		$epubCont = preg_replace("/\<html(.*)\"[[:blank:]]*\>[\n\r]/Usi", "", $epubCont); 				//löscht html begin tag da nicht benötigt
		
		$epubCont = preg_replace("/[\n\r][\n\r][\n\r]/","\n",$epubCont); 							//löscht viele CRs für hübsche formatierung
		$epubCont = preg_replace("/[\n\r][\n\r]/","\n",$epubCont);									//
		$epubCont = preg_replace("/\<div id=\"chapter\"\>/", "\n<div id=\"chapter\">", $epubCont); 	//fügt CR vor jedem kapitel hinzu
		
		
		//speichert den formatierten content in eine datei for unabhängigkeit vom epub
		$epubContFile = $folder."/epub.html";
		$epubContFileHandler = fopen($epubContFile, 'w') or die("can't open file");
		fclose($epubContFileHandler);
		$current = file_get_contents($epubContFile);
		file_put_contents($epubContFile, $epubCont);
	}
	
	//ließt den epub content anhand von der hash übergabe
	function readEpub($md5_hash){
		if(is_file("books/".$md5_hash."/epub.html")){
			$ebookContent = file_get_contents("books/".$md5_hash."/epub.html");
			echo $ebookContent;
		}
		else {
			echo "Buch existiert nicht.";
		}
	}
	
	//funktion zum erstellen  der xml datei für die benötigten epub infos, zur unabhängigkeit zum epub file
	function writeEpubInfo($books, $folder){
		$doc = new DOMDocument();
		$doc->formatOutput = true;
		
		$r = $doc->createElement("books");
		$doc->appendChild($r);
		
		foreach($books as $book)
		{
			$b = $doc->createElement("book");
			
			$title = $doc->createElement("title");
			$title->appendChild(
			$doc->createTextNode($book['title'])
			);
			$b->appendChild($title);
			
			$author = $doc->createElement("author");
			$author->appendChild(
			$doc->createTextNode($book['author'])
			);
			$b->appendChild($author);
			
			$desc = $doc->createElement("desc");
			$desc->appendChild(
			$doc->createTextNode($book['desc'])
			);
			$b->appendChild($desc);
			
			$isbn = $doc->createElement("isbn");
			$isbn->appendChild(
			$doc->createTextNode($book['isbn'])
			);
			$b->appendChild($isbn);
			
			$lang = $doc->createElement("lang");
			$lang->appendChild(
			$doc->createTextNode($book['lang'])
			);
			$b->appendChild($lang);
			
			$publisher = $doc->createElement("publisher");
			$publisher->appendChild(
			$doc->createTextNode($book['publisher'])
			);
			$b->appendChild($publisher);
			
			$date = $doc->createElement("date");
			$date->appendChild(
			$doc->createTextNode($book['date'])
			);
			$b->appendChild($date);
			
			$r->appendChild($b);
		}

		$cont = $doc->saveXML();
			
		$contFile = $folder."/info.xml";
		$contFileHandler = fopen($contFile, 'w') or die("can't open file");
		fclose($contFileHandler);
		$current = file_get_contents($contFile);
		file_put_contents($contFile, $cont);
	}
	
	//funktion zum lesen der xml info file
	//erstellt eine formatierte ausgabe für den info screen und die bibliothek
	function createEpubInfo($md5_hash){
		if(is_dir("books/".$md5_hash)){
			//öffnet und ließt xml datei
			$doc = new DOMDocument();
			$doc->load('books/'.$md5_hash.'/info.xml');
			
			$books = $doc->getElementsByTagName("book");
			foreach($books as $book){
				$titles = $book->getElementsByTagName("title");
				$title = $titles->item(0)->nodeValue;
				
				$authors = $book->getElementsByTagName("author");
				$author = $authors->item(0)->nodeValue;
				
				$descs = $book->getElementsByTagName("desc");
				$desc = $descs->item(0)->nodeValue;
				
				$isbns = $book->getElementsByTagName("isbn");
				$isbn = $isbns->item(0)->nodeValue;
				
				$langs = $book->getElementsByTagName("lang");
				$lang = $langs->item(0)->nodeValue;
				
				$publishers = $book->getElementsByTagName("publisher");
				$publisher = $publishers->item(0)->nodeValue;
				
				$dates = $book->getElementsByTagName("date");
				$date = $dates->item(0)->nodeValue;
				
				
			}
			//ersetzt nicht vorhandenen daten mit Not Available
			if( $title == ""){$title = "N/A";}
			if( $author == ""){$author = "N/A";}
			if( $desc == ""){$desc = "N/A";}
			if( $isbn == ""){$isbn = "N/A";}
			if( $lang == ""){$lang = "N/A";}
			if( $publisher == ""){$publisher = "N/A";}
			if( $date == ""){$date = "N/A";}
			
			//erstellt formatierte ausgabe
			$epubInfo = "<img alt=\"cover\" src=\"books/".$md5_hash."/cover_big.jpg\">\n";
			$epubInfo .= "<h1>".$title." (".$lang.")</h1>\n";
			$epubInfo .= "<h2>von ".$author."</h2>\n";
			$epubInfo .= "<h3>erschienen am ".$date."</h3>\n";
			$epubInfo .= "<h3>".$publisher."</h3>\n";
			$epubInfo .= "<h3>ISBN<br>".$isbn."</h3>\n";
			$epubInfo .= "<h4>Inhaltsangabe</h4>\n".$desc."\n";
			
			return $epubInfo;
		}
		else {
			echo "Buch existiert nicht.";
		}
	}
	
	//funktion zum auslesen alle epubs
	//erstellt eine formatierte liste für die biblithekt
	//anzeige aller hochgeladener epubs
	function readBib(){
		//scant den richtigen ordner
		$dirs = scandir("books/");
		$bibcont = "";
		//geht alle relevanten ordner/ebooks durch und erstellt formatierte liste + infos
		foreach($dirs as $dir)  {
			if(is_dir("books/".$dir) && $dir != "." && $dir != ".."){
				$bibcont .= "<li>";
				$bibcont .= "<img alt=\"book\" src=\"books/".$dir."/cover_small.jpg\">\n";
				$bibcont .= "<div class=\"bib-book-desc\">"."<div class=\"bib-delete\" name=\"".$dir."\"></div>
				<div class=\"bib-link\" name=\"".$dir."\"></div>".createEpubInfo($dir)."</div>";
				$bibcont .= "</li>\n";
			}
		}
		
		echo $bibcont;
	}
	
	//funktion zum rekursiven löschen eines ordners, zum löschen einer hochgeladenen epub file struktur.
	function rrmdir($dir) { 
		if (is_dir($dir)) { 
			$objects = scandir($dir); 
			foreach ($objects as $object) { 
				if ($object != "." && $object != "..") { 
					if (filetype($dir."/".$object) == "dir") rrmdir($dir."/".$object); else unlink($dir."/".$object); 
				} 
			} 
			reset($objects); 
			rmdir($dir); 
		} 
	} 
	
	//funktion zum unterscheiden der platform
	//ob table/touch device oder normaler PC
	function getPlatform(){
		if (strstr($_SERVER['HTTP_USER_AGENT'],'Android') || strstr($_SERVER['HTTP_USER_AGENT'],'iPhone') || strstr($_SERVER['HTTP_USER_AGENT'],'iPad')){ 
			return "touch";
		} 
		else{
			return "normal";
		}
	}
?>