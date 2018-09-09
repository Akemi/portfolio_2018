$(document).ready(function(){
//fügt click-funktion für zurückblättern zum zugehörigen button hinzu
	$("#bib-arrowleft").click(function () {
		//berechnet richte länge zum verschieben und macht eine Paginitaion
		var ele = document.getElementById('bib-booklist');
		var currentmarg = parseInt($("#bib-booklist").css("margin-left"));
		var offset = parseInt($("#bib-books").css("width"))-56;
		var iteration = Math.ceil($("#bib-booklist").find("li").length/7);
		var page = Math.abs(Math.floor(currentmarg/offset));
		
		//testet of mindest margin
		if(-offset*(page-1) > 0){
			ele.style.marginLeft = '0px';
		}
		else{
			ele.style.marginLeft = -offset*(page-1) + 'px';
		}
	});

//fügt click-funktion für weiterblättern zum zugehörigen button hinzu	
	$("#bib-arrowright").click(function () {
		//berechnet richte länge zum verschieben und macht eine Paginitaion
		var ele = document.getElementById('bib-booklist');
		var currentmarg = parseInt($("#bib-booklist").css("margin-left"));
		var offset = parseInt($("#bib-books").css("width"))-56;
		var iteration = Math.ceil($("#bib-booklist").find("li").length/7);
		var page = Math.abs(Math.ceil(currentmarg/offset));
		
		//testet auf max margin
		if(-offset*(page+1) <= -offset*(iteration-1)){
			ele.style.marginLeft = -offset*(iteration-1) + 'px';
		}
		else{
			ele.style.marginLeft = -offset*(page+1) + 'px';
		}
	});

//fügt click-funktion zum löschen zum zugehörigen button hinzu
	$(".bib-delete").click(function () {
		var id = $(this).attr("name"); 
		check = confirm("Wollen sie dieses ePub wirklich löschen?");
		if(check) {
			location.href = "bib.php?delete=" + id;
		}
	});

//fügt click-funktion für linkausgabe zum zugehörigen button hinzu
	$(".bib-link").click(function () {
		var id = $(this).attr("name");
		var url = location.href;
		var pos = url.lastIndexOf('/');

		url = url.substr(0,pos);
		
		prompt("Dies is der Link zum Teilen ihres ePubs.", url + "/index.php?id=" + id)
	});
});