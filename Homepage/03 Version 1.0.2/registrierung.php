<?php
	$nachname = $_POST['nachname'];
	$vorname = $_POST['vorname'];
	$email = $_POST['email'];
	$pw = $_POST['passwort'];
	$check = $_POST['check'];
	if ($check) {
		$datei = "registrierung.txt";
		file_put_contents($datei,"Nachname: " . $nachname . "; Vorname: " .
		 $vorname . "; Email: " . $email . "; Passwort: " . $pw . ";\r\n", FILE_APPEND);
		fclose($datei);
		$empfaenger = $email;
		$betreff = "Die Registrierungsbestätigung";
		$from = "From: ChiiHut-Team";
		$text = "Ihre Registrierung war erfolgreich. Vielen Dank für ihre Daten.";
		mail($empfaenger, $betreff, $text, $from);
		$link = "http://jonathan.sv.hs-mannheim.de/~p.oesterlin/chiiHut/index.html";
		echo "<html>
		<head><title>Anmeldung Erfolgreich</title></head>
		<body>
		Ihre Anmeldung war Erfolgreich! <br />
		Es wurde eine Bestätigungs-Email versendet. <br />
		Ihre Daten wurden erfolgreich gespeichert! <br />
		<a href=$link>Zur Hauptseite</a>
		</body>
		</html>";
	} else {
		$link = "http://jonathan.sv.hs-mannheim.de/~p.oesterlin/chiiHut/login.html";
		echo"<html>
		<head><title>Anmeldung NICHT Erfolgreich</title></head>
		<body>
		Ihre Anmeldung war nicht Erfolgreich! <br />
		Ihre Daten wurden nicht gespeichert. <br />
		<a href=$link>nochmal probieren</a>
		</body>
		</html>";
	}
?>
