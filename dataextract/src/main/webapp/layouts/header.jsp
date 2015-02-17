<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	ShiroUser currentuser = (ShiroUser) SecurityUtils.getSubject()
			.getPrincipal();
%>
<div class="top">
	<div class="topleft">
		<img src="${ctx}/static/images/head.jpg" width="800" height="60" />
	</div>
	<div class="topright">
		<span style="color:#FFF;">欢迎您，<a
			style="color:#f00; font-weight:bold;" href="#"><%=currentuser.name%></a>&nbsp;&nbsp;
		</span> <span> <a onclick="changePWD('${ctx}/profile');"
			href="javascript:void(0)"> <img align="absmiddle"
				src="${ctx}/static/images/shezhi.png" height="23" width="24" alt="" />&nbsp;&nbsp;<spring:message
					code="Header_profile" />
		</a>
		</span> <span><img align="absmiddle"
			src="${ctx}/static/images/off.png" height="23" width="21" alt="" /></span>
		<span><a style=" font-weight: bold;" href="${ctx}/logout"><spring:message
					code="profile_logout" /></a></span> &nbsp;&nbsp;
		<!-- <span style="width:0%;" id="clock1"></span>  -->
	</div>
</div>

