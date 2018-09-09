function toolbarscroll() {
	var referenceObj = document.getElementById("content-wrapper");
	var toAlignObj = document.getElementById("admin-toolbar");
			
	var referenceObjTop = 0;
	if (referenceObj.offsetParent) {	
		do {
			referenceObjTop += referenceObj.offsetTop;	
		} while (referenceObj = referenceObj.offsetParent);
	}
			
	var scrollFromTop;
	if (self.pageYOffset) {
		scrollFromTop = self.pageYOffset;
	} else if (document.documentElement && document.documentElement.scrollTop) {
		scrollFromTop = document.documentElement.scrollTop;
	} else if (document.body) {
		scrollFromTop = document.body.scrollTop;
	}
	var toAlignObjTopWindow = referenceObjTop-scrollFromTop;
 	
 	if(toAlignObjTopWindow<=0){
 		toAlignObj.style.position="fixed";
 		toAlignObj.style.top="0px";
 	}
 	else {
 		toAlignObj.style.position="absolute";
 		toAlignObj.style.top="270px";
 		}
}




function resizePreviewIFrame() {
	var frame = window.parent.document.getElementById("preview-iframe");
	var resizerObj = document.getElementById("resizer");
			
	var newFrameHeight = resizerObj.clientHeight + 30;
			
	frame.style.height = newFrameHeight + "px";
}




function submitFormAddArtikel() {
	if(document.pressed == 'Abschicken') {
		document.form.action ="index.php?add=11";
		document.form.target="_self";
	}
	else if(document.pressed == 'Vorschau') {
		document.form.action ="preview.php";
		document.form.target="preview-iframe";
	}
	return true;
}

function submitFormAddTest() {
	if(document.pressed == 'Abschicken') {
		document.form.action ="index.php?add=21";
		document.form.target="_self";
	}
	else if(document.pressed == 'Vorschau') {
		document.form.action ="preview.php";
		document.form.target="preview-iframe";
	}
	return true;
}



function submitFormEditNavi(id) {
	if(document.pressed == 'Speichern') {
		document.form.action ="index.php?naviid="+id+"&edit=2";
		document.form.target="_self";
	}
	else if(document.pressed == 'Vorschau') {
		document.form.action ="preview.php";
		document.form.target="preview-iframe";
	}
	return true;
}

function submitFormEditTest(id) {
	if(document.pressed == 'Speichern') {
		document.form.action ="index.php?testid="+id+"&edit=2";
		document.form.target="_self";
	}
	else if(document.pressed == 'Vorschau') {
		document.form.action ="preview.php";
		document.form.target="preview-iframe";
	}
	else if(document.pressed == 'Löschen') {
		check = confirm("Wollen sie diesen Eintrag wirklich löschen?");
		if (check == false){
			document.form.action ="index.php?testid="+id+"&edit=1";
			document.form.target="_self";
		}
		else {
			document.form.action ="index.php?testid="+id+"&edit=3";
			document.form.target="_self";
		}
	}
	return true;
}

function submitFormEditArtikel(id) {
	if(document.pressed == 'Speichern') {
		document.form.action ="index.php?artikelid="+id+"&edit=2";
		document.form.target="_self";
	}
	else if(document.pressed == 'Vorschau') {
		document.form.action ="preview.php";
		document.form.target="preview-iframe";
	}
	else if(document.pressed == 'Löschen') {
		check = confirm("Wollen sie diesen Eintrag wirklich löschen?");
		if (check == false){
			document.form.action ="index.php?artikelid="+id+"&edit=1";
			document.form.target="_self";
		}
		else {
			document.form.action ="index.php?artikelid="+id+"&edit=3";
			document.form.target="_self";
		}
	}
	return true;
}

function delupload(datei){
	check = confirm("Wollen sie diesen Upload wirklich löschen?");
	if(check) {
		location.href = "index.php?delupload="+datei;
	}
}