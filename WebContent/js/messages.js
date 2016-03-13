$(function setTabAction() {
	var $tab = $(".tab");
	$tab.click(function() {
		for (var i = 0; i < $tab.length; i++) {
			if (this == $tab[i]) {
				$tab.attr("class", "tab");
				$(this).attr("class", "tab active");
			}
		}
	});
});

function sendRequest(actionName) {
	$.ajax({
		type : 'POST',
		url : 'MessageServlet',
		data : {
			action : actionName,
		},
		dataType : "json",
		success : function(data) {
			appendMessage(data);
			setSingleMessagePreviewAction();
		},
		error : function() {
			alert("Connection error!");
		}
	});
}

$("#messagesSent").click(function() {
	$("#viewMessagesDiv").removeClass("hidden");
	sendRequest('getAllSentMessages');
	$("#newMessageDiv").addClass("hidden");
});

$("#messagesRead").click(function() {
	$("#viewMessagesDiv").removeClass("hidden");
	sendRequest('getAllReadMessages');
	$("#newMessageDiv").addClass("hidden");
});

$("#messagesUnread").click(function() {
	$("#viewMessagesDiv").removeClass("hidden");
	sendRequest('getAllUnreadMessages');
	$("#newMessageDiv").addClass("hidden");
});

$("#newMessage").click(function() {
	$("#viewMessagesDiv").addClass("hidden");
	$("#newMessageDiv").removeClass("hidden");
});

$("#sendMessageButton").click(function() {
	sendMessage();
});

function appendMessage(data) {
	$("#viewMessagesDiv").html("");
	if (data != null) {
		for (i = 0; i < data.length; i++) {
			addMessage(data[i].userID, data[i].avatarPath, data[i].messageID,
					data[i].content, data[i].username);
		}
	}
}

function addMessage(userID, avatarPath, messageID, content, username) {
	var newDiv = document.createElement('div');
	newDiv.setAttribute('class', 'columnDiv');
	newDiv.innerHTML = '<div class="userWrapper"><a href="profile.jsp?id='
			+ userID
			+ '"><img class="userAvatar" src="img/avatar/'
			+ avatarPath
			+ '" /><span class="userName">'
			+ username
			+ '</span></a></div><div class="singleMessagePreviewDiv"><a class="singleMessagePreview">'
			+ content + '</a><div class="hidden">' + messageID
			+ '</div></div><div class="clear"></div>';
	$("#viewMessagesDiv").append(newDiv);
}

function setSingleMessagePreviewAction() {
	$(".singleMessagePreview").unbind('click');
	$(".singleMessagePreview").bind('click', function() {
		var messageID = $(this).next().html();
		getMessageByID(messageID);
	});
}

function sendMessage() {
	var receiverUsername = $("#receiverUsername").val();
	if (receiverUsername == "") {
		$("#receiverUsername").attr("placeholder", "请输入收信人的用户名！");
		$("#receiverUsername").focus();
		return;
	}
	var content = $("#messageContent").val();
	if (content == "") {
		$("#messageContent").attr("placeholder", "请输入私信内容！");
		$("#messageContent").focus();
		return;
	}
	content = content.replace(/(\r\n|\n|\r)/gm, '<br />');
	$("#messageContent").val("");
	$("#receiverUsername").val("");
	$.ajax({
		type : 'POST',
		url : 'MessageServlet',
		data : {
			action : 'addMessage',
			receiverUsername : receiverUsername,
			content : content
		},
		success : function(data) {
			if (data == "0") {
				$("#sendMessageErrorMessageDiv").html("你输入的用户名不存在！");
			} else {
				$("#messagesSent").click();
			}
		},
		error : function() {
			alert("Connection error!");
		}
	});
}

function getMessageByID(messageID) {
	$.ajax({
		type : 'POST',
		url : 'MessageServlet',
		data : {
			action : 'getMessageByID',
			messageID : messageID
		},
		dataType : "json",
		success : function(data) {
			processMessageData(data);
		},
		error : function() {
			alert("Connection error!");
		}
	});
}

function processMessageData(data) {
	$("#showSingleMessageDiv").html(
			'<div class="columnDiv"><a href="profile.jsp?id=' + data.fromUserID
					+ '"><img class="userAvatar" src="img/avatar/'
					+ data.fromUserAvatarPath + '"><span class="userName">'
					+ data.fromUsername
					+ '</span></a></div><div class="columnDiv">' + data.content
					+ '</div>');
	$("#singleMessageDialogBackground").removeClass("hidden");
	$("#singleMessageWrapper").removeClass("hidden");
}

$("#singleMessageDialogClose").click(function() {
	$("#singleMessageDialogBackground").addClass("hidden");
	$("#singleMessageWrapper").addClass("hidden");
	$(".tab.active").click();
});

$("#messagesUnread").trigger("click");
