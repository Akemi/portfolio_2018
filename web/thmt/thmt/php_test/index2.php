<?php


// Include Composer autoloader if not already done.
include 'libs/vendor/autoload.php';

// Parse pdf file and build necessary objects.
$parser = new \Smalot\PdfParser\Parser();
$pdf    = $parser->parseFile('facher.pdf');

$text = $pdf->getText();

$text = substr($text, strpos($text, "Kürzel Bezeichnung")+strlen("Kürzel Bezeichnung")+2);
$text = preg_replace('/\n\nFächerbezeichnungen in Friedberg sortiert nach Kürzel /', '', $text);
$text = preg_replace('/Kürzel Bezeichnung/', '', $text);
$text = preg_replace('/Seite [0-9]* von *[0-9]* *Stand.*\n/', '', $text);
$text = preg_replace('/\n \n/', "\n", $text);
$text = preg_replace('/-\n/', '', $text);
//$text = preg_replace('/^[^A-Z][^A-ZäÄüÜöÖ][^A-ZäÄüÜöÖ]*[^0-9]*[^A-ZäÄüÜöÖ]*\n/m', 'gggg$1$2$3$4', $text);
//$text = preg_replace('/\n(^[A-zäÄüÜöÖ][A-zäüö][a-zäüö].*)/m', '$1', $text);
$text = preg_replace('/\n(^[\(].*[\)])/m', '$1', $text);
$text = preg_replace('/\n(^[A-zäÄüÜöÖ][a-zäüö][a-zäüö].*)/m', '$1', $text);
$text = preg_replace('/\n(^Übung.*)/m', '$1', $text);
$text = preg_replace('/\n(^\/.*)/m', '$1', $text);
$text = preg_replace('/\n(^[A-ZÄÜÖ][A-ZÄÜÖ][A-ZÄÜÖ]\))/m', '$1', $text);
$text = preg_replace('/\n(^[A-zäÄüÜöÖ]{1,3} \n)/m', '$1', $text);
$text = preg_replace('/\n(^[a-zA-Z\d]+\) \n)/m', '$1', $text);
$text = preg_replace('/\n(^MA5104 \n)/m', '$1', $text);
//$text = preg_replace('/(^[^\s]*\s)/m', '$1--', $text);
//$text = preg_replace('/\n(^[A-zäÄüÜöÖ].*[\)]$)/m', '$1', $text);
//$text = preg_replace('/\n(^[\(]*.*[\)])/m', '$1', $text);

//echo '<textarea style="width: 523px; height: 574px;">';
echo nl2br($text);
//echo $text;
//echo '</textarea>';


$arr = explode("\n", $text);
$lectureArray = array();

foreach ($arr as $key => $value) {
    $tmp = explode(' ',trim($value), 2);
    //echo $tmp[0].'-'.$tmp[1].'<br>';

    if($lectureArray[trim($tmp[0])] == NULL) {
        $lectureArray[trim($tmp[0])] = trim($tmp[1]);
    }
    else {
        echo 'duplicate: '.$tmp[0].'-'.$tmp[1].'<br>';
    }


    /*$lectureObject = ['profID'     => trim($tmp[0]),
        'profName'           => trim($tmp[1])];
    array_push($lectureArray, $lectureObject);*/
}




var_dump($lectureArray);


?>