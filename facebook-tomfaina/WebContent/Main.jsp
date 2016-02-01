<!DOCTYPE html>
<html>
<head>
<title>Facebook</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="Js/JQueryRotate.js" type="text/javascript"></script>
<script src="Js/GlobalFunctions.js" type="text/javascript"></script>

<script type="text/javascript">
var currentUserId =	 '<%=session.getAttribute("userid")%>';
var userObject = '<%=session.getAttribute("userDetails")%>';
var userData = jQuery.parseJSON( userObject );
</script>

<style>
footer {
	background-color: #47639D;
	color: white;
	padding: 15px;
	bottom: 0px;
}

#mynavbar {
	background-color: #47639D;
}

.navbar-inverse .navbar-nav>li>a {
	color: white;
}

.navbar-brand>img {
	margin-bottom: 0px;	
}

.popup-data{
	width: 160px;

}

.no-bullet{
	list-style-position: inside;
    list-style-type: none;
    list-style:none;
    text-align: left;
    padding-left:0;
    margin-left: -30px;
}
</style>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);

	if (session.getAttribute("userid") == null) {
		response.sendRedirect("LogReg.jsp");
		return;
	}
%>
</head>
<body>

	<nav id="mynavbar" class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href='UserProfile.jsp?user=<%= session.getAttribute("userid") %>'><img
					src="Pics/facebook-logo-small.png"></a>
			</div>
			<div class="collapse navbar-collapse" id="myNavbar">
				<ul class="nav navbar-nav">
					<li><form class="navbar-form" role="search">
						<span class="form-group input-group">
							<input type="text" placeholder="Search Friends"
								onkeypress="searchFriends(currentUserId);" id="autocomplete"
								class="form-control" placeholder="Search.."> <span
								class="input-group-btn">
								<button class="btn btn-default" type="button"
									onclick="onSearchBtnClick();">
									<span class="glyphicon glyphicon-search"></span>
								</button>
							</span>
						</span>
					</form></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li id="li-account"><a href='UserProfile.jsp?user=<%= session.getAttribute("userid") %>'><span class="glyphicon glyphicon-user"></span>My Profile</a></li>
					<li id="li-home"><a href="#">Home</img></a></li>
					<li id="li-msg"><a id = "msg-btn" href="#" data-toggle="popover" title="Messages" data-trigger="focus" data-placement="bottom"><img src="Pics/message.png"></a></li>
					<li id="li-ntf"><a id = "ntf-btn" href="#" data-toggle="popover" title="Notifications" data-trigger="focus" data-placement="bottom"><img src="Pics/earth.png"></a></li>
					<li id="li-logout"><a id = "logout-btn" href="#" data-toggle="popover" data-trigger="focus" data-html="true" data-content='<a id="logout" href="LogReg.jsp?action=logout"><img src=Pics/off.png></a>' data-placement="bottom"><img src="Pics/down.png"></img></a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container text-center">
		<div class="row">
			<div class="col-sm-3 well">
				<div class="well">
					<p>
						<a href="#"><img id="userProfilePic"></img></a>
					</p>
					<!--<img src="bird.jpg" class="img-circle" height="65" width="65"
						alt="Avatar">-->
				</div>
				<div class="well" >
					<ul>
						<li class="no-bullet"><button type="button" class="btn btn-success btn-block" data-toggle="collapse" data-target="#friendsList" style="margin-bottom: 5px;">Online Friends</button></li>
						<li class="no-bullet">
							<table style="width: 176px;" class="table collapse" id="friendsList">
								<tbody>
								</tbody>
							</table>
						</li>
						<li class="no-bullet"><button type="button" class="btn btn-warning btn-block" data-toggle="collapse" data-target="#pUserList" style="margin-bottom: 5px;">All Users</button></li>
						<li class="no-bullet">
						<table style="width: 176px;" class="table collapse" id="pUserList">
							<tbody>
							</tbody>
						</table>
						</li>
					</ul>
				</div>
			</div>	
			<div class="col-sm-7">
				<div class="row">
					<div class="col-sm-12">
						<div class="panel panel-default text-left">
							<div class="panel-body">
								<textarea class="form-control input-lg" rows="5" id="comment" placeholder="What's on your mind?"></textarea>
								<br>
								<button id="postBtn" onclick="getNewPost(currentUserId)" type="button" class="btn btn-primary">Post</button>
							</div>
						</div>
					</div>
				</div>
				<div id="postList"></div>
			</div>
		</div>
	
	</div>

	<footer class="container-fluid text-center">
		<p>Faina & Tom</p>
	</footer>

</body>
</html>