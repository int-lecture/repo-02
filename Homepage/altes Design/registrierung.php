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
		mkdir("chats/".$vorname  .  $nachname,0777);
		chmod("chats/".$vorname  .  $nachname,0777);
		$datei = "user.txt";
		file_put_contents($datei,"<option value=\"" . $vorname . $nachname . "\">" .  $vorname . " " . $nachname . "</option>\r\n", FILE_APPEND);	
		$empfaenger = $email;
		$betreff = "Ihre Registrierungsbestätigung";
		$from = "From: ChiiHut-Team";
		$text = "Ihre Registrierung war erfolgreich. Vielen Dank für ihre Daten.";
		mail($empfaenger, $betreff, $text, $from);
		$link = "index.html";
		echo "<html>
		<head><title>Registrierung Erfolgreich</title></head>
		<body>
		Ihre Registrierung war Erfolgreich! <br />
		Es wurde eine Bestätigungs-Email versendet. <br />
		Ihre Daten wurden erfolgreich gespeichert! <br />
		Mit Ihrem Vornamen und Nachnamen können Sie sich jetzt bei Login anmelden! <br />
		<a href=$link>Zur Hauptseite</a> <br />
		<a href=\"login.html\">Link zum Login</a>
		</body>
		</html>";
	} else {
		$link = "registrierung.html";
		echo"<html>
		<head><title>Registrierung NICHT Erfolgreich</title></head>
		<body>
		Ihre Registrierung war nicht Erfolgreich! <br />
		Ihre Daten wurden nicht gespeichert. <br />
		<a href=$link>nochmal probieren</a>
		</body>
		</html>";
	}
?>

