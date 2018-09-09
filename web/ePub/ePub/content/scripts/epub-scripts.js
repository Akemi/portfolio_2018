$(document).ready(function(){

//fügt click-funktion für zurückblättern zum zugehörigen button hinzu
	$("#toolbar-page-back").click(function () {
		var ele = document.getElementById('book-content');	//welches element is betroffen
		ele.style.opacity = "0";							//mach das element unsichtbar mit transition effekt
		resetBookmark();									//resets bookmark object
		setTimeout('pageBackward()', 500);					//wartet bis transition abegeschlossen und geht eine seite weiter
	});

//fügt click-funktion für weiterblättern zum zugehörigen button hinzu
	$("#toolbar-page-forward").click(function () {
		var ele = document.getElementById('book-content');	//gleiche wie funktion oben nur zum zurück blättern
		ele.style.opacity = "0";
		resetBookmark();
		setTimeout('pageForward(\'1\')', 500);
	});

//fügt click-funktion zum aus und einblenden der buchinformationen zum zugehörigen fenster hinzu
	$("#book-info").click(function () {
		hideDiv("book-info-wrapper", 999);	//versteckt die infobox
	});

//fügt click-funktion zum aus und einblenden des inhaltsverzeichnisses zum zugehörigen button hinzu
	$("#book-table_of_content").click(function () {
		hideDiv("book-toc", 10);			//blended ein/aus inhaltsverzeichnis
	});

//fügt click-funktion zum springen zu kapiteln zu jedem inhaltverzeicnis punkt hinzu
	$(".toc-item").click(function () {
		var ele = document.getElementById('book-content');	//welches element is betroffen
		ele.style.opacity = "0";							//mach das element unsichtbar mit transition effekt
		div_id = $(this).attr("name"); 						//holt den kapitel namen zu dem gesprungen werden soll
		hideDiv("book-toc", 10);							//blended inhaltsverzeichnis aus
		resetBookmark();									//resets bookmark object
		setTimeout('gotoPage(div_id)', 500);				//wartet bis transition abegeschlossen und gehe zu angegebenem kapitel
	});

//fügt click-funktion zum ändern ein-/zweiseiten-ansicht zum zugehörigen button hinzu
	$("#toolbar-page-view").click(function () {
		var obj = $("#toolbar-page-view").attr("class");				//holt the aktuelle ansicht
		document.getElementById('book-content').style.opacity = "0";	//mach das element unsichtbar mit transition effekt
		resetBookmark();												//resets bookmark object
		if(obj == "toolbar-buttons toolbar-oneside") {
			setTimeout('changeView(\'1\')', 500);						//wartet bis transition abegeschlossen und ändert view
		}
		else if(obj == "toolbar-buttons toolbar-twoside"){
			setTimeout('changeView(\'2\')', 500);						//wartet bis transition abegeschlossen und ändert view
		}
	});

//fügt click-funktion für nächstes kapitel zum zugehörigen button hinzu
	$("#toolbar-chapter-forward").click(function () {
		var marginset = getRightMargin();								//gibt richtige margin zurück die geblättert werden muss
		var currentmarg = parseInt($("#book-content").css("margin-left"));
		div_id = "";
		resetBookmark();												//resets bookmark object
		$('.book-chapter-helper').each(function(index) {				//sucht alle chapter helper und geht diese durch
			
			if(getScaledPos($(this).position().left) > (-1*currentmarg)+marginset){ //bis am aktuellen chapter + 1 angekommen
				var ele = document.getElementById('book-content');
				ele.style.opacity = "0";								//mach das element unsichtbar mit transition effekt
				div_id = $(this).attr("id"); 							//gibt name des kapitels zurück
				setTimeout('gotoPage(div_id)', 500);					//wartet bis transition abegeschlossen und gehe zu angegebenem kapitel
				return false;											//beende schleife
			}
		});
	});

//fügt click-funktion für vorheriges kapitel zum zugehörigen button hinzu	
	$("#toolbar-chapter-back").click(function () {
		var marginset = getRightMargin();								//gibt richtige margin zurück die geblättert werden muss
		var currentmarg = parseInt($("#book-content").css("margin-left"));
		div_id = "";
		resetBookmark();												//resets bookmark object
		$('.book-chapter-helper').each(function(index) {				//sucht alle chapter helper und geht diese durch
			if(getScaledPos($(this).position().left) < (-1*currentmarg)){	//bis am aktuellen chapter - 1 angekommen
				div_id = $(this).attr("id"); 								//gibt name des kapitels zurück
			}
		});
		if(div_id != ""){												//wenn gefunden
			var ele = document.getElementById('book-content');
			ele.style.opacity = "0";									//mach das element unsichtbar mit transition effekt
			setTimeout('gotoPage(div_id)', 500);						//wartet bis transition abegeschlossen und gehe zu angegebenem kapitel
		}
	});

//fügt click-funktion für vergrößern zum zugehörigen button hinzu	
	$("#toolbar-zoom-in").click(function () {
		var matrixRegex = /matrix\((-?\d*\.?\d+),\s*0,\s*0,\s*(-?\d*\.?\d+),\s*0,\s*0\)/,
		obj = $("#book");
		
		//gibt aktuelle skalierung zurück
		curscale = obj.css("-webkit-transform").match(matrixRegex) ||
					obj.css("-moz-transform").match(matrixRegex) ||
					obj.css("-ms-transform").match(matrixRegex) ||
					obj.css("-o-transform").match(matrixRegex) ||
					obj.css("transform").match(matrixRegex);
		
		//berechnet neue skalierung
		setscale = parseFloat(curscale[1]) + 0.1;
		setscale = Math.floor(setscale*10)/10;
		//darf nicht mehr als 200% haben
		if(setscale > 2){
			setscale = 2;
		}
		//setzt skalierung
		setScale(setscale);
	});

//fügt click-funktion für verkleinern zum zugehörigen button hinzu	
	$("#toolbar-zoom-out").click(function () {
		var matrixRegex = /matrix\((-?\d*\.?\d+),\s*0,\s*0,\s*(-?\d*\.?\d+),\s*0,\s*0\)/,
		obj = $("#book");
		//gibt aktuelle skalierung zurück
		curscale = obj.css("-webkit-transform").match(matrixRegex) ||
					obj.css("-moz-transform").match(matrixRegex) ||
					obj.css("-ms-transform").match(matrixRegex) ||
					obj.css("-o-transform").match(matrixRegex) ||
					obj.css("transform").match(matrixRegex);
		
		//berechnet neue skalierung
		setscale = parseFloat(curscale[1]) - 0.1;
		setscale = Math.floor(setscale*10)/10;
		//darf nicht weniger als 80% haben
		if(setscale < 0.8){
			setscale = 0.8;
		}
		//setzt skalierung
		setScale(setscale);
	});

 //fügt click-funktion für lesezeichen setzen zum zugehörigen button hinzu	
	$("#book-bookmark").click(function () {
		//verschiebt lesezeichen und setzt cookie
		document.getElementById('book-bookmark').style.marginRight = "-115px";
		createCookie();
		//eraseCookie();
	});
	
//fügt click-funktion zum aus und einblenden der buchinformationen zum zugehörigen button hinzu
	$("#toolbar-info").click(function () {
		//blended buchinfo ein
		hideDiv("book-info-wrapper", 999);
	});
	
//wenn lesezeichen gesetzt, springe zur richtigen seite, und bendet buchinfo automatisch aus
	document.getElementById('book-content').style.marginLeft = readCookie() + "px";
	if(readCookie() != null) {
		var ele = document.getElementById("book-info-wrapper");
		ele.style.opacity = "0";
		ele.style.display = "none";
		ele.style.zIndex = "-999";
		ele.className = "hidden";
	}				
});

