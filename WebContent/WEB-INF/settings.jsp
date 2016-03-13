<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bean.*"%>
<%
	User user = (User) request.getSession().getAttribute("user");
	if (user == null) {
		response.sendRedirect("login.jsp");
		return;
	}
	String saveAvatarResponseMessage = (String) request.getSession()
			.getAttribute("saveAvatarResponseMessage");
	request.getSession().removeAttribute("saveAvatarResponseMessage");
%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="css/base.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<link type="text/css" rel="stylesheet" href="css/tabBar.css" />
<link type="text/css" rel="stylesheet" href="css/settings.css" />
<script src="lib/jquery-2.1.3.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>识乎 - 设置</title>
</head>
<body>
	<jsp:include page="navigator.jsp" />
	<div id="content">
		<div id="contentWrapper">
			<div id="tabBar" class="columnDiv">
				<div class="tab active">头像</div>
				<div class="tab">个人简介</div>
				<div class="tab">密码</div>
			</div>
			<div id="settingsDiv">
				<div class="tabPane active">
					<div id="setAvatarDiv" class="columnDiv">
						<div id="saveAvatarResponseMessageDiv"><%=(saveAvatarResponseMessage != null) ? saveAvatarResponseMessage
					: ""%></div>
						<div>
							<div id="oldAvatarDiv">
								<div class="avatarDivWrapper">
									<div id="oldAvatarContainer">
										<img id="oldAvatar" src="img/avatar/<%=user.getAvatarPath()%>" />
									</div>
									<div>现在的头像</div>
								</div>
							</div>
							<div id="newAvatarDiv">
								<div class="avatarDivWrapper">
									<div id="view"></div>
									<div>新的头像</div>
									<div>
										<button id="selectImgButton" class="standardButton">
											<img src="img/icon/attachment.png" class="icon" />
										</button>
										<button id="saveAvatarButton" class="standardButton">保存</button>
									</div>
									<form id="uploadAvatarForm" class="hidden" method="POST"
										action="FileUploadServlet?action=uploadUserAvatar"
										enctype="multipart/form-data">
										<input type="file" id="file" name="file">
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tabPane">
					<div id="setMottoDiv" class="columnDiv">
						<div>
							<input class="standardInput" type="text" id="motto"
								placeholder="<%=user.getMotto()%>" />
						</div>
						<div id="saveMottoResponseMessageDiv"></div>
						<div>
							<button class="standardButton" id="saveMottoButton">保存</button>
						</div>
					</div>
				</div>
				<div class="tabPane">
					<div id="setPasswordDiv" class="columnDiv">
						<div>
							<label for="oldPassword">当前密码</label> <input
								class="standardInput" type="password" id="oldPassword" />
						</div>
						<div>
							<label for="newPassword">新的密码</label> <input
								class="standardInput" type="password" id="newPassword" />
						</div>
						<div>
							<label for="newPasswordRepeat">确认密码</label> <input
								class="standardInput" type="password" id="newPasswordRepeat" />
						</div>
						<div id="savePasswordResponseMessageDiv"></div>
						<div>
							<button class="standardButton" id="savePasswordButton">保存</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/settings.js"></script>
</body>
</html>