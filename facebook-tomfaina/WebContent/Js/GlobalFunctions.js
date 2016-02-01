var intervals = [];

var currentUserPic;
var selectID;

get_data_for_popover_and_display1 = function() {
    var el = $(this);
    var _data = el.attr('alt');
    var html = '<table class="popup-data">'
    $.ajax({
		url : "GetMessages",
		type : 'POST',
		dataType : "json",
		async: false,
		data : 'userName=' + currentUserId,
		success : function(data) {
			$.each(data, function(i, msg) {	           
				
				html+='<tr><td style="border-bottom: 1px solid #CDC1A7; line-height: 24px;"><span><b>' + msg.FirstName
								+ " " + msg.LastName
								+ ': </b></span>'
								+ msg.content + '<br></td></tr>';
			});
		},
		error : function(e) {
			alert("error in msg data!" + e);
		}
	});
    html += '</table>';
    el.attr('data-content', html);
    el.popover('show');
}

get_data_for_popover_and_display2 = function() {
    var el = $(this);
    var _data = el.attr('alt');
    var html = '<table class="popup-data">'
    	$.ajax({
    		url : "GetNotifications",
    		type : 'POST',
    		dataType : "json",
    		async: false,
    		data : 'userName=' + currentUserId,
    		success : function(data) {
    			$.each(data, function(i, notif) {
    				html+='<tr><td style="border-bottom: 1px solid #CDC1A7; line-height: 24px;"><span>'
    								+ notif.content + '<br></span></td></tr>';
    			});
    		},
    		error : function(e) {
    			alert("error in getNotifData!" + e);
    		}
    	});
    html += '</table>';
    el.attr('data-content', html);
    el.popover('show');
}

function getOnlineFriends() {
	$("#friendsList").empty();
	$.ajax({
		url : "GetOnlineFriends",
		type : 'POST',
		dataType : "json",
		data : 'userName=' + currentUserId,
		success : function(data) {
			$.each(data,
					function(j, item) {
						if (item.isOnline) {
							var str = item.FullName;
							var res = str.split(" ");
							$("#friendsList").append('<tr><td><a href=UserProfile.jsp?user='+ item.username + '>' + str+ '</a></tr></td>');
						}

					});
		},
		error : function(e) {
			alert("error in getOnlineFriends!");
		}
	});
}

function addFriend(element) {
	var friendName = $(element).data("data-user");

	if (friendName) {

		$.ajax({
			url : 'AddFriend',
			type : 'POST',
			async : false,
			datatype : 'json',
			data : 'user=' + currentUserId + '&friend=' + friendName,
			success : function(data) {

				if (data && data.result) {

					getOnlineFriends();
					getOnlineFriendsForUsers(friendName);

				} else {
					alert('Opps..!!');
				}
			},
			error : function(e) {
				alert("error in ajax adding friend");
			}
		});
	}
}

function getAllUsers() {
	$("#pUserList").empty();

	$.ajax({
		url : "GetAllUsers",
		type : "POST",
		dataType : "json",
		data : 'currentUserId=' + currentUserId,
		success : function(data) {

			$.each(data, function(j, item) {
				var str = item.name;
				$("#pUserList").append('<tr><td><a href=UserProfile.jsp?user=' + item.user + '>' + str + '</a></tr></td>');
			});
		},
		error : function(e) {
			alert("error in getting users!");
		}
	});
}

function setDialog(btn, list) {
	$(btn).click(function() {
		if ($(btn).hasClass("active")) {
			$(list + ' table').empty();
			hideAllOptions(btn, list);
			unbindClickOutsideTrigger();
		} else {
			(btn == '#msg') ? getMsgData() : getNotifData();
			var position = $(btn).position();
			$(list).css({
				'top' : position.top + 45,
				'left' : position.left,
				'border' : '1px solid black'
			});

			expandAllOptions(btn, list);
			bindClickOutsideTrigger(btn, list);
		}
	});
}

function searchFriends(user) {
		$("#autocomplete").autocomplete({
		source : function(request, response) {
			var p = $.ui.autocomplete.escapeRegex(request.term);
			var result = [];

			$.ajax({
				url : 'Search',
				type : 'POST',
				async : false,
				datatype : 'json',
				data : 'prefix=' + p + '&userName=' + user,
				success : function(data) {

					$.each(data, function(j, item) {
						result.push({
							label : item.FullName,
							value : item.username
						});

					});

				},
				error : function(e) {
					alert("error in ajax getAllUsers");
				}
			});

			response(result);
		},
		minLength : 1,
		select : function(event, ui) {
			$('#autocomplete').val(ui.item.label);
			selectID = ui.item.value;
			window.open("UserProfile.jsp?user=" + selectID, "_self");
			return false;
		},
		focus : function(event, ui) {
			$('#autocomplete').val(ui.item.label);
			selectID = ui.item.value;
			return false;
		}
	});
}

