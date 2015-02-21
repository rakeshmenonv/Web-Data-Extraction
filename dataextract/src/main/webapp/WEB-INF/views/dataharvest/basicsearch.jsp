
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body>
<form id="dataharvest_form_inputForm" name="dataharvest_form_inputForm" >
	<center>
	<div style="margin:20px 0 10px 0;"></div>
	<div class="easyui-tabs" style="width:700px;height:250px">
		<div title="Basic Search" style="padding:10px">
		<%-- URL:
		<input type="text" name="URL" id="URL"  class="easyui-validatebox" data-options="missingMessage:'<spring:message code="hyperlinkinfo_linkName" />URL',required:true"   /> --%>	
		<table cellpadding="5">
		
	    		<tr>
	    			<td>URL:</td>
	    			<td><input class="easyui-textbox" type="text" name="URL" data-options="required:true"></input></td>
	    		</tr>	
	    </table>
		</div>
		<div title="Pattern Search" style="padding:10px">
		<table cellpadding="5">
	    		<tr>
	    			<td>ELEMENT:</td>
	    			<td><input class="easyui-textbox" type="text" name="ELEMENT" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>ATTRIBUTE:</td>
	    			<td><input class="easyui-textbox" type="text" name="ATTRIBUTE" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>VALUE:</td>
	    			<td><input class="easyui-textbox" type="text" name="VALUE" data-options="required:true"></input></td>
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
	<div style="text-align:center;padding:30px">
	    	
	    	<input class="loginin" type="submit"  name="submit"   >
	   	
	    </div>
	</center>
	</form>
</body>
</html>