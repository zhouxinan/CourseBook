function setReplyCountDivAction() {
	var $replyCountDiv = $(".replyCountDiv");
	$replyCountDiv.unbind('click');
	$replyCountDiv.bind('click', function() {
		var currentReplyListDiv = $(this).parent().next();
		if (currentReplyListDiv.attr("class") == "replyListDiv") {
			var answerID = $(this).next().html();
			getReply(answerID, currentReplyListDiv);
			currentReplyListDiv.attr("class", "replyListDiv active");
		} else {
			currentReplyListDiv.attr("class", "replyListDiv");
		}
	});
};

function setSendReplyButtonAction() {
	$(".sendReplyButton").click(function() {
		sendReply($(this));
	});
}

function sendReply(sendReplyButton) {
	var answerID = sendReplyButton.parent().parent().prev().children().last()
			.html();
	var content = sendReplyButton.prev().val();
	if (content == "") {
		sendReplyButton.prev().attr("placeholder", "请输入回复！");
		sendReplyButton.prev().focus();
		return;
	}
	$.ajax({
		type : 'POST',
		url : 'QuestionServlet',
		data : {
			action : 'addReply',
			answerID : answerID,
			content : content
		},
		dataType : "json",
		success : function(data) {
			processReplyData(data, sendReplyButton.parent().parent());
			setSendReplyButtonAction();
		},
		error : function() {
			alert("Connection error!");
		}
	});
}

function getReply(answerID, replyListDiv) {
	$.ajax({
		type : 'POST',
		url : 'QuestionServlet',
		data : {
			action : 'getReply',
			answerID : answerID
		},
		dataType : "json",
		success : function(data) {
			processReplyData(data, replyListDiv);
			setSendReplyButtonAction();
		},
		error : function() {
			alert("Connection error!");
		}
	});
}

function processReplyData(data, replyListDiv) {
	if (data == "-1") {
		replyListDiv
				.html('<div class="replyDiv new"><div class="errorMessageDiv">登录后才可以查看和发表评论</div></div>');
		return;
	}
	replyListDiv
			.html('<div class="replyDiv new"><input type="text" class="standardInput" placeholder="说你什么好呢……" /><button class="standardButton sendReplyButton">发表评论</button></div>');
	if (data != null) {
		for (i = 0; i < data.length; i++) {
			addReplyToPage(data[i].userID, data[i].avatarPath,
					data[i].username, data[i].content, data[i].replyTime,
					replyListDiv);
		}
	}
}

function addReplyToPage(userID, avatarPath, username, content, replyTime,
		replyListDiv) {
	var replyDiv = document.createElement('div');
	replyDiv.setAttribute('class', 'replyDiv');
	replyDiv.innerHTML = '<div><a href="profile.jsp?id='
			+ userID
			+ '"><img class="userAvatar" src="img/avatar/'
			+ avatarPath
			+ '" /></a></div><div class="replyData"><div class="userName"><a href="profile.jsp?id='
			+ userID + '">' + username + '</a></div><div class="replyContent">'
			+ content + '</div><div class="replyTime">' + replyTime
			+ '</div></div><div class="clear"></div>';
	replyListDiv.prepend(replyDiv);
}

function sendAnswer() {
	var content = $("#newAnswerContent").val();
	var file = document.getElementById("file").files[0];
	if (content == "" && file == undefined) {
		$("#newAnswerContent").attr("placeholder", "请输入回答内容，或选择上传一张照片");
		$("#newAnswerContent").focus();
		return;
	}
	content = content.replace(/(\r\n|\n|\r)/gm, '<br />');
	if (file != undefined) {
		$("#questionID").val($("#questionIDDiv").html());
		$("#newAnswerForm").submit();
	} else {
		$.ajax({
			type : 'POST',
			url : 'QuestionServlet',
			data : {
				action : 'addAnswer',
				questionID : $("#questionIDDiv").html(),
				content : content
			},
			dataType : "json",
			success : function(data) {
				addAnswerToPage(data.userID, data.avatarPath, data.username,
						data.motto, data.content, data.answerTime,
						data.answerID, data.replyCount);
				$("#newAnswerContent").val("");
				setReplyCountDivAction();
			},
			error : function() {
				alert("Connection error!");
			}
		});
	}
}

function getAnswer(startingIndex, numberOfAnswers) {
	$.ajax({
		type : 'POST',
		url : 'QuestionServlet',
		data : {
			action : 'getAnswer',
			questionID : $("#questionIDDiv").html(),
			startingIndex : startingIndex,
			numberOfAnswers : numberOfAnswers
		},
		dataType : "json",
		success : function(data) {
			processAnswerData(data);
			setReplyCountDivAction();
		},
		error : function() {
			alert("Connection error!");
		}
	});
}

function processAnswerData(data) {
	for (i = 0; i < data.length; i++) {
		addAnswerToPage(data[i].userID, data[i].avatarPath, data[i].username,
				data[i].motto, data[i].content, data[i].answerTime,
				data[i].answerID, data[i].replyCount);
	}
	startingIndex += data.length;
	if (data.length < 5) {
		$("#getMoreAnswersButton").html("没有更多答案了");
		$("#getMoreAnswersButton").unbind("click");
		$("#getMoreAnswersButton").css({
			"color" : "#bdc3c7",
			"border" : "1px solid #bdc3c7",
			"cursor" : "default"
		});
	}
}

function addAnswerToPage(userID, avatarPath, username, motto, content,
		answerTime, answerID, replyCount) {
	var columnDiv = document.createElement('div');
	columnDiv.setAttribute('class', 'columnDiv');
	columnDiv.innerHTML = '<div class="userInfoDiv"><a href="profile.jsp?id='
			+ userID + '"><img class="userAvatar" src="img/avatar/'
			+ avatarPath + '" /><span class="userName">' + username
			+ ' </span></a><span class="userSignature">' + motto
			+ '</span></div><div class="answer"><p>' + content
			+ '</p></div><div class="answerMetadata"><div>' + answerTime
			+ '</div> <div class="replyCountDiv noSelect">评论 (' + replyCount
			+ ')</div><div class="answerID hidden">' + answerID
			+ '</div></div><div class="replyListDiv"></div>';
	$("#getMoreAnswersDiv").before(columnDiv);
}

function setFileTypeValidator() {
	$("#file").on('change', function() {
		var file = document.getElementById("file").files[0];
		if (!file.type.match('image.*')) {
			alert("你只能选择图片文件，请重新选择！");
			resetFileInput();
		}
	});
}

function resetFileInput() {
	$("#resetFileDiv").html('<input type="file" id="file" name="file" />');
	setFileTypeValidator();
}

$("#getMoreAnswersButton").bind('click', function() {
	getAnswer(startingIndex, 5);
});

$("#sendAnswerButton").bind('click', function() {
	sendAnswer();
});

startingIndex = 0;

$("#getMoreAnswersButton").click();

$("#uploadImgButton").bind('click', function() {
	$("#file").trigger('click');
});

setFileTypeValidator();