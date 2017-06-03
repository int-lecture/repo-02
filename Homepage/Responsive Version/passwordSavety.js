$( document ).ready(function() {

});

function onEnter(event) {
  if(event.which==13){
    setLoginCookie();
  }
}

function checkPw(){
	var ip = "141.19.142.57";
	var pw1 = $("#Passwort1").val();
	var pw2 = $("#Passwort2").val();
 if (pw1 != pw2){
		$("#passwAllert").html("Passwörter sind nicht Identisch!");
		return false;
	}
	if (pw1.length < 4){
		$("#passwAllert").html("Dein Passwort ist nicht lang genug.");
		return false;
	}
	if (!$('#agb').prop('checked')){
		$("#passwAllert").html("Um den Service nutzen zu können musst du die AGBs aktzeptieren.");
		return false;
	}
	var URL = "http://" + ip + ":5002/register/";
	$.ajax({
		url: URL,
		type: 'PUT',
		data: JSON.stringify({'pseudonym': $("#pseudonym").val(), 'user': $("#email").val(), 'password': pw1 }),
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		success: function(result) {
			alert("Registrierung erfolgreich! Melde dich jetzt an");
			window.location.href = "login.php";
		},
		error: function(xhr, ajaxOptions, thrownError){
			console.log("fehler");
			$("#passwAllert").html("Sorry but your password must contain an uppercase letter, a number, a haiku, a gang sign, a hieroglyph and the blood of a virgin. Or your name is taken");
		}
	});
	return false;
}
