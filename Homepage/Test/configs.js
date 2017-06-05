//Kontainer um die user_id zu speichern.
var user_id;
var chatUrl = setSettings("","chat");
var loginUrl = setSettings("","login");
var registerUrl = setSettings("","register");
function chatWindowOpener() {

	var chatWindow = document.getElementById("equal");
	var chatString = "<div class=\"col-md-3 equal\" id=\"equal\">" +
						"<div class=\"col-md-12\" id=\"Chatkontakt\">" +
							"<img src=\"benutzer.png\" id=\"kontaktbild\" class=\"img-rounded\">" +
								"Beispielkontakt" +
						"</div>" +
							"<div id=\"chatBox\" style=\"height:590px;width:320px;border:1px solid #ccc;font:16px/26px Georgia, Garamond, Serif;overflow-y:auto; overflow-x:hidden;\">" +
							"</div>" +
								"<div class=\"input-group\" id=\"send\">" +
									"<input type=\"text\" class=\"form-control\" id=\"newMessage\" placeholder=\"...\" >" +
										"<span class=\"input-group-btn\">" +
											"<button class=\"btn btn-default\" id=\"sendtwo\" onclick=addElement()>" +
												"<span class=\"glyphicon glyphicon-send\">" + "</span>" +
													"Senden" +
											"</button>" +
										"</span>" +
								"</div>" +
					"</div>"
	chatWindow.insertAdjacentHTML("beforeend",chatString);
}

function loadChatValues(){

	var chatRequest = new XMLHttpRequest();

	//Testweise wird als SequenceNumber die 0 übergeben damits überhaupt tut.
	var messageServerAdress = "141.19.142.56/messages/" + user_id + "/" + "/" + "0";
	var messageString;

	chatRequest.open(messageServerAdress);
	chatRequest.onload() = function () {

		//Bin nicht sicher ob ichs überhaupt parsen muss.
		var messagesAsJSON = JSON.parse(chatRequest.responseTest);
	}
}

function sendMessage(){
		var text = $("newMessage").val(); //id (newMessage)
		
		var myJSON = {
			"token": "",
			"to": $("#pn").val(),
			"from": $("#email").val(),
			"text": text,
			"sequence": ""
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
    var URL = chatUrl + "/messages/" + pseudonym + "/";
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
                if (typeof (sequenceNumbers[pseudonym]) == 'undefined') {
                    messages = result;
                } else {

                    $.each(result, function (index, value) {
                        messages = messages.concat(value);
                    });
                    showMessages();
                }
            }
        },
        error: function (xhr, a, b) {
            //alert(" error");
            alert("getMessages von " + pseudonym + " fehlgeschlagen");
        }
    });

}

function loadContacts(user){

	var contactList = new XMLHttpRequest();
	var profile = contactList("GET", "http://141.19.142.56:5002/profile");

	for(var i = 0; i < profile.contacts.length; i++){
		var contactWindow = document.getElementById("kontakte");
		var contactString = "<button type=\"button\" class=\"btn btn-default\" id=\"user	\" onclick=chatWindowOpener()>" +
							"<span class=\"glyphicon glyphicon-user\"></span>" +
							"profile.contacts[i];" +
							"</button>";
		contactWindow.insertAdjacentHTML("beforeend",contactString);
	}
}

function getToken(){
		var date = new Date();
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
	
