<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
$.parser.onComplete = function() {
	parent. $ .messager.progress('close');
	setValidation();
};
var formId="dataharvest_form_inputForm";
var showLogUrl="${ctx}/dataharvest/showlog";
function beginExtract(){
	setValidation();
	var inputForm = $('#'+ formId);
	var isValid = inputForm.form('validate');
	if (isValid) {
		showLog(showLogUrl);
		inputForm.submit();
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
		width : 600,
		height : 400,
		title : '信息',
		href : url,
		iconCls : 'icon-application_form_add',
		buttons : [
				{
					text : '保存',
					iconCls : 'icon-save',
					id : 'formSaveBtn',
					handler : function() {
						
					}
				}, {
					text : '取消',
					id : 'formCancelBtn',
					iconCls : 'icon-cross',
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
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
		<div title="Basic Search" style="padding: 10px">
		
			<%-- URL:
			
		<input type="text" name="URL" id="URL"  class="easyui-validatebox" data-options="missingMessage:'<spring:message code="hyperlinkinfo_linkName" />URL',required:true"   /> --%>
			<table cellpadding="5">

				<tr>
					<td>URL:</td>
					<td><input class="easyui-validatebox textbox" type="text"  name="url" id="url"
						data-options="required:true,validType:'url'"  style="width: 267px;"></input></td>
				</tr>
				
				
			</table>
		</div>
		<div title="PatternSearch" style="padding: 10px">
			<table cellpadding="5">
				<tr>
					<td>ELEMENT:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="element" id="element"
						data-options="required:true" onkeyup="setValidation()"></input></td>
						
				</tr>
				<tr>
					<td>ATTRIBUTE:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="attribute" id="attribute"
						data-options="required:true" onkeyup="setValidation()"></input></td>
				</tr>
				<tr>
					<td>VALUE:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="value" id="value" 
						data-options="required:true" onkeyup="setValidation()"></input></td>
				</tr>
			</table>
		</div>

		<div title="Scheduler" style="padding:10px">
		<table cellpadding="5">
		<tr>
	    			<td>INTERVAL:</td> 
	    			<td>
	    				<select class="easyui-combobox" name="state" style="width:200px;">
						<option value="10">10</option>
						<option value="20">20</option>
						<option value="30">30</option>
						</select>
	    			</td>
	    		</tr>
		</table>
		</div>
		
	</div>


	

	

	<div style="text-align: center;">
		<a href="#" class="easyui-linkbutton" onclick="beginExtract();"
			data-options="iconCls:'icon-search'">Extract</a>
	</div>

</form:form>

