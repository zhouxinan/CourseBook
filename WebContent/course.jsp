<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="dao.*"%>
<%@ page import="java.util.*"%>
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
	Course currentCourse = null;
	try {
		currentCourse = dao.getCourseByID(Integer.parseInt(id));
	} catch (NumberFormatException e) {
		response.sendRedirect("404.jsp");
		return;
	}
	if (currentCourse == null) {
		response.sendRedirect("404.jsp");
		return;
	}
	ArrayList<Integer> sectionList = currentCourse.getSectionList();
%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/question.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- For iPhone to display normally -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>课谱 - 评课</title>
</head>
<body>
	<jsp:include page="navigator.jsp" />
	<div id="content">
		<div id="contentWrapper">
			<div id="leftColumn">
				<div class="columnDiv">
					<div id="questionIDDiv" class="hidden"><%=currentCourse.getCourseID()%></div>
					<div id="questionTitle"><%=currentCourse.getCourseSN() + " " + currentCourse.getCourseName()%></div>
					<button id="followButton"></button>
					<div class="clear"></div>
					<div id="questionMetadata">
						<span class="userName"><span>开课老师：</span><a
							href="profile.jsp?id=<%=currentCourse.getTeacherID()%>"><%=currentCourse.getTeacherName()%></a></span><span>学分：</span><span><%=currentCourse.getCourseCredit()%></span><span>用户平均评分：
							<%
							double rate = currentCourse.getRate();
							out.print((rate == 0) ? "暂无" : rate);
						%>
						</span>
					</div>
				</div>
				<div class="columnDiv">
					<%
						for (int i = 0; i < sectionList.size(); i++) {
							out.println("<div class=\"section\">" + sectionList.get(i) + "</div>");
						}
					%>
				</div>
				<script type="text/javascript" src="js/section.js"></script>
				<div id="chartContainer" class="columnDiv"></div>
				<script type="text/javascript" src="lib/highcharts.js"></script>
				<script type="text/javascript" src="lib/exporting.js"></script>
				<script type="text/javascript" src="js/chart.js"></script>
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
						<div id="rateDiv">
							<input type="radio" name="rate" value="1" />1分 <input
								type="radio" name="rate" value="2" />2分 <input type="radio"
								name="rate" value="3" />3分 <input type="radio" name="rate"
								value="4" />4分 <input type="radio" name="rate" value="5" />5分
						</div>
						<textarea id="newAnswerContent" name="newAnswerContent"
							placeholder="我来告诉大家这门课怎么样"></textarea>
						<div id="resetFileDiv" class="hidden">
							<input type="file" id="file" name="file" />
						</div>
						<input type="text" id="questionID" class="hidden"
							name="questionID" />
					</form>
					<button id="uploadImgButton" class="standardButton">
						<img src="img/icon/attachment.png" class="icon" />
					</button>
					<button class="standardButton" id="sendAnswerButton">发布课评</button>
					<div class="clear"></div>
					<%
						} else {
					%>
					<div class="errorMessageDiv">登录后才可以发表课评</div>
					<%
						}
					%>
				</div>
			</div>
			<jsp:include page="sidebar.jsp" />
			<div class="clear"></div>
		</div>
	</div>
	<script type="text/javascript" src="js/course.js"></script>
</body>
</html>