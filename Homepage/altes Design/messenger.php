<?php
	echo "<div class=\"menü\" id=\"myTopnav\">
			<a href=\"index.html\">Home</a>
			</div>";
	
	$from = $_COOKIE["benutzer"];
	if(isset($_COOKIE["benutzer"])){
		$from = $_COOKIE["benutzer"];
		} else {
		setcookie("benutzer",$from);
		} 	
	if(isset($_COOKIE["empfaenger"])){
		$to = $_COOKIE["empfaenger"];
		} else {
		$to = $_POST["empfaenger"];
		setcookie("empfaenger",$to);
		}
	echo "<h1> Willkommen " . $from . " du chattest mit " . $to . ": </h1>";
	echo "<h2> Hier siehst du euren Chatverlauf: </h2>";
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
					$pfad = "chats/" . $from . "/" . $to;
					if(file_exists($pfad)){
					echo "<hr>". file_get_contents("chats/" . $from . "/" . $to . "/chat.txt");	
					}else{
					echo "noch kein Chatverlauf verfügbar";
					}				
	echo 			"<form action=\"action.php\" method=\"post\">
					<input type=\"text\" name=\"nachricht\" />
					<button type=\"submit\" formmethod=\"post\" formaction=\"chat.php\">Senden</button>
					</form>
					<a href=\"wechseln.php\">Benutzer wechseln</a><br />
					<a href=\"Benutzerwechseln.php\">Zur Kontaktauswahl</a>
					</body>
				</head>
				<html>";

?>