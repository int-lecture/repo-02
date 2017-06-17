var sequence;

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

window.setInterval(function(){
	getMessages();
}, 1000);

function messagePusher(){
	var neu = $("#newMessage").val();
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

function showMessages(responseJSON) {
	var neu;
	var newDownload = false;
	for (var i = 0; i < responseJSON.length; i++) {
		if(responseJSON[i].from == $("#Chatkontakt").text()){
			if (responseJSON[i].sequence > sequence) {
				newDownload = true;
				neu = responseJSON[i].text;
	      $("#chatBox").append(formatMessage($("#Chatkontakt").text(), neu, "nachrichtContact", responseJSON.date));
				sequence = responseJSON[i].sequence;
				document.getElementById('chatBox').scrollTop = 1000;
			}
		}
		if (newDownload) {
			notifyMe();
		}
	}
}

function formatMessage(name, text, id, date) {
  if (!date){
    date = new Date().toString();
  }
	text = $( $.parseHTML(text) ).text();
  return "<div class=\"col-xs-12\" id=\"" + id + "\"><p id=\"messageName\">" + name + "</p><hr><p> " + text + "</p><p id=\"uhrZ\">" + formatDate(date) + "</p></div>"
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
	messagePusher();
	var myJSON = {
		"token": getToken(),
		"from": getUser(),
		"date":"2017-06-15T12:16:30+0200",
		"to": $("#Chatkontakt").text(),
		"text": text,
	};
	$.ajax({
		headers: {
			"Authorization": getToken()
		},
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
