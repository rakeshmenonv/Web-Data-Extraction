<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var pagedatainfo_form_inputform_id = 'pagedatainfo_form_inputForm';

$ .parser.onComplete = function() {
		parent. $ .messager.progress('close');
		$('#'+pagedatainfo_form_inputform_id).form(
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

<form:form id="pagedatainfo_form_inputForm" name="pagedatainfo_form_inputForm" action="${ctx}/pagedatainfo/${action}"
		 modelAttribute="pagedatainfo" method="post" class="form-horizontal">
	<input type="hidden" name="id" id="id" value="${ pagedatainfo.id}" />
	<table class="content" style="width: 100%;">
	 		<tr>
			<td class="biao_bt3"><spring:message code="pagedatainfo_tableGroupKey" /></td>
			<td><input type="text" name="tableGroupKey" id="tableGroupKey" value="${ pagedatainfo.tableGroupKey }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pagedatainfo_tableGroupKey" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pagedatainfo_rowGroupKey" /></td>
			<td><input type="text" name="rowGroupKey" id="rowGroupKey" value="${ pagedatainfo.rowGroupKey }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pagedatainfo_rowGroupKey" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pagedatainfo_content" /></td>
			<td><input type="text" name="content" id="content" value="${ pagedatainfo.content }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pagedatainfo_content" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pagedatainfo_type" /></td>
			<td><input type="text" name="type" id="type" value="${ pagedatainfo.type }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pagedatainfo_type" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pagedatainfo_extractedDate" /></td>
			<td><input type="text" name="extractedDate" id="extractedDate" value="${ pagedatainfo.extractedDate }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pagedatainfo_extractedDate" />不能为空.',required:true"   />	</td>
		</tr>
	   	</table>
</form:form>
	