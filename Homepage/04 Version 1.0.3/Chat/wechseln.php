<?php
	if(isset($_COOKIE["benutzer"])) {
		unset($_COOKIE["benutzer"]);
		setcookie("benutzer","",time() - 3600, "/");
	} 
	header("Location: login.html");
	exit;
?>
