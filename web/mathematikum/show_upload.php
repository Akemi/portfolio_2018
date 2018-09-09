<div class="headline-big">Uploads</div>

<div class="headline-small">Bilder</div>
<ul id="upload-list">
<?php
	$dir = "uploads";
 
	$uploads = scandir($dir);
 
	foreach ($uploads as $bild) {
		$fileinfo = pathinfo($dir."/".$bild);
		$extension = $fileinfo['extension'];
 
 		if (($bild != "." && $bild != ".."  && $bild != "_notes" && $fileinfo['basename'] != "Thumbs.db") && ($extension == png || $extension == gif || $extension == jpg)) {
?>
    <li>
    	<a class="loschen-link" href='javascript:delupload("<?php echo rawurlencode($fileinfo['basename']);?>")'>x</a>
        <a href="<?php echo $fileinfo['dirname']."/".rawurlencode($fileinfo['basename']);?>">
        <img src="<?php echo $fileinfo['dirname']."/".rawurlencode($fileinfo['basename']);?>" alt="Vorschau"></a><br>
    	<?php echo $dir."/".$fileinfo['filename'].".".$fileinfo['extension']; ?>
    </li>
<?php
		};
	};
?>
</ul>
<div style="clear: both;"></div>



<br><div class="headline-small">Videos</div>
<ul id="upload-list">
<?php
	$dir = "uploads";
 
	$uploads = scandir($dir);
 
	foreach ($uploads as $bild) {
		$fileinfo = pathinfo($dir."/".$bild);
		$extension = $fileinfo['extension'];
 
 		if (($bild != "." && $bild != ".."  && $bild != "_notes" && $fileinfo['basename'] != "Thumbs.db") && ($extension == mp4 || $extension == webm || $extension == ogg)) {
?>
    <li>
        <a class="loschen-link" href='javascript:delupload("<?php echo rawurlencode($fileinfo['basename']);?>")'>x</a>
        <video controls="controls">
        <source src="<?php echo $fileinfo['dirname']."/".rawurlencode($fileinfo['basename']);?>" type="video/<?php echo $fileinfo['extension'];?>">
        Your browser does not support the video tag.</video>
    	<a href="<?php echo $fileinfo['dirname']."/".rawurlencode($fileinfo['basename']);?>"><?php echo $dir."/".$fileinfo['filename'].".".$fileinfo['extension']; ?></a>
    </li>
<?php
		};
	};
?>
</ul>
<div style="clear: both;"></div>



<br><div class="headline-small">Audio</div>
<ul id="upload-list">
<?php
	$dir = "uploads";
 
	$uploads = scandir($dir);
 
	foreach ($uploads as $bild) {
		$fileinfo = pathinfo($dir."/".$bild);
		$extension = $fileinfo['extension'];
 
 		if (($bild != "." && $bild != ".."  && $bild != "_notes" && $fileinfo['basename'] != "Thumbs.db") && ($extension == mp3 || $extension == wav)) {
?>
    <li style="width:220px; height:100px;">
        <a class="loschen-link" href='javascript:delupload("<?php echo rawurlencode($fileinfo['basename']);?>")'>x</a>
        <audio controls="controls" preload="auto" autobuffer>
        <source src="<?php echo $fileinfo['dirname']."/".rawurlencode($fileinfo['basename']);?>" type="audio/<?php echo $fileinfo['extension'];?>">
        Your browser does not support the audio tag.</audio>
    	<a href="<?php echo $fileinfo['dirname']."/".rawurlencode($fileinfo['basename']);?>"><?php echo $dir."/".$fileinfo['filename'].".".$fileinfo['extension']; ?></a>
    </li>
<?php
		};
	};
?>
</ul>
<div style="clear: both;"></div>