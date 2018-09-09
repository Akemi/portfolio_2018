<?php

class THMT {

    var $timetableUpdateDate;
    var $lectureUpdateDate;
    var $teacherUpdateDate;
    var $lasWrittenTimeTableDates;
    var $lastWrittenDates;
    var $teacherList;
    var $lectureList;
    var $degreeList;
    var $timetableList;
    var $weekDays;
    var $degreeCodes;
    var $lastWrittenFileName;
    var $lastWrittenTimeTablesFileName;
    var $teacherFileName;
    var $lectureFileName;
    var $degreeFileName;
    var $allTimeTablesFilesname;

    function THMT() {
        /*
         *  array for dayID to day Name relation
         */
        $this->weekDays = array(
            0   => "Monday",
            1   => "Tuesday",
            2   => "Wednesday",
            3   => "Thursday",
            4   => "Friday",
            5   => "Saturday",
        );

        /*
         *  array for degree code to name relation
         */
        $this->degreeCodes = array(
            'B'    => 'Bachelor',
            'D'    => 'Diplom',
            'M'    => 'Master',
            'S'    => 'Studierende',
            'Alle' => 'Studierenden',
        );

        /*
         *  array for last updated and parsed
         */
        $this->lastWrittenDates = array(
            'timetable'    => 0,
            'lecture'    => 0,
            'teacher'    => 0,
        );

        /*
         *  filenames for prsed data
         */
        $this->lastWrittenFileName = 'lastwritten';
        $this->lastWrittenTimeTablesFileName = 'lastwrittentimetables';
        $this->teacherFileName = 'teacher';
        $this->lectureFileName = 'lecture';
        $this->degreeFileName = 'degree';
        $this->allTimeTablesFilesname = 'alltimetables';

        /*
         *  array for last updated and parsed timetables
         */
        $this->lasWrittenTimeTableDates = array();

        /*
         *  create both new last written update files, if nor read them
         */
        $file = file_get_contents('data/'.$this->lastWrittenFileName.'.json');
        if($file == false) $this->writeJsonFile($this->lastWrittenFileName, $this->getLastWrittenDatesJson());
        else $this->lastWrittenDates = json_decode($file, true);

        $file = file_get_contents('data/'.$this->lastWrittenTimeTablesFileName.'.json');
        if($file == false) $this->writeJsonFile($this->lastWrittenTimeTablesFileName, $this->getLastWrittenTimeTableDatesJson());
        else $this->lasWrittenTimeTableDates = json_decode($file, true);

        // get the latest update dates
        $this->timetableUpdateDate = $this->getTimetableUpdateDate('http://homepages-fb.thm.de/plaene/stundenplan/Kla1.htm');
        $this->getLectureTeacherUpdateDate('http://homepages-fb.th-mittelhessen.de/plaene/fachbezeichnung/faecherbez.htm', $this->lectureUpdateDate, $this->teacherUpdateDate);

        // check if files need update, parse and write them or read existing
        if($this->lastWrittenDates['teacher'] < $this->teacherUpdateDate) {
            $this->teacherList = $this->parseTeacherDoc('http://homepages-fb.th-mittelhessen.de/plaene/fachbezeichnung/Doz_01.pdf');
            $this->lastWrittenDates['teacher'] = $this->teacherUpdateDate;
            $this->writeDataFile($this->teacherFileName, $this->getTeacherListJson(), $this->getTeacherListXml());
            $this->writeJsonFile($this->lastWrittenFileName, $this->getLastWrittenDatesJson());
        }
        else {
            $file = file_get_contents('data/'.$this->teacherFileName.'.json');
            if($file != false) $this->teacherList = json_decode($file, true);
        }

        if($this->lastWrittenDates['lecture'] < $this->lectureUpdateDate) {
            $this->lectureList = $this->parseLectureDoc('http://homepages-fb.th-mittelhessen.de/plaene/fachbezeichnung/Faecher_01.pdf');
            $this->lastWrittenDates['lecture'] = $this->lectureUpdateDate;
            $this->writeDataFile($this->lectureFileName, $this->getLectureListJson(), $this->getLectureListXml());
            $this->writeJsonFile($this->lastWrittenFileName, $this->getLastWrittenDatesJson());
        }
        else {
            $file = file_get_contents('data/'.$this->lectureFileName.'.json');
            if($file != false) $this->lectureList = json_decode($file, true);
        }

        if($this->lastWrittenDates['timetable'] < $this->timetableUpdateDate) {
            $this->degreeList = $this->parseDegreeTable('http://homepages-fb.thm.de/plaene/stundenplan/HTML.htm');
            $this->timetableList = $this->parseAllTimetables('http://homepages-fb.thm.de/plaene/stundenplan/Kla1.htm');
            $this->lastWrittenDates['timetable'] = $this->timetableUpdateDate;
            $this->writeDataFile($this->degreeFileName, $this->getDegreeListJson(), $this->getDegreeListXml());
            $this->writeDataFile($this->allTimeTablesFilesname, $this->getAllTimetablesJson(), $this->getAllTimetablesXml());
            $this->writeJsonFile($this->lastWrittenFileName, $this->getLastWrittenDatesJson());
        }
        else {
            $file = file_get_contents('data/'.$this->degreeFileName.'.json');
            if($file != false) $this->degreeList = json_decode($file, true);
            $file = file_get_contents('data/'.$this->allTimeTablesFilesname.'.json');
            if($file != false) $this->timetableList = json_decode($file, true);
        }
    }


