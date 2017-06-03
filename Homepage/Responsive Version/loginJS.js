$( document ).ready(function() {

});


function onEnter(event) {
  if(event.which==13){
    setLoginCookie();
  }
}

function setLoginCookie(){
  var ip="141.19.142.57";
  var URL = "http://" + ip + ":5001/login/";
  var dataObject = {'user': $("#email").val(), 'password': $("#password").val()};
	$.ajax({
		url: URL,
		type: 'POST',
		data: JSON.stringify(dataObject),
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		success: function(result) {
      document.cookie = "token=" + result.token;
      document.cookie="pseudonym=" + result.pseudonym + ";expires=" + result["expire-date"];
      alert("Du wirst eingeloggt");
			window.location.href = "ChatFenster.html";

    },
		error: function(xhr, ajaxOptions, thrownError){
      alert("Mit der Anmeldung ist etwas schief gelaufen... Überprüfe deine Daten und versuch´s noch mal!");
		}
	});

	return false;
}
