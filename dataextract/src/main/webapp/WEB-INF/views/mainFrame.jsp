<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>临沂市骨干企业信息直报系统</title>
<%@ include file="/common/easyui_inc.jsp"%>
<script type="text/javascript"
	src="${ctx}/static/js/extAppJs/app_index.js"></script>
<script type="text/javascript"
	src="${ctx }/static/js/extAppJs/common.js"></script>
	
</head>
<body>

	<div id="index_layout">
		<div
			data-options="region:'north',border:false,href:'${ctx }/layouts/header.jsp'"
			style="height: 61px;"></div>

		<div
			data-options="iconCls:'icon-application_view_tile',region:'west',title:'导航菜单',split:true,href:'${ctx }/layouts/left.jsp'"
			style="width: 15%; background-color: white;float:left;"></div>

		<div data-options="iconCls:'icon-calendar',region:'east'" title="日历"
			style="width: 15%; overflow: hidden;">
			<div class="easyui-panel" data-options="border:false"
				style="height: 180px; overflow: hidden;">
				<div id="index_calendar" style="height: 180px; overflow: hidden;"></div>
			</div>
		</div>
		<div data-options="iconCls:'icon-logo',region:'center'"
			title="拓普网络-引领科技现代生活！" style="overflow: hidden;">
			<div id="index_tabs" style="overflow: hidden;">
			<div title="拓普" data-options="href:'${ctx }/home/coverpage',iconCls:'icon-house'"></div></div>
		</div>
		
		
		<div data-options="iconCls:'icon-logo',region:'center'"
			title="拓普网络-引领科技现代生活！" style="overflow: hidden;">
			<div id="index_tabs" style="overflow: hidden;"></div>
		</div>

		<div data-options="region:'south',border:true"
			style="height: 3%; text-align: center;">
			<span style="font-weight: bold;">技术支持：</span><span><a
				href="http://www.iamtop.com" target="_blank">临沂市拓普网络股份有限公司</a></span> <span
				style="font-weight: bold;">咨询热线：</span><span>400-006-7996,800-860-7996</span>
		</div>
	</div>
</body>
</html>