function getPostDeatails(user,param) {

	var postData = 'userName=' + currentUserId;
	if(param == undefined)
		$('#postList').empty();
	else{
		postData += '&postIds='+param;		
	}
	
	//$('#postList').empty();
	var htmlString = "";
	$.ajax({
				url : 'GetPosts',
				type : 'POST',
				dataType : "json",
				async : false,
				data : postData,
				success : function(data) {
					$.each(data, function(i, value) {
						var btnID = "#toggle_comment_"
								+ value.postId;
						var divID = "#comments_div_"
								+ value.postId;
						var inputId = "addComment_"
								+ value.postId;
						var existingCommentDiv = "#existingComments_"
								+ value.postId;

						htmlString ='<div id="post_'+ value.postId+ '" class="row">' +
						'<div class="col-sm-3">' +
							'<div class="well" style="height: 120px;">' +
								/* '<p> '  + value.FullName  + ' </p>' + */
								'<img src="'+ value.profilePic+ '" class="img-circle" height="55" width="55"' +
									'alt="Avatar">' + 
							'</div>' +
						'</div>' +
						'<div class="col-sm-9" style="margin-bottom:7px;">' +
							'<div class="well" style="height: 120px; margin-bottom: 0px;  overflow: auto;">' +
								'<p>' +  value.content +'</p>' +
									/* '<button type="button" class="btn btn-default btn-sm" >' +
										'<span class="glyphicon glyphicon-thumbs-up"></span>Like' +
									'</button>' +   javascript:void(0);      onclick="setCommentsDiv(\"' + btnID + '\",\"' + existingCommentDiv + '"\",\"' + value.postId + '\");"    
									
									 * onkeypress="onCommentEnter(event,this,'+value.postId + ')" 
									 */
							'</div>' +
							'<div><a id="toggle_comment_'+ value.postId + '" onclick="setCommentsDiv(\'' + btnID + '\',\'' + existingCommentDiv + '\',\'' + value.postId + '\');" href="javascript:void(0)";><img src =Pics/down-blue.png class="rotate"></a></div>' +
							'<div id="comments_div_'+ value.postId+'" class="row"  >' +
								'<div id="existingComments_'+  value.postId +'" style="display:none" >' +
													
								'</div>' +
								'<div id="new_Comments_'+value.postId  +'" style="display:none">' +
										'<div class="col-sm-3" >' +
											'<div class="well" style="height: 70px; padding-top:9px; margin-bottom:5px;">' +
												'<img src="'+ userData.profilePic + '" class="img-circle" height="45"' +
													'width="45" alt="Avatar">' +
											'</div>' +
										'</div>' +
										'<div class="col-sm-8">' +
											'<div class="well" style="height: 70px; width: 370px; padding-left: 0px; padding-right: 0px; margin-left:-19px; margin-bottom:5px;">' +
												'<input type="text" id="'+inputId + '" size="30" style="margin-right:5px; display:inline;"    >' +
												'<button id="postBtn" style=" display:inline;" onclick="addComment(event,this,' +value.postId +')" type="button" class="btn btn-primary">comment</button>' +
											'</div>' +
										'</div>' +
								'</div>' +
							'</div>' +
						'</div>' +
					'</div>'
					
						$('#postList').append(htmlString);
					});
				},
				error : function(e) {
					alert("error in getPostDeatails!!!!!");
				}
			});
}

