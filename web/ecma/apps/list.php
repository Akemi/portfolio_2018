<?php

require_once('crypt.php');

header('Content-Type: application/json');
$password = "testPassword";

function removeData($id) {
    $fileName = 'data.json';
    $input = file_get_contents($fileName);

    if($input == false) { return; }

    $tempArray = json_decode($input, true);
    unset($tempArray[$id]);
    $jsonData = json_encode($tempArray, JSON_PRETTY_PRINT);
    file_put_contents($fileName, $jsonData);
}


function appendData($data) {
    $fileName = 'data.json';
    $input = file_get_contents($fileName);

    if($input == false) {
        file_put_contents($fileName, "{}");
        $input = file_get_contents($fileName);
    }

    $tempArray = json_decode($input, true);
    $tempArray[uniqid()] = $data;
    $jsonData = json_encode($tempArray, JSON_PRETTY_PRINT);
    file_put_contents($fileName, $jsonData);
}

function getList() {
    global $password;
    $fileName = 'data.json';
    $input = file_get_contents($fileName);

    if($input == false) { return "{}"; }
    $tempArray = json_decode($input, true);

    foreach($tempArray as $key => $value) {
        $tempArray[$key] = Crypt::encrypt($value, $password);
    }
    return json_encode($tempArray, JSON_PRETTY_PRINT);
}

function checkString($hash1, $hash2) {
    if(strcmp($hash1, $hash2) == 0){
        return true;
    }
    return false;
}


$requMethod = $_SERVER['REQUEST_METHOD'];

switch ($requMethod) {
    case "GET":
        echo getList();
        break;
    case "POST":
        $data = json_decode(file_get_contents("php://input"));
        switch ($data->action) {
            case "check":
                $dataPass = str_replace(Crypt::decrypt($data->time, $password), "", Crypt::decrypt($data->password, $password));
                $dataPassOrg = Crypt::decrypt($data->password, $password);
                $dataPassHash = Crypt::decrypt($data->passwordHash, $password);
                if(checkString(Crypt::hashHelper($dataPassOrg), $dataPassHash)) {
                    if(checkString($dataPass, $password)){
                        $tmp = [ 'response' => 'accepted' ];
                        echo json_encode($tmp, JSON_PRETTY_PRINT);
                    }
                    else {
                        $tmp = [ 'response' => 'wrong password'];
                        echo json_encode($tmp, JSON_PRETTY_PRINT);
                    }
                }
                else {
                    $tmp = [ 'response' => 'wrong password or manipulated data'];
                    echo json_encode($tmp, JSON_PRETTY_PRINT);
                }
                break;
            case "add":
                $dataData = Crypt::decrypt($data->data, $password);
                $dataPass = str_replace(Crypt::decrypt($data->time, $password), "", Crypt::decrypt($data->password, $password));
                $dataPassOrg = Crypt::decrypt($data->password, $password);
                $dataDataHash = Crypt::decrypt($data->dataHash, $password);
                $dataPassHash = Crypt::decrypt($data->passwordHash, $password);
                $dataTime = Crypt::decrypt($data->time, $password);

                //echo Crypt::hashHelper($dataPassOrg).' - '. $dataPassHash;
                //echo $dataData.' - '. $dataDataHash;
                if(checkString(Crypt::hashHelper($dataPassOrg), $dataPassHash) && checkString(Crypt::hashHelper($dataData), $dataDataHash)) {
                    if(checkString($dataPass, $password)){
                        appendData($dataData);
                        $tmp = [ 'response' => 'accepted' ];
                        echo json_encode($tmp, JSON_PRETTY_PRINT);
                    }
                    else {
                        $tmp = [ 'response' => 'wrong password'];
                        echo json_encode($tmp, JSON_PRETTY_PRINT);
                    }
                }
                else {
                    $tmp = [ 'response' => 'wrong password or manipulated data'];
                    echo json_encode($tmp, JSON_PRETTY_PRINT);
                }
                break;
            default:
                error_log("Undifined Method", 0);
                break;
        }
        break;
    case 'DELETE':
        $data = json_decode(file_get_contents("php://input"));
        $dataData = Crypt::decrypt($data->data, $password);
        $dataPass = str_replace(Crypt::decrypt($data->time, $password), "", Crypt::decrypt($data->password, $password));
        $dataPassOrg = Crypt::decrypt($data->password, $password);
        $dataDataHash = Crypt::decrypt($data->dataHash, $password);
        $dataPassHash = Crypt::decrypt($data->passwordHash, $password);
        $dataTime = Crypt::decrypt($data->time, $password);

        if(checkString(Crypt::hashHelper($dataPassOrg), $dataPassHash) && checkString(Crypt::hashHelper($dataData), $dataDataHash)) {
            if(checkString($dataPass, $password)){
                removeData($dataData);
                $tmp = [ 'response' => 'accepted' ];
                echo json_encode($tmp, JSON_PRETTY_PRINT);
            }
            else {
                $tmp = [ 'response' => 'wrong password'];
                echo json_encode($tmp, JSON_PRETTY_PRINT);
            }
        }
        else {
            $tmp = [ 'response' => 'delete wrong password or manipulated data'];
            echo json_encode($tmp, JSON_PRETTY_PRINT);
        }
        break;
    default:
        error_log("Undifined Method", 0);
        break;
}
?>