<?php

include 'libs/ganon.php';

$html = file_get_dom('https://homepages-fb.thm.de/plaene/stundenplan/Kla1.htm');

foreach($html('body') as $ele) {
    $ele->setInnerText($ele('table')[1]->getOuterText());
}

$degreeCodes = array(
    'B'   => "Bachelor",
    'D'   => "Diplom",
    'M'   => "Master",
    'S'   => "Studierende",
    'Alle'   => "Alle Studierende",
);

$timetablesData = array();

foreach($html('table tr td') as $ele) {
    $timetableID = $ele->getPlainText();

    if(strlen($timetableID) >= 3) {
        if($timetableID == 'Alle') {
            $degreeID = $timetableID;
            $degreeCode = $timetableID;
            $semester = '';
            $timetableVariant = '';
        }
        else {
            $degreeID = substr($timetableID, 0, 2);
            $degreeCode = substr($timetableID, 2, 1);
            $semester = '';
            $timetableVariant = '';
            if(strlen($timetableID) >= 4) $semester = substr($timetableID, 3, 1);
            if(strlen($timetableID) >= 5) $timetableVariant = substr($timetableID, 4, 1);
        }

        $timetableObject = ['degreeID'     => $degreeID,
            'degreeName'       => '',
            'degreeCode'       => $degreeCodes[$degreeCode],
            'semester'         => $semester,
            'timetableVariant' => $timetableVariant];

        $timetablesData[$timetableID] = $timetableObject;
    }
}
echo '<pre>';
var_dump($timetablesData);
echo '</pre>';
//echo $html;


?>