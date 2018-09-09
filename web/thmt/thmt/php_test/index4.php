<?php

include 'libs/ganon.php';

$html = file_get_dom('https://homepages-fb.thm.de/plaene/stundenplan/HTML.htm');

foreach($html('body') as $ele) {
    $ele->setInnerText($ele('table.Tabellengitternetz')[0]->getOuterText());
}
foreach($ele('table tr:last-child') as $tdEle) {
    $tdEle->setOuterText('');
}

foreach($html('table tr td') as $ele) {
    $ele->setInnerText($ele->getPlainText());
}

$degreeData = array();

foreach($html('table tr') as $ele) {
    $array = $ele('td');

    $degree1ID   = trim($array[0]->getPlainText());
    $degree1Name = trim($array[1]->getPlainText());
    $degree2ID   = trim($array[2]->getPlainText());
    $degree2Name = trim($array[3]->getPlainText());

    if (preg_match('/[A-Za-z]+/', $degree1ID) && preg_match('/[A-Za-z0-9]+/', $degree1Name)) {
        $degreeData[$degree1ID] = preg_replace('/,/', '', $degree1Name);
    }
    if (preg_match('/[A-Za-z]+/', $degree2ID) && preg_match('/[A-Za-z0-9]+/', $degree2Name)) {
        $degreeData[$degree2ID] = preg_replace('/,/', '', $degree2Name);
    }

}

var_dump($degreeData);

echo $html;

?>