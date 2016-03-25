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
	double[] analysisData = dao.getAnalysisData(currentCourse.getCourseID());
	double[] scoreData = dao.getScore(currentCourse.getCourseID());
%>
<html>
<head>
<link href="css/bootstrap.min.css" rel="stylesheet">
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
					<%
						if (user != null) {
					%>
					<button id="followButton"></button>
					<%
						}
					%>
					<div class="clear"></div>
					<div id="questionMetadata">
						<span class="userName"><span>开课老师：</span><a
							href="profile.jsp?id=<%=currentCourse.getTeacherID()%>"><%=currentCourse.getTeacherName()%></a></span><span>学分：</span><span><%=currentCourse.getCourseCredit()%></span><span>学生平均评分：
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
							int sectionID = sectionList.get(i);
					%>
					<div class="section"><%=sectionID%></div>
					<div class="progress">
						<%
							JSONObject gradeList = dao.getSectionGrade(currentCourse, sectionID);
								int count = gradeList.getInt("count");
								for (int j = 0; j < gradeList.names().length(); j++) {
									String color = null;
									String grade = null;
									switch (gradeList.names().getString(j)) {
									case "count":
										continue;
									case "4.00":
										color = "#e31e50";
										grade = "A";
										break;
									case "3.70":
										color = "#e5781d";
										grade = "A-";
										break;
									case "3.30":
										color = "#e3e51d";
										grade = "B+";
										break;
									case "3.00":
										color = "#7ee51d";
										grade = "B";
										break;
									case "2.70":
										color = "#1daee5";
										grade = "B-";
										break;
									case "2.30":
										color = "#d2aa2c";
										grade = "C+";
										break;
									case "2.00":
										color = "#55601b";
										grade = "C";
										break;
									case "1.70":
										color = "#007d81";
										grade = "C-";
										break;
									case "1.30":
										color = "#073b5a";
										grade = "D";
										break;
									case "1.00":
										color = "#330017";
										grade = "D-";
										break;
									}
						%>
						<div class="progress-bar"
							style=<%="width:" + gradeList.getDouble(gradeList.names().getString(j)) * 100 / count
							+ "%;background-color:" + color%>><%=grade%></div>
						<%
							}
						%>
					</div>
					<%
						}
					%>
				</div>
				<script type="text/javascript" src="js/section.js"></script>
				<script type="text/javascript" src="lib/Chart.js"></script>
				<div id="chartContainer" class="columnDiv">
					<div id="answerRateChart"></div>
					<div id="analysisChartDiv">
						<canvas id="analysisChart"></canvas>
					</div>
					<table id="scoreTable" class="table">
					<thead>
						<tr>
							<th>本课程综合评分</th>
							<th>所有课程平均综合评分</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><%=scoreData[0] %></td>
							<td><%=scoreData[1] %></td>
						</tr>
					</tbody>
					</table>
					<script>
					var ctx = $("#analysisChart");
					var data = {
							labels: ["学生平均成绩", "学生评分", "给分靠谱度"],
							datasets: [
								{
									label: "本课程",
									backgroundColor: "rgba(138,189,95,0.2)",
									borderColor: "rgba(138,189,95,1)",
									pointBackgroundColor: "rgba(138,189,95,1)",
									pointBorderColor: "#fff",
									pointHoverBackgroundColor: "#fff",
									pointHoverBorderColor: "rgba(138,189,95,1)",
									data: [<%=analysisData[0]%>,<%=analysisData[1]%>,<%=analysisData[2]%>]
								},
								{
									label: "所有课程平均值",
									backgroundColor: "rgba(151,187,205,0.2)",
									borderColor: "rgba(151,187,205,1)",
									pointBackgroundColor: "rgba(151,187,205,1)",
									pointBorderColor: "#fff",
									pointHoverBackgroundColor: "#fff",
									pointHoverBorderColor: "rgba(151,187,205,1)",
									data: [<%=analysisData[6]%>,<%=analysisData[7]%>,<%=analysisData[8]%>]
								}
							]
						};
					var myRadarChart = new Chart(ctx, {
						type: 'radar',
						data: data,
						options: {
				            legend: {
				                position: 'top',
				            },
				            title: {
				                display: true,
				                text: '课程统计数据'
				            },
				            scale: {
				              reverse: false,
				              ticks: {
				                beginAtZero: true
				              }
				            }
				        }
					});
					</script>
				</div>
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
	<script src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/course.js"></script>
</body>
</html>