<html>
<head>
  <title>Login</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="formate.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script src="loginJS.js"></script>
</head>
<body onkeypress="onEnter(event)">
  <?php
  include 'navbar.html';
  ?>

  <div class="jumbotron text-center" style="background-color:#C7E2F4">
    <h1 style="color:#605340">Login</h1>
    <p style="color:#605340">Bist du schon drin?</p>
  </div>

  <form class="form-signin" onsubmit='return setLoginCookie();'>

    <div class="col-lg-4 col-sm-3 col-xs-1">
    </div>

    <div class="col-lg-4 col-m-5 col-sm-6 col-xs-10">
      <div class="input-group">
        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
        <input id="user" type="email" class="form-control" placeholder="Email" required>
      </div>
      <div class="input-group">
        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
        <input id="password" type="password" class="form-control" placeholder="Password" required>
      </div>
      <input type="submit" class="btn btn-info" value="Einloggen"></input>
      <br>
        <a href="register.php">noch nicht angemeldet?</a>
    </div>
  </form>
</body>
</html>
