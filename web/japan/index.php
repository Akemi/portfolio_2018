    <?php 
    include('header.php');
    $curImage = date('d')%8;
    echo '<body class="startseite" style="background:url(img/'.$curImage.'.jpg) 100% 100% fixed no-repeat">';
    include('nav.php');
     ?>
    <!-- CONTENT Anfang -->
    <main class="container marketing">
      <div class="row">
        <div class="col-xs-12 randomfact-box">
            <div class="col-xs-1 info-icon vcenter"><i class="fa fa-info"></i></div>
            <div class="col-xs-12 col-sm-10 col-md-11 randomfact vcenter">Gefragt nach ihrer besten Erfindung des 20. Jahrhunderts antwortete die Mehrheit der Japaner mit Instant-Nudeln.</div>
        </div>
      </div>
      <div class="row vcenter">
        <div class="col-lg-3 col-sm-6">
          <div class="marketing-box">
            <div id="all" class="info-circle info-circle-hover">
              <a href="geographie.php"><img id="geo" class="home-hover-desc" src="img/geo-gray.jpg" alt="Geography"></a><a href="touristik.php"><img id="tour" class="home-hover-desc" src="img/tour-gray.jpg" alt="Touristik"></a>
            </div>
            <div id="all-content-main">
            	<h2>Touristik</h2>
              <p>Verschaff dir hier zunächst einen Überblick über das Land als solches und welche Schätze und Besonderheiten es zu bieten hat. Neben geografischen Fakten kannst du dir hier auch ein Bild über die Festivalkultur machen.</p>
            </div>
            
            <div id="geo-content" class="all-sub-content" style="display: none;">
              <h2>Geographie</h2>
              <p>Japan ist ein Land der Gegensätze - Von ruhiger Natur im Landesinneren hin zu 
				hektischen Millionenstädten an den Küstenstädten ist alles vertreten. Der folgende Abschnitt 
 				geht auf diese Gegensätze genauer ein und hilft dir, ein genaueres Bild von Japan zu vermitteln.</p>
            </div>
            
            <div id="all-content" class="all-sub-content" style="display: none;">
              <h2>Touristik</h2>
              <p>Verschaff dir hier zunächst einen Überblick über das Land als solches und welche Schätze und Besonderheiten es zu bieten hat. Neben geografischen Fakten kannst du dir hier auch ein Bild über die Festivalkultur machen.</p>
            </div>
            
            <div id="tour-content" class="all-sub-content" style="display: none;">
              <h2>Festivalführer</h2>
              <p>Das ganze Jahr über finden in Japan große Festivals statt, die du auf Grund der Vielfältigkeit erlebt haben musst. Neben einer kurzen Erläuterung, wie die Festivals aufgebaut sind kannst du im Festivalführer einen Reisezeitraum auswählen und dir mögliche Feste einblenden lassen. </p>
            </div>
          </div>
        </div>

        <div class="col-lg-3 col-sm-6">
          <div class="marketing-box">
            <div id="reisevor" class="info-circle-single">
              	<a href="reisevorbereitung.php"><img src="img/reisevor-gray.jpg" alt="Reisevorbereitung"></a>
            </div>
            <h2>Reisevorbereitung</h2>
            <p>Eine Reise nach Japan erfordert eine gute Planung, 
			um einerseits den Urlaub stressfreier zu gestalten, andererseits 
			um die Kosten zu senken. Schau hier hinein, wenn du alles über 
			Einreisebedingungen, Flüge, Transportmittel usw. erfahren möchtest.</p>
          </div>
        </div>
        <div class="col-lg-3 col-sm-6">
          <div class="marketing-box">
            <div id="reiseknigge" class="info-circle-single">
              	<a href="reiseknigge.php"><img src="img/reiseknig-gray.jpg" alt="Reisevorbereitung"></a>
            </div>
            <div id="kultur-content-main">
            <h2>Reiseknigge</h2>
              <p>Japan ist bekannt für seine Höflichkeitsregeln. 
				Wenn du einen möglichst authentischen Urlaub erleben willst, viel 
				Kontakt mit Einheimischen suchst und ihre Handlungen deuten möchtest, 
				sollte hier einen Blick riskieren.</p>
			</div>         
          </div>
        </div>
        <div class="col-lg-3 col-sm-6">
          <div class="marketing-box">
          	<div id="sprache" class="info-circle-single">
              	<a href="sprache.php"><img src="img/sprache-gray.jpg" alt="Sprache"></a>
            </div>
            <h2>Sprache</h2>
            <p>Englisch ist nicht in jeder Region verbreitet. 
			Es ist also gut, wenn du dir zumindest Grundkenntnisse der 
			japanischen Sprache aneignest. Im folgenden Abschnitt werden dir 
			die grundlegenden Vokabeln mit Hörbeispiel vorgestellt.</p>
          </div>
        </div>
      </div>
    </main>
    <!-- CONTENT Ende -->
    <?php 
    include('footer.php');
     ?>
  </body>
</html>

