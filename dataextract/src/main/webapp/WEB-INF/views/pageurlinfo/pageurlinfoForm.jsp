<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var pageurlinfo_form_inputform_id = 'pageurlinfo_form_inputForm';

$ .parser.onComplete = function() {
		parent. $ .messager.progress('close');
		$('#'+pageurlinfo_form_inputform_id).form(
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

<form:form id="pageurlinfo_form_inputForm" name="pageurlinfo_form_inputForm" action="${ctx}/pageurlinfo/${action}"
		 modelAttribute="pageurlinfo" method="post" class="form-horizontal">
	<input type="hidden" name="id" id="id" value="${ pageurlinfo.id}" />
	<table class="content" style="width: 100%;">
	 		<tr>
			<td class="biao_bt3"><spring:message code="pageurlinfo_url" /></td>
			<td><input type="text" name="url" id="url" value="${ pageurlinfo.url }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pageurlinfo_url" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pageurlinfo_element" /></td>
			<td><input type="text" name="element" id="element" value="${ pageurlinfo.element }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pageurlinfo_element" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pageurlinfo_attribute" /></td>
			<td><input type="text" name="attribute" id="attribute" value="${ pageurlinfo.attribute }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pageurlinfo_attribute" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pageurlinfo_value" /></td>
			<td><input type="text" name="value" id="value" value="${ pageurlinfo.value }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pageurlinfo_value" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="pageurlinfo_extractedDate" /></td>
			<td><input type="text" name="extractedDate" id="extractedDate" value="${ pageurlinfo.extractedDate }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="pageurlinfo_extractedDate" />不能为空.',required:true"   />	</td>
		</tr>
	   	</table>
</form:form>
	