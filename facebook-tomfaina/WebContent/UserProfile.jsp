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
    padding-left:0;
    margin-left: -30px;
}

.cover{
	position: relative;
	top: 0; 
	left: 0;
	border:1px solid black;
	
}
.profile{
	position: absolute; 
	top: 0px; 
	left: 0px; 
	border:1px solid black;
	box-shadow: 5px 5px 3px #444444; /*right down effect*/
	
}
.profilePics{
	position: relative; 
	left: 0; 
	top: 0;
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
				<a class="navbar-brand" href='Main.jsp?user=<%= session.getAttribute("userid") %>'><img
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
					<li id="li-home"><a  href='Main.jsp?user=<%= session.getAttribute("userid") %>'>Home</a></li>
					<li id="li-msg"><a id = "msg-btn" href="#" data-toggle="popover" title="Messages" data-trigger="focus" data-placement="bottom"><img src="Pics/message.png"></img></a></li>
					<li id="li-ntf"><a id = "ntf-btn" href="#" data-toggle="popover" title="Notifications" data-trigger="focus" data-placement="bottom"><img src="Pics/earth.png"></img></a></li>
					<li id="li-logout"><a id = "logout-btn" href="#" data-toggle="popover" data-trigger="focus" data-html="true" data-content='<a id="logout" href="LogReg.jsp?action=logout"><img src=Pics/off.png></a>' data-placement="bottom"><img src="Pics/down.png"></img></a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container text-center">
		<div id="section">
			<div class="profilePics" ></div>
			<div style="display: inline"></div>
			<div id="btnAddDiv" style="display: inline">
				<img id='imgAddFriend' onclick='addFriend("#userName")' src='Pics/addUserBig.png' title='Add as a firend' alt='Add as a firend'>
			</div>
			<div class ="row center">
				<div id="pFriendListDiv" class ="col-sm-3 well" style="margin-top: 50px">
					<h3 id="titleFirends">Friends</h3>
					<hr>
					<ul id="pFriendList"></ul>
				</div>
				<div class="col-sm-9" style="margin-top: 50px;">
		          <div class="well" style="height: 126px;">
		            <h1 id="fullName"></h1>
		          </div>
		        </div>
			</div>
		</div>	
	</div>
	
	<%
		if (request.getParameter("user") != null) {
			String user = request.getParameter("user");	
	%>
		<script type="text/javascript">
			var theuser = "<%= user %>";
			getProfile(theuser);
			getOnlineFriendsForUsers(theuser);
			isMyFriend(currentUserId, theuser);
		</script>
	<%
		}

	%>

	<footer class="container-fluid text-center">
		<p>Faina & Tom</p>
	</footer>

</body>
</html>