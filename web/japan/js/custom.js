$(document).ready(function () {

    /* NAVBAR HIDE BEHAVIOUR */
    $(window).scroll(function () {
        navbarHeight = $(".navbar").height();
        //wie viel vom kreis zu sehen sein soll
        redcirleOffset = 0.25 * navbarHeight + 1;

        if (self.pageYOffset < navbarHeight) {
            $(".navbar").css("top", -self.pageYOffset);
            $("img.redcircle").css("margin-top", redcirleOffset * (self.pageYOffset / navbarHeight));
            $("img.redcircle").css("margin-bottom", -redcirleOffset * (self.pageYOffset / navbarHeight));
        } else {
            $(".navbar").css("top", -navbarHeight - 1);
            $("img.redcircle").css("margin-top", redcirleOffset);
            $("img.redcircle").css("margin-bottom", -redcirleOffset);
        }
        
        if($('.nord-japan-sillhouette').length){
        	var marginJapanTop = 100;
			var marginJapanBottom = 100;
			var scrollPosition = $(window).scrollTop();
			var windowHeight = $(window).height();
			var docHeight = $(document).height();
			var imgHeight = $(".nord-japan-sillhouette").height();
		
			var scrollPositionPercent = scrollPosition/(docHeight-windowHeight);
			var scollWidth = windowHeight-marginJapanTop-marginJapanBottom-imgHeight;
		
			var topPosition = scollWidth*scrollPositionPercent + marginJapanTop;
			
			$(".nord-japan-sillhouette").css({ top: topPosition+'px' });
        }
        
    });


    /* SIDE INDEX BEHAVIOUR */
    $('#subnav').affix({
        offset: {
            bottom: function () {
                return (this.bottom = $('.footer').outerHeight(true))
            }
        }
    })

    $('body').scrollspy({
        target: '.sidebar',
        offset: 40
    });

    //Startseite
    /*var backgroundCount=8;
    var factCount=13;
    var choosenImg=(new Date()).getDay()%backgroundCount;
    $(".startseite").ready(function(){
        if($('.startseite').length ==0) return;
      $("body.startseite").css({
        "background":"url(img/"+choosenImg+".jpg) 100% 100% fixed no-repeat",
        "background-size":"cover"
      });
    });*/
    var factCount=13;
    var choosenFact=(new Date()).getDay()%factCount;
    $(".randomfact").ready(function(){
       if($('.randomfact').length ==0) return;
       $.getJSON("data/randomfacts.json", function (json) {
          if(choosenFact>=json.facts.length) choosenFact=0;
          $(".randomfact").html(json.facts[choosenFact]);
      });
    });

    /* HOME PAGE CIRCLE HOVER BEHAVIOUR */
    var timeout = 400;

    $(".home-hover-desc")
        .on("mouseenter", function () {
            var elenameEnterDesc = $(this).attr('id');
            var parentEleEnterDesc = $(this).parent().parent().attr('id');
            var descEnterDesc = $("#" + elenameEnterDesc + "-content").html();
            $("#" + parentEleEnterDesc + "-content-main").fadeOut(timeout, function () {
                $("#" + parentEleEnterDesc + "-content-main").html(descEnterDesc);
                $("#" + parentEleEnterDesc + "-content-main").fadeIn(timeout);
            });
        }).on("mouseleave", function () {
            var parentEleLeaveDesc = $(this).parent().parent().attr('id');
            var descEnterDesc = $("#" + parentEleLeaveDesc + "-content").html();
            $("#" + parentEleEnterDesc + "-content-main").stop();
        });

    $(".info-circle-hover")
        .on("mouseleave", function () {
            var elenameLeaveCircle = $(this).attr('id');
            var descLeaveCircle = $("#" + elenameLeaveCircle + "-content").html();
            $("#" + elenameLeaveCircle + "-content-main").fadeOut(timeout, function () {
                $("#" + elenameLeaveCircle + "-content-main").html(descLeaveCircle);
                $("#" + elenameLeaveCircle + "-content-main").fadeIn(timeout);
            });
        });
      

    //Reisevorbereitung

    $(".faq-box").ready(function () {
        if($('.faq-box').length ==0) return;
        /*$.getJSON("data/faq.json", function (json) {
            var htmlString = '<div class="faq-inhaltsverzeichnis">';
            var subNavString="";

            for (var i = 0; i < json.length; i++) {
                htmlString += '<h2><a href="#category' + i + '">' +(i+1)+". "+ json[i].category + "</a></h2>";
                var faq = json[i].faq;
                for (var j = 0; j < faq.length; j++) {
                    htmlString += '<h4><a href="#frage' + i + ',' + j + '">'+(i+1)+"."+(j+1)+" " + faq[j].frage + '</a></h4>';
                }
            }
            htmlString += '</div>';

            for (var i = 0; i < json.length; i++) {
                htmlString += '<h3 id="category' + i + '">' +(i+1)+". "+ json[i].category + '</h2>';
                subNavString+='<li><a href="#category' + i + '">'+(i+1)+". "+json[i].category+'</a></li>';
                var faq = json[i].faq;
                htmlString += '<div class="panel panel-default accordion">'
                for (var j = 0; j < faq.length; j++) {
                    htmlString += '<h4 id="frage' + i + ',' + j + '" class="panel-heading">'+(i+1)+"."+(j+1)+" " + faq[j].frage + '<i class="indicator glyphicon glyphicon-chevron-down  pull-right"></i></h4>';
                    htmlString += '<div class="panel-body">' + faq[j].antwort + '</div>'
                }
                htmlString += '</div>'
            }

            $(".faq-box").html(htmlString);
            $("#subnav").html(subNavString);*/
            $('.accordion').find('h4').click(function () {
                $(".accordion > div").not($(this).next()).slideUp();
                $(this).next().slideToggle();
                var down= $(this).find('i').hasClass("glyphicon-chevron-down");
                $(".faq-box i").removeClass("glyphicon-chevron-up");
                $(".faq-box i").addClass("glyphicon-chevron-down");
                if(down){
                	 $(this).find('i').removeClass("glyphicon-chevron-down");
                	 $(this).find('i').addClass("glyphicon-chevron-up");
                 }  
                
            });

            /* REFRESH SCROLL SPY LISTENER AFTER DYNAMIC CREATION OF PAGE */
                $('[data-spy="scroll"]').each(function () {
                    var $spy = $(this).scrollspy('refresh')
                });

       // });
    });

    //Touristik
    if (!Modernizr.inputtypes.date) {
        $('input[type=date]').datepicker({
            // Consistent format with the HTML5 picker
            dateFormat: 'dd.mm.yy'
        });
    }

    var map;
    var markers;

    function festivalmapinitialize() {
        var mapOptions = {
            zoom: 5,
            center: new google.maps.LatLng(38.0, 138.0)
        };
        $.getJSON("data/festivals.json", function (json) {
            markers = new Array();
            for (var i = 0; i < json.length; i++) {
                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(json[i].latitude, json[i].longitude),
                    map: map,
                    title: json[i].title
                });

                var latlng = marker.position;
                if (markers.length != 0) {
                    for (i=0; i < markers.length; i++) {
                        var existingMarker = markers[i].marker;
                        var pos = existingMarker.getPosition();
                        //if a marker already exists in the same position as this marker
                        if (latlng.equals(pos)) {
                            console.log("found duplicate");
                            //update the position of the coincident marker by applying a small multipler to its coordinates
                            var newLat = latlng.lat() + (Math.random()*(1575-1500)) / 1500;// * (Math.random() * (max - min) + min);
                            var newLng = latlng.lng() + (Math.random()*(1575-1500)) / 1500;// * (Math.random() * (max - min) + min);
                            marker.position = new google.maps.LatLng(newLat,newLng);
                        }
                    }
                }
                json[i].marker = marker;
                json[i].starttime = parseJSONDate(json[i].starttime);
                json[i].endtime = parseJSONDate(json[i].endtime);
                var infowindow = new google.maps.InfoWindow();
                google.maps.event.addListener(marker, 'click', (function (marker, i) {
                    return function () {
                        infowindow.setContent("Das Festival " + json[i].title + " findet hier vom " + printDateInInfobox(json[i].starttime) + " bis " + printDateInInfobox(json[i].endtime) + " statt. Weitere Informationen finden Sie <a href=\"" + json[i].link + "\">hier</a>");
                        infowindow.open(map, marker);
                    }
                })(marker, i));
                markers.push(json[i]);
            }
        });
        map = new google.maps.Map(document.getElementById('festivalmap'), mapOptions);
    }

    var startDate = new Date(1970, 1, 1);
    var endDate = new Date(2100, 12, 31);

    $('#filterFestivals').click(function () {
        startDate = parseDatepickerDate($('#date-after').val());
        endDate = parseDatepickerDate($('#date-before').val());
        if (startDate > endDate) {
            alert('Dies ist nicht möglich!');
        }
        filterFestivals();
    })

    function filterFestivals() {
        for (var i = 0; i < markers.length; i++) {
            if (
                (markers[i].starttime < startDate && markers[i].endtime > startDate) ||
                (markers[i].starttime > startDate && markers[i].starttime < endDate)
            ) {
                markers[i].marker.setMap(map);
            } else {
                markers[i].marker.setMap(null);
            }
        }
    }

    function printDateInInfobox(inputdate) {
        console.log(inputdate);
        var test=(inputdate.getDate()) + "." + (inputdate.getMonth() + 1) + "." + inputdate.getFullYear();
        console.log(test);
        return test;
    }

    function parseDatepickerDate(input) {
        var parts = input.match(/^(\d{2}).(\d{2}).(\d{4})$/);
        return new Date(parts[3], parts[2] - 1, parts[1]);
    }

    function parseJSONDate(input) {
        var parts = input.match(/^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2})$/);
        //console.log(parts);
        return new Date(parts[1], parts[2] - 1, parts[3], parts[4], parts[5]);
    }

    google.maps.event.addDomListener(window, 'load', festivalmapinitialize);

    var language;
    /* Sprache */
    $(".language").ready(function(){
        if($('.language').length ==0) return;
       $.getJSON("data/sprache.json", function (json) {
            language=json;
            var htmlString="";
            var subNavString="";
            for(var i=0; i<json.length;i++){
                var category=json[i];
                htmlString+='<h3 id="'+makeSafeForCSS(category.category)+'">'+category.category+'</h3>';
                subNavString+='<li><a href="#'+makeSafeForCSS(category.category)+'">'+category.category+'</a></li>';
                htmlString+='<div class="table-responsive"><table class="table">';
                htmlString+='<th>Deutsch</th><th>Japanisch</th>';
                var vokabeln = category.vokabeln;
                for(var j=0; j<vokabeln.length;j++){
                    var audio=vokabeln[j].aussprache;
                    htmlString+='<tr>';
                    htmlString+='<td style="width: 50%">'+vokabeln[j].deutsch+'</td>';
                    htmlString+='<td style="width: 50%">';
                    htmlString+='<a id="'+makeSafeForCSS(vokabeln[j].japan)+'" class="play-voc"><i class="fa fa-play"></i></a> ';
                    htmlString+=vokabeln[j].japan;
                    htmlString+='<audio id="'+makeSafeForCSS(vokabeln[j].japan)+'-play" preload="none">';
                    htmlString+='<source src="audio/'+audio[0]+'" type="audio/mp3" />';
                    htmlString+='<source src="audio/'+audio[1]+'" type="audio/ogg; codecs=vorbis" />';
                    htmlString+='<source src="audio/'+audio[2]+'" type="audio/mp4" />';
                    htmlString+='Your Browser does not support the html5 audio tag.';
                    htmlString+='</audio>';
                    htmlString+='</td>';
                    htmlString+='</tr>';
                }
                htmlString+='</table></div>';
                $(".language").html(htmlString);
                $("#subnav").html(subNavString);
                
                /* Add onClick function zu play button, nur ein element spielt zur gleichen zeit */
                $('.play-voc').click(function () {
        			var elenamePlayAudio = $(this).attr('id');
        			var audiole = $("#"+elenamePlayAudio+"-play")[0];
        			
        			$('.play-voc').removeClass("play-voc-active");
        			$(this).addClass("play-voc-active");
        			
        			$('audio').each(function(){
    					if($(this).attr('id') != elenamePlayAudio+"-play")
    						this.pause();
					}); 
					
        			if(audiole.paused) {
                   		audiole.play();
						audiole.media.addEventListener('playing', function(){
   							 audiole.setCurrentTime(0);
						});
                	}
                	else {
                    	$(this).removeClass("play-voc-active");
                    	audiole.pause();
                	}
    			});
    			
    			/* EVENT TO REMOVE ACTIVE CLASS AFTER END OF TRACK */
    			$('audio').each(function(){
    				var audioele2 = $(this).attr('id');
    				audioele2 = audioele2.replace("-play", "");
    				$(this)[0].addEventListener("ended",function() {
        				$("#"+audioele2).removeClass("play-voc-active");
    				});
				}); 
    			
    			/* REFRESH SCROLL SPY LISTENER AFTER DYNAMIC CREATION OF PAGE */
    			$('[data-spy="scroll"]').each(function () {
  					var $spy = $(this).scrollspy('refresh')
				});
            }
        });
        
        
    });

    
    function makeSafeForCSS(name) {
    	return name.replace(/[^a-z0-9]/g, function(s) {
        	var c = s.charCodeAt(0);
        	if (c == 32) return '-';
        	if (c >= 65 && c <= 90) return '_' + s.toLowerCase();
        	return '__' + ('000' + c.toString(16)).slice(-4);
    	});
	}
    
    
    /* QUIZ ERSTELLEN */
    var quiz;
    $("#quiz-frame").ready(function(){
    	if($('#quiz-frame').length == 0) return;
    	var quizName = $('#quiz-frame').attr('name');
        $.getJSON("data/fragen-" + quizName + ".json", function (json) {
            quiz=json;
            var questionList = "";
            var circlePagination = "";
            var hintBox = "";
            for(var i=0; i < quiz.length-1; i++){
            	var question = quiz[i];
            	
            	if(i == 0){
            		circlePagination += "<li class=\"fa fa-circle-o active\"></li>";
            		questionList += "<li>";
            	}
            	else {
            		circlePagination += "<li class=\"fa fa-circle-o\"></li>";
            		questionList += "<li style=\"display: none\">";
            	}
            	
            	
            	questionList += "<h1>" + question.aufgabe + "</h1>";
            	questionList += "<form action=\"\">";
            	
            	
 				var answers = question.answers;
                
            	if(question.type == "single") {
            		//alert(question.type);
            		for(var j = 0; j < answers.length; j++){
                    	questionList += "<div><input type=\"radio\" name=\"antwort\" value=\"" + j + "\">" + answers[j] + "</div>";
            		}
            	}
            	if(question.type == "multiple") {
            		//alert(question.type);
            		for(var j = 0; j < answers.length; j++){
                    	questionList += "<div><input type=\"checkbox\" name=\"antwort\" value=\"" + j + "\">" + answers[j] + "</div>";
            		}
            	}
            	if(question.type == "singleImage") {
            		//alert(question.type);
            		questionList += "<img alt=\"quiz bild\" class=\"quiz-image\" src=\"img/" + question.bild + "\">"
            		for(var j = 0; j < answers.length; j++){
                    	questionList += "<div><input type=\"radio\" name=\"antwort\" value=\"" + j + "\">" + answers[j] + "</div>";
            		}
            	}
            	if(question.type == "multipleImage") {
            		questionList += "<div class=\"multiple-image-q\">";
            		for(var j = 0; j < answers.length; j++){
                    	questionList += "<label class=\"radio-image\"><input type=\"radio\" name=\"antwort\" value=\"" + j + "\"><img src=\"img/" + answers[j] + "\"></label>";
            		}
            		questionList += "</div>";
            	}
            	if(question.type == "singleImageMultiple") {
            		//alert(question.bild);
            		questionList += "<img alt=\"quiz bild\" class=\"quiz-image\" src=\"img/" + question.bild + "\">"
            		for(var j = 0; j < answers.length; j++){
                    	questionList += "<div><input type=\"checkbox\" name=\"antwort\" value=\"" + j + "\">" + answers[j] + "</div>";
            		}
            	}
            	if(question.type == "multipleImageMultiple") {
            		questionList += "<div class=\"multiple-image-q\">";
            		for(var j = 0; j < answers.length; j++){
                    	questionList += "<label class=\"radio-image\"><input type=\"checkbox\" name=\"antwort\" value=\"" + j + "\"><img src=\"img/" + answers[j] + "\"></label>";
            		}
            		questionList += "</div>";
            	}
            	if(question.type == "custom") {
                    questionList += answers;
            	}
            	
            	questionList += "<div class=\"button submit fa fa-check-circle\"></div><div class=\"button hint fa fa-question-circle\"></div>";
            	questionList += "</form>";
            	questionList += "</li>";
            }
            
            $("#question-list").html(questionList);
            $("#circle-pagination").html(circlePagination);
            
            
            /* QUIZ BEHAVIOUR */
			$('#circle-pagination > li').click(function () {
				var circleClickIndex = $(this).index()+1;
		
				quizJumpToQuestion(circleClickIndex);
			});
	
			$('#question-list .submit').click(function () {
				var indexCurrentQuiz = $('#circle-pagination > li.active').index()+1;
				var quizResult = false;
				
				//teste ob nicht custom quiz
				if(quiz[indexCurrentQuiz-1].type != "custom") {
					//erstelle array mit index checked antworten
					var arrayCheckedAnswers = [];
					$("#question-list li:nth-child(" + indexCurrentQuiz + ") input").each(function( index ) {
						if($(this).is(':checked')) {
							arrayCheckedAnswers.push(index);
						}
					});
					//vergleiche ergebnis
					quizResult = compareArrays(arrayCheckedAnswers, quiz[indexCurrentQuiz-1].rightanswer);
				}
				else {
					quizResult = checkCustomTest(quiz[indexCurrentQuiz-1].rightanswer);
				}
				
				//wenn richtig, add solved class und gehe zur nächsten frage, ansonsten feedback + failed class
				if(quizResult) {
					$('#circle-pagination > li:nth-child(' + indexCurrentQuiz + ')').addClass('solved');
					quizNextQuestion();
				}
				else {
					$('#circle-pagination > li:nth-child(' + indexCurrentQuiz + ')').addClass('failed');
					
					if(quiz[indexCurrentQuiz-1].feedback == "") {
						//$('#hint > #hint-content').html("Sorry hier gibt es keine Hints, es wäre zu einfach.");
						var answer = quiz[indexCurrentQuiz-1].rightanswer;
						var text = "";
						
						//wenn bilder als antwort soll die nummer des bildes ausgegeben werden, ansonsten gebe die richtigen antworten zurück
						if(quiz[indexCurrentQuiz-1].type == "multipleImageMultiple" || quiz[indexCurrentQuiz-1].type == "multipleImage") {
							if(answer.length <= 1) {
								text += "Bild Nummer " + (parseInt(answer[0])+1).toString() + " ist richtig.";
							}
							else {
								text += "Die Bilder ";
								for(var i=0; i < answer.length-1; i++){
									if(i >= answer.length-2) {
										text += (parseInt(answer[i])+1).toString() + " und " + (parseInt(answer[i+1])+1).toString();
									}
									else {
										text += (parseInt(answer[i])+1).toString() + ", ";
									}
								}
								text += " sind richtig.";
							}
						}
						else {
							if(answer.length <= 1) {
								text += "Folgende Antwort ist richtig:</br></br>";
							}
							else {
								text += "Folgende Antworten sind richtig:</br></br>";
							}
							for(var i=0; i < answer.length; i++){
								if(i >= answer.length-1) {
									text += quiz[indexCurrentQuiz-1].answers[answer[i]];
								}
								else {
									text += quiz[indexCurrentQuiz-1].answers[answer[i]] + "</br>";
								}
            				}
						}
            			$('#feedback > #feedback-content').html(text);
					}
					else {
						$('#feedback > #feedback-content').html(quiz[indexCurrentQuiz-1].feedback);
					}
					
					$('#feedback').fadeIn(timeout);
				}
				
				$(this).hide();
			});
			
			$('#question-list .hint').click(function () {
				var indexNextQuiz = $('#circle-pagination > li.active').index();
				
				if(quiz[indexNextQuiz].hint == "") {
					$('#hint > #hint-content').html("Sorry hier gibt es keine Hints, es wäre zu einfach.");
				}
				else {
					$('#hint > #hint-content').html(quiz[indexNextQuiz].hint);
				}
				$('#hint').fadeIn(timeout);
			});
			$('#hint .hint-close').click(function () {
				$('#hint').fadeOut(timeout);
			});
			
			$('#feedback .feedback-close').click(function () {
				$('#feedback').fadeOut(timeout, function() {
					quizNextQuestion();
				});
			});
			
			function quizNextQuestion() {
				var indexNextQuiz = $('#circle-pagination > li.active').index()+2;
				if($('#circle-pagination > li').size() >= indexNextQuiz){
					quizJumpToQuestion(indexNextQuiz);
				}
				quizEndFeedback();
			}
	
			function quizJumpToQuestion(index) {
				if($('#circle-pagination > li').size() >= index){
					$('#question-list > li').stop();
					$('#circle-pagination > li').removeClass("active");
					$('#question-list > li:visible').fadeOut(timeout, function() {
						$('#question-list > li:nth-child(' + index + ')').fadeIn(timeout);
						$('#circle-pagination > li:nth-child(' + index + ')').addClass('active');
					});
				}
			}
            
            function compareArrays(arr1, arr2) {
    			return $(arr1).not(arr2).length == 0 && $(arr2).not(arr1).length == 0;
			}
			
			function quizEndFeedback() {
    			var solvedCount = $('#circle-pagination > li.solved').length;
    			var failedCount = $('#circle-pagination > li.failed').length;
    			var allCount = $('#circle-pagination > li').length;
    			var solvedPer = (solvedCount/allCount)*100;
    			
    			if(solvedCount + failedCount == allCount) {
    				var text = "Du hast " + solvedCount + " von " + allCount + " Fragen richtig beantwortet.</br></br>";
    				if(solvedPer >= 85) {
    					text += quiz[quiz.length-1].feedback1;
    				}
    				else if(solvedPer < 85 && solvedPer >= 35) {
    					text += quiz[quiz.length-1].feedback2;
    				}
    				else {
    					text += quiz[quiz.length-1].feedback3;
    				}
    				
    				$('#hint > #hint-content').html(text);
					$('#hint').fadeIn(timeout);
    			}	
			}
        }); 
    });
});

/* CUSTOM TEST FUNCTIONS CHECK */
function checkCustomTest(test) {
	if(test = "mapTest") {
		solved = true;
	
		$('.drop-ele').each(function( index ) {
			elementname = $(this).attr('id');
			elementname = elementname.slice(0, -5);
			
			if($(this).children().length = 1) {
				if($(this).children().eq(0).attr('id') == elementname + "-drag" && solved == true) {
					solved = true;
				}
				else {
					solved = false;
					return solved;
				}
			}
		});
		return solved;
	}
}


/* CUSTOM TEST FUNCTIONS */

function allowDrop(ev) {
	ev.preventDefault();
}
function drag(ev) {
	ev.dataTransfer.setData("Text", ev.target.id);
}
function drop(ev) {
	ev.preventDefault();
	var data = ev.dataTransfer.getData("Text");
	ev.target.appendChild(document.getElementById(data));
}
