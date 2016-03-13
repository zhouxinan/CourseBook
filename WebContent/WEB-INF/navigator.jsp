<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bean.*"%>
<%
	String username = "登陆";
	String avatarPath = "default.jpg";
	User user = (User) request.getSession().getAttribute("user");
	if (user != null) {
		username = user.getUsername();
		avatarPath = user.getAvatarPath();
	}
%>

<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/navigator.css" />
<div id="navigator">
	<div id="navigatorWrapper">
		<div id="logo">
			<a href="index.jsp">识乎</a>
		</div>
		<div id="searchDiv">
			<input type="text" id="searchInput" placeholder="搜索问题或用户" />
			<button type="submit" id="searchButton">搜索</button>
		</div>
		<%
			if (user != null) {
		%>
		<div>
			<button id="newQuestionButton" class="standardButton">提问题</button>
		</div>
		<%
			}
		%>
		<div id="menuDiv">
			<div class="changeHoverBackground clickbox">
				<a href="index.jsp">首页</a>
			</div>
			<div class="changeHoverBackground clickbox">
				<a href="#">话题</a>
			</div>
			<div class="changeHoverBackground clickbox">
				<a href="discovery.jsp">发现</a>
			</div>
		</div>
		<div id="userMenuDiv" class="changeHoverBackground  clickbox">
			<div id="myAvatarAndNameDiv">
				<img id="myAvatar" src="img/avatar/<%=avatarPath%>" />
				<%
					if (user != null) {
				%>
				<div class="notificationBubble"></div>
				<%
					}
				%>
				<div id="myName">
					<a href="profile.jsp"><%=username%></a>
				</div>
			</div>
			<%
				if (user != null) {
			%>
			<div id="userMenu">
				<div class="clickbox">
					<img src="img/icon/user.png" class="icon" /><a href="profile.jsp">我的主页</a>
				</div>
				<div class="clickbox">
					<img src="img/icon/envelop.png" class="icon" /><a
						href="messages.jsp">私信</a>
					<div class="notificationBubble" id="userMenuNotificationBubble"></div>
				</div>
				<div class="clickbox">
					<img src="img/icon/cog.png" class="icon" /><a href="settings.jsp">设置</a>
				</div>
				<div class="clickbox">
					<img src="img/icon/exit.png" class="icon" /><a
						href="LoginServlet?action=logout">退出</a>
				</div>
			</div>
			<%
				}
			%>
		</div>
	</div>
</div>
<div class="modalDialogBackground hidden" id="newQuestionDialogBackground"></div>
<div class="modalWrapper hidden" id="newQuestionWrapper">
	<div class="modalDialog">
		<div class="modalDialogTitle">
			<span class="modalDialogTitleText">提问</span><span
				class="modalDialogTitleClose"></span>
		</div>
		<div class="modalDialogContent">
			<div>
				<b>问题标题（必填）</b>
			</div>
			<div>
				<input type="text" class="standardInput" id="newQuestionTitle"
					placeholder="请输入问题标题">
			</div>
			<div>
				<b>问题描述</b>
			</div>
			<div>
				<textarea id="newQuestionContent"
					placeholder="请输入问题描述"></textarea>
			</div>
			<div>
				<button id="submitNewQuestionButton" class="standardButton">提交</button>
				<div class="clear"></div>
			</div>
			<form id="newQuestionForm" method="POST"
				action="QuestionServlet?action=addQuestion" class="hidden">
				<input type="text" name="title" id="newQuestionTitleChecked">
				<input type="text" name="content" id="newQuestionContentChecked">
			</form>
		</div>
	</div>
</div>
<script src="lib/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="js/navigator.js"></script>