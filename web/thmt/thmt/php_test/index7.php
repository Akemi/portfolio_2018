<?php

include 'libs/ganon.php';

$html = file_get_dom('URL');

$html('p');
$html('.myClass');
$html('#myID');
$html('td:first-child');
$html('table table');
$html('li:odd');
$html('a', 2);
$html('a', -1);

$node->parent;
$node->getSibling(-1);
$node->getSibling(1);
$node->href;
$node->id;
$node->html();
$node->getInnerText();
$node->getPlainText();
$node->firstChild();
$node->lastChild();

foreach($ele('font') as $fontEle) {
    $fontEle->setOuterText($fontEle->getInnerText());
}



$node->id = 'myid';
$node->class = 'myclass';
$node->addAttribute('href', 'www.test.com');
$node->href = 'www.test.com';
$node->href = null;
$node->deleteChild(2);
$node->clear();
$node->setOuterText('<a>New</a>');
$node->setInnerText('<a>New</a>');
$node->setPlainText('New Plain Text');



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