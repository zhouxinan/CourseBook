<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bean.*"%>
<%@ page import="dao.*"%>
<%@ page import="java.util.List"%>
<%
	String keyword = request.getParameter("keyword");
	Dao dao = Dao.getInstance();
	List<Course> courseList = dao.searchCourseByKeyword(keyword);
	List<User> userList = dao.searchUsernameByKeyword(keyword);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/tabBar.css" />
<link type="text/css" rel="stylesheet" href="css/search.css" />
<title>课谱 - 搜索</title>
</head>
<body>
	<jsp:include page="navigator.jsp" />
	<div id="content">
		<div id="contentWrapper">
			<div id="leftColumn">
				<div id="tabBar" class="columnDiv">
					<div class="tab active">课程</div>
					<div class="tab">用户</div>
				</div>
				<div>
					<div class="tabPane active">
						<%
							if (courseList.size() == 0) {
						%>
						<div class="columnDiv">没有找到符合条件的课程。</div>
						<%
							} else {
								for (Course course : courseList) {
						%>
						<div class="columnDiv">
							<div class="questionTitleDiv">
								<a href="course.jsp?id=<%=course.getCourseID()%>"><%=course.getCourseName()%></a>
							</div>
						</div>
						<%
							}
							}
						%>

					</div>
					<div class="tabPane">
						<%
							if (userList.size() == 0) {
						%>
						<div class="columnDiv">没有找到符合条件的用户。</div>
						<%
							} else {
								for (User user : userList) {
						%>
						<div class="columnDiv">
							<div>
								<a href="profile.jsp?id=<%=user.getUserID()%>"> <img
									src="img/avatar/<%=user.getAvatarPath()%>" class="userAvatar" />
								</a>
							</div>
							<div>
								<div class="userName">
									<a href="profile.jsp?id=<%=user.getUserID()%>" class="userName"><%=user.getUsername()%></a>
								</div>
								<div class="userSignature"><%=user.getMotto()%></div>
							</div>
						</div>
						<%
							}
							}
						%>
					</div>
				</div>
			</div>
			<jsp:include page="sidebar.jsp" />
		</div>
	</div>
	<script type="text/javascript" src="js/search.js"></script>
</body>
</html>