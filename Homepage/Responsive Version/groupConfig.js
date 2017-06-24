var groupSelected = false;
var emptyContact

window.onload = function() {
  emptyContact = $("#Chatkontakt").text;
  showGroups();
  makeContactButton();
  if (getUrl("c")) {
    openChat(getUrl("c"), getUrl("g"));
  }
  if (typeof history.pushState === "function") {
     history.pushState($("#Chatkontakt").text, null, null);
     window.onpopstate = function () {
         if(getUrl("c")){
           openChat(getUrl("c"), getUrl("g"));
         }
     };
}
};

window.onhashchange = function() {
  if(getUrl("c")){
    openChat(getUrl("c"), getUrl("g"));
  }
}


function groupFenster(i){
  sequence = 0;
  groupSelected = true;
  var contact = $("#group" + i).text();
  document.getElementById("Chatkontakt").innerHTML= contact;
  $("#chatBox").html("");
  window.history.pushState("", "", 'Chatfenster.html?c=' + contact + '&g=' + groupSelected);
}

function showGroups() {
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
      formatGroups(responseJSON);
    },
    error: function (xhr, ajaxOptions, thrownError) {
      console.log("Problem bei der Abfrage nach den Kontakten.");
    }
  });
}

function formatGroups(json) {
  console.log(json);
  var groupSize = json.groups.length;
  var htmlString;
  for (var i = 0; i < groupSize; i++) {
    htmlString = "<button type=\"button\" class=\"btn btn-default\" id=\"group" + i + "\" onclick=\"groupFenster(" + i + ")\">" +
    "<span class=\"glyphicon glyphicon-user\">" +
    "</span>" + json.groups[i] + "</button>";
    var g1 = document.getElementById('gruppenBox');
    g1.insertAdjacentHTML("beforeend", htmlString);
    console.log(json.groups[i]);
  }
}

function addGroup(){
  console.log("group");
  tokenValid();
  var newGoup = document.getElementById("groupName").value;
  $('#groupModal').modal('hide');
  console.log(newGoup);
  document.getElementById("groupName").value = "";
  var URL = "http://" + registerUrl + "/contact/";
  console.log({'pseudonym': getUser(), 'token': getToken(), 'contact': newGoup, 'group':true});
  $.ajax({
    url: URL,
    type: 'POST',
    data: JSON.stringify({'pseudonym': getUser(), 'token': getToken(), 'contact': newGoup, 'group':true}),
    contentType: "application/json; charset=utf-8",
    dataType: 'json',
    success: function (result, textStatus, xhr) {
      if (xhr.status == 201) {
        window.location.href = "Chatfenster.html";
      }
    },
    error: function (xhr, a, b) {
      console.log("Fehler beim hinzufügen der Gruppe " + xhr.status);
    }
  });
}