//varibalen für die funktionen
var bookWidthFull = 810;								//volle buchweite 2 seiten darstellung
var bookWidthHalf = Math.floor(bookWidthFull/2);		//halbe buchweite 1 seiten darstellung
var startLeftMargin = 25;								//anfangsmargin der buchseite

//funktion zum ändern der buch ansicht
//cas - variable zu welcher ansicht gewechselt werden soll
function changeView(cas){
	//setze auf ein seiten darstellung
	if(cas == "1") {
		document.getElementById("toolbar-page-view").className = "toolbar-buttons toolbar-twoside";
		$("#book-pages").css({
			"width": "399px",
		});
		//fügt richtige klasse zum identifizieren hinzu, and löscht die andere
		$("#book-pages").addClass("book-onesided");
		$("#book-pages").removeClass("book-twosided");
	}
	//setze auf zwei seiten darstellung
	else if(cas == "2"){
		document.getElementById("toolbar-page-view").className = "toolbar-buttons toolbar-oneside";
		$("#book-pages").css({
			"width": "800px",
		});
		//fügt richtige klasse zum identifizieren hinzu, and löscht die andere
		$("#book-pages").addClass("book-twosided");
		$("#book-pages").removeClass("book-onesided");
		
		//rechnet die margin so um dass ein und zwei seiten ansicht identisch sind, seiten verschieben sich so nicht.
		var currentmarg = parseInt($("#book-content").css("margin-left"));
		var iter = Math.round(currentmarg/bookWidthFull);
		document.getElementById('book-content').style.marginLeft = startLeftMargin+iter*bookWidthFull+'px';
	}
	setTimeout('showDiv(\'book-content\')', 520);		//wartet bis transition abegeschlossen und zeige buch wieder an
}

