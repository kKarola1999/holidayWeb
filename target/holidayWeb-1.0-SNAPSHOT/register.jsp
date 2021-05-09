  <!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">

    <title>Leaves Managment</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/signin.css" rel="stylesheet">
  </head>

  <body class="text-center">
  <form action="UserrServlet" method="get">
    <input type="hidden" name="command" value="ADDE">
    <form class="form-signin">
      <img class="mb-4" src="img/calendar-event.svg" alt="" width="72" height="72">
      <h1 class="h3 mb-3 font-weight-normal">Please sign up</h1>
      <label for="inputName" class="sr-only">Name</label>
      <input type="name" id="inputName" class="form-control" placeholder="Name" name="name" required autofocus>
      <label for="inputEmail" class="sr-only">Email address</label>
      <input type="email" id="inputEmail" class="form-control" placeholder="Email address" name="email" required autofocus>
      <label for="inputSeniority" class="sr-only">Seniority</label>
      <input type="seniority" id="inputSeniority" class="form-control" placeholder="Seniority" name="seniority"required autofocus>
      <label for="inputJobPosition" class="sr-only">Job Position</label>
      <input type="jobPosition" id="inputJobPosition" class="form-control" placeholder="Job position" name="etat" required autofocus>

      <label for="inputExtraDays" class="sr-only">Extra days</label>
      <input type="extraDays" id="inputExtraDays" class="form-control" placeholder="Extra days" name="extraDays" required autofocus>
      <label for="inputPassword" class="sr-only">Password</label>
      <input type="password" id="inputPassword" class="form-control" placeholder="Password" name="password"required autofocus>


      <button class="btn btn-lg btn-primary btn-block" type="submit" href="index.html">Sign up
      </button>
      <div class="form-group"/>
    </form>

    </form>
  </body>
</html>
