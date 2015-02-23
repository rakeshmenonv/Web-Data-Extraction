<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <%@ include file="/common/easyui_inc.jsp"%> --%>
<%-- 
<head>
<script type="text/javascript" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
</head> --%>
<script>
$.parser.onComplete = function() {
	parent. $ .messager.progress('close');	
};
var formId="dataharvest_form_inputForm";
var showLogUrl="${ctx}/dataharvest/showlog";
function beginExtract(){
	var inputForm = $('#'+ formId);
	var a = $('#element').val();
	
	 var b = $('#attribute').val();
	 var c = $('#value').val();
	 
	/*  if(a==''){
		 $("#spanid1").text("enter element value");
		
	 }
	 if(b==''){
		 $("#spanid2").text("enter b value");
	 }
	 if(c=='')
		 {
		 $("#spanid3").text("enter c value");
		 } */
	var isValid = inputForm.form('validate');
	if (isValid) {
		showLog(showLogUrl);
		inputForm.submit();
	}
	if(a == '' && b == '' && c == ''){
		$("#spanid1").hide();
		$("#spanid2").hide();
		$("#spanid3").hide();
		 }
	else if(a !='' && b == '' && c == ''){
		
		$("#spanid2").text("enter attribute value").show();
		$("#spanid3").text("enter value").show();
		$("#spanid1").hide();
		 }
	else if(a == '' && b != '' && c == ''){
		$("#spanid1").text("enter element value").show();
		$("#spanid3").text("enter value").show();
		$("#spanid2").hide();
		 }else if(a == '' && b == '' && c !=''){
			 $("#spanid1").text("enter element value").show();
			 $("#spanid2").text("enter attribute value").show();
			 $("#spanid3").hide();
		 }
		 else if(a != '' && b!= '' && c ==''){
			 $("#spanid3").text("enter value").show();
			 $("#spanid1").hide();
			 $("#spanid2").hide();
			 
		 }
		 else if(a != '' && b== '' && c!==''){
			
			 $("#spanid2").text("enter attribute value").show();
			  $("#spanid1").hide();
			 $("#spanid3").hide(); 
		 }
	else
		{
		$("#spanid1").text("enter element value").show();
		$("#spanid2").hide();
		$("#spanid3").hide();
		
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
		style="width: 400px; height: 250px; margin: 50px auto;">
		<div title="Basic Search" style="padding: 10px">
		
			<%-- URL:
			
		<input type="text" name="URL" id="URL"  class="easyui-validatebox" data-options="missingMessage:'<spring:message code="hyperlinkinfo_linkName" />URL',required:true"   /> --%>
			<table cellpadding="5">

				<tr>
					<td>URL:</td>
					<td><input class="easyui-validatebox" type="text"  name="url" id="url"
						data-options="required:true,validType:'url'" style="width: 267px;"></input></td>
				</tr>
				
				
			</table>
		</div>
		<div title="PatternSearch" style="padding: 10px">
			<table cellpadding="5">
				<tr>
					<td>ELEMENT:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="element" id="element"
						data-options="validate:true"></input><span id="spanid1" style="color:red"></span></td>
						
				</tr>
				<tr>
					<td>ATTRIBUTE:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="attribute" id="attribute"
						data-options="validate:true"></input><span id="spanid2" style="color:red"></span></td>
				</tr>
				<tr>
					<td>VALUE:</td>
					<td><input class="easyui-validatebox textbox" type="text" name="value" id="value" 
						data-options="validate:true"></input><span id="spanid3" style="color:red"></span></td>
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