//hilfsfunktion um ein div wieder anzuzeigen, wird benutzt von changeView()
//name - name des elementes
function showDiv(name){
	document.getElementById(name).style.opacity = "1";
}

//gibt die richtige position von elementen zurück selbst wenn skaliert
//pos - unskalierte position eines elementes
function getScaledPos(pos){
	var matrixRegex = /matrix\((-?\d*\.?\d+),\s*0,\s*0,\s*(-?\d*\.?\d+),\s*0,\s*0\)/,
	obj = $("#book");
	
	curscale = obj.css("-webkit-transform").match(matrixRegex) ||
				obj.css("-moz-transform").match(matrixRegex) ||
				obj.css("-ms-transform").match(matrixRegex) ||
				obj.css("-o-transform").match(matrixRegex) ||
				obj.css("transform").match(matrixRegex);
	
	setscale = parseFloat(curscale[1]);
	
	//umrechnung der position bei aktivierter skalierung, dies verhindert schlechte rundungen and 1-5px verschiebungen
	pos = Math.round((pos-startLeftMargin)/setscale/bookWidthHalf)*bookWidthHalf+startLeftMargin;
	return pos;
}

//springt zu einem gegebenen kapitel
//loc - name/location des kapitels
function gotoPage(loc){
		var ele2 = document.getElementById('book-content');
		ele2.style.marginLeft = startLeftMargin + 'px';										//reset margin zu standardwert
		var ele = $("#"+loc);
		var position = ele.position();
		var currentmarg = parseInt($("#book-content").css("margin-left"));
		var marginset = getRightMargin();
	
		//richtige rundung für die anzahl der seiten die geblättert werden muss
		var iter = Math.floor(((getScaledPos(position.left)) - currentmarg)/marginset);
		pageForward(iter);																	//springt zur richtigen seite
		
		ele2.style.opacity = "1";															//macht buch wieder sichtbar
}

//geht eine gegebene anzahl von seiten vorwärts
//iter - anzahl der seiten die geblättert werden soll 
function pageForward(iter){
	var ele = document.getElementById('book-content');
	var currentmarg = parseInt($("#book-content").css("margin-left"));
	var marginset = getRightMargin();
	
	//pagination
	//currentmarg = Math.floor((currentmarg-25)/marginset)*marginset+25;
	//berechnet richtige gerundete position and die anzahl der zu blätternden seiten
	var position = $("#book-end").position();
	var iter2 = Math.floor((getScaledPos(position.left))/marginset);
	
	//kann nicht mehr blättern als möglich, schießt nicht über das ende hinaus
	if(currentmarg-(iter*marginset) >= startLeftMargin-(iter2*marginset)){
		ele.style.marginLeft = currentmarg-(iter*marginset) + 'px';						//setzt richtige margin
	}
	
	ele.style.opacity = "1";															//macht buch wieder sichtbar
}

