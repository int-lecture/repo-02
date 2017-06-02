$( document ).ready(function() {


});

function checkPw(){
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
    $("#passwAllert").html("Sorry but your password must contain an uppercase letter, a number, a haiku, a gang sign, a hieroglyph and the blood of a virgin.");
	return false;
}
