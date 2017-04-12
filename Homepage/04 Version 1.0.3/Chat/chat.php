<?php 
	$from = $_COOKIE["benutzer"];
	$to = $_POST["empfaenger"];
	$message = $_POST["nachricht"];
	echo "Nachricht von " . $from . " an " . $to . "-->  " . $message . " wurde versandt.";
	$datei = "chats/" . $to . "/" . "nachrichten.txt";
	file_put_contents ($datei, $from . " schrieb um " . date("h:i:sa") . ": " . $message . "<hr>\r\n",FILE_APPEND);
	fclose($datei);
	echo "<br /><a href=\"messenger.php\">Rückkehr zum Messenger, falls automatische Umleitung nach 3 Sekunden nicht funktioniert hat</a>";
	header("location: messenger.php");
?>
