
$(document).ready(function(){	
//fügt swipe-funktion zum blättern hinzu
	$("#book-content-wrapper").swipe({
		swipe:function(event, direction, distance, duration, fingerCount) {
			if (direction == "left"){
				var ele = document.getElementById('book-content');
				ele.style.opacity = "0";
				resetBookmark();
				setTimeout('pageForward(\'1\')', 500);
			}
			else if (direction == "right"){
				var ele = document.getElementById('book-content');
				ele.style.opacity = "0";
				resetBookmark();
				setTimeout('pageBackward()', 500);
			}
  		},
  		threshold:200
	});
					
});