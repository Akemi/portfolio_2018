<?php
	
	//epub reader requirements	
	require_once('ebookRead.php');
	require_once('ebookData.php');
	require_once('ebookFkt.php');
	require_once('resize-class.php');

	//generiert für die datei und das datum einen eindeutigen hash
	//gleiche datei am gleichen tag bekommt gleichen hash, gleiche datei anderer tag einen anderen hash
	//kann mit stunden minuten und sekunden erweitert werden falls nötig
	$md5 = md5_file($HTTP_POST_FILES['epub_files']['tmp_name'][0]);
	$md5_hash = md5($md5.date("Ymd"));	//jahr, monat, tag für den hash. kann um uhrzeit ergänzt werden
	
	//dateiendung der beiden hochgeladenen dateien.
	$fileExtension1 = pathinfo($HTTP_POST_FILES['epub_files']['name'][0], PATHINFO_EXTENSION);
	$fileExtension2 = pathinfo($HTTP_POST_FILES['epub_files']['name'][1], PATHINFO_EXTENSION);
	
	//testet ob epub und image file
	if($fileExtension1 != "epub"){
		echo "Erste Datei muss ein ePub File sein.<br>\n";
	}
	if(!($fileExtension2 == "jpg" | $fileExtension2 == "png" | $fileExtension2 == "gif" | $fileExtension2 == "jpeg")){
		echo "Zweite Datei muss ein jpg, jpeg, png oder gif File sein.\n";
	}
	
	//frage ob dateiendungen richtig
	if($fileExtension1 == "epub" && ($fileExtension2 == "jpg" | $fileExtension2 == "png" | $fileExtension2 == "gif" | $fileExtension2 == "jpeg")){
	
		//existiert filestruktur/datei schon mache nichts
		if(!is_dir("books/".$md5_hash)){
			mkdir("books/".$md5_hash);		//erstelle ordner
			
			//pfad für beide hochgeladenen dateien
			$path1 = "books/".$md5_hash."/epub.epub";
			$path2 = "books/".$md5_hash."/cover_original.".pathinfo($HTTP_POST_FILES['epub_files']['name'][1], PATHINFO_EXTENSION);
			
			
			//kopiert datein an den richtigen ort vom temp folder
			copy($HTTP_POST_FILES['epub_files']['tmp_name'][0], $path1);
			copy($HTTP_POST_FILES['epub_files']['tmp_name'][1], $path2);
			
			//erstellt ein großes cover image und ein thumbnail
			$resizeObj = new resize($path2);
			$resizeObj -> resizeImage(200, 250, 'crop');
			$resizeObj -> saveImage("books/".$md5_hash."/cover_big.jpg", 100);
			$resizeObj -> resizeImage(55, 69, 'crop');
			$resizeObj -> saveImage("books/".$md5_hash."/cover_small.jpg", 100);
				
			
			//enzipt das epub dokument
			//wird später gebraucht um bilder stylsheets und andere resourcen dahin zu linken
			$zip = new ZipArchive;
			$res = $zip->open($path1);
			if ($res === TRUE) {
				$zip->extractTo("books/".$md5_hash."/epub/");
				$zip->close();
			}
			else {
				echo 'failed';
			}
			
			//läd epub mit der ebookReader lib
			$ebookk = new ebookRead("books/".$md5_hash."/epub.epub");
			//erstellt ebook data objekt and andere info objekte
			$ebookData = $ebookk->getEBookDataObject();
			$ebook = new ebookRead($ebookData);
			$spineInfo = $ebook->getSpine();
			
			//schreibt alle wichtigen infos in ein array
			$books = array();
			$books [] = array(
			'title' => getEpubData($ebook, "title"),
			'author' => getEpubData($ebook, "creator"),
			'desc' => getEpubData($ebook, "desc"),
			'isbn' => getEpubData($ebook, "isbn"),
			'lang' => getEpubData($ebook, "lang"),
			'publisher' => getEpubData($ebook, "publisher"),
			'date' => getEpubData($ebook, "date")
			);
			
			//übergibt das array and eine funktion um dieses infos in eine extra xml datei zu schreiben
			//infos sind so unabhängig vom epub file und können auch nach dem löschen dieser genutzt werden.
			writeEpubInfo($books, "books/".$md5_hash);
			
			//übergibt das ebook data object einer funktion um den inhalt des epubs separat vom epub zu speichern
			//epub file muss so nicht immer wieder ausgelesen werden, daten werden erstellt ohne das epub später zu benötigen
			writeEpub($ebook, $spineInfo, "books/".$md5_hash);
			
			//ließt die erstellte datei von vorheriger funktion and gibt den inhalt aus
			//gibt auch den link zum verteilen aus
			if(is_file("books/".$md5_hash."/epub.html")){
				$ebookContent = file_get_contents("books/".$md5_hash."/epub.html");
				echo "<h1><a href=\"index.php?id=".$md5_hash."\">Hier findet man das EPub.</a></h1>\n";
				echo "<section id=\"book-content\">".$ebookContent."</section>";
			}
			else {
				echo "Upload wurde nicht akzeptiert.<br>";
				rrmdir("books/".$md5_hash);
			}
		}
		else {
			//gibt meldung und link aus falls bereits hochgeladen
			echo "Bereits hochgeladen.";
			$ebookContent = file_get_contents("books/".$md5_hash."/epub.html");
			echo "<h1><a href=\"index.php?id=".$md5_hash."\">Hier findet man das EPub.</a></h1>\n";
		}
	}
	
?>