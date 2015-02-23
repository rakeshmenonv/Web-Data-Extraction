<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>


var formId="dataharvest_form_inputForm";
var showLogUrl="${ctx}/dataharvest/showlog";
$.parser.onComplete = function() {
	setValidation();
	parent. $ .messager.progress('close');	
	$('#'+formId).form(
			{
				onSubmit : function() {
					var isValid = $(this).form('validate');
					if (!isValid) {
						parent. $ .messager.progress('close');
					}
					return isValid;
				},
				success : function(result) {
					source.close();
					console.info("fineshed");
					var result = $ .parseJSON(result);
					console.info("value :"+result.success);
					var id = result.data.id;
					$('#formSaveBtn').linkbutton({disabled : false});
					$('#formSaveBtn').bind('click', function(){
						 parent.$.modalDialog.handler.dialog('close');						
						 indexTabsAddTab('href',{title:'result',url:'${ctx}/dataharvest/showdata/'+id,iconCls:'icon-plugin'});
					});
					//parent.$.modalDialog.handler.dialog('close');
				}
			});
};

function beginExtract(){
	setValidation();
	var inputForm = $('#'+ formId);
	var isValid = inputForm.form('validate');
	if (isValid) {
		inputForm.submit();
		showLog(showLogUrl);
		
	}

}
function setValidation(){
	var a = $('#element');
	var b = $('#attribute');
	var c = $('#value');
	if($(a).val()||$(b).val()||$(c).val()){
		$(a).validatebox('enableValidation');
		$(b).validatebox('enableValidation');
		$(c).validatebox('enableValidation');
	}else{
		$(a).validatebox('disableValidation');
		$(b).validatebox('disableValidation');
		$(c).validatebox('disableValidation');
	}

}


function showLog(url,params) {
	var opts = {
		width : 900,
		height : 600,
		title : '信息',
		href : url,
		iconCls : 'icon-application_form_add',
		buttons : [
				{
					text : 'Next',
					iconCls : 'icon-note_go',
					disabled : true,
					id : 'formSaveBtn',
					handler : function() {
						
					}
				}]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);

}
</script>

<form:form id="dataharvest_form_inputForm"
	name="dataharvest_form_inputForm" action="${ctx}/dataharvest/${action}"
	modelAttribute="pageurlinfo" method="post" class="form-horizontal">
	<div class="easyui-tabs"
		style="width: 400px; height: 250px; margin: 20px auto;">
		<div title="Basic Search" style="padding: 10px" data-options="iconCls:'icon-page_white_magnify'">
		
			<%-- URL:
			
		<input type="text" name="URL" id="URL"  class="easyui-validatebox" data-options="missingMessage:'<spring:message code="hyperlinkinfo_linkName" />URL',required:true"   /> --%>
			<table cellpadding="5">

				<tr>
					<td><spring:message code="webharvest_url" />:</td>
					<td><input class="easyui-validatebox textbox" type="text"  name="url" id="url"
						data-options="required:true,validType:'url'"  style="width: 267px;"></input></td>
				</tr>
				
				
			</table>
		</div>
		<div title="PatternSearch" style="padding: 10px" data-options="iconCls:'icon-page_white_star'">
			<table cellpadding="5">
				<tr>
					<td><spring:message code="webharvest_element" />:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="element" id="element"
						data-options="required:true" onkeyup="setValidation()"></input></td>
						
				</tr>
				<tr>
					<td><spring:message code="webharvest_attribute" />:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="attribute" id="attribute"
						data-options="required:true" onkeyup="setValidation()"></input></td>
				</tr>
				<tr>
					<td><spring:message code="webharvest_value" />:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="value" id="value" 
						data-options="required:true" onkeyup="setValidation()"></input></td>
				</tr>
			</table>
		</div>

		<div title="Scheduler" style="padding:10px" data-options="iconCls:'icon-hourglass'">
		<table cellpadding="5">
		<tr>
	    			<td><spring:message code="webharvest_url" />:</td> 
	    			<td>
	    				<select name="state" id="state" style="width:200px;">
							<c:forEach items="${schedulerList}" var="par">
							<option value="${par.name}">${par.name}</option>
							</c:forEach>
						</select>
	    			</td>
	    		</tr>
		</table>
		</div>
		
	</div>

	<div style="text-align: center;">
		<a href="#" class="easyui-linkbutton" onclick="beginExtract();"
			data-options="iconCls:'icon-search'"><spring:message code="webharvest_extract" /></a>
	</div>

</form:form>
