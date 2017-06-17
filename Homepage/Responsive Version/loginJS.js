$(document).ready(function() {

});


function onEnter(event) {
  if(event.which==13){
    setLoginCookie();
  }
}

function getUrl(key) {
  var url = location.href;
  key = key.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+key+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( url );
  return results == null ? null : results[1];
}

function setLoginCookie(){
  var ip = getUrl("ip");
  var port = getUrl("port");
  if(ip == null || port==null){
    ip = "141.19.142.56";
    port = 5001;
  }
  var URL = "http://" + ip + ":" + port + "/login";
  var dataObject = {'user': $("#user").val(), 'password': $("#password").val()};
  $.ajax({
    url: URL,
    type: 'POST',
    data: JSON.stringify(dataObject),
    contentType: "application/json; charset=utf-8",
    success: function(response) {
      var exdate=new Date(response['expire-date']);
      document.cookie = "token=" + response.token + "; expires="
      + exdate.toUTCString();
      document.cookie = "pseudonym=" + response.pseudonym + "; expires="+exdate.toUTCString();
      window.location.href = "Chatfenster.html";
    },
    error: function(xhr, ajaxOptions, thrownError){
      alert("Mit der Anmeldung ist etwas schief gelaufen... Überprüfe deine Daten und versuch´s noch mal!");
    }
  });
  return false;
}
