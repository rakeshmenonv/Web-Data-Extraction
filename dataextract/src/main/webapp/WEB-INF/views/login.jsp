<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page
	import="com.infotop.system.account.service.CaptchaFormAuthenticationFilter"%>
<%@ page
	import="com.infotop.system.account.service.IncoreectCaptchaException"%>
<%@ page import="org.apache.shiro.authc.LockedAccountException "%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>

<head>
<title>临沂市拓普网络股份有限公出品</title>
<%@ include file="/common/meta.jsp"%>

<link rel="stylesheet" type="text/css"
	href="${ctx }/static/styles/login/login.css" />
<!-- jQuery -->
<script src="${ctx}/static/js/jquery-1.9.1.min.js"></script>

<!-- easyUI reference js files-->
<script type="text/javascript"
	src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
	
<script type="text/javascript"
	src="${ctx}/static/js/extAppJs/extEasyUI.js"></script>
	
<!-- jQuery validation engine for form validation with tooltip -->
<link rel="stylesheet"
	href="${ctx}/static/js/plugins/jQuery-Validation-Engine/validationEngine.css"
	type="text/css" />
<script type="text/javascript"
	src="${ctx}/static/js/plugins/jQuery-Validation-Engine/jquery.validationEngine.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="${ctx}/static/js/plugins/jQuery-Validation-Engine/languages/jquery.validationEngine-zh_CN.js"
	charset="utf-8"></script>

</head>

<body>
	<script>
	$(document).ready(function() {
		parent.$.messager.progress('close');
		 if(window.parent.length>0){ 
				top.location.href = self.location.href;
		 }
		var Sys = {};
		var ua = navigator.userAgent.toLowerCase();
		var s;
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : (s = ua
				.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : (s = ua
				.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : (s = ua
				.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : (s = ua
				.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

		//以下进行测试
		if (Sys.ie == '6.0'|| Sys.ie == '7.0') {
			$("#loginForm").hide();
			$("#browsercheck").show();

		}
		else{
			$("#loginForm").show();
		}
		$("#loginForm").validationEngine();
	<c:choose>
	<c:when test="${fn:contains(shiroLoginFailure,'UnknownAccountException')}">
			$("#loginForm").validationEngine("showPrompt","该用户不存在","error");
		</c:when>
		<c:when test="${fn:contains(shiroLoginFailure,'IncorrectCredentialsException')}">
			$("#loginForm").validationEngine("showPrompt","用户名或密码输入错误，请重试！","error");
		</c:when>
		<c:when test="${fn:contains(shiroLoginFailure,'IncoreectCaptchaException')}">
			$("#loginForm").validationEngine("showPrompt","验证码错误，请重试！","error");
		</c:when>
		<c:when test="${shiroLoginFailure ne null}">
			$("#loginForm").validationEngine("showPrompt","登录认证错误，请重试！","error");
		</c:when>
	</c:choose>
	});
</script>
	<div class="login">
		<div id="login0">
			<div class="loginmiddle">
				<div class="logintitile">临沂市骨干企业信息直报系统</div>
				<br />
				<div id="browsercheck"
					style="color: red; display: none; text-align: center;">
					请使用IE浏览器或谷歌浏览器，并保证其版本为8.0或更高！</div>
				<form id="loginForm" name="loginForm" action="${ctx}/login"
					method="post" data-prompt-position="bottomRight:-230,50">
					<input type="hidden" id="systype" name="systype" value="1" />
					<div class="loginform">
						<b class="userimg"></b><input type="text" name="username"
							id="username" class="in validate[required]"
							data-prompt-position="bottomRight:-220,10" />
					</div>
					<div class="loginform">
						<b class="passwordimg"></b><input type="password" id="password"
							name="password" class="in validate[required]"
							data-prompt-position="bottomRight:-220,10" />
					</div>
					<div class="loginform">
						<b class="checkimg"></b><input type="text" name="captcha"
							class="in validate[required]"
							data-prompt-position="bottomRight:-220,10" />
					</div>
					<input class="loginin" type="submit" value="登　　　录" />
					<div
						style="position: relative; width: 65px; height: 30px; top: -98px; right: -245px; border: #000 1px solid;">
						<img src="${ctx }/static/kaptcha.jpg" width="65" height="30"
							alt="" id="kaptchaImage" style="cursor: pointer;" />
						<script type="text/javascript">
							    $(function(){
							        $('#kaptchaImage').click(function () {
							        	$(this).hide().attr('src', '${ctx }/static/kaptcha.jpg?' + Math.floor(Math.random()*100) ).fadeIn(); 
							        	});
							    });
							</script>
					</div>
				</form>

			</div>
			<div id="foots"
				style="height: 20px; text-align: center; position: relative; top: 280px;">
				<span style="font-weight: bold;">技术支持：</span><span><a
					href="http://www.iamtop.com" target="_blank">临沂市拓普网络股份有限公司</a></span> <span
					style="font-weight: bold;">咨询热线：</span><span>400-006-7996,800-860-7996</span>
			</div>
		</div>
	</div>

</body>
</html>