//geht eine seite zurück
function pageBackward(){
	var ele = document.getElementById('book-content');
	var currentmarg = parseInt($("#book-content").css("margin-left"));
	var marginset = getRightMargin();
	
	if(currentmarg+marginset <= startLeftMargin){
		ele.style.marginLeft = currentmarg+marginset + 'px';
	}
	ele.style.opacity = "1";															//macht buch wieder sichtbar
}

//hilfsfunktion um ein div ein-/auszublenden und einen z-index zu setzen, wird benutzt von #book-info, #book-table_of_content, .toc-item
//name - id des divs
//zindex - der zu setzende index wenn eingeblendet
function hideDiv(name, zindex){
	var ele = document.getElementById(name);
	//prüft auf zustand
	if($("#"+name).attr("class") == "visible"){
		ele.style.opacity = "0";
		setTimeout('closeHelpDiv(\''+name+'\')', 1000);			//wartet auf transition und macht sichtbar/unsichtbar
	}
	else{
		ele.style.display = "block";
		ele.style.zIndex = zindex;
		setTimeout('closeHelpDiv(\''+name+'\')', 100);			//wartet auf transition und macht sichtbar/unsichtbar
	}
}

//hilsfunktion für hideDiv() um eine schönen transition effekt zu ermöglichen, ohne z-index setzen
//name - id des divs
function closeHelpDiv(name){
		var ele = document.getElementById(name);
		var val = $("#"+name).attr("class");
		
		if(val == "visible"){
			ele.style.display = "none";
			ele.style.zIndex = "-999";
			ele.className = "hidden";
		}
		else {
			ele.style.opacity = "1";
			ele.className = "visible";
		}	
}

//gibt die richtige margin zurück um zu blättern
function getRightMargin(){
	var marginset = "";
	if($("#book-pages").attr("class") == "book-onesided"){
		marginset = bookWidthHalf;
	}
	else if($("#book-pages").attr("class") == "book-twosided"){
		marginset = bookWidthFull;
	}
	return marginset;
}

//setzt die richtige skalierung für elemente
//setscale - die zu setzende skalierung
function setScale(setscale){
	$("#book").css({
		"-webkit-transform": "scale("+setscale+")", 
		"-moz-transform": "scale("+setscale+")", 
		"-o-transform": "scale("+setscale+")", 
		"-ms-transform": "scale("+setscale+")", 
		"transform": "scale("+setscale+")", 

	});
	//inverse skalirung + richtige rundung	
	setscale = Math.round((1/setscale)*10)/10;
	//toolbar soll nicht mit skaliert werden deswegen inverse skalierung
	$("#toolbar").css({
		"-webkit-transform": "scale("+setscale+")", 
		"-moz-transform": "scale("+setscale+")", 
		"-o-transform": "scale("+setscale+")", 
		"-ms-transform": "scale("+setscale+")", 
		"transform": "scale("+setscale+")", 
	});

}

//setzt die position des bookmarks zur normalen position
function resetBookmark(){
	document.getElementById('book-bookmark').style.marginRight = "-127px";
}

//erstellt das cookie für das lesezeichen
function createCookie(){
	var days = 356;
	var name = getEbookID();
	var value = parseInt($("#book-content").css("margin-left"));
	value = (Math.ceil((value-startLeftMargin)/810)*810)+startLeftMargin;
	
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

//ließt das cookie für das lesezeichen aus
function readCookie(){
	var name = getEbookID();
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

//löscht das cookie, wird nicht active benutzt
function eraseCookie(){
	var days = -1;
	var name = getEbookID();
	var value = "";
	alert(value);
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

//gibt do ebook ID zurück
function getEbookID(){
	var id = window.location.search;
	id = id.replace("?id=", "");
	return id;
}