    /*
     ******************************************************************************************************
     *
     *  All Document and Table parsing functions
     *
     ******************************************************************************************************
     */

    /*
     *  parses a teacher pdf document, returns an array
     *  $doc -   link to the teacher pdf file
     */
    function parseTeacherDoc($doc) {
        $parser = new \Smalot\PdfParser\Parser();
        $pdf    = $parser->parseFile($doc);

        $text = $pdf->getText();

        $text = substr($text, strpos($text, "Kürzel Langname")+strlen("Kürzel Langname")+2);
        $text = preg_replace('/\n\nVerzeichnis der Dozentenkürzel in Friedberg /', '', $text);
        $text = preg_replace('/\nKürzel Langname/', '', $text);
        $text = preg_replace('/Stand:.*Seite [0-9]*.*\n/', '', $text);

        $arr = explode("\n", $text);
        $teacherArray = array();

        foreach ($arr as $key => $value) {
            $tmp = explode(' ',trim($value), 2);
            if (!isset($tmp[1])) {
                error_log('parseTeacherDoc() - regex is broken, inconsistent data', 0);
            }
            $teacherArray[trim($tmp[0])] = trim($tmp[1]);
        }
        return $teacherArray;
    }

    /*
     *  parses a lecture pdf document, returns an array
     *  $doc -   link to the lecture pdf file
     */
    function parseLectureDoc($doc) {
        $parser = new \Smalot\PdfParser\Parser();
        $pdf    = $parser->parseFile($doc);

        $text = $pdf->getText();

        $text = substr($text, strpos($text, "Kürzel Bezeichnung")+strlen("Kürzel Bezeichnung")+2);
        $text = preg_replace('/\n\nFächerbezeichnungen in Friedberg sortiert nach Kürzel /', '', $text);
        $text = preg_replace('/Kürzel Bezeichnung/', '', $text);
        $text = preg_replace('/Seite [0-9]* von *[0-9]* *Stand.*\n/', '', $text);
        $text = preg_replace('/\n \n/', "\n", $text);
        $text = preg_replace('/-\n/', '', $text);
        $text = preg_replace('/\n(^[\(].*[\)])/m', '$1', $text);
        $text = preg_replace('/\n(^[A-zäÄüÜöÖ][a-zäüö][a-zäüö].*)/m', '$1', $text);
        $text = preg_replace('/\n(^Übung.*)/m', '$1', $text);
        $text = preg_replace('/\n(^\/.*)/m', '$1', $text);
        $text = preg_replace('/\n(^[A-ZÄÜÖ][A-ZÄÜÖ][A-ZÄÜÖ]\))/m', '$1', $text);
        $text = preg_replace('/\n(^[A-zäÄüÜöÖ]{1,3} \n)/m', '$1', $text);
        $text = preg_replace('/\n(^[a-zA-Z\d]+\) \n)/m', '$1', $text);
        $text = preg_replace('/\n(^MA5104 \n)/m', '$1', $text);

        $arr = explode("\n", $text);
        $lectureArray = array();

        foreach ($arr as $key => $value) {
            $tmp = explode(' ',trim($value), 2);
            if (!isset($tmp[1])) {
                error_log('parseLectureDoc() - regex is broken, inconsistent data', 0);
            }

            if(!array_key_exists(trim($tmp[0]), $lectureArray)) {
                $lectureArray[trim($tmp[0])] = trim($tmp[1]);
            }
        }
        return $lectureArray;
    }

