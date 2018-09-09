<?php

include 'libs/ganon.php';
include 'libs/vendor/autoload.php';
include 'libs/lib.php';



if(isset($_GET["action"])) $action = $_GET["action"];
else $action = "";

if(isset($_GET["id"])) $id = $_GET["id"];
else $id = "";

if(isset($_GET["type"])) $type = $_GET["type"];
else $type = "";


$thmt = new THMT();

if($action == 'teacherlist') {
    if($type == 'xml') {
        header('Content-Type: text/xml');
        $dom = dom_import_simplexml($thmt->getTeacherListXml())->ownerDocument;
        $dom->formatOutput = true;
        echo $dom->saveXML();
    }
    elseif($type == 'json') {
        header('Content-Type: application/json');
        echo $thmt->getTeacherListJson();
    }
    else {
        header('Content-Type: application/json');
        echo $thmt->getTeacherListJson();
    }
}
elseif($action == 'leacturelist') {
    if($type == 'xml') {
        header('Content-Type: text/xml');
        $dom = dom_import_simplexml($thmt->getLectureListXml())->ownerDocument;
        $dom->formatOutput = true;
        echo $dom->saveXML();
    }
    elseif($type == 'json') {
        header('Content-Type: application/json');
        echo $thmt->getLectureListJson();
    }
    else {
        header('Content-Type: application/json');
        echo $thmt->getLectureListJson();
    }
}
elseif($action == 'degreelist') {
    if($type == 'xml') {
        header('Content-Type: text/xml');
        $dom = dom_import_simplexml($thmt->getDegreeListXml())->ownerDocument;
        $dom->formatOutput = true;
        echo $dom->saveXML();
    }
    elseif($type == 'json') {
        header('Content-Type: application/json');
        echo $thmt->getDegreeListJson();
    }
    else {
        header('Content-Type: application/json');
        echo $thmt->getDegreeListJson();
    }
}
elseif($action == 'timetablelist') {
    if($type == 'xml') {
        header('Content-Type: text/xml');
        $dom = dom_import_simplexml($thmt->getAllTimetablesXml())->ownerDocument;
        $dom->formatOutput = true;
        echo $dom->saveXML();
    }
    elseif($type == 'json') {
        header('Content-Type: application/json');
        echo $thmt->getAllTimetablesJson();
    }
    else {
        header('Content-Type: application/json');
        echo $thmt->getAllTimetablesJson();
    }
}
elseif($action == 'timetable') {
    if($type == 'xml') {
        header('Content-Type: text/xml');
        $dom = dom_import_simplexml($thmt->getTimetableXml($id))->ownerDocument;
        $dom->formatOutput = true;
        echo $dom->saveXML();
    }
    elseif($type == 'json') {
        header('Content-Type: application/json');
        echo $thmt->getTimetableJson($id);
    }
    else {
        header('Content-Type: application/json');
        echo $thmt->getTimetableJson($id);
    }
}

?>