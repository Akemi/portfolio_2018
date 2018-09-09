<?php

include 'libs/ganon.php';
$time_start = microtime(true);

$html = file_get_dom('https://homepages-fb.th-mittelhessen.de/plaene/fachbezeichnung/faecherbez.htm');
$dateString1 = '';
$dateString2 = '';

foreach($html('p') as $ele) {
    $datePlaintext = mb_convert_encoding($ele->getPlainText(), 'UTF-8', 'ISO-8859-1');
    if (strpos($datePlaintext, 'Fächer: ') !== false) {
        $dateString1 = str_replace('Fächer: ', '', $datePlaintext);
    }
    if (strpos($datePlaintext, 'Dozenten: ') !== false) {
        $dateString2 = str_replace('Dozenten: ', '', $datePlaintext);
    }
}

echo $dateString1;
echo $dateString2;

$time_end = microtime(true);
$time = $time_end - $time_start;

echo $time;
//echo $html;
//echo '<pre>';
//var_dump($timetablesData);
//echo '</pre>';
//echo strtotime($dateString);
//echo date('d.m.Y G:i', strtotime($dateString))

?>