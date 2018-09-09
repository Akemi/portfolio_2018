<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8" />
    <title>THM Timetable Parser</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<?php
$url = $_SERVER['HTTP_HOST'].$_SERVER['SCRIPT_NAME'];
$url = "http://".substr($url, 0, strrpos($url, "/"));
?>

<div class="infobox">
    <div class="info">
        <a class="header" href="thmtdata.php?action=teacherlist&amp;type=json">Dozentenliste</a>
        Liste mit Zuordung von Dozentenkürzel und Namen.
    </div><div class="info">
        <a class="header" href="thmtdata.php?action=leacturelist&amp;type=json">Modulliste</a>
        Liste mit Zuordnung von Modulkürzel und Namen.
    </div><div class="info">
        <a class="header" href="thmtdata.php?action=degreelist&amp;type=json">Studiengangsliste</a>
        Liste mit Zuordnung von Studiengangskürzel und Namen.
    </div><br>
    <div class="info" style="max-width: 904px; width: auto;">
        <a class="header" href="thmtdata.php?action=timetablelist&amp;type=json">Stundenplanliste</a>
        Liste von allen Stundenplänen. Zurodnung von Stundenplan ID und Daten (Studiengangskürzel, Studiengangsname, Studiengangskennung, eventuelle Gruppenvariante).
        Mit dieser Liste lassen sich die URLs zu den zugehörigen Stundenplänen generieren. Siehe Beispielslink und Liste aller
        Stundenpläner.<br><br>
        <?php echo $url.'/'; ?>thmtdata.php?action=timetable&id=<span>&lt;TIMETABLE KEY&gt;</span>&type=<span>&lt;json|xml&gt;</span>
    </div>

</div>

<div class="list">

<?php

//https://code.google.com/p/ganon/
//http://www.pdfparser.org
include 'libs/ganon.php';
include 'libs/vendor/autoload.php';
include 'libs/lib.php';

$time_start = microtime(true);
$thmt = new THMT();
$degreeID = '';
$new = false;

foreach($thmt->timetableList as $key => $value) {
    if($degreeID == $value['degreeID'].$value['degreeCode']) {
        echo '<a href="thmtdata.php?action=timetable&amp;id='.$key.'&amp;type=json">'.$value['semester'].$value['timetableVariant'].'</a> ';
        $new = false;
    }
    else {
        if ($new == true) echo '</div></div>';
        $new = true;
        $degreeID = $value['degreeID'].$value['degreeCode'];
        echo '<div id="'.$value['degreeID'].$value['degreeCode'].'" class="degree">'.'<div class="header">'.$value['degreeName'].' '.$value['degreeCode'].'</div><div> Semester: ';
        echo '<a href="thmtdata.php?action=timetable&amp;id='.$key.'&amp;type=json">'.$value['semester'].$value['timetableVariant'].'</a> ';
    }
    $new = true;
}
echo '</div></div>';
$time_end = microtime(true);
$time = $time_end - $time_start;
?>
</div>

<div id="footer">&copy; 2015 Franz Weisflug, generated in <?php echo $time; ?> sec</div>

</body>
</html>