function getNewPost(user) {
	var content = $('#comment').val();
	var htmlString = "";
	$.ajax({
				url : 'PostPost',
				type : 'POST',
				dataType : "json",
				async : false,
				data : 'userName=' + currentUserId +"&postData=" + content,
				success : function(value) {
					
						var btnID = "#toggle_comment_"
								+ value.postId;
						var divID = "#comments_div_"
								+ value.postId;
						var inputId = "addComment_"
								+ value.postId;
						var existingCommentDiv = "#existingComments_"
								+ value.postId;

						htmlString ='<div id="post_'+ value.postId+ '" class="row">' +
						'<div class="col-sm-3">' +
							'<div class="well" style="height: 120px;">' +
								'<img src="'+ userData.profilePic+ '" class="img-circle" height="55" width="55"' +
									'alt="Avatar">' + 
							'</div>' +
						'</div>' +
						'<div class="col-sm-9" style="margin-bottom:7px;">' +
							'<div class="well" style="height: 120px; margin-bottom: 0px;  overflow: auto;">' +
								'<p>' +  value.content +'</p>' +
							'</div>' +
							'<div><a id="toggle_comment_'+ value.postId + '" onclick="setCommentsDiv(\'' + btnID + '\',\'' + existingCommentDiv + '\',\'' + value.postId + '\');" href="javascript:void(0)";><img src =Pics/down-blue.png></a></div>' +
							'<div id="comments_div_'+ value.postId+'" class="row"  >' +
								'<div id="existingComments_'+  value.postId +'" style="display:none" >' +
													
								'</div>' +
								'<div id="new_Comments_'+value.postId  +'" style="display:none">' +
										'<div class="col-sm-3" >' +
											'<div class="well" style="height: 70px; padding-top:9px; margin-bottom:5px;">' +
												'<img src="'+ userData.profilePic + '" class="img-circle" height="45"' +
													'width="45" alt="Avatar">' +
											'</div>' +
										'</div>' +
										'<div class="col-sm-8">' +
											'<div class="well" style="height: 70px; width: 370px; padding-left: 0px; padding-right: 0px; margin-left:-19px; margin-bottom:5px;">' +
												'<input type="text" id="'+inputId + '" size="30" style="margin-right:5px; display:inline;"    >' +
												'<button id="postBtn" style=" display:inline;" onclick="addComment(event,this,' +value.postId +')" type="button" class="btn btn-primary">comment</button>' +
											'</div>' +
										'</div>' +
								'</div>' +
							'</div>' +
						'</div>' +
					'</div>'
					
					$('#postList').prepend(htmlString);
				},
				error : function(e) {
					alert("error in getPostDeatails!!!!!");
				}
			});
}

function setCommentsDiv(btn, list, postId) {

	if ($(btn).hasClass("active")) {
		hideComments(btn, list);
		deleteInterval(postId);
	} else {
		$(list).empty();
		var hasComments = getComments(postId);
		expandComments(btn, list);
		createNewInterval(checkForNewComments, 10000, [ postId ]);
	}
}

function checkForNewComments(array) {

	var postId = array[0];
	if (postId) {
		$("#existingComments_" + postId).empty();
		getComments(postId);
	}
}

function expandComments(btn, list) {
	$(list).fadeIn("slow");
	$(list).next().show();
	$(btn).addClass("active");
	$(btn + ' img').addClass("active");
}

function hideComments(btn, list) {
	$(list).fadeOut("slow");
	$(list).next().hide();
	$(btn).removeClass("active");
	$(btn + ' img').removeClass("active");

}

function getComments(postId) {
	var result = false;
	var htmlString = "";
	$
			.ajax({
				url : "GetComments",
				type : 'POST',
				dataType : "json",
				data : 'post=' + postId,
				async : false,
				success : function(data) {
					var divID = '#existingComments_' + postId;

					for (var i = 0; i < data.length; i++) {

						htmlString =	'<div id="comments_id_'+postId  +'" style="dispaly:inline;">' +
						'<div class="col-sm-3" >' +
							'<div class="well" style="height: 70px; padding-top:9px; margin-bottom:5px;">' +
								'<img src="'+ data[i].pic + '" class="img-circle" height="45"' +
									'width="45" alt="Avatar">' +
							'</div>' +
						'</div>' +
						'<div class="col-sm-8">' +
							'<div class="well" style="height: 70px; width: 370px; margin-left:-19px; margin-bottom:5px;">' +
								'<p>'+  data[i].content + '</p>' +
							'</div>' +
						'</div>' +	
						'</div>'
						
						$(divID).append(htmlString);
					}
					if (data && data.length > 0) {
						result = true;
					}
				},
				error : function(e) {
					alert("error in getComments!");
				}
			});
	return result;
}

function addComment(e,btn, postId) {
	var content = $("#addComment_" + postId).val();

	$.ajax({
		url : 'PostComment',
		type : 'POST',
		async : false,
		datatype : 'json',
		data : 'post=' + postId + '&content=' + content + '&userName='
				+ currentUserId,
		success : function(data) {

			if (data.result) {

				$("#existingComments_" + postId).empty();
				$("#addCommet_" + postId).val('');

				getComments(postId);
			} else {
				alert('Opps..!!');
			}
		},
		error : function(e) {
			alert("error in ajax adding comment");
		}
	});

}

