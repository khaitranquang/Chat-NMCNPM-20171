<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<%
	if(session.getAttribute("login")!=null){
			response.sendRedirect("/BTLCNPM/pages/room.jsp");
			
	}
 %>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Demo Chat</title>
	<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
	
	<script src="http://localhost:8080/BTLCNPM/js/jquery-3.2.1.min.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="http://localhost:8080/BTLCNPM/css/styleLogin.css">
	<script src="http://localhost:8080/BTLCNPM/js/loginEvent.js" type="text/javascript"></script>
</head>

<body>
	<!-- Login Area -->
	<div id="loginArea">
		<div id="title">
			<h1>Chat NMCNPM - 20171</h1>
			<h3>What's your account?</h3>
		</div>

		<div id="login">
			<!-- Change mode -->
			<div class="on">
				<a id="onLogin" >Login</a>
				<a id="onSignUp">Sign Up </a>
			</div>

			<!-- Show status -->
			<div id="status">
				<p>The password is incorrect</p>
			</div>

			<!-- Login form -->
			<form action="login" method="post" name="form-login" id="form-login">
				<span class="fontawesome-user"></span>
				<input type="text" id="user" placeholder="Username" name="username">

				<span class="fontawesome-lock"></span>
				<input type="password" id="pass" placeholder="Password" name="password">

				<input type="submit" value="LOGIN" id="btnLogin" >
			</form>
			
			<!-- Sign Up form -->
			<form action="register" method="post" name="form-signup" id="form-signup">
				<span class="fontawesome-user"></span>
				<input type="text" id="register-fullname" placeholder="Type your fullname" name="fullname">

				<span class="fontawesome-user"></span>
				<input type="text" id="register-age" placeholder="Type your age" name="age">	

				<span class="fontawesome-user"></span>
				<input type="text" id="register-user" placeholder="Type your account" name="username">

				<span class="fontawesome-lock"></span>
				<input type="password" id="register-pass" placeholder="Enter password" name="password">

				<span class="fontawesome-lock"></span>
				<input type="password" id="register-re-pass" placeholder="Re-enter Password">

				<input type="submit" value="Sign Up" id="btnSignin">
			</form>
		</div>
	</div>
</body>
</html>