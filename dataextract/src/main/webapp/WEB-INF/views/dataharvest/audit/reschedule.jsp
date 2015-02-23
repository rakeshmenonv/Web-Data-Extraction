<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var reschedule_form_inputform_id = 'reschedule_form_inputForm';

$ .parser.onComplete = function() {
		parent. $ .messager.progress('close');
		$('#'+reschedule_form_inputform_id).form(
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

<form:form id="reschedule_form_inputForm" name="reschedule_form_inputForm" action="${ctx}/pageurlinfo/${action}"
		 modelAttribute="pageurlinfo" method="post" class="form-horizontal">
	<input type="hidden" name="id" id="id" value="${ pageurlinfo.id}" />
	<table class="content" style="width: 100%;">
	 		<tr>
			<td class="biao_bt3"><spring:message code="pageurlinfo_url" /></td>
			<td><input type="text" name="url" id="url" value="${ pageurlinfo.url }" readonly="true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3">Schedule By</td>
			<td style="width:10%;"><input type="radio" name="jobon" id="jobon" value="day" <c:if test='${pageurlinfo.jobon == "day"}'>checked="true"</c:if> />Day &nbsp 
			<input type="radio" name="jobon" id="jobon" value="month"<c:if test='${pageurlinfo.jobon == "month"}'>checked="true"</c:if> />Month &nbsp
			<input type="radio" name="jobon" id="jobon" value="year"<c:if test='${pageurlinfo.jobon == "year"}'>checked="true"</c:if> />Year</td>
		</tr>
	   	</table>
</form:form>
	