    /*
     *  parses a degree html table, returns an array
     *  $link -   link with degree table html document
     */
    function parseDegreeTable($link) {
        $html = file_get_dom($link);

        foreach($html('body') as $ele) {
            $ele->setInnerText($ele('table.Tabellengitternetz')[0]->getOuterText());
        }
        foreach($ele('table tr:last-child') as $tdEle) {
            $tdEle->setOuterText('');
        }

        foreach($html('table tr td') as $ele) {
            $ele->setInnerText($ele->getPlainText());
        }
        /*foreach($html('table tr td') as $ele) {
            //$ele->deleteAttribute('style');
            $ele->setOuterText('<td>'.$ele->getInnerText().'</td>');
        }
        foreach($html('table tr') as $ele) {
            //$ele->deleteAttribute('style');
            $ele->setOuterText('<tr>'.$ele->getInnerText().'</tr>');
        }
        echo $html;*/
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
        return $degreeData;
    }

    /*
     *  parses timetable html table with all timetables, returns an array
     *  $link -   link with timetable html document
     */
    function parseAllTimetables($link) {
        $html = file_get_dom($link);

        foreach($html('body') as $ele) {
            $ele->setInnerText($ele('table')[1]->getOuterText());
        }
        /*foreach($html('table tr td') as $ele) {
            //$ele->deleteAttribute('style');
            $ele->setOuterText('<td>'.$ele->getPlainText().'</td>');
        }
        foreach($html('table tr') as $ele) {
            //$ele->deleteAttribute('style');
            $ele->setOuterText('<tr>'.$ele->getInnerText().'</tr>');
        }
        echo $html;*/

        $timetablesData = array();

        foreach($html('table tr td') as $ele) {
            $timetableID = $ele->getPlainText();

            if(strlen($timetableID) >= 3) {
                if($timetableID == 'Alle') {
                    $degreeID = $timetableID;
                    $degreeCode = $timetableID;
                    $semester = 'Alle';
                    $timetableVariant = '';
                }
                else {
                    $degreeID = substr($timetableID, 0, 2);
                    $degreeCode = substr($timetableID, 2, 1);
                    $semester = 'Alle';
                    $timetableVariant = '';
                    if(strlen($timetableID) >= 4) $semester = substr($timetableID, 3, 1);
                    if(strlen($timetableID) >= 5) $timetableVariant = substr($timetableID, 4, 1);
                }

                if(array_key_exists($degreeID, $this->degreeList)) $name = $this->degreeList[$degreeID];
                else $name = $degreeID;

                $semester = ($semester == 'S' ? 'Alle' : $semester);

                $timetableObject = ['degreeID'     => $degreeID,
                    'degreeName'       => $name,
                    'degreeCode'       => $this->degreeCodes[$degreeCode],
                    'semester'         => $semester,
                    'timetableVariant' => $timetableVariant];

                $timetablesData[$timetableID] = $timetableObject;
            }
        }
        //ksort($timetablesData);
        return $timetablesData;
    }

