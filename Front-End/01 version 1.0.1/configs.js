function chatWindowOpener() { 
	var chatWindow = document.getElementById("equal");
	var chatString = "<div class=\"col-md-3 equal\" id=\"equal\">" + 
						"<div class=\"col-md-12\" id=\"Chatkontakt\">" +  
							"<img src=\"benutzer.png\" id=\"image\" class=\"img-rounded\">" +  
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
	
}

function loadSettings(){

}

function setSettings(){
	
}

function loadContacts(user){
	var contactList = new XMLHttpRequest();
	var profile = contactList("GET", "http://141.19.142.56:5002/profile", "http://141.19.142.56:5001/login");
	for(var i = 0; i < profile.contacts.length; i++){
		var contactWindow = document.getElementById("kontakte");
		var contactString = "<button type=\"button\" class=\"btn btn-default\" id=\"user\" onclick=chatWindowOpener()>" +
							"<span class=\"glyphicon glyphicon-user\"></span>" +
							"profile.contacts[i];" +
							"</button>";
		contactWindow.insertAdjacentHTML("beforeend",contactString);
	}
}

function getToken(){
	
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