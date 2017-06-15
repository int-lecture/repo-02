//Kontainer um die user_id zu speichern.
var user_id;
var chatUrl = setSettings("","chat");
var loginUrl = setSettings("","login");
var registerUrl = setSettings("","register");
// function chatWindowOpener() {

	// var chatWindow = document.getElementById("equal");
	// var chatString = "<div class=\"col-md-3 equal\" id=\"equal\">" +
						// "<div class=\"col-md-12\" id=\"Chatkontakt\">" +
							// "<img src=\"benutzer.png\" id=\"kontaktbild\" class=\"img-rounded\">" +
								// "Beispielkontakt" +
						// "</div>" +
							// "<div id=\"chatBox\" style=\"height:590px;width:320px;border:1px solid #ccc;font:16px/26px Georgia, Garamond, Serif;overflow-y:auto; overflow-x:hidden;\">" +
							// "</div>" +
								// "<div class=\"input-group\" id=\"send\">" +
									// "<input type=\"text\" class=\"form-control\" id=\"newMessage\" placeholder=\"...\" >" +
										// "<span class=\"input-group-btn\">" +
											// "<button class=\"btn btn-default\" id=\"sendtwo\" onclick=addElement()>" +
												// "<span class=\"glyphicon glyphicon-send\">" + "</span>" +
													// "Senden" +
											// "</button>" +
										// "</span>" +
								// "</div>" +
					// "</div>"
	// chatWindow.insertAdjacentHTML("beforeend",chatString);
// }

function sendMessage(){
		var text = $("newMessage").val(); //id (newMessage)

		var myJSON = {
			"token": getToken(),
			"from": getUser(),
			"to": $("#pn").val(),
			"text": text,
			"sequence": "0"
			};


		$.ajax({
			url: chatUrl,
			type: "POST",
			contentType: "application/json; charset=utf-8",
			dataType:"json",
			succes : function(response){

			},
			error : function(xhr,status,error){
				alert(status);
			}
		});
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

function getMessages() {
    var URL = chatUrl + "/messages/" + getUser();
    $.ajax({
        headers: {
            "Authorization": getToken()
        },
        url: URL,
        type: 'GET',
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (result, textStatus, xhr) {
            if (xhr.status == 200) {
							console.log(result);
                if (typeof (sequenceNumbers[pseudonym]) == 'undefined') {
                    messages = result;
                } else {
                    $.each(result, function (index, value) {
                        messages = messages.concat(value);
                    });
                    showMessages(result);
                }
            }
        },
        error: function (xhr, a, b) {
            alert("getMessages von " + getUser() + " fehlgeschlagen");
        }
    });
}

function getToken(){
		console.log(readCookie("token"));
		var token = readCookie("token");
		if(!token){
			alert("bitte melde dich an");
			window.location.href = "login.php";
		}
		return token;
}

function addContacts(){
	var newContact = document.getElementById("contactName").value;
	$('#contactsModal').modal('hide');
	console.log(newContact);
	document.getElementById("contactName").value = "";
	var URL = registerUrl + "/contact/";
	console.log(JSON.stringify({'pseudonym': getUser(), 'token': getToken(), 'contact': newContact}));
	$.ajax({
			url: URL,
			type: 'PUT',
			data: JSON.stringify({'pseudonym': getUser(), 'token': getToken(), 'contact': newContact}),
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			success: function (result, textStatus, xhr) {
					if (xhr.status == 201) {
						console.log(result);
						//window.location.href = "ChatFenster.php";
					}
			},
			error: function (xhr, a, b) {
					alert("Deine Mama wurde nicht gefunden... Bitte melde dich im Småland");
			}
	});
}

function getUser(){
		console.log(readCookie("pseudonym"));
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

function messagePusher(){

	var old = $("#chatBox").val();
	var neu = $("#newMessage").val();
	$("#chatBox").html(old + neu);

}

$(function(){
	$("sendtwo").click(function(){
	var neu = $("#newMessage").val();
	$("#chatBox").append(neu);
	});

});

function addElement() {
    var scrollbox = document.getElementById('chatBox');

    // Create some element, e.g. div
    var newElement = document.createElement('div');
    newElement.innerHTML = $("#newMessage").val();
	$("#newMessage").value = "";
    scrollbox.append(newElement);
}


/* Dropdownmenü: alles was mit dem Dropdown zu tun hat*/
/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}

