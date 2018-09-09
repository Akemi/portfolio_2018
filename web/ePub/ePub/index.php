<?php    
	require_once('ebookFkt.php');
	require_once('ebookRead.php');
	require_once('ebookData.php');
	
	if(isset($_GET["id"])){
		$id = $_GET["id"];
	}
	else {
		$id = "";
	}


?>

<!DOCTYPE html>
<html lang="de">
<head>
	<meta charset="utf-8" />
	<title>EPub-Reader - <?php if($id != ""){echo getEpubDataXML($id, "title");} else{echo "Leere Seite.";} ?></title>
	
	<link rel="stylesheet" type="text/css" href="content/style/style.css" />
	<script type="text/javascript" src="content/scripts/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="content/scripts/epub-scripts.js"></script>
	<?php 
		//fügt css and scripts hinzu falls tablet/touch bases system
		if(getPlatform() == "touch"){
			echo "<script type=\"text/javascript\" src=\"content/scripts/jquery.touchSwipe.js\"></script>";
			echo "<script type=\"text/javascript\" src=\"content/scripts/epub-scripts-touch.js\"></script>";
			echo "<link rel=\"stylesheet\" type=\"text/css\" href=\"content/style/style-touch.css\" />";
		}
	?>
</head>
<body>
	<div id="book-info-wrapper" class="visible">
	 	<div id="book-info">
	 		<?php
  				//echo $id;
  				if($id != ""){
  					echo createEpubInfo($id);
  				}
  						
  			?>
	 	</div>
	</div>

	<div id="book">
		<article id="book-pages" class="book-twosided">
  			<aside id="book-table_of_content">Inhaltsverzeichnis</aside>
			<div id="book-middle-line"></div>
			
			<div id="book-toc" class="hidden">
				<h1>Inhaltsverzeichnis</h1>
				<?php
					//echo $id;
					if($id != ""){
						readToc($id);
					}
							
				?>
			</div>
			
			<aside id="book-bookmark">Lesezeichen</aside>
  			
  			
  			<div id="book-content-wrapper">
  				<section id="book-content">
  					<?php
  						//echo $id;
  						if($id != ""){
  							readEpub($id);
  						}
  						
  					?>
  				</section>
			</div>
		</article>	
		
		<nav id="toolbar">
			<div class="toolbar-buttons" id="toolbar-chapter-back"></div>
			<div class="toolbar-buttons" id="toolbar-page-back"></div>
			<div class="toolbar-buttons" id="toolbar-page-forward"></div>
			<div class="toolbar-buttons" id="toolbar-chapter-forward"></div>
			<div class="toolbar-buttons toolbar-oneside" id="toolbar-page-view"></div>
			<div class="toolbar-buttons" id="toolbar-zoom-in"></div>
			<div class="toolbar-buttons" id="toolbar-zoom-out"></div>
			<div class="toolbar-buttons" id="toolbar-info"></div>
		</nav>
	</div>

</body>
</html>
