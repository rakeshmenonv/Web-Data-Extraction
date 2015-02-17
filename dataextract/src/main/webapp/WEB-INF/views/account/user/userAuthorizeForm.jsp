<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#roleIds').combotree(
				{
					url : '${ctx}/account/role/tree',
					parentField : 'pid',
					lines : true,
					panelHeight : 'auto',
					required : true,
					idField : 'id',
					multiple : true,
					onLoadSuccess : function() {
						$('#roleIds').combotree('setValues',
								infotop.stringToList('${user.rids}'));
						parent.$.messager.progress('close');
					},
					cascadeCheck : false
				});

		$('#user_form_inputForm').form(
				{
					onSubmit : function() {
						parent.$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						var isValid = $(this).form('validate');
						if (!isValid) {
							parent.$.messager.progress('close');
						}
						return isValid;
					},
					success : function(result) {
						parent.$.messager.progress('close');
						result = $.parseJSON(result);
						if (result.success) {
							parent.$.modalDialog.openner_dataGrid
									.datagrid('reload');
							parent.$.modalDialog.openner_dataGrid.datagrid(
									'uncheckAll').datagrid('unselectAll')
									.datagrid('clearSelections');
							parent.$.modalDialog.handler.dialog('close');
							$.messager.show({ // show error message
								title : '提示',
								msg : result.message,
								timeout : 800,
								showType : 'fade',
								style : {
									right : '',
									top : $(window).height() / 2,
									bottom : ''
								}
							});
						} else {
							$.messager.alert('错误', result.message, 'error');
						}
					}
				});

	});
</script>

<form id="user_form_inputForm" method="post"
	action="${ctx}/account/user/authorize">
	<table width="100%" border="0" class="content" cellpadding="0"
		cellspacing="0">
		<tr>
			<td class="biao_bt3" align="center"><spring:message
					code="account_loginName" /></td>
			<td valign="middle"><input type="hidden" name="id" id="id"
				value="${user.id}" /><input type="text" name="loginName"
				id="loginName" value="${user.loginName }" disabled="disabled" /></td>
		</tr>
		<tr>
			<td class="biao_bt3" align="center"><spring:message
					code="account_name" /></td>
			<td valign="middle"><input type="text" name="name" id="name"
				value="${user.name }" disabled="disabled" /></td>
		</tr>
		<tr>
			<td class="biao_bt3" align="center">角色:</td>
			<td valign="middle"><select id="roleIds"
				name="roleIds" style="width: 350%; height: 29px;"></select><img
				src="${ctx}/static/jquery-easyui-1.3.4/themes/icons/no.png"
				onclick="$('#roleIds').combotree('clear');" /></td>
		</tr>
	</table>
</form>
