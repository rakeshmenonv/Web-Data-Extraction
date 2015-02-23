
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body>
<script>

if (!!window.EventSource) {
       
	   console.log("Event source available");
	   var source = new EventSource('sse');
	  
	   source.addEventListener('message', function(e) {
		 
	        console.log(e);
	        
	        //event.target.close();
	   });

	   source.addEventListener('open', function(e) {
		  
	        console.log("Connection was opened.");
	   }, false);

	   source.addEventListener('error', function(e) {
		  	if (e.readyState == EventSource.CLOSED) {
	            console.log("Connection was closed.");
	        } else {
	        	
	            //console.log(e.readyState);
	        }
	   }, false);
	} else {
		 
	        console.log("No SSE available");
	}
</script>

<form id="dataharvest_form_inputForm" name="dataharvest_form_inputForm" >
	<center>
	<div style="margin:20px 0 10px 0;"></div>
	<div class="easyui-tabs" style="width:700px;height:250px">
		<div title="<spring:message code="webharvest_basicSearch" />" style="padding:10px">
		<%-- URL:
		<input type="text" name="URL" id="URL"  class="easyui-validatebox" data-options="missingMessage:'<spring:message code="hyperlinkinfo_linkName" />URL',required:true"   /> --%>	
		<table cellpadding="5">
		
	    		<tr>
	    			<td><spring:message code="webharvest_url" />:</td>
	    			<td><input class="easyui-textbox" type="text" name="URL" data-options="required:true"></input></td>
	    		</tr>	
	    </table>
		</div>
		<div title="<spring:message code="webharvest_patternSearch" />" style="padding:10px">
		<table cellpadding="5">
	    		<tr>
	    			<td><spring:message code="webharvest_element" />:</td>
	    			<td><input class="easyui-textbox" type="text" name="ELEMENT" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td><spring:message code="webharvest_attribute" />:</td>
	    			<td><input class="easyui-textbox" type="text" name="ATTRIBUTE" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td><spring:message code="webharvest_value" />:</td>
	    			<td><input class="easyui-textbox" type="text" name="VALUE" data-options="required:true"></input></td>
	    		</tr>	
	    </table>
		</div>
		<div title="Scheduler" style="padding:10px">
		<table cellpadding="5">
		<tr>
	    			<td><spring:message code="webharvest_interval" />:</td>
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
	    	
	    	<input type="submit"  name="submit" class="easyui-submitbutton" >
	    	
	    </div>
	</center>
	</form>
</body>
</html>