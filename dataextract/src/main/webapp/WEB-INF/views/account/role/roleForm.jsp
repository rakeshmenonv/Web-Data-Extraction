<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$.parser.onComplete = function() {
		parent.$.messager.progress('close');

		$('#role_form_inputForm').form({
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
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

	};
</script>

<form id="role_form_inputForm" method="post"
	action="${ctx}/account/role/${action}">
	<input type="hidden" id="id" name="id" value="${role.id }" />
	<table class="content" style="width: 100%;">
		<tr>
			<td class="biao_bt3"><label>角色名称:</label></td>
			<td><label id="nameLbl"></label> <input type="text" id="name"
				name="name" value="${role.name}"
				class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'角色名称不能为空.',required:true" /></td>
		</tr>
		<tr>
			<td class="biao_bt3">备注</td>
			<td><textarea rows="5" cols="45" name="remark"
					style="margin-top: 5px; margin-bottom: 5px; width: 80%;"
					id="remark">${ role.remark }</textarea></td>
		</tr>
	</table>
</form>
