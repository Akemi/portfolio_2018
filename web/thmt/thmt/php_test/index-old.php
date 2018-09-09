<?php

//https://code.google.com/p/ganon/
//http://www.pdfparser.org

include('ganon.php');

//ini_set('default_charset','latin1');

//$html = file_get_dom('https://homepages-fb.thm.de/plaene/stundenplan/Kla1_MIM2.htm');
//$html = file_get_dom('https://homepages-fb.thm.de/plaene/stundenplan/Kla1_WKM2.htm');
$html = file_get_dom('http://localhost:8888/do/test3.html');

foreach($html('body') as $ele) {
    $ele->setInnerText($ele('table')[1]->getOuterText());
}

foreach($html('table table') as $ele) {
    foreach($ele('font') as $fontEle) {
        $fontEle->setOuterText($fontEle->getInnerText());
    }

    foreach($ele('tr') as $trEle) {
        $trEle->setOuterText($trEle->getInnerText());
    }

    foreach($ele('td') as $tdEle) {
        $tdEle->setOuterText('<span>'.$tdEle->getInnerText().'</span>');
    }

    $ele->setOuterText($ele->getInnerText());
}

//$html('table > tr',0)->setOuterText("");
echo $html;
foreach($html('table tr') as $ele) {
    foreach($ele('td:first-child') as $tdEle) {
        $tdEle->setOuterText("");
    }
}

/*
 *  array of all possible blocks for a lecture in a timetable
 *  every block can contain two lectures at most
 *  two columns correspond to two days (Monday to Saturday, 12 columns)
 *  needed because a DOM table is not a simple matrix because of colspan/rowspan
 *
 *  possible states:
 *  0   -   not checked
 *  1   -   occupied by a lecture
 *  2   -   unoccupied block but checked
 */
$occupiedLectureBlocks = array(
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
);

/*
 *
 *
 *
 *
 */
$weekDays = array(
    0   => "Monday",
    1   => "Tuesday",
    2   => "Wednesday",
    3   => "Thursday",
    4   => "Friday",
    5  => "Saturday",
);

/*
 *
 *
 *  $column         -   current column we are in, two columns correspond to days of a Week, 2 lectures possible per block
 *                  possible values:
 *                  0-1     -   Monday
 *                  2-3     -   Tuesday
 *                  4-5     -   Wednesday
 *                  6-7     -   Thursday
 *                  8-9     -   Friday
 *                  10-11   -   Saturday
 *  $col            -   temp jumper to find open blocks (0)
 *  $lectureLength  -   length of the current lecture
 *  $lectureCount   -   number of lecture in the current block (1 or 2)
 *
 */
$data = array();

