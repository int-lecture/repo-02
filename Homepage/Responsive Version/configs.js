
// function chatWindowOpener() {
// var chatWindow = document.getElementById("equal");
// var chatString = "<div class=\"col-md-3 equal\" id=\"equal\">" +
// "<div class=\"col-md-12\" id=\"Chatkontakt\">" +
// "<img src=\"benutzer.png\" id=\"kontaktbild\" class=\"img-rounded\">" +
// "Beispielkontakt" +
// "</div>" +
// "<div id=\"chatBox\" style=\"height:590px;width:320px;border:1px solid #ccc;font:16px/26px Georgia, Garamond, Serif;overflow-y:auto; overflow-x:hidden;\">" +
// "</div>" +
// "<div class=\"input-group\" id=\"send\">" +
// "<input type=\"text\" class=\"form-control\" id=\"newMessage\" placeholder=\"...\" >" +
// "<span class=\"input-group-btn\">" +
// "<button class=\"btn btn-default\" id=\"sendtwo\" onclick=addElement()>" +
// "<span class=\"glyphicon glyphicon-send\">" + "</span>" +
// "Senden" +
// "</button>" +
// "</span>" +
// "</div>" +
// "</div>"
// chatWindow.insertAdjacentHTML("beforeend",chatString);
// }

// function addElement() {
// 	var scrollbox = document.getElementById('chatBox');
// 	// Create some element, e.g. div
// 	var newElement = document.createElement('div');
// 	newElement.innerHTML = $("#newMessage").val();
// 	$("#newMessage").value = "";
// 	scrollbox.append(newElement);
// }
//
// /* Dropdownmenü: alles was mit dem Dropdown zu tun hat*/
// /* When the user clicks on the button,
// toggle between hiding and showing the dropdown content */
// function myFunction() {
// 	document.getElementById("myDropdown").classList.toggle("show");
// }
//
// // Close the dropdown if the user clicks outside of it
// window.onclick = function(event) {
// 	if (!event.target.matches('.dropbtn')) {
// 		var dropdowns = document.getElementsByClassName("dropdown-content");
// 		var i;
// 		for (i = 0; i < dropdowns.length; i++) {
// 			var openDropdown = dropdowns[i];
// 			if (openDropdown.classList.contains('show')) {
// 				openDropdown.classList.remove('show');
// 			}
// 		}
// 	}
// }
