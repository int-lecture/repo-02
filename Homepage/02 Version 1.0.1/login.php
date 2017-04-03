<!DOCTYPE html>
<html>
  <body>
    test
</body>
</html>

<?php
$myfile = fopen("users.txt", "r") or die("Unable to open file!");
echo fgets($myfile);
fclose($myfile);
?>
