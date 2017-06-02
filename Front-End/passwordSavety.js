$( document ).ready(function() {


});

function checkPw(){
	var ip="141.19.142.57";
	var a= $("#Passwort1");
	var pw1= $("#Passwort1").val();
	var pw2= $("#Passwort2").val();

	if (pw1 != pw2){
		$("#passwAllert").html("Passwörter sind nicht Identisch!");
		return false;
	}
	if (pw1.length < 10){
		$("#passwAllert").html("Dein Passwort ist nicht lang genug.");
		return false;
	}
	var URL = "http://" + ip + ":5002/register/";
	console.log(URL);
	console.log(JSON.stringify({'pseudonym': $("#pseudonym").val(), 'user': $("#email").val(), 'password': pw1 }));
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
		}
	});
	$("#passwAllert").html("Sorry but your password must contain an uppercase letter, a number, a haiku, a gang sign, a hieroglyph and the blood of a virgin.\n\n\nOr your name is taken");
	return false;
}
