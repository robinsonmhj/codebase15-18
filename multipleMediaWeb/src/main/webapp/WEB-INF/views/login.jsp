<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html >
  <head>
    <meta charset="UTF-8">
    <title>Login</title>
    <c:url value="/resources/css/login.css"	var="loginCss" />
	<link href="${loginCss}" rel="stylesheet" />
    <c:url var="home" value="/" scope="request" />
  </head>

  <body>


    <div class="wrapper">
	<div class="container">
		<h1>Welcome</h1>
		<form class="form">
			<div>
			<input type="text" id="username" placeholder="Username" > 
			<span class="error">Please input your user name</span>
			</div>
			<div>
			<input type="password" id="password" placeholder="Password" > 
			<span class="error">password must be at least 6 characters long</span>
			</div>
			<input type="text" id="kaptcha" placeholder="kaptcha"> 
			<img class="kaptcha" id="img_kaptcha" src="kaptcha"/>  
			<span class="error">Please input kaptcha</span>
			<div id="errors" class="error_show"></div>
			<button type="submit" id="login-button">Login</button>
		</form>
	</div>
	
	<div class="warning">
	
	<ul class="bg-bubbles">
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
	</ul>
</div>
    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

      <script src="<c:url value="/resources/js/login.js" />"></script>

    
    
    
  </body>
</html>
