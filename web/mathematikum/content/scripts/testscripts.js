		function testResults(tForm) {
			var ergebnis = tForm.elements["ergebnis"];
			var ergebnisValue = ergebnis.value;

			
			var chosen = "0";
			var laufer = "1";
			var objToColor = document.getElementById(laufer);
			

			for (i=0; i<document.testForm.r1.length; i++){
				if (document.testForm.r1[i].checked==true) {
					if (document.testForm.r1[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}			
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r2.length; i++){
				if (document.testForm.r2[i].checked==true) {
					if (document.testForm.r2[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			
			
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r3.length; i++){
				if (document.testForm.r3[i].checked==true) {
					if (document.testForm.r3[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r4.length; i++){
				if (document.testForm.r4[i].checked==true) {
					if (document.testForm.r4[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r5.length; i++){
				if (document.testForm.r5[i].checked==true) {
					if (document.testForm.r5[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r6.length; i++){
				if (document.testForm.r6[i].checked==true) {
					if (document.testForm.r6[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r7.length; i++){
				if (document.testForm.r7[i].checked==true) {
					if (document.testForm.r7[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r8.length; i++){
				if (document.testForm.r8[i].checked==true) {
					if (document.testForm.r8[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r9.length; i++){
				if (document.testForm.r9[i].checked==true) {
					if (document.testForm.r9[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			chosen = "0";
			laufer++;
			objToColor = document.getElementById(laufer);
			
			for (i=0; i<document.testForm.r10.length; i++){
				if (document.testForm.r10[i].checked==true) {
					if (document.testForm.r10[i].value==ergebnisValue.charAt(laufer-1)) {
						objToColor.style.backgroundColor="#8aff51";
						chosen = "1";
					}
					else {
						objToColor.style.backgroundColor="#FF4343";
						chosen = "1";
					}
				}
				else if (chosen != 1) {objToColor.style.backgroundColor="#FF4343";}
			}
			
			

		}