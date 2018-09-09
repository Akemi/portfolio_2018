<?php


// Include Composer autoloader if not already done.
include 'libs/vendor/autoload.php';

// Parse pdf file and build necessary objects.
$parser = new \Smalot\PdfParser\Parser();
$pdf    = $parser->parseFile('doz.pdf');

$text = $pdf->getText();

$text = substr($text, strpos($text, "Kürzel Langname")+strlen("Kürzel Langname")+2);
$text = preg_replace('/\n\nVerzeichnis der Dozentenkürzel in Friedberg /', '', $text);
$text = preg_replace('/\nKürzel Langname/', '', $text);
$text = preg_replace('/Stand:.*Seite [0-9]*.*\n/', '', $text);
//$text = preg_replace('/Seite [0-9]* von *[0-9]* *Stand.*\n/', '', $text);
//$text = preg_replace('/\n \n/', "\n", $text);
//$text = preg_replace('/-\n/', '', $text);
////$text = preg_replace('/^[^A-Z][^A-ZäÄüÜöÖ][^A-ZäÄüÜöÖ]*[^0-9]*[^A-ZäÄüÜöÖ]*\n/m', 'gggg$1$2$3$4', $text);
////$text = preg_replace('/\n(^[A-zäÄüÜöÖ][A-zäüö][a-zäüö].*)/m', '$1', $text);
//$text = preg_replace('/\n(^[\(].*[\)])/m', '$1', $text);
//$text = preg_replace('/\n(^[A-zäÄüÜöÖ][a-zäüö][a-zäüö].*)/m', '$1', $text);
//$text = preg_replace('/\n(^Übung.*)/m', '$1', $text);
//$text = preg_replace('/\n(^\/.*)/m', '$1', $text);
//$text = preg_replace('/\n(^[A-ZÄÜÖ][A-ZÄÜÖ][A-ZÄÜÖ]\))/m', '$1', $text);
//$text = preg_replace('/\n(^[A-zäÄüÜöÖ]{1,3} \n)/m', '$1', $text);
//$text = preg_replace('/(^[^\s]*\s)/m', '$1--', $text);
//$text = preg_replace('/\n(^[A-zäÄüÜöÖ].*[\)]$)/m', '$1', $text);
//$text = preg_replace('/\n(^[\(]*.*[\)])/m', '$1', $text);
//$text = preg_replace('/[ \t]+/', '-', $text);

//echo '<textarea style="width: 523px; height: 574px;">';
//echo nl2br($text);
//echo $text;
//echo '</textarea>';

$arr = explode("\n", $text);
$profArray = array();

foreach ($arr as $key => $value) {
    $tmp = explode(' ',trim($value), 2);
    //echo $tmp[0].'-'.$tmp[1].'<br>';

    //$profArray[trim($tmp[0])] = trim($tmp[1]);

    $profObject = ['profID'     => trim($tmp[0]),
        'profName'           => trim($tmp[1])];
    array_push($profArray, $profObject);
}




var_dump($profArray);
//var_dump();

?>