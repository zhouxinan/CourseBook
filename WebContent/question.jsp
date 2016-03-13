<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="dao.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.json.*"%>
<%@ page import="bean.*"%>
<%
	User user = (User) request.getSession().getAttribute("user");
	String id = request.getParameter("id");
	if (id == null) {
		response.sendRedirect("index.jsp");
		return;
	}
	Dao dao = Dao.getInstance();
	Question currentQuestion = null;
	try {
		currentQuestion = dao.getQuestionByID(Integer.parseInt(id));
	} catch (NumberFormatException e) {
		response.sendRedirect("404.jsp");
		return;
	}
	if (currentQuestion == null) {
		response.sendRedirect("404.jsp");
		return;
	}
%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/question.css" />
<script src="lib/jquery-2.1.3.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- For iPhone to display normally -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>识乎 - 提问</title>
</head>
<body>
	<jsp:include page="navigator.jsp" />
	<div id="content">
		<div id="contentWrapper">
			<div id="leftColumn">
				<div class="columnDiv">
					<div id="questionIDDiv" class="hidden"><%=currentQuestion.getQuestionID()%></div>
					<div id="questionTitle"><%=currentQuestion.getTitle()%></div>
					<div id="questionMetadata">
						<span class="userName"><a
							href="profile.jsp?id=<%=currentQuestion.getUserID()%>"><%=currentQuestion.getUsername()%></a></span><span>提问于</span><span><%=currentQuestion.getTime()%></span>
					</div>
					<%
						String questionContent = currentQuestion.getContent();
						if (questionContent != null) {
					%>
					<div>
						<%=questionContent%>
					</div>
					<%
						}
					%>
				</div>
				<div class="columnDiv" id="getMoreAnswersDiv">
					<button id="getMoreAnswersButton">显示更多</button>
				</div>
				<div class="columnDiv" id="newAnswerDiv">
					<%
						if (user != null) {
					%>
					<form id="newAnswerForm" method="POST"
						action="FileUploadServlet?action=addAnswerWithImage"
						enctype="multipart/form-data">
						<textarea id="newAnswerContent" name="newAnswerContent"
							placeholder="我来告诉你们人生的经验……"></textarea>
						<div id="resetFileDiv" class="hidden">
							<input type="file" id="file" name="file" />
						</div>
						<input type="text" id="questionID" class="hidden"
							name="questionID" />
					</form>
					<button id="uploadImgButton" class="standardButton">
						<img src="img/icon/attachment.png" class="icon" />
					</button>
					<button class="standardButton" id="sendAnswerButton">发布回答</button>
					<div class="clear"></div>
					<%
						} else {
					%>
					<div class="errorMessageDiv">登录后才可以发表回答</div>
					<%
						}
					%>
				</div>
			</div>
			<jsp:include page="popularQuestionAndUser.jsp" />
			<div class="clear"></div>
		</div>
	</div>
	<script type="text/javascript" src="js/question.js"></script>
</body>
</html>