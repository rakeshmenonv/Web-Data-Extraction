<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var imageinfo_form_inputform_id = 'imageinfo_form_inputForm';

$ .parser.onComplete = function() {
		parent. $ .messager.progress('close');
		$('#'+imageinfo_form_inputform_id).form(
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

<form:form id="imageinfo_form_inputForm" name="imageinfo_form_inputForm" action="${ctx}/imageinfo/${action}"
		 modelAttribute="imageinfo" method="post" class="form-horizontal">
	<input type="hidden" name="id" id="id" value="${ imageinfo.id}" />
	<table class="content" style="width: 100%;">
	 		<tr>
			<td class="biao_bt3"><spring:message code="imageinfo_SourceImageUrl" /></td>
			<td><input type="text" name="SourceImageUrl" id="SourceImageUrl" value="${ imageinfo.SourceImageUrl }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="imageinfo_SourceImageUrl" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="imageinfo_localImageUrl" /></td>
			<td><input type="text" name="localImageUrl" id="localImageUrl" value="${ imageinfo.localImageUrl }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="imageinfo_localImageUrl" />不能为空.',required:true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="imageinfo_PageInfoId" /></td>
			<td><input type="text" name="PageInfoId" id="PageInfoId" value="${ imageinfo.PageInfoId }" class="easyui-validatebox" data-options="missingMessage:'<spring:message code="imageinfo_PageInfoId" />不能为空.',required:true"   />	</td>
		</tr>
	   	</table>
</form:form>
	