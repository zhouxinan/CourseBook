<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bean.*"%>
<%@ page import="dao.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.json.*"%>
<%
	User user = (User) request.getSession().getAttribute("user");
	if (user == null) {
		response.sendRedirect("login.jsp");
		return;
	}
	Dao dao = Dao.getInstance();
	List<JSONObject> discoveryEntryList = dao
			.getDiscoveryEntryListForUser();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/index.css" />
<title>识乎 - 发现</title>
</head>
<body>
	<jsp:include page="navigator.jsp" />
	<div id="content">
		<div id="contentWrapper">
			<div id="leftColumn">
				<%
					for (JSONObject obj : discoveryEntryList) {
				%>
				<div class="columnDiv">
					<%
						if (obj.get("hasAnswer").equals("0")) {
					%>
					<div>
						<a href="profile.jsp?id=<%=obj.get("userID")%>"><img
							src="img/avatar/<%=obj.get("avatarPath")%>" class="userAvatar" /></a>
					</div>
					<div class="trendEntry">
						<div class="questionDiv">
							<a href="profile.jsp?id=<%=obj.get("userID")%>" class="userName"><%=obj.get("username")%></a>
							提问了 <a href="question.jsp?id=<%=obj.get("questionID")%>"><%=obj.get("questionTitle")%></a>
						</div>
						<div class="replyTime"><%=obj.get("time")%></div>
					</div>
					<%
						} else {
					%>
					<div>
						<a href="profile.jsp?id=<%=obj.get("userID")%>"><img
							src="img/avatar/<%=obj.get("avatarPath")%>" class="userAvatar" /></a>
					</div>
					<div class="trendEntry">
						<div class="questionDiv">
							<a href="profile.jsp?id=<%=obj.get("userID")%>" class="userName"><%=obj.get("username")%></a>
							回答了 <a href="question.jsp?id=<%=obj.get("questionID")%>"><%=obj.get("questionTitle")%></a>
						</div>
						<div class="answerContent"><%=obj.get("content")%></div>
						<div class="replyTime"><%=obj.get("time")%></div>
					</div>
					<%
						}
					%>

				</div>
				<%
					}
				%>
			</div>
			<jsp:include page="popularQuestionAndUser.jsp" />
		</div>
	</div>
</body>
</html>