<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
var formId="dataharvest_form_inputForm";
var showLogUrl="${ctx}/dataharvest/showlog";
var basicinfo_saveonly_url="${ctx}/dataharvest/saveElement";
var basicinfo_save_showdata_url="${ctx}/dataharvest/${action}";
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
					var inputForm = $('#'+ formId);
					inputForm.prop('action',basicinfo_save_showdata_url); 
					
					es.close();					
					var result = $ .parseJSON(result);
					if(result.success){
						var id = result.data.id;
						$('#formSaveBtn').linkbutton({disabled : false});
						$('#formSaveBtn').bind('click', function(){
							 parent.$.modalDialog.handler.dialog('close');						
							 indexTabsUpdateTab('href',{title:'<spring:message code="webharvest_extracteddata" />',url:'${ctx}/dataharvest/showdata/'+id,iconCls:'icon-table_multiple'});
						});
					}			
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
function saveElement(){
	setValidation();
	var inputForm = $('#'+ formId);
	inputForm.prop('action',basicinfo_saveonly_url); 
	var isValid = inputForm.form('validate');
	if (isValid) {
		inputForm.submit();
		$.messager.show({ // show error message
			title : '提示',
			msg : 'data saved'
		});
	}

}


function clearForm(){
	$('#'+ formId).trigger("reset");
	$.messager.show({ // show error message
		title : '提示',
		msg : 'form cleared'
	});
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
					text : '<spring:message code="webharvest_next" />',
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
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">	
		<div data-options="region:'north',border:false" >	
			<div class="datagrid-toolbar">
				<table>
					<tr>
						<td>
							<div style="text-align: center;">
								<a href="javascript:saveElement(basicinfo_save_url)" class="easyui-linkbutton"
									data-options="iconCls:'icon-disk',plain:true">Save</a>
							</div>
						</td>
						<td>
							<div style="text-align: center;">
								<a href="#" class="easyui-linkbutton" onclick="clearForm();"
									data-options="iconCls:'icon-error_delete',plain:true">Clear</a>
							</div>
						</td>
						<td>
							<div style="text-align: center;">
								<a href="#" class="easyui-linkbutton" onclick="beginExtract();"
									data-options="iconCls:'icon-search',plain:true"><spring:message code="webharvest_extract" /></a>
							</div>
						</td>
					</tr>
				</table>
			</div>	
		</div>	
		<div data-options="region:'center',border:false">	
			<form:form id="dataharvest_form_inputForm"
				name="dataharvest_form_inputForm" action="${ctx}/dataharvest/${action}"
				modelAttribute="pageurlinfo" method="post" class="form-horizontal">
				<div class="easyui-tabs" data-options="border:false"
					style="width: 100%; height:400px;;overflow: hidden;">
					<div title="<spring:message code="webharvest_basicSearch" />" style="padding: 10px" data-options="iconCls:'icon-page_white_magnify'">
					
						<%-- URL:
						
					<input type="text" name="URL" id="URL"  class="easyui-validatebox" data-options="missingMessage:'<spring:message code="hyperlinkinfo_linkName" />URL',required:true"   /> --%>
						<table cellpadding="5" style="float:left;">
			
							<tr>
								<td><spring:message code="webharvest_url" />:</td>
								<td><input class="easyui-validatebox textbox" type="text"  name="url" id="url"
									data-options="required:true,validType:'url',multiline:true"  style="width:300px;height:100px"></input></td>
							</tr>
							
							
						</table>
						<div class="easyui-panel" title="Description" style="height:150px;padding:10px;float:right;font-size:14px;">					        
					        <ul>
					            <li>1. Basic search module is used to extract data from given URL.</li> 
					            <li>2. In that URL field need to provide URL of which page want to extract.</li>
					        </ul>
					        <br>					        
					        <p style="color:blue;">Example:</p>					        
					        <div style="padding-left:60px;">
					        <p>URL : http://iamtop.net/</p>
					        </div>
					    </div>
					</div>
					<div title="<spring:message code="webharvest_patternSearch" />" style="padding: 10px" data-options="iconCls:'icon-page_white_star'">
						<table cellpadding="5" style="float:left;">
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
						<div class="easyui-panel" title="Description" style="height:210px;padding:10px;float:right;font-size:14px;">					        
					        <ul>
					            <li>1. Pattern search module is helps to customize search. </li>
					            <li>2. With the help of this we can get specific data of the page, for that we have to provide three parameters. </li>
					            <li>3. The first one is exact name of the element or tag. </li>
					            <li>4. The second one is attribute of element or tag, at last value of that attribute.</li>
					        </ul>
					        <br>
					       <p style="color:blue;">Example:</p>					       
					        <div style="padding-left:60px;">
					        <p><spring:message code="webharvest_element" /> : div</p>
					        <p><spring:message code="webharvest_attribute" /> : id</p>
					        <p><spring:message code="webharvest_value" /> : idvalue</p>
					        </div>
					    </div>
					</div>
					<div title="<spring:message code="webharvest_betweenSelected" />" style="padding:10px" data-options="iconCls:'icon-page_code'">
						<table cellpadding="5" style="float:left;">
								<tr>
									<td>开始模式:</td>
									<td><textarea class="easyui-validatebox textbox" type="text" name="startTag" id="startTag"
										data-options="required:false,multiline:true"  style="width:500px;height:150px" onkeyup="setValidation()"></textarea></td>
										
								</tr>
								<tr>
									<td>结束模式:</td>
									<td><textarea class="easyui-validatebox textbox" type="text" name="endTag" id="endTag"
										data-options="required:false,multiline:true"  style="width:500px;height:150px" onkeyup="setValidation()"></textarea></td>
								</tr>
						</table>
						<div class="easyui-panel" title="Description" style="height:230px;padding:10px;float:right;font-size:14px;">					      
					        <ul>
					            <li>1. The between select module is used to get in between data from given start tag to end tag.</li> 
					            <li>2. We have to mention start and end tag.</li>
					        </ul>
					        <br>
					        <p style="color:blue;">Example:</p>					        
					        <div style="padding-left:60px;">
					        <p>开始模式: &lt;table cellpadding="5" style="float:left;"&gt;</p>
					        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;tr&gt;</p>
					        <br>
					        <p>结束模式: &lt;/table&gt;</p>
					        </div>
					    </div>
					</div>
					<div title="高级搜索" style="padding:10px" data-options="iconCls:'icon-find'">
						<table cellpadding="5" style="float:left;">
							<tr>
								<td>页面格式</td>
								<td><input class="easyui-validatebox textbox" type="text" name="pageFormat" id="pageFormat" style="width:300px;height:100px"
									data-options="validType:'url',multiline:true" onkeyup="setValidation()"></input></td>
									
							</tr>
							<tr>
								<td>页面开始索引:</td>
								<td><input class="easyui-numberbox" type="text" name="startPage" id="startPage" value="0"
									onkeyup="setValidation()"></input></td>
							</tr>
							<tr>
								<td>页末指数:</td>
								<td><input class="easyui-numberbox" type="text" name="endPage" id="endPage"  value="0"
									 onkeyup="setValidation()"></input></td>
							</tr>
						</table>
						<div class="easyui-panel" title="Description" style="height:230px;padding:10px;float:right;font-size:14px;">					       
					        <ul>
					            <li>1. This advance search module is used to get pageable data.</li>
					            <li>2. We need to provide exact page data URL in that page format filed. </li>
					            <li>3. We have to mention * instated of the index value of the URL. </li>
					            <li>4. In start page index field need to provide starting index of the page and end page index field need to provide ending index of the page.</li>
					          </ul>
					        <br>
					        <p style="color:blue;">Example:</p>					        
					        <div style="padding-left:60px;">
					        <p>页面格式 : http://iamtop.net/employelist?page*.html</p>
					        <p>页面开始索引: 2</p>
					        <p>页末指数: 5</p>
					        </div>
					    </div>
					</div>
					<div title="<spring:message code="webharvest_scheduler" />" style="padding:10px" data-options="iconCls:'icon-hourglass'">
						<table cellpadding="5" style="float:left;">
							<tr>
					    		<td><spring:message code="webharvest_interval" />:</td> 
					    		<td>
					    			<input class="easyui-numberspinner" style="width:100px;" name="jobon" id="jobon" data-options="min:1,max:1000,editable:true"></input>
				<!-- 	    				<select class="easyui-combobox" name="state" id="state" style="width:200px;"> -->
				<!-- 							<option value="">选择任意1..</option> -->
				<%-- 							<c:forEach items="${schedulerList}" var="par"> --%>
				<%-- 							<option value="${par.name}">${par.name}</option> --%>
				<%-- 							</c:forEach> --%>
				<!-- 						</select> -->
					    		</td>
					    		<td>(<font color="red">指定在小时的间隔</font>)</td> 
					    	</tr>
						</table>
						<div class="easyui-panel" title="Description" style="height:150px;padding:10px;float:right;font-size:14px;">					        
					        <ul>
					           <li>1. This scheduler module is used to set the interval by hours. </li> 
					           <li>2. At the time of interval it will automatically extract data form that given specification.</li> </ul>
					         <br>
					       <p style="color:blue;">Example:</p>					        
					        <div style="padding-left:60px;">
					        <p><spring:message code="webharvest_interval" />: 10</p>
					        </div>
					    </div>
					</div>
				</div>
			
			</form:form>
		</div>
	</div>
</div>
