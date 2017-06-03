
<?php
echo "<div class=\"menü\" id=\"myTopnav\">
	<a href=\"index.html\">Home</a>
	</div>";

$name = $_POST["benutzername"];

if(isset($_COOKIE["benutzer"])){
 	$name = $_COOKIE["benutzer"];
} else {
	setcookie("benutzer",$name);
}
echo "<h1>" . $name . ", hier sind deine Nachrichten: </h1>";

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
	echo "<hr>". file_get_contents("chats/" . $name . "/nachrichten.txt");
	echo "<h2>Wähle dir ein Girl aus und schreibe eine Nachricht:</h2>";
	echo "<form action=\"action.php\" method=\"post\">
	<select name=\"empfaenger\">";
	echo file_get_contents("user.txt");
	echo "</select>
	<input type=\"text\" name=\"nachricht\" />
	<button type=\"submit\" formmethod=\"post\" formaction=\"chat.php\">Senden</button>
	</form>
	<a href=\"wechseln.php\">Benutzer wechseln</a>
</body>
</head>
</html>";
?>