function getProfile(userId) {
	var htmlString = "";
	$('.profilePics').empty();

	$.ajax({
		url : "GetProfile",
		type : 'POST',
		dataType : "json",
		data : 'userName=' + userId,
		success : function(data) {
			
			htmlString = "<img class=cover src=" + data.cover + ">"
					+ "<img class=profile src=" + data.profile + ">";
			$('.profilePics').append(htmlString);
			
			var span = $('<span />').attr("id", "userName").data("data-user",
					data.username).html(data.FirstName + " " + data.LastName);

			$('#fullName').append(span);
		},
		error : function(e) {
			alert("error in getProfile!");
		}
	});
}

function isMyFriend(currentUserId, theuser) {
	
	$.ajax({
		url : "IsMyFriend",
		type : 'POST',
		dataType : "json",
		data : 'currentUserId=' + currentUserId + "&theuser=" + theuser,
		success : function(data) {
			if (data.isFriend === "true" || currentUserId == theuser) {
				$("#imgAddFriend").css("display", "none");
			} else {
				$("#imgAddFriend").css("display", "inline");
			}
		},
		error : function(e) {
			alert("error in isMyFriend");
		}
	});
}

function getOnlineFriendsForUsers(userid) {
	$("#pFriendList").empty();
	$.ajax({
		url : "GetOnlineFriends",
		type : "POST",
		dataType : "json",
		data : 'userName=' + userid + '&connectedUser=' + currentUserId,
		success : function(data) {
			if (data.length == 0) {
				if (userid == currentUserId)
					$("#pFriendList").append(
							"<li>You dont have friends yet...</li>");
				else
					$("#pFriendList").append(
							"<li>this user dont have friends yet...</li>");
			} else {

				$.each(data, function(j, item) {
					if (item.isOnline) {
						var str = item.FullName;
						if (item.isFriend == "Yes")
							$("#pFriendList").append(
									"<li class='no-bullet'><a href=UserProfile.jsp?user="
											+ item.username
											+ "&catagory=Friends" + ">" + str
											+ "</a></li>");
						else
							$("#pFriendList").append(
									"<li class='no-bullet'><a href=UserProfile.jsp?user="
											+ item.username
											+ "&catagory=Others" + ">" + str
											+ "</a></li>");
					}
				});
			}
		},
		error : function(e) {
			alert("error in getOnlineFriends!!!!!");
		}
	});
}

function createNewInterval (fun, delay, arryParam) {

	// var newKey = this.intervals.length + 1;
	var newKey = arryParam[0];

	// this.intervals[newKey] = setInterval(fun, delay);

	intervals[newKey] = setInterval(function() {
		fun(arryParam);
	}, delay);

	console.log('new interval created: ' + newKey);

	return newKey;
}

function deleteInterval(id) {

	if(intervals[id]){
		clearInterval(intervals[id]);
		delete intervals[id];

		console.log('removed interval : ' + id);

		return true;
	}
	else{
		console.log('didnt find interval : ' + id);
	}

	
}

function checkForNewPost(array){
	

	var posts = $("#postList").children();
	var potstIds = [];
	
	if(posts){

		for (var i = 0; i < posts.length; i++) {
			var idx = posts[i].id.substr(posts[i].id.lastIndexOf("_") + 1);
			potstIds.push( idx );
		}
		
		getPostDeatails(currentUserId,  potstIds.toString());
	}
}

$(document).ready(function(){
	
	getOnlineFriends();
	getAllUsers();
	setInterval(getOnlineFriends, 8000);	
	
    $('[data-toggle="popover"]').popover({html:true});
    $('a#msg-btn').popover({"trigger": "manual", "data-html":"true"});
    $('a#msg-btn').click(get_data_for_popover_and_display1);
    
    $('a#ntf-btn').popover({"trigger": "manual", "data-html":"true"});
    $('a#ntf-btn').click(get_data_for_popover_and_display2);
    
    getPostDeatails(currentUserId);
    $("#userProfilePic").attr("src", userData.profilePic);
    
   
    
    var currentPage = location.pathname.substring(location.pathname.lastIndexOf("/")+1);
	if(currentPage == 'Main.jsp'){
		createNewInterval(checkForNewPost, 10000, ["postChecker"]);
	}
	
	 $(".rotate").rotate({bind:{
		  click: function(){
			  if ($(this).hasClass("active")) {
				  $(this).rotate({
				      angle: 180,
				      animateTo:360
			      })
			 } else {
				 $(this).rotate({
				      angle: 0,
				      animateTo:180
			      })
			 }
		    }
		  }
		});
});
