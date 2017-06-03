<?php
	if(isset($_COOKIE["empfaenger"])){
		unset($_COOKIE["empfaenger"]);
		setcookie("empfaenger","",time() - 3600, "/");
	}
	header("Location: Kontaktauswahl.php");
	exit;
?>
