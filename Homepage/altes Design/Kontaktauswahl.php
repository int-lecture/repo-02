<?php
	echo "<div class=\"menü\" id=\"myTopnav\">
			<a href=\"index.html\">Home</a>
			</div>";	
	if(isset($_COOKIE["benutzer"])){
		$name = $_COOKIE["benutzer"];
		} else {
		$name = $_POST["benutzername"];	
		setcookie("benutzer",$name);
		}
	echo "<h1> Willkommen " . $name . " zur Kontaktauswahl </h1>";
	echo "<h2> Hier ihre Chat Partner: </h2>";
	echo "<!DOCTYPE HTML>
			<meta charset=\"UTF-8\">
			<html>
				<head>
				<link rel=\"stylesheet\" type=\"text/css\" href=\"formate.css\">
				<title>ChiiHut-Messenger</title>
					<body>
					<a class=\"logo\" href=\"index.html\">
					<img src=\"logo.jpg\" alt=\"Fehler\" width=\"10%\">
					</a>";
	echo 			"<form><select name=\"empfaenger\">";
	echo 			file_get_contents("user.txt");
	echo 			"</select>
					<button type=\"submit\" formmethod=\"post\" formaction=\"messenger.php\">Senden</button><br /></form>
						<a href=\"wechseln.php\">Benutzer wechseln</a>
					</body>
				</head>
			</html>";



?>