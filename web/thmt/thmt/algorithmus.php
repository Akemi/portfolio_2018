<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8" />
    <title>THM Timetable Algorithmen Illustration</title>
    <style>
        div.table {
            display: table;
        }

        div > div {
            display: table-row;
            border: 1px solid black;
            border-width: 1px 0px 0px 0px;
            text-align: center;
        }

        div > div > div {
            display: table-cell;
            padding: 2px;
        }

        div > div > div:nth-child(n+2) {
            width: 30px;
        }

        div > div > div:nth-child(2),
        div > div > div:nth-child(3) {
            color: red;
        }
        div > div > div:nth-child(4),
        div > div > div:nth-child(5) {
            color: blue;
        }
        div > div > div:nth-child(6),
        div > div > div:nth-child(7) {
            color: green;
        }
        div > div > div:nth-child(8),
        div > div > div:nth-child(9) {
            color: blueviolet;
        }
        div > div > div:nth-child(10),
        div > div > div:nth-child(11) {
            color: orange;
        }
        div > div > div:nth-child(12),
        div > div > div:nth-child(13) {
            color: teal;
        }




    </style>
</head>
<body style="padding-top: 150px;">

    <?php
    include 'libs/ganon.php';
    include 'libs/vendor/autoload.php';
    include 'libs/lib2.php';

    $thmt = new THMT();

    $thmt->parseTimetable('lol');
    ?>
</body>
</html>