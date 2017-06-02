function chatWindowOpener() { 
	var chatWindow = document.getElementById("equal");
	var chatString = "<div class=\"col-md-3 equal\" id=\"equal\">" + "<div class=\"col-md-12\" id=\"Chatkontakt\">" +  
								"<img src=\"benutzer.png\" id=\"image\" class=\"img-rounded\">" +
										"Beispielkontakt" +
					"</div>" + "<div class=\"scrollbar\" id=\"style-1\">" + "<div class=\"force-overflow\">" +
							"</div>" + 	"</div>" +
								"<div class=\"input-group\" id=\"send\">"+
									"<input type=\"text\" class=\"form-control\" placeholder=\"...\" >"+
										"<span class=\"input-group-btn\">"+
											"<button class=\"btn btn-default\">"+
												"<span class=\"glyphicon glyphicon-send\">"+
												"</span> "+
													"Senden"+
											"</button>"+
										"</span>"+
								"</div>"+
				"</div>";
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
	var profile = contactList("GET", "http://141.19.142.56/profile");
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