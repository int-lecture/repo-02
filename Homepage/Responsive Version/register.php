<html lang="de">
<head>
  <title>Registrierung</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="formate.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script src="passwordSavety.js"></script>
</head>
<body>
  <?php
  include 'navbar.html';
  ?>
  <div class="jumbotron text-center" style="background-color:#C7E2F4">
    <h1 style="color:#605340">Registrierung</h1>
    <p style="color:#605340">Registriere dich und erlebe den einzigartigen Secure-Messenger!</p>
  </div>
  <div class="form-group row" onsubmit='return checkPw();'>

    <div class="col-lg-3 col-xs-1">
    </div>

    <div class="col-lg-6 col-xs-10">
      <form class="form-signin" onsubmit='return checkPw();'>
        <div class="input-group">
          <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
          <input id="pseudonym" type="text" class="form-control" name="pseudonym" placeholder="pseudonym" required>
        </div>

        <div class="input-group">
          <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
          <input id="email" type="email" class="form-control" name="email" placeholder="Email" required>
        </div>

        <p>
          <div class="col-lg-6">
            <input id="Passwort1" type="password" class="form-control" name="Passwort1" placeholder="Passwort" required>
          </div>

          <div class="col-lg-6">
            <input id="Passwort2" type="password" class="form-control" name="Passwort2" placeholder="Passwort wiederhohlen" required>
          </div>
        </p>
        <hr>
        <hr>
        <input type="submit" class="btn btn-info" value="Loslegen!">
        <input type="checkbox" name="agb">Ich stimme den <a href="AGB.html">AGBs</a> zu.</input>
        <p id="passwAllert"></p>
        <br>
      </from>
    </div>
  </div>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <img src="reaction.jpg" class="img-responsive center-block" />
</body>
</html>
