var intervals = [];

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
				$("#pUserList").append(
						"<li><a href=MainProfile.jsp?user=" + item.user
								+ "&catagory=" + item.catagory + ">" + str
								+ "</a></li>");
			});

		},
		error : function(e) {
			alert("error in getting users!");
		}
	});
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

							$("#friendsList").append(
									"<li><a href=MainProfile.jsp?user="
											+ item.username
											+ "&catagory=Friends>" + str
											+ "</a></li>");
						}

					});

		},
		error : function(e) {
			alert("error in getOnlineFriends!");
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
									"<li><a href=MainProfile.jsp?user="
											+ item.username
											+ "&catagory=Friends" + ">" + str
											+ "</a></li>");
						else
							$("#pFriendList").append(
									"<li><a href=MainProfile.jsp?user="
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

function addComment(postId) {
	var content = $("#addCommet_" + postId).val();

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

function searchFriends(user) {

	$("#autocomplete").autocomplete({
		source : function(request, response) {

			var p = $.ui.autocomplete.escapeRegex(request.term);
			// the array that holds the result of all users we need
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
			window.open("MainProfile.jsp?user=" + selectID, "_self");
			return false;
		},
		focus : function(event, ui) {
			$('#autocomplete').val(ui.item.label);
			selectID = ui.item.value;
			return false;
		}
	});
}

function onSearchBtnClick() {
	window.open("MainProfile.jsp?user=" + selectID, "_self");
}

function setAddBtn(isFriend, theuser) {

	if (isFriend === "Friends") {
		$("#imgAddFriend").css("display", "none");
	} else {
		if (theuser == currentUserId) {
			$("#imgAddFriend").css("display", "none");
		} else {
			$("#imgAddFriend").css("display", "inline");
		}
	}
}

function checkForNewComments(array) {

	var postId = array[0];
	if (postId) {
		$("#existingComments_" + postId).empty();
		getComments(postId);
	}
}

function getPostDeatails(user) {

	$('#postList').empty();
	var htmlString = "";
	$
			.ajax({
				url : 'GetPosts',
				type : 'POST',
				dataType : "json",
				async : false,
				data : 'userName=' + currentUserId,
				success : function(data) {
					$
							.each(
									data,
									function(i, value) {
										var btnID = "#toggle_comment_"
												+ value.postId;
										var divID = "#comments_div_"
												+ value.postId;
										var inputId = "addCommet_"
												+ value.postId;
										var existingCommecntDiv = "#existingComments_"
												+ value.postId;

										htmlString = "<div id='post_"
												+ value.postId
												+ "' class='post_class'><div class='post_title><a href='#'><img src="
												+ value.profilePic
												+ " class='pic_post' border='1px'></a>"
												+ "<span class='userName'>"
												+ value.FullName
												+ "</span><span>says:</span>"
												+ "<div class='dateTitle'>"
												+ value.date
												+ "</div></div>"
												+ "<div id='post_Content_1' class='post_Content'>"
												+ value.content
												+ "</div>"
												+ "<div id='postAction'><a href='javascript:void(0);'><img  id='likeBtn_"
												+ value.postId
												+ "' onmouseover='this.src=\"Pics/thumb.png\";' onmouseout='this.src=\"Pics/thumb-hover.png\";'  src='Pics/thumb-hover.png' class='likePic'  ></a><a id='toggle_comment_"
												+ value.postId
												+ "' onclick='setCommentsDiv(\""
												+ btnID
												+ "\",\" "
												+ existingCommecntDiv
												+ "\",\""
												+ value.postId
												+ "\");' href='javascript:void(0);' >show comments</a></div>"
												+ "<div class='' id='comments_div_"
												+ value.postId
												+ "' >"
												+ "<div id='existingComments_"
												+ value.postId
												+ "' style=' display:none'></div>"
												+ "<div id='newCommentDiv_"
												+ value.postId
												+ "' style=' display:none'><input type='text' id="
												+ inputId
												+ " size='60' style='margin-right:5px;' >"
												+ "<a href='javascript:void(0);' class='cmtBtn' onclick='addComment("
												+ value.postId
												+ ")'>comment</a></div>"
												+ "</div>";
										$('#postList').append(htmlString);
									});
				},
				error : function(e) {
					alert("error in getPostDeatails!!!!!");
				}
			});
}

function getComments(postId) {
	var result = false;
	var htmlString = "";
	$.ajax({
				url : "GetComments",
				type : 'POST',
				dataType : "json",
				data : 'post=' + postId,
				async : false,
				success : function(data) {
					var divID = '#existingComments_' + postId;

					for (var i = 0; i < data.length; i++) {
						htmlString = "<div class='comment_class'><div class='comment_title'><a href='#'><img class='images_size' src="
								+ data[i].pic
								+ " class='pic_post' border='1px'></a><span class='userName'>"
								+ (data[i].FirstName + " " + data[i].LastName)
								+ "</span><span>: "
								+ data[i].content
								+ "</span></div></div>";
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

function showComments(value) {

	getComments(value);
}

function getFullName(userId) {

	$('#fullName').empty();
	$.ajax({
		url : "GetProfile",
		type : 'POST',
		dataType : "json",
		data : 'userName=' + userId,
		success : function(data) {
			var span = $('<span />').attr("id", "userName").data("data-user",
					data.username).html(data.FirstName + " " + data.LastName);

			$('#fullName').append(span);
		},
		error : function(e) {
			alert("error in getFullName!");
		}
	});
}

function getUsers(id, data, callback) {

	$.ajax({
		url : "Js/users.js",
		dataType : "json",
		success : callback
	});
}

function getPictures(userId) {
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
		},
		error : function(e) {
			alert("error in getPictures!");
		}
	});
}

function getMsgData() {

	$.ajax({
		url : "GetMessages",
		type : 'POST',
		dataType : "json",
		data : 'userName=' + currentUserId,
		success : function(data) {
			$.each(data, function(i, msg) {
				$("#msgDropDown table").append(
						'<tr><td class="td_text"><span><b>' + msg.FirstName
								+ " " + msg.LastName
								+ ': </b></span></td><td class="td_text">'
								+ msg.content + '</td></tr>');
			});
		},
		error : function(e) {
			alert("error in msg data!" + e);
		}
	});
}

function getNotifData() {

	$.ajax({
		url : "GetNotifications",
		type : 'POST',
		dataType : "json",
		data : 'userName=' + currentUserId,
		success : function(data) {
			$.each(data, function(i, notif) {
				$("#NotifDropDown table").append(
						'<tr class="border_bottom"><td class="td_text"><span>'
								+ notif.content + '</span></td></tr>');
			});
		},
		error : function(e) {
			alert("error in getNotifData!" + e);
		}
	});
}

function checkForNewPost(array){
	

	var posts = $("#postList").children();
	var potstIds = [];
	
	if(posts){

		for (var i = 0; i < posts.length; i++) {
			var idx = posts[i].id.substr(posts[i].id.lastIndexOf("_") + 1);
			potstIds.push( idx );
		}
		
		getPostDeatails(potstIds.toString());

	}
	
}

function expandAllOptions(btn, list) {

	$(list).toggle("fast");
	$(btn).addClass("active");

}

function hideAllOptions(btn, list) {

	$(list).toggle("fast");
	$(btn).removeClass("active");
}

function unbindClickOutsideTrigger() {

	$(document).off("mouseup.alloptions");
}

function bindClickOutsideTrigger(btn, list) {

	$(document).on('mouseup.alloptions', function(e) {
		var container = $(list);
		if (!container.is(e.target) && container.has(e.target).length === 0) {
			if (e.target.parentNode.id == $(btn)[0].id)
				return;
			console.log("fold up all-options");
			$(list + " table").empty();
			hideAllOptions(btn, list);
			unbindClickOutsideTrigger();
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

function expandComments(btn, list) {
	$(list).fadeIn("slow");
	$(list).next().show();
	$(btn).addClass("active");
	$(btn).text('hide comments');
}
function hideComments(btn, list) {
	$(list).fadeOut("slow");
	$(list).next().hide();
	$(btn).removeClass("active");
	$(btn).text('show comments');
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

function deleteInterval(id) {

	if (intervals[id]) {
		clearInterval(this.intervals[id]);
		delete intervals[id];
		return true;
	}
}

function createNewInterval(fun, delay, paramArr) {

	var newIntervalKey = paramArr[0];
	intervals[newIntervalKey] = setInterval(function() {
		fun(paramArr);
	}, delay);
	return newIntervalKey;

}

function unbindClickOutsideComments() {

	$(document).off("mouseup.Comments");
}

function bindClickOutsideComments(btn, div) {

	$(document).on('mouseup.Comments', function(e) {
		var container = $(div);
		if (!container.is(e.target) && container.has(e.target).length === 0) {
			if (e.target.parentNode.id == $(btn)[0].id)
				return;
			console.log("fold up all-options");
			$(div).empty();
			hideAllOptions(btn, div);
			unbindClickOutsideTrigger();
		}
	});
}

$(document).ready(function() {

	$(document).on("mousemove", function(event) {
		pageX = event.pageX;
		pageY = event.pageY;
	});

	getPostDeatails();
	$("#btnPost").click(getPostDeatails());
	getOnlineFriends();
	getAllUsers();
	setInterval(getOnlineFriends, 8000);
	setDialog('#msg', '#msgDropDown');
	setDialog('#notif', '#NotifDropDown');
});

function isMyFriend(currentUserId, theuser) {
	$.ajax({
		url : "IsMyFriend",
		type : 'POST',
		dataType : "json",
		data : 'currentUserId=' + currentUserId + "&theuser=" + theuser,
		success : function(data) {
			if (data.isFriend === "true") {
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

