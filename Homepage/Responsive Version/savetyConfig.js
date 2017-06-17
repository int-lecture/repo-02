var chatUrl = setSettings("","chat");
var loginUrl = setSettings("","login");
var registerUrl = setSettings("","register");

function tryLogin() {
	tokenValid();
	var URL = "http://" + loginUrl + "/auth";
	var dataObject = {'pseudonym': getUser(), 'token': getToken()};
	$.ajax({
		url: URL,
		type: 'POST',
		data: JSON.stringify(dataObject),
		contentType: "application/json; charset=utf-8",
		error: function(xhr, ajaxOptions, thrownError){
			window.location.href = "login.php";
		}
	});
}

function tokenValid(){
	if(!getToken()){
		window.location.href = "login.php";
	}
}

function getToken(){
	var token = readCookie("token");
	if(!token){
		alert("bitte melde dich an");
		window.location.href = "login.php";
	}
	return token;
}

function getUser(){
	var user = readCookie("pseudonym");
	if(!user){
		alert("bitte melde dich an");
		window.location.href = "login.php";
	}
	return user;
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function setSettings(url, type){
	if(!url.includes(":")){
		switch (type) {
			case "chat":
			return "141.19.142.56:5000"
			case "login":
			return "141.19.142.56:5001"
			case "register":
			return "141.19.142.56:5002"
			default:
			return "localhost"
		}
	} else {
		return url;
	}
}

function loadSettings(){
	chatUrl = document.getElementById("chatUrl").value;
	loginUrl = document.getElementById("loginUrl").value;
	registerUrl = document.getElementById("registerUrl").value;
	$('#myModal').modal('hide');
	chatUrl = setSettings(chatUrl,"chat");
	loginUrl = setSettings(loginUrl,"login");
	registerUrl = setSettings(registerUrl,"register");
	console.log(chatUrl);
	console.log(loginUrl);
	console.log(registerUrl);
	document.getElementById("chatUrl").value = "";
	document.getElementById("loginUrl").value = "";
	document.getElementById("registerUrl").value = "";
}
