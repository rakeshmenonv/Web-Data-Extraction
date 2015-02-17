<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var $!{bealowerNmae}_form_inputform_id = '${bealowerNmae}_form_inputForm';

$ .parser.onComplete = function() {
		parent. $ .messager.progress('close');
		$('#'+$!{bealowerNmae}_form_inputform_id).form(
				{
					onSubmit : function() {
						parent. $ .messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						var isValid = $(this).form('validate');
						if (!isValid) {
							parent. $ .messager.progress('close');
						}
						return isValid;
					},
					success : function(result) {
						parent. $ .messager.progress('close');
						result = $ .parseJSON(result);
						if (result.success) {
							parent. $ .modalDialog.openner_dataGrid
									.datagrid('reload');
							parent. $ .modalDialog.openner_dataGrid.datagrid(
									'uncheckAll').datagrid('unselectAll')
									.datagrid('clearSelections');
							parent. $ .modalDialog.handler.dialog('close');
							$ .messager.show({ // show error message
								title : '提示',
								msg : result.message
							});
						} else {
							$ .messager.alert('错误', result.message, 'error');
						}
					}
				});

	} ;
</script>

<form:form id="${bealowerNmae}_form_inputForm" name="${bealowerNmae}_form_inputForm" action="${ctx}/$!bealowerNmae/${action}"
		 modelAttribute="$!bealowerNmae" method="post" class="form-horizontal">
	<input type="hidden" name="id" id="id" value="${ $!{bealowerNmae}.id}" />
	<table class="content" style="width: 100%;">
	#foreach($field in $!fields) #if($!field.name !="id")
		<tr>
			<td class="biao_bt3"><spring:message code="$!{bealowerNmae}_$!{field.name}" /></td>
			<td><input type="text" name="$!field.name" id="$!field.name" value="${ $!{bealowerNmae}.$!field.name }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="$!{bealowerNmae}_$!{field.name}" />不能为空.',required:true"   />	</td>
		</tr>
	#end #end
	</table>
</form:form>
	