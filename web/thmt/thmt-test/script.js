



$( document ).ready(function() {
    var xmlhttp = new XMLHttpRequest();
    var requestURL = 'http://comp.judging.it/test/thmt/thmtdata.php?action=timetablelist&type=json';

    xmlhttp.open("GET", requestURL, false);
    xmlhttp.send();
    var data = $.parseJSON(xmlhttp.responseText);

    degreeID = '';
    newDegree = false;
    html = '';

    $.each( data, function( key, val ) {
        if(degreeID == val['degreeID'] + val['degreeCode']) {
            html += '<span id="' + key + '">' + val['semester'] + val['timetableVariant'] + '</span> ';
            newDegree = false;
        }
        else {
            if (newDegree == true) html += '</div></div>';
            newDegree = true;
            degreeID = val['degreeID'] + val['degreeCode'];
            html += '<div id="' + val['degreeID'] + val['degreeCode'] + '" class="degree">' + '<div class="header">' + val['degreeName'] + ' ' + val['degreeCode'] + '</div><div class="semester">';
            html += '<span id="' + key + '">' + val['semester'] + val['timetableVariant'] + '</span> ';
        }
        newDegree = true;
    });

    html += '</div></div>';
    $('#timetablelist').append(html);

    //tmp = $('#timetable').html();
    $('#timetable > ul li .blank').remove();
    tmp2 = $('#timetable').html();
    $('#timetable').html('');

    $('#tthide').click(function() {
        $('#timetablelist').slideToggle('slow');
    });

    $('.semester > span').click(function() {
        $('#timetable').html('');
        $('#timetablelist').slideToggle('slow');
        eleID = $(this).attr('id');

        var requestURL = 'http://comp.judging.it/test/thmt/thmtdata.php?action=timetable&id=' + eleID + '&type=json';

        xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET", requestURL, true);
        xmlhttp.onload = function (e) {
            if (xmlhttp.readyState === 4) {
                if (xmlhttp.status === 200) {
                    $('#timetable').html(tmp2);
                    var data = $.parseJSON(xmlhttp.responseText);
                    $.each( data, function( key, val ) {
                        day = parseInt(val['dayID']+1);
                        lectureName = (val['lectureName'] == null) ? val['lectureID'] : val['lectureName'];


                        $('#' + day + '_' + val['block']).append('<div class="block' + val['lectureLength'] + ' ' + val['occurrence'] + '"><div><div><b>' + lectureName + '</b><br>' + val['teacherName'] + '<br><b>' + val['room'] +'</b></div></div></div>');

                        for (i = parseInt(val['block'] + 1); i < parseInt(val['block'] + val['lectureLength']); i++) {
                            $('#' + day + '_' + i).append('<div class="blank"></div>')
                        }

                        //$('#timetable > ul > li:empty').append('<div class="blank"></div>');
                    });
                    $('#timetable > ul > li:empty').each(function( index ) {
                        //alert($(this).html() + ' - ' + $(this).html().length)
                        if($(this).html().length <= 0) {
                            $(this).append('<div class="blank"></div>')
                        }

                    });
                } else {
                    console.error(xmlhttp.statusText);
                }
            }
        };
        xmlhttp.onerror = function (e) {
            console.error(xmlhttp.statusText);
        };
        xmlhttp.send();

    });

});