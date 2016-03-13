$(".clickbox").click(function() {
	window.location = $(this).find("a").attr("href");
	return false;
});

$("#searchButton").click(function() {
	var keyword = $("#searchInput").val();
	if (keyword == "") {
		$("#searchInput").attr("placeholder", "请输入关键词");
		$("#searchInput").focus();
		return;
	}
	window.location = "search.jsp?keyword=" + encodeURIComponent(keyword);
});

$("#newQuestionButton").click(function() {
	$("#newQuestionDialogBackground").removeClass("hidden");
	$("#newQuestionWrapper").removeClass("hidden");
});

$(".modalDialogTitleClose").click(function() {
	$("#newQuestionDialogBackground").addClass("hidden");
	$("#newQuestionWrapper").addClass("hidden");
});

$("#submitNewQuestionButton").click(function() {
	var newQuestionTitle = $("#newQuestionTitle").val();
	var newQuestionContent = $("#newQuestionContent").val();
	if (newQuestionTitle == "") {
		$("#newQuestionTitle").attr("placeholder","问题标题是必填的");
		$("#newQuestionTitle").focus();
		return;
	}
	newQuestionContent = newQuestionContent.replace(/(\r\n|\n|\r)/gm, '<br />');
	$("#newQuestionTitleChecked").val(newQuestionTitle);
	$("#newQuestionContentChecked").val(newQuestionContent); 
	$("#newQuestionForm").submit();
});

$.ajax({
	type : 'POST',
	url : "MessageServlet",
	data : {
		action : 'getUnreadMessageCount',
	},
	success : function(data) {
		if (data != "0") {
			$(".notificationBubble").html(data);
			$(".notificationBubble").css('display', 'block');
		}
	},
	error : function() {
		alert("Connection error!");
	}
});