foreach($html('table tr:odd') as $i1 => $ele) {
    $column = -1;
    foreach($ele('td') as $i2 => $tdEle) {
        $lectureLength = round($tdEle->rowspan/2);
        $lectureCount = (round($tdEle->colspan/3) < 2 ? 2 : 1);
        $column = $column + 1;

        // find the first open block
        $col = 0;
        for($i = $column; $i < max(array_map('count', $occupiedLectureBlocks)); $i++) {
            if($occupiedLectureBlocks[$i1][$i] == 0) {
                $col = $i;
                break;
            }
        }

        // test if lecture or empty block
        if(strlen($tdEle->getPlainText()) > 0) {
            // if only one lecture in given block
            if($lectureCount < 2) {
                // if there is already a lecture in that block, move to next day
                if($col % 2 != 0) {
                    $col++;
                }
                // change all blocks to 1 which are occupied, lectures duration, fill both columns (only 1 lecture possible)
                for($i = 0; $i < $lectureLength; $i++) {
                    $occupiedLectureBlocks[$i1+$i][$col] = 1;
                    $occupiedLectureBlocks[$i1+$i][$col+1] = 1;
                }
            }
            else {
                // change all blocks to 1 which are occupied, lectures duration, fill one column (2 lecture possible)
                for($i = 0; $i < $lectureLength; $i++) {
                    $occupiedLectureBlocks[$i1+$i][$col] = 1;
                }
            }

            $lectureID = htmlentities(utf8_encode(str_replace('.', '', trim($tdEle('span', 0)->getPlainText()))));
            $teacherID = htmlentities(utf8_encode(trim($tdEle('span', 1)->getPlainText())));
            if(count($tdEle('span')) < 4) $room = htmlentities(utf8_encode(trim($tdEle('span', 2)->getPlainText())));
            else $room = htmlentities(utf8_encode(trim($tdEle('span', 3)->getPlainText())));
            $weekDayNumber = floor(($col)/2);

            $lectureObject = ['dayID'     => $weekDayNumber,
                'day'           => $weekDays[$weekDayNumber],
                'block'         => $i1+1,
                'room'          => $room,
                'lectureLength' => $lectureLength,
                'lectureID'     => $lectureID,
                'teacherID'     => $teacherID];
            array_push($data, $lectureObject);

            //echo /*$tdEle->getPlainText().' '.*/$i1.' '.$column.' '.$tdEle->rowspan.' '.$lectureLength.' '.$lectureCount.' -- ';
        }
        else {
            /*$col = 0;

            for($i = $column; $i < max(array_map('count', $occupiedLectureBlocks)); $i++) {
                if($occupiedLectureBlocks[$i1][$i] == 0) {
                    $col = $i;
                    break;
                }
            }*/
            //echo $col;

            // see above if, same just sets free blocks in array (2)
            if($lectureCount < 2) {
                if($col % 2 != 0) {
                    $col++;
                }
                $occupiedLectureBlocks[$i1][$col] = 2;
                $occupiedLectureBlocks[$i1][$col+1] = 2;
            }
            else {
                $occupiedLectureBlocks[$i1][$col] = 2;
            }
        }
        // move current column by 1 when both blocks of the day are occupied
        if($lectureCount < 2) {
            $column = $column + 1;
        }

    }
    
    //if($i1 >= 3) break;
}



foreach($occupiedLectureBlocks as $child2) {
    foreach($child2 as $child) {
        echo $child." ";
    }
    echo "<br>";
}

//$data = array();
//$iter = 0;

/*for($i = 0; $i < 6; $i++) {
    foreach($html('table tr:even') as $index => $ele) {
        foreach($ele('td:nth-child('.$i.')') as $tdEle) {
            if(strlen($tdEle->getPlainText()) > 0){
                $name = htmlentities(utf8_encode(str_replace('.', '', trim($tdEle('span', 0)->getPlainText()))));
                $teacher = htmlentities(utf8_encode(trim($tdEle('span', 1)->getPlainText())));
                $length = round($tdEle->rowspan/2);
                //3 or 2 needs if
                $room = htmlentities(utf8_encode(trim($tdEle('span', 3)->getPlainText())));

                //echo $name.'lol';
                $tmp = ['day' => $i,
                    'name' => $name,
                    'teacher' => $teacher,
                    'length' => $length,
                    'room' => $room];
                //$data['module'] = $tmp;
                //$iter++;
                array_push($data, $tmp);
            }
        }
    }
}*/
//print_r ($data);
echo '<pre>'.json_encode($data, JSON_PRETTY_PRINT).'</pre>';

/*$xml = new SimpleXMLElement("<?xml version=\"1.0\"?><TimeTable></TimeTable>");*/
//$node = $xml->addChild('request');

// function call to convert array to xml
//array_to_xml($data, $xml);

// display XML to screen
//echo htmlentities($xml->asXML());
//echo '<pre>', htmlentities($xml->asXML()), '</pre>';

/*$dom = new DOMDocument;
$dom->preserveWhiteSpace = FALSE;
$dom->loadXML(htmlentities($xml->asXML()));
$dom->formatOutput = TRUE;
echo $dom->saveXml();*/

/*$doc = new DOMDocument();
$doc->loadXML(htmlentities($xml->asXML()));
echo $doc->saveXML();*/

/*$dom = dom_import_simplexml($xml)->ownerDocument;
$dom->formatOutput = true;
echo '<pre>'.htmlentities($dom->saveXML()).'</pre>';

// function to convert an array to XML using SimpleXML
function array_to_xml($array, &$xml) {
    foreach($array as $key => $value) {
        if(is_array($value)) {
            if(is_numeric($key)){
                $subnode = $xml->addChild("lecture");
                array_to_xml($value, $subnode);
            } else {
                array_to_xml($value, $xml);
            }
        } else {
            $xml->addChild($key,$value);
        }
    }
}*/

echo $html;
?>