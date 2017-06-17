var sequence = 0;

window.setInterval(function(){
	getMessages();
}, 1000);

function onEnter(event) {
  if(event.which==13){
    sendMessage();
  }
}

function notifyMe() {
	console.log("play");
	var snd = new Audio("TING.WAV");
	snd.play();
}


function messagePusher(neu){
  if(neu.length > 0){
    $("#chatBox").append(formatMessage(getUser(), neu, "nachrichtEigene"));
    document.getElementById("newMessage").value = "";
		document.getElementById('chatBox').scrollTop = 10000000;
  }
}

function getMessages() {
	tokenValid();
	var URL = "http://" + chatUrl + "/messages/" + getUser();
	$.ajax({
		headers: {
			"Authorization": getToken()
		},
		url: URL,
		type: 'GET',
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		success: function (responseJSON) {
			showMessages(responseJSON);
		},
		error: function (xhr, a, b) {
			console.log("getMessages von " + getUser() + " fehlgeschlagen");
		}
	});
}

function formatMessage(name, text, id, date) {
  if (!date){
    date = new Date().toString();
  }
	text = $( $.parseHTML(text) ).text();
  return "<div class=\"col-xs-12\" id=\"" + id + "\"><p id=\"messageName\">" + name + "</p><hr><p> " + text + "</p><p id=\"uhrZ\">" + formatDate(date) + "</p></div>"
}

function showMessages(responseJSON) {
	var neu;
	var newDownload = false;
	console.log(sequence);
	for (var i = 0; i < responseJSON.length; i++) {
		if (responseJSON[i].sequence > sequence) {
			if(responseJSON[i].from == getUser()){
					messagePusher(responseJSON[i].text);
			}
			if(groupSelected){
				if(responseJSON[i].to == $("#Chatkontakt").text() && responseJSON[i].from != getUser()){
					newDownload = true;
					neu = responseJSON[i].text;
					$("#chatBox").append(formatMessage(responseJSON[i].from, neu, "nachrichtContact", responseJSON.date));
					document.getElementById('chatBox').scrollTop = 10000000;
				}
			} else {
				if(responseJSON[i].from == $("#Chatkontakt").text()){
						newDownload = true;
						neu = responseJSON[i].text;
						$("#chatBox").append(formatMessage(responseJSON[i].from, neu, "nachrichtContact", responseJSON.date));
						document.getElementById('chatBox').scrollTop = 10000000;
				}
			}
			sequence = responseJSON[i].sequence;
		}
	}
	if (newDownload) {
			notifyMe();
	}
}

function formatDate(date){
  var time = new Date(date);
  if (time.getMinutes() < 10){
    return time.getHours() + ":0"  + time.getMinutes();
  }
  return time.getHours() + ":"  + time.getMinutes();
}

$(function(){
	$("sendtwo").click(function(){
		tryLogin();
		var neu = $("#newMessage").val();
		$("#chatBox").append(neu);
	});
});

function sendMessage(){
	tokenValid();
	var text = $("#newMessage").val(); //id (newMessage)
	if(text.lenth == 0){
		return 0;
	}
	if(!groupSelected){
		messagePusher(text);
	}
	var myJSON = {
		"token": getToken(),
		"from": getUser(),
		"date":"2017-06-15T12:16:30+0200",
		"to": $("#Chatkontakt").text(),
		"text": text,
		"group":groupSelected
	};
	console.log(myJSON);
	$.ajax({
		url: "http://" + chatUrl + "/send",
		type: "PUT",
		contentType: "application/json; charset=utf-8",
		dataType:"json",
		data: JSON.stringify(myJSON),
		succes : function(response){
		},
		error : function(xhr,status,error){
			console.log(status);
		}
	});
}
