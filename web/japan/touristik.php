    <?php 
    include('header.php');
    echo '<body class="subside">';
    include('nav.php');
     ?>
    <!-- CONTENT Anfang -->
    <main class="container-fluid">
      <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-lg-8 col-lg-offset-2 content-box text">
          <p>Japan bietet das ganze Jahr über eine breite Palette an Volksfesten, die matsuri genannt werden.<br />
          Das liegt vor allem daran, dass fast jeder buddhistische oder shintoistische Schrein im Land 
          zelebriert wird. Die meisten Festivals werden dabei jährlich abgehalten und werden dabei nach 
          saisonalen oder historischen Eregnissen ausgerichtet. Alle größeren Festivals sind dabei von mehreren 
          Tagen Länge. Das zentrale Element stellen dabei Prozessionen dar (ähnlich wie bei katholischen Festen), 
          bei denen der Schrein auf einer Sänfte  durch die Stadt getragen wird.
          <div class="row">
                  <div class="col-xs-offset-3 col-xs-6 well">
                    <img class="img-responsive center-block" src="img/Schrein (Mikoshi).jpg"><br />
                    <span class="pull-right caption">Schrein (Mikoshi)</span>
                  </div>
          </div>
          Es ist oft der einzige Anlass, bei der der Schrein seinen angestammten Ort verlässt. Die meisten Prozessionen werden 
          dabei von dekorierten Booten.
          <div class="row">
                  <div class="col-xs-offset-3 col-xs-6 well">
                    <img class="img-responsive center-block" src="img/Dekoriertes Boot (Dashi).jpg"><br />
                    <span class="pull-right caption">Dekoriertes Boot (Dashi)</span>
                  </div>
              </div>
          Jedes Festival hat eine eigene Charakteristik. Während manche eher ruhiger Natur sind, 
          sind die allermeisten laut und energetisch. Für jeden Geschmack ist also etwas dabei.<br /></p>
        
          <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading">Festivalkarte <a data-toggle="collapse" data-parent="#accordion" href="#explainFestival"><span class="glyphicon glyphicon-exclamation-sign pull-right"></span></a></div>
              <div class="panel-body">
              <div id="explainFestival" class="panel-collapse collapse alert alert-info" role="alert">
              Mit dieser Festivalkarte kannst du einfach sehen, welche Festivals in einem bestimmten Zeitrahmen stattfinden.
              Einfach Start- und Enddatum eingeben und auf den Filter (<span class="glyphicon glyphicon-filter"></span>)-knopf drücken.
              Standardmäßig siehst du alle Festivals des Jahres.
              </div>
                <div class="col-sm-5"><div class="input-group"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span><input id="date-after" type="date" class="form-control" placeholder="Startdatum"/></div></div>
                <div class="col-sm-5"><div class="input-group"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span><input id="date-before" type="date" class="form-control" placeholder="Enddatum"/></div></div>
              <div class="col-sm-2 btn btn-primary" id="filterFestivals"><span class="glyphicon glyphicon-filter"></span></div></div>
              <div class="col-xs-12" id="festivalmap"></div>
            </div>
          </div>
        </div>

        <!-- SUBNAV Anfang -->
        <!--<div class=" col-sm-2 col-xs-offset-1 col-lg-offset-0 hidden-xs sidebar">
              <ul class="nav nav-stacked fixed" id="subnav" data-spy="affix" >
                <li><a href="#title0"> Um des Essens Willen</a>
                    <ul class="nav nav-stacked" >
                      <li><a href="#GroupASub1">Leckereien</a></li>
                      <li><a href="#GroupASub2">und anderes</a></li>
                  </ul>
                </li>
              </ul>
        </div>-->
        <!-- SUBNAV Ende -->

      </div>
    </main>
    <!-- CONTENT Ende -->

    <?php 
    include('footer.php');
     ?>
  </body>
</html>
