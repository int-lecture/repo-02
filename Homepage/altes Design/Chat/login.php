<?php
$email_eingabe = $_POST["benutzername "];
#$passwort_eingabe = $POST[" "];

$file = "registrierung.txt";

$file_handle = fopen($file, "r");

while (!foef($file_handle)) {
 $line = fgets($file_handle);
 $datensatz = explode(" ", $line);
 $email = datensatz[5];
 $passwort = datensatz[7];
echo "
<html charset="UTF-8">
<head><title>Öffne Messenger</title></head>
<body>
<a href = "messenger.php"></a>
</body>
</html>";


header("Location: messenger.php");

if ($email == $email_eingabe) {
 fclose($file_handle);
 header("Location: messenger.php");
}

else {
 fclose($file_handle);
 header("Location: login.html");
}
}
exit;
?>
