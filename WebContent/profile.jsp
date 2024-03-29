<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="bean.*"%>
<%@ page import="dao.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.json.*"%>
<%
	User user = (User) request.getSession().getAttribute("user");
	String id = request.getParameter("id");
	User currentUser = null;
	if (user == null) { // The visitor has not logged in.
		if (id == null) {
			response.sendRedirect("login.jsp");
			return;
		} else {
			currentUser = Dao.getInstance().getUserByID(id);
		}
	} else if (id != null) {
		currentUser = Dao.getInstance().getUserByID(id);
	} else {
		currentUser = user;
	}
	if (currentUser == null) { // It will only happen if id is invalid.
		response.sendRedirect("404.jsp");
		return;
	}
	Dao dao = Dao.getInstance();
	List<Course> courseList = dao.getCourseListByTeacherID(currentUser.getUserID());
	List<JSONObject> answerList = dao.getAnswerListByUserID(currentUser.getUserID());
%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/profile.css" />
<link type="text/css" rel="stylesheet" href="css/tabBar.css" />
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="lib/jquery-2.2.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- For iPhone to display normally -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>课谱 - 个人资料</title>
</head>
<body>
	<jsp:include page="navigator.jsp" />
	<div id="content">
		<div id="contentWrapper">
			<div id="leftColumn">
				<div id="tabBar" class="columnDiv">
					<div class="tab active">所开课程</div>
					<div class="tab">课评</div>
				</div>
				<div id="myQuestionAndAnswerDiv">
					<div id="myQuestionDiv" class="tabPane active">
						<%
							for (Course course : courseList) {
						%>
						<div class="columnDiv">
							<div>
								<a href="course.jsp?id=<%=course.getCourseID()%>"><%=course.getCourseSN() + " " + course.getCourseName() + "（" + course.getTeacherName() + "）"%></a>
							</div>
						</div>
						<%
							}
						%>
					</div>
					<div id="myAnswerDiv" class="tabPane">
						<%
							for (JSONObject obj : answerList) {
						%>
						<div class="columnDiv">
							<div>
								<a href="course.jsp?id=<%=obj.get("courseID")%>"><%=obj.get("courseSN") + " " + obj.get("courseName") + "（" + obj.get("teacherName") + "）"%></a>
							</div>
							<div class="replyContent"><%=obj.get("content")%></div>
							<div class="replyTime"><%=obj.get("answerTime")%></div>
						</div>

						<%
							}
						%>
					</div>
				</div>
			</div>
			<div id="rightColumn">
				<div class="columnDiv" id="myBigAvatarDiv">
					<img id="myBigAvatar"
						src="img/avatar/<%=currentUser.getAvatarPath()%>" />
				</div>
				<%
					if (user != null && id != null && !id.equals("" + user.getUserID())) {
				%>
				<div class="columnDiv">
					<button id="followButton">关注</button>
				</div>
				<%
					}
				%>
				<div class="columnDiv" id="myInfoDiv">
					<div id="userIDDiv"><%=(id != null) ? id : user.getUserID()%></div>
					<div class="myInfoRowDiv">
						<img src="img/icon/user_green.png" class="icon" />
						<div><%=currentUser.getUsername()%></div>
					</div>
					<div class="myInfoRowDiv">
						<img src="img/icon/signature.png" class="icon" />
						<div><%=currentUser.getMotto()%></div>
					</div>
					<div class="clear"></div>
				</div>
				<div class="columnDiv" id="followerDiv">
					<div>
						<div class="bigNumber">
							<span id="followerCount"></span>
						</div>
						<div class="followerDivText">
							<span>Follower</span>
						</div>
					</div>
					<div>
						<div class="bigNumber">
							<span id="followingCount"></span>
						</div>
						<div class="followerDivText">
							<span>Followed</span>
						</div>
					</div>
				</div>
				<div class="columnDiv">
					<div class="rightColumnTitle">最近关注了</div>
					<div class="avatarList" id="followingListDiv"></div>
					<div class="clear"></div>
				</div>
				<div class="columnDiv">
					<div class="rightColumnTitle">最近被他们关注</div>
					<div class="avatarList" id="followerListDiv"></div>
					<div class="clear"></div>
				</div>
				<div class="columnDiv">
					<div id="author">© 2016 课谱</div>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<script src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/profile.js"></script>
</body>
</html>