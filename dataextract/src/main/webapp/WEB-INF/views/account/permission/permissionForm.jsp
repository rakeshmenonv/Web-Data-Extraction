<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$.parser.onComplete = function() {
		parent.$.messager.progress('close');
		$('#permission_form_inputForm').form(
				{
					onSubmit : function(param) {
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
							var node = parent.$.modalDialog.openner_dataGrid
									.treegrid('getSelected');
							if (node) {
								if (node.pid != 0) {
									parent.$.modalDialog.openner_dataGrid
											.treegrid('reload');
								}
							} else {
								parent.$.modalDialog.openner_dataGrid
										.treegrid('reload');
							}
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

	function typeChange(type) {
		switch (type) {
		case '1':
			$("#value").val("system:");
			break;
		case '2':
			$("#value").val("menu:");
			break;
		case '3':
			$("#value").val("fun:");
			break;
		default:
			$("#value").val("");
		}
	}
</script>

<form id="permission_form_inputForm" method="post"
	action="${ctx}/account/permission/${action}">
	<input type="hidden" id="id" name="id" value="${permission.id }" /><input
		type="hidden" id="ckey" name="ckey" value="${permission.ckey }"
		readonly="readonly" />
	<table class="content" style="width: 100%;">
		<tr>
			<td class="biao_bt3"><label><spring:message
						code="permission_name" />:</label></td>
			<td style="text-align: left;"><label id="nameLbl"></label> <input
				type="text" id="name" name="name" value="${permission.name }"
				class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'<spring:message code="permission_name" />不能为空.',required:true" /></td>
		</tr>
		<tr>
			<td class="biao_bt3"><label>权限类型:</label></td>
			<td style="text-align: left;"><select id="permissionType"
				name="permissionType" onchange="typeChange(this.value);">
					<option value="">请选择权限类型</option>
					<option value="1"
						<c:if test="${permission.permissionType==1 }">selected="selected"</c:if>>系统</option>
					<option value="2"
						<c:if test="${permission.permissionType==2 }">selected="selected"</c:if>>菜单</option>
					<option value="3"
						<c:if test="${permission.permissionType==3 }">selected="selected"</c:if>>功能</option>
			</select></td>
		</tr>
		<tr>
			<td class="biao_bt3"><label><spring:message
						code="permission_value" />:</label></td>
			<td style="text-align: left;"><input type="text" id="value"
				name="value" value="${permission.value }"
				class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'<spring:message code="permission_value" />不能为空.',required:true" /></td>
		</tr>
		<tr>
			<td class="biao_bt3">上级权限:</td>
			<td colspan="3"><input type=hidden id="pid" name="pid" value="${permission.pid }" />${pName }</td>
		</tr>
	</table>
</form>
