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
			<td class="biao_bt3"><spring:message code="webharvest_url" /></td>
			<td><input type="text" name="url" id="url" value="${ pageurlinfo.url }" readonly="true"   />	</td>
		</tr>
	  		<tr>
			<td class="biao_bt3"><spring:message code="webharvest_scheduleBy" /></td>
			<td style="width:10%;">
			<input class="easyui-numberspinner" style="width:80px;" name="jobon" id="jobon" value="${ pageurlinfo.jobon }" data-options="min:0,max:1000,editable:false"></input>
			<%-- <select class="easyui-combobox" name="jobon" id="jobon">
			<option value="">选择任意1..</option>
			<c:forEach items="${schedulerList}" var="par">
			<option value="${par.name}"
			<c:if test="${ pageurlinfo.jobon == par.name }"> selected="selected"</c:if>>${par.name}</option>
			</c:forEach>
			</select> --%>
			</td>
			</tr>
	   	</table>
</form:form>
	