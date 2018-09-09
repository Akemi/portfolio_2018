# THM Timetable
A Live Demo of the parser and a small explanation can be found [here](http://comp.judging.it/test/thmt/). A Live Demo of an example web app that uses the generated json output can be found [here](http://comp.judging.it/test/thmt-test/).

Course and degree data was parsed from [this](http://homepages-fb.thm.de/plaene/stundenplan/HTML.htm) html document. The result is [this json](http://comp.judging.it/test/thmt/thmtdata.php?action=degreelist&type=json) or [xml](http://comp.judging.it/test/thmt/thmtdata.php?action=degreelist&type=xml) file.

All available timetables were parsed from [this](http://homepages-fb.thm.de/plaene/stundenplan/Kla1.htm) html document. The result is [this json](http://comp.judging.it/test/thmt/thmtdata.php?action=timetablelist&type=json) or [xml](http://comp.judging.it/test/thmt/thmtdata.php?action=timetablelist&type=xml) file.

All abbreviation and names for courses were parsed from [this](http://homepages-fb.th-mittelhessen.de/plaene/fachbezeichnung/Faecher_01.pdf) pdf document. The result is [this json](http://comp.judging.it/test/thmt/thmtdata.php?action=leacturelist&type=json) or [xml](http://comp.judging.it/test/thmt/thmtdata.php?action=leacturelist&type=xml) file.

All abbreviation and names for teachers and professors were parsed from [this](http://homepages-fb.th-mittelhessen.de/plaene/fachbezeichnung/Doz_01.pdf) pdf document. The result is [this json](http://comp.judging.it/test/thmt/thmtdata.php?action=teacherlist&type=json) or [xml](http://comp.judging.it/test/thmt/thmtdata.php?action=teacherlist&type=xml) file.

A specific timetable json output (Medieninformatik Master S1) can be found [here](http://comp.judging.it/test/thmt/thmtdata.php?action=timetable&id=MIM1&type=json)

Results are saved on disk and cached so nothing is parsed twice. A small documentation can be found [here](Doku.pdf).