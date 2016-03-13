$(function setTabAction() {
	var $tab = $(".tab");
	var $tabPane = $(".tabPane");
	$tab.click(function() {
		for (var i = 0; i < $tab.length; i++) {
			if (this == $tab[i]) {
				$tab.attr("class", "tab");
				$(this).attr("class", "tab active");
				$tabPane.attr("class", "tabPane");
				$tabPane.eq(i).attr("class", "tabPane active");
			}
		}
	});
});

$("#savePasswordButton").click(function() {
	$.ajax({
		type : 'POST',
		url : 'SettingsServlet',
		data : {
			action : 'modifyPassword',
			oldPassword : $("#oldPassword").val(),
			newPassword : $("#newPassword").val(),
			newPasswordRepeat : $("#newPasswordRepeat").val()
		},
		success : function(data) {
			$("#savePasswordResponseMessageDiv").html(data);
			if (data == '修改密码成功！') {
				$("#oldPassword").val("");
				$("#newPassword").val("");
				$("#newPasswordRepeat").val("");
			}
		},
		error : function() {
			alert("Connection error!");
		}
	});
});

$("#saveMottoButton").click(function() {
	var motto = $("#motto").val();
	if (motto == "") {
		$("#saveMottoResponseMessageDiv").html("请输入个人简介！");
		return;
	}
	$.ajax({
		type : 'POST',
		url : 'SettingsServlet',
		data : {
			action : 'modifyMotto',
			motto : motto
		},
		success : function(data) {
			if (data == "true") {
				$("#saveMottoResponseMessageDiv").html("修改成功！");
				$("#motto").attr('placeholder', motto);
				$("#motto").val("");
			} else {
				$("#saveMottoResponseMessageDiv").html("服务器错误");
			}
		},
		error : function() {
			alert("Connection error!");
		}
	});
});

$("#saveAvatarButton").click(function() {
	var file = document.getElementById("file").files[0];
	if (file == undefined) {
		alert("你还没有选择新的头像，请重新选择！");
	} else {
		document.getElementById("uploadAvatarForm").submit();
	}
});

function setFileTypeValidator() {
	$("#file").on('change', function() {
		var file = document.getElementById("file").files[0];
		if (!file.type.match('image.*')) {
			alert("你只能选择图片文件，请重新选择！");
			resetFileInput();
			return;
		}
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#view").html('<img src="' + e.target.result + '"/>');
		};
		reader.readAsDataURL(file);
	});
}

function resetFileInput() {
	$("#uploadAvatarForm").html('<input type="file" id="file" name="file">');
	$("#view").html("");
	setFileTypeValidator();
}

$("#selectImgButton").bind('click', function() {
	$("#file").trigger('click');
});

setFileTypeValidator();
