<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Facebook</title>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="Js/LogReg.js" type="text/javascript" ></script>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="Css/LogReg.css">

<%
	coreservlets.MyConnection con;

	if (request.getParameter("action") != null) {
		if (request.getParameter("action").equals("logout")) {
			session.setAttribute("userid", null);
			if (session.getAttribute("connection") != null) {
				con = (coreservlets.MyConnection) session.getAttribute("connection");
				if (con != null){
					con.closeConnection();
					session.setAttribute("connection", null);
				}
				request.getSession().invalidate();
			}
		}
	}
%>

</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-md-4 col-md-offset-4">
				<div class="account-wall">
					<div id="my-tab-content" class="tab-content">
						<div class="tab-pane active" id="login">
							<img class="profile-img" src="Pics/facebook-logo-big.png"
								height="100%" width="100%">
							<div id = "err"></div>
							<form class="form-signin" action="Register" method="POST">
								<input name="un" type="text" class="form-control" placeholder="Username"
									required autofocus>
								<input name="pw" type="password"
									class="form-control" placeholder="Password" required> <input
									type="submit" class="btn btn-lg btn-default btn-block"
									value="Sign In"/>
							</form>
							<div id="tabs" data-tabs="tabs">
								<p class="text-center">
									<a href="#register" data-toggle="tab">Need an Account?</a>
								</p>
							</div>
						</div>
						<div class="tab-pane" id="register">
						<img class="profile-img" src="Pics/facebook-logo-big.png"
								height="100%" width="100%">
							<form class="form-signin" action="Register" method="POST" onsubmit="return onBtnClick();">
								<input name="fn" id = "fn" type="text" class="form-control"
									placeholder="First Name ..." required autofocus>
								<span id="fnErr" class="error" style="display: none;"></span>
								
								<input name="ln" id = "ln" type="text" class="form-control"
									placeholder="Last Name ..." required autofocus>
								<span  id="lnErr" class="error" style="display: none;"> </span>
								
								<input name="userRegister" id = "userRegister" type="text" class="form-control"
									placeholder="User Name ..." required autofocus>
								<span  id="userErr" class="error" style="display: none;"></span>
								
								<input name="email" id = "email" type="email" class="form-control"
									placeholder="Email Address ..." required>
								<span id="emailErr"  class="error" style="display: none;"></span>
								
								<input name="password" id = "password" type="password" class="form-control" placeholder="Password ..." required>
								<span id="passErr" class="error"></span>
								
								<input name="cfmPassword" id = "cfmPassword" type="password" class="form-control" placeholder="Re-enter password ..." required>
								<span id="cfmPasswordErr" class="error" style="display: none;"></span>
								
								<input type="submit" class="btn btn-lg btn-default btn-block" value="Sign Up" />
							</form>
							<div id="tabs" data-tabs="tabs">
								<p class="text-center">
									<a href="#login" data-toggle="tab">Have an Account?</a>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<%
	String str = request.getParameter("err");
	
	if(str!=null){
		%>
			<script>
			var err=document.getElementById("err");
			var str = '<%= str %>';
			if (str == "userDoesNotExist"){
				err.innerHTML="Username or password is incorrect";
			} else if (str == "RegisterError"){
				err.innerHTML="Error in registration";
			}
			err.style.display="inline";
			err.style.color="Red";
			</script>
		<% 
	}
%>
</body>
</html>