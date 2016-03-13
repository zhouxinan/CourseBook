<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bean.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.json.*"%>
<%@ page import="dao.*"%>
<%
	Dao dao = Dao.getInstance();
	List<JSONObject> popularUserList = dao.getPopularUserList(3);
	List<Question> popularQuestionList = dao.getPopularQuestionList(5);
%>
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/popularQuestionAndUser.css" />
<div id="rightColumn">
	<div class="columnDiv">
		<div class="rightColumnTitle">热门问题</div>
		<div id="popularQuestionList">
			<%
				for (Question question : popularQuestionList) {
			%>
			<div>
				<a href="question.jsp?id=<%=question.getQuestionID()%>"><%=question.getTitle()%></a>
			</div>
			<%
				}
			%>
		</div>
	</div>
	<div class="columnDiv">
		<div class="rightColumnTitle">热门用户</div>
		<div id="popularUserList">
			<%
				for (JSONObject obj : popularUserList) {
			%>
			<div class="popularUser">
				<a href="profile.jsp?id=<%=obj.get("userID")%>"> <img
					class="userAvatar" src="img/avatar/<%=obj.get("avatarPath")%>" />
				</a>
				<div class="userName">
					<a href="profile.jsp?id=<%=obj.get("userID")%>"><%=obj.get("username")%></a>
				</div>
				<div class="userSignature"><%=obj.get("motto")%></div>
			</div>
			<%
				}
			%>
			<div class="clear"></div>
		</div>
	</div>
	<div class="columnDiv">
		<div id="author">© 2015 Zhou Xinan</div>
	</div>
</div>