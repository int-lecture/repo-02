

function makeContactButton() {
    var URL = "http://141.19.142.56:5002/profile";
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
		alert("Problem bei der Abfrage nach den Kontakten.");
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
							console.log(contacts[i]);
	}
}
