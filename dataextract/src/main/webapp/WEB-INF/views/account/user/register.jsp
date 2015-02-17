<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$.parser.onComplete = function() {
		parent.$.messager.progress('close');
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
								msg : result.message
							});
						} else {
							$.messager.alert('错误', result.message, 'error');
						}
					}
				});
	};
</script>

<form id="user_form_inputForm" method="post" action="${ctx}/register">
	<input type="hidden" id="userType" name="userType"
		value="${user.userType }">
	<table style="width:100%;" class="content">
		<tr>
			<td class="biao_bt3"><spring:message code="account_loginName" /></td>
			<td><input type="text" name="loginName" id="loginName"
				class="easyui-validatebox"
				validType="remote['${ctx }/register/checkLoginName','loginName']"
				data-options="missingMessage:'登陆帐号不能为空.',invalidMessage:'登陆帐号已存在，请重新输入！',required:true" /></td>
		</tr>
		<tr>
			<td class="biao_bt3"><spring:message code="account_password" /></td>
			<td><input type="password" id="plainPassword"
				name="plainPassword" class="easyui-validatebox"
				data-options="missingMessage:'密码不能为空.',required:true" /></td>
		</tr>
		<tr>
			<td class="biao_bt3"><spring:message
					code="account_reTypePassword" /></td>
			<td><input type="password" id="confirmPassword"
				name="confirmPassword" validType="equalTo['plainPassword']"
				data-options="missingMessage:'<spring:message
					code="account_reTypePassword" />不能为空.',required:true"
				class="easyui-validatebox" /></td>
		</tr>
		<c:if test="${user.userType==0 }">
			<tr>
				<td class="biao_bt3"><spring:message code="account_name" /></td>
				<td><input type="text" name="name" id="name"
					class="easyui-validatebox"
					data-options="missingMessage:'名称不能为空.',required:true" /></td>
			</tr>
		</c:if>

		<c:if test="${user.userType==1 }">
			<tr>
				<td class="biao_bt3"><spring:message code="account_name" /></td>
				<td><input type="text" name="name" id="name"
					class="easyui-validatebox"
					data-options="missingMessage:'名称不能为空.',required:true" /></td>
			</tr>
			<tr>
				<td class="biao_bt3">所属帮扶组</td>
				<td><select id="groupId" name="groupId">
						<c:forEach items="${group }" var="obj">
							<option value="${obj.value }">${obj.name }</option>
						</c:forEach>
				</select></td>
			</tr>
		</c:if>
		<c:if test="${user.userType==2 }">
			<tr>
				<td class="biao_bt3"><spring:message code="account_name" /></td>
				<td><input type="text" name="name" id="name"
					class="easyui-validatebox"
					data-options="missingMessage:'名称不能为空.',required:true" /></td>
			</tr>
			<tr>
				<td class="biao_bt3">所属县区</td>
				<td><select id="area" name="area">
						<c:forEach items="${area }" var="obj">
							<option value="${obj.value }">${obj.name }</option>
						</c:forEach>
				</select></td>
			</tr>
		</c:if>
		<c:if test="${user.userType==3 }">
			<tr>
				<td class="biao_bt3">企业名称</td>
				<td><input type="text" name="name" id="name"
					class="easyui-validatebox"
					data-options="missingMessage:'企业名称不能为空.',required:true" /></td>
			</tr>
			<tr>
				<td class="biao_bt3">所属县区</td>
				<td><select id="area" name="area">
						<c:forEach items="${area }" var="obj">
							<option value="${obj.value }">${obj.name }</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td class="biao_bt3">所属帮扶组</td>
				<td><select id="groupId" name="groupId">
						<c:forEach items="${group }" var="obj">
							<option value="${obj.value }">${obj.name }</option>
						</c:forEach>
				</select></td>
			</tr>
		</c:if>
	</table>
</form>