    /*
     *  parses a timetable html table, returns an array
     *  $timetableID -   timetable id, key of array parseAllTimetables() or timetableList
     */
    function parseTimetable($timetableID) {
        $html = file_get_dom('offline_files/test7.html');
        //$html = file_get_dom('https://homepages-fb.thm.de/plaene/stundenplan/Kla1_'.$timetableID.'.htm');

        // remove everything besidees the timetable
        foreach($html('body') as $ele) {
            $ele->setInnerText($ele('table')[1]->getOuterText());
        }

        // clean up all tables in table
        foreach($html('table table') as $ele) {
            // remove al font tags
            foreach($ele('font') as $fontEle) {
                $fontEle->setOuterText($fontEle->getInnerText());
            }
            // remove all tr tags
            foreach($ele('tr') as $trEle) {
                $trEle->setOuterText($trEle->getInnerText());
            }
            // remove all td tags
            foreach($ele('td') as $tdEle) {
                $tdEle->setOuterText('<span>'.$tdEle->getInnerText().'</span>');
            }
            // remove the inner table
            $ele->setOuterText($ele->getInnerText());
        }

        $breakFound = false;
        // remove time column
        foreach($html('table tr') as $ele) {
            // remove the break tr
            if($breakFound == true) {
                $ele->setOuterText("");
                $breakFound = false;
            }
            elseif (strpos($ele->getPlainText(),'Mittag') !== false) {
                $ele->setOuterText("");
                $breakFound = true;
            }
            else {
                foreach($ele('td:first-child') as $tdEle) {
                    $tdEle->setOuterText("");
                }
            }
        }

        echo '<div style="position: fixed; background-color: white; top: 0px;">'.$html.'</div>';
        echo '<br>';
        echo '<br>';
        echo '<br>';

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

        $occupiedLectureBlocks2 = array(
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        );

        echo '<div class="table">';
        echo '<div><div></div><div>M</div><div></div><div>T</div><div></div><div>W</div><div></div><div>T</div><div></div><div>F</div><div></div><div>S</div><div></div></div>';
        foreach($occupiedLectureBlocks2 as $key => $result) {
            echo '<div><div>B'.($key+1).'</div>';
            foreach($result as $result2) {
                echo '<div>';
                echo $result2;
                echo '</div>';
            }
            echo '</div>';
        }
        echo '</div>';
        echo '<br>';
        echo '<br>';
        /*
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
         *  $lectureID      -   id of lecture
         *  $teacherID      -   id of teacher
         *  $room           -   room number
         *  $weekDayNumber  -   number of week day, starts at 0
         *  $lectureObject  -   one lecture array with all data
         *
         */
        $timetableData = array();

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
                    $lectureID = str_replace("*","",utf8_encode(str_replace('.', '', trim($tdEle('span', 0)->getPlainText()))));

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
                            $occupiedLectureBlocks2[$i1+$i][$col] = $lectureID;
                            $occupiedLectureBlocks2[$i1+$i][$col+1] = $lectureID;
                        }
                    }
                    else {
                        // change all blocks to 1 which are occupied, lectures duration, fill one column (2 lecture possible)
                        for($i = 0; $i < $lectureLength; $i++) {
                            $occupiedLectureBlocks[$i1+$i][$col] = 1;
                            $occupiedLectureBlocks2[$i1+$i][$col] = $lectureID;
                        }
                    }




                    $lectureID = str_replace("*","",utf8_encode(str_replace('.', '', trim($tdEle('span', 0)->getPlainText()))));
                    $teacherID = utf8_encode(trim($tdEle('span', 1)->getPlainText()));

                    $tmp1 = htmlentities(utf8_encode(trim($tdEle('span', 2)->getPlainText())));
                    $tmp2 = ' )';
                    if(count($tdEle('span')) > 3) {
                        $tmp2 = htmlentities(utf8_encode(trim($tdEle('span', 3)->getPlainText())));
                    }

                    if (strpos($tmp1,')') == false) {
                        $room = $tmp1;
                    }
                    elseif(strpos($tmp2,')') == false) {
                        $room = $tmp2;
                    }
                    else {
                        $room = 'NO ROOM';
                    }

                    $weekDayNumber = floor(($col)/2);

                    if($tdEle->bgcolor == '#80FFFF') $occurrence = 'even';
                    else if($tdEle->bgcolor == '#FFFF80') $occurrence = 'odd';
                    else $occurrence = 'both';

                    $lectureObject = ['dayID'     => $weekDayNumber,
                        'day'           => $this->weekDays[intval($weekDayNumber)],
                        'occurrence'    => $occurrence,
                        'block'         => $i1+1,
                        'room'          => $room,
                        'lectureLength' => $lectureLength,
                        'lectureID'     => $lectureID,
                        'lectureName'   => $this->getLectureName($lectureID),
                        'teacherID'     => $teacherID,
                        'teacherName'   => $this->getTeacherName($teacherID)];
                    array_push($timetableData, $lectureObject);

                    echo $this->getLectureName($lectureID);
                    echo '<br>';
                    echo '<br>';
                }
                else {
                    // see above if, same just sets free blocks in array (2)
                    if($lectureCount < 2) {
                        if($col % 2 != 0) {
                            $col++;
                        }
                        $occupiedLectureBlocks[$i1][$col] = 2;
                        $occupiedLectureBlocks[$i1][$col+1] = 2;
                        $occupiedLectureBlocks2[$i1][$col] = 2;
                        $occupiedLectureBlocks2[$i1][$col+1] = 2;
                    }
                    else {
                        $occupiedLectureBlocks[$i1][$col] = 2;
                        $occupiedLectureBlocks2[$i1][$col] = 2;
                    }
                }
                echo '<div class="table">';
                echo '<div><div></div><div>M</div><div></div><div>T</div><div></div><div>W</div><div></div><div>T</div><div></div><div>F</div><div></div><div>S</div><div></div></div>';
                foreach($occupiedLectureBlocks2 as $key => $result) {
                    echo '<div><div>B'.($key+1).'</div>';
                    foreach($result as $result2) {
                        echo '<div>';
                        echo $result2;
                        echo '</div>';
                    }
                    echo '</div>';
                }
                echo '</div>';
                echo '<br>';
                echo '<br>';
                echo '<br>';
                // move current column by 1 when both blocks of the day are occupied
                if($lectureCount < 2) {
                    $column = $column + 1;
                }

            }
        }

        return $timetableData;
    }

    /*
     *  searches for update time of timetables, returns timestamp
     *  $link -   link with timetable update date html document
     */
    function getTimetableUpdateDate($link) {
        $html = file_get_dom($link);
        $dateString = '';

        foreach($html('body') as $ele) {
            $ele->setInnerText($ele('table')[0]->getOuterText());
        }

        foreach($html('table') as $ele) {
            $ele->setInnerText($ele('tr')[1]->getOuterText());
        }

        foreach($html('table tr') as $ele) {
            $dateString = $ele('td')[2]->getPlainText();
        }

        return strtotime($dateString);
    }

    /*
     *  searches for update time of lectures and teacher
     *  $link -         link with lecture and teacher update date html document
     *  $lectureDate -  pointer to variable for lecture update date
     *  $teacherDate -  pointer to variable for teacher update date
     */
    function getLectureTeacherUpdateDate($link, &$lectureDate, &$teacherDate) {
        $html = file_get_dom($link);

        foreach($html('p') as $ele) {
            $datePlaintext = mb_convert_encoding($ele->getPlainText(), 'UTF-8', 'ISO-8859-1');
            if (strpos($datePlaintext, 'Fächer: ') !== false) {
                $lectureDate = strtotime(str_replace(' ', '', str_replace('Fächer: ', '', $datePlaintext)));
            }
            if (strpos($datePlaintext, 'Dozenten: ') !== false) {
                $teacherDate = strtotime(str_replace(' ', '', str_replace('Dozenten: ', '', $datePlaintext)));
            }
        }
    }


    /*
     ******************************************************************************************************
     *
     *  All JSON convert functions
     *
     ******************************************************************************************************
     */

    /*
     *  returns teacher list id => name
     */
    function getTeacherListJson() {
        return json_encode($this->teacherList, JSON_PRETTY_PRINT);
    }

    /*
     *  returns lecture list id => name
     */
    function getLectureListJson() {
        return json_encode($this->lectureList, JSON_PRETTY_PRINT);
    }

    /*
     *  returns degree list id => name
     */
    function getDegreeListJson() {
        return json_encode($this->degreeList, JSON_PRETTY_PRINT);
    }

    /*
     *  returns all timetables id => data
     */
    function getAllTimetablesJson() {
        return json_encode($this->timetableList, JSON_PRETTY_PRINT);
    }

    /*
     *  returns all timetables id => data
     *  $timetableID    -   timetable id, id of array getAllTimetablesJson()
     */
    function getTimetableJson($timetableID) {
        // test if it ever was written or updated, then write json and xml
        if(!array_key_exists($timetableID, $this->lasWrittenTimeTableDates) || $this->lasWrittenTimeTableDates[$timetableID] < $this->timetableUpdateDate) {
            $timetableArray = $this->parseTimetable($timetableID);
            $timetableJson = json_encode($timetableArray, JSON_PRETTY_PRINT);
            $timetableXml = new SimpleXMLElement("<?xml version=\"1.0\"?><TimeTable></TimeTable>");
            $this->arrayWithoutKeyToXml($timetableArray, $timetableXml, 'lecture');

            $this->lasWrittenTimeTableDates[$timetableID] = $this->timetableUpdateDate;
            $this->writeJsonFile($this->lastWrittenTimeTablesFileName, $this->getLastWrittenTimeTableDatesJson());
            $this->writeDataFile($timetableID, $timetableJson, $timetableXml);
        }
        else {
            $file = file_get_contents('data/'.$timetableID.'.json');
            if($file != false) $timetableJson = $file;
        }

        return $timetableJson;
    }

    /*
     *  returns last written dates id => data
     */
    function getLastWrittenDatesJson() {
        return json_encode($this->lastWrittenDates, JSON_PRETTY_PRINT);
    }

    /*
     *  returns last written dates id => data
     */
    function getLastWrittenTimeTableDatesJson() {
        return json_encode($this->lasWrittenTimeTableDates, JSON_PRETTY_PRINT);
    }


    /*
     ******************************************************************************************************
     *
     *  All XML convert functions
     *
     ******************************************************************************************************
     */

    /*
     *  returns teacher list id => name
     */
    function getTeacherListXml() {
        $xml = new SimpleXMLElement("<?xml version=\"1.0\"?><TeacherList></TeacherList>");
        $this->arrayKeyValueToXml($this->teacherList, $xml, 'teacher');
        return $xml;
    }

    /*
     *  returns lecture list id => name
     */
    function getLectureListXml() {
        $xml = new SimpleXMLElement("<?xml version=\"1.0\"?><LectureList></LectureList>");
        $this->arrayKeyValueToXml($this->lectureList, $xml, 'lecture');
        return $xml;
    }

    /*
     *  returns degree list id => name
     */
    function getDegreeListXml() {
        $xml = new SimpleXMLElement("<?xml version=\"1.0\"?><DegreeList></DegreeList>");
        $this->arrayKeyValueToXml($this->degreeList, $xml, 'degree');
        return $xml;
    }

    /*
     *  returns all timetables id => data
     */
    function getAllTimetablesXml() {
        $xml = new SimpleXMLElement("<?xml version=\"1.0\"?><TimeTableList></TimeTableList>");
        $this->arrayKeyValueToXml($this->timetableList,$xml, 'timetable');
        return $xml;
    }

    /*
     *  returns all timetables id => data
     *  $timetableID    -   timetable id, id of array getAllTimetablesXml()
     */
    function getTimetableXml($timetableID) {
        // test if it ever was written or updated, then write json and xml
        if(!array_key_exists($timetableID, $this->lasWrittenTimeTableDates) || $this->lasWrittenTimeTableDates[$timetableID] < $this->timetableUpdateDate) {
            $timetableArray = $this->parseTimetable($timetableID);
            $timetableJson = json_encode($timetableArray, JSON_PRETTY_PRINT);
            $timetableXml = new SimpleXMLElement("<?xml version=\"1.0\"?><TimeTable></TimeTable>");
            $this->arrayWithoutKeyToXml($timetableArray, $timetableXml, 'lecture');

            $this->lasWrittenTimeTableDates[$timetableID] = $this->timetableUpdateDate;
            $this->writeJsonFile($this->lastWrittenTimeTablesFileName, $this->getLastWrittenTimeTableDatesJson());
            $this->writeDataFile($timetableID, $timetableJson, $timetableXml);
        }
        else {
            if (file_exists('data/'.$timetableID.'.xml'))  $timetableXml = simplexml_load_file('data/'.$timetableID.'.xml');
        }

        return $timetableXml;
    }


    /*
     ******************************************************************************************************
     *
     *  All database/array functions.
     *  only parse/use array when new data needed/was parsed otherwise use database.
     *
     ******************************************************************************************************
     */

    function getTeacherName($teacherID) {
        if (array_key_exists($teacherID, $this->teacherList)) {
            return $this->teacherList[$teacherID];
        }
        else {
            return $teacherID;
        }
    }

    function getLectureName($lectureID) {
        if (array_key_exists($lectureID, $this->lectureList)) {
            return $this->lectureList[$lectureID];
        }
        else {
            return $lectureID;
        }

    }


    /*
     ******************************************************************************************************
     *
     *  All helper functions
     *
     ******************************************************************************************************
     */

    /*
     *  converts a simple key value array to xml
     */
    function arrayKeyValueToXml($array, &$xml, $elementName) {
        foreach($array as $key => $value) {
            $subnode = $xml->addChild($elementName);
            if(is_array($value)) {
                $subnode->addChild('id', $key);
                foreach($value as $key2 => $value2) {
                    $subnode->addChild($key2, $value2);
                }
            }
            else {
                $subnode->addChild('id', $key);
                $subnode->addChild('name', $value);
            }
        }
    }

    /*
     *  converts an array without keys into xml structure
     */
    function arrayWithoutKeyToXml($array, &$xml , $elementName) {
        foreach($array as $key => $value) {
            if(is_array($value)) {
                $subnode = $xml->addChild($elementName);
                $this->arrayWithoutKeyToXml($value, $subnode, $elementName);
            }
            else {
                $xml->addChild($key,$value);
            }
        }
    }

    /*
     *  writes a json file
     *  $fileName   - name of the file
     *  $data       - data of the file
     */
    function writeJsonFile($fileName, $data) {
        $fp = fopen('data/'.$fileName.'.json', 'w');
        fwrite($fp, $data);
        fclose($fp);
    }

    /*
     *  writes a json file
     *  $fileName   - name of the file
     *  $jsonData   - json data of the file
     *  $xmlData    - xml data of the file
     */
    function writeDataFile($fileName, $jsonData, $xmlData) {
        $fp = fopen('data/'.$fileName.'.json', 'w');
        fwrite($fp, $jsonData);
        fclose($fp);

        $xml = dom_import_simplexml($xmlData)->ownerDocument;
        $xml->formatOutput = true;
        $fp = fopen('data/'.$fileName.'.xml', 'w');
        fwrite($fp, $xml->saveXML());
        fclose($fp);
    }
}

?>