function chatFenster(i){
	messageList = [];
	sequence = 0;
	groupSelected = false;
	var contact = $("#user" + i).text();
	document.getElementById("Chatkontakt").innerHTML= contact;
	$("#chatBox").html("");
	window.history.pushState("", "", 'Chatfenster.html?c=' + contact + '&g=' + groupSelected);
}

function openChat(contact, group) {
	document.getElementById("Chatkontakt").innerHTML= contact;
	$("#chatBox").html("");
	groupSelected = group;
	console.log(contact + ", " + group);
}

function makeContactButton() {
  var URL = "http://" + registerUrl + "/profile";
    var requestJSON = {
        "token" : getToken(),
        "getownprofile" : getUser()
    };
    $.ajax({
		url: URL,
		type: 'POST',
		data: JSON.stringify(requestJSON),
		contentType: "application/json; charset=utf-8",
		success: function (responseJSON) {
			loadContacts(responseJSON.contacts);
		},
		error: function (xhr, ajaxOptions, thrownError) {
		console.log("Problem bei der Abfrage nach den Kontakten.");
	}
});
}

function loadContacts(json) {
	var contacts = json;
	var contactsSize = contacts.length;
	var htmlString;
	var contactButton = document.getElementById("user");
	for (var i = 0; i < contactsSize; i++) {
	htmlString = "<button type=\"button\" class=\"btn btn-default\" id=\"user" + i + "\" onclick=\"chatFenster(" + i + ")\">" +
							"<span class=\"glyphicon glyphicon-user\">" +
							"</span>" + contacts[i] + "</button>";
							var d1 = document.getElementById('userBox');
							d1.insertAdjacentHTML("beforeend", htmlString);
	}
}

function addContacts(){
	tokenValid();
	var newContact = document.getElementById("contactName").value;
	$('#contactsModal').modal('hide');
	console.log(newContact);
	document.getElementById("contactName").value = "";
	var URL = "http://" + registerUrl + "/contact/";
	$.ajax({
		url: URL,
		type: 'POST',
		data: JSON.stringify({'pseudonym': getUser(), 'token': getToken(), 'contact': newContact, 'group': false}),
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		success: function (result, textStatus, xhr) {
			if (xhr.status == 201) {
				window.location.href = "Chatfenster.html";
			}
		},
		error: function (xhr, a, b) {
			alert("Deine Mama wurde nicht gefunden... Bitte melde dich im Småland" + xhr.status);
		}
	});
}
