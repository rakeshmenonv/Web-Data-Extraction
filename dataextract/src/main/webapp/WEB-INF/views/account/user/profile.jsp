<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
	$(function() {
		parent.$.messager.progress('close');
		$('#profile_inputForm').form({
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
</head>
<form id="profile_inputForm" method="post" action="${ctx}/profile">
	<input type="hidden" name="id" value="${user.id}" />
	<table width="100%" border="0" class="content" cellpadding="0"
		cellspacing="0">
		<tr>
			<td  class="biao_bt3" align="center"><spring:message
					code="account_loginName" /></td>
			<td width="30%" valign="middle">${user.loginName}</td>
		</tr>
		<tr>
			<td class="biao_bt3" align="center"><spring:message
					code="account_name" /></td>
			<td width="30%" valign="middle">${user.name}</td>
		</tr>
		<tr>
			<td class="biao_bt3" align="center"><spring:message
					code="account_password" /></td>
			<td width="30%" valign="middle"><input type="password" id="plainPassword"
				name="plainPassword" class="easyui-validatebox"
				data-options="missingMessage:'密码不能为空.',required:true,validType:'safepass'" /></td>
		</tr>
		<tr>
			<td class="biao_bt3" align="center"><spring:message
					code="account_reTypePassword" /></td>
			<td width="30%" valign="middle"><input type="password" id="confirmPassword"
				name="confirmPassword" validType="equalTo['plainPassword']"
				data-options="missingMessage:'<spring:message
					code="account_reTypePassword" />不能为空.',required:true"
				class="easyui-validatebox" /></td>
		</tr>
	</table>
</form>