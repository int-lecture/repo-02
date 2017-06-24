function logout() {
	var token = "token";
	var pseudonym = "pseudonym";
	document.cookie = token + "=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
	document.cookie = pseudonym + "=;expires=Thu, 01 Jan 1970 00:00:01 GMT;";
	window.location.href = "login.php";
}