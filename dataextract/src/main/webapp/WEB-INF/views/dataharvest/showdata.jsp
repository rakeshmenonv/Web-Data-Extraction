<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
$.parser.onComplete = function() {
	parent. $ .messager.progress('close');	
};
</script>
<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;border-color:#999;}
.tg td{font-family:Arial, sans-serif;font-size:12px;padding:5px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#999;color:#444;background-color:#F7FDFA;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:5px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#999;color:#fff;background-color:#26ADE4;}
.tg td a{color:#1b70fb;}
</style>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">		
		<div data-options="region:'center',border:false" id="tabledata" style="padding-left: 30px !important;">			
			<c:set var="tableGroupkey" scope="session" value=""/>
			<c:set var="rowGroupkey" scope="session" value=""/>
			<c:set var="tableindex" scope="session" value="0"/>
			<c:set var="rowindex" scope="session" value="0"/>
			<table>
			<c:forEach items="${pagedatainfoList}" var="obj" varStatus="loop">
				<c:choose>
				    <c:when test="${obj.tableGroupKey==tableGroupkey}">
				    	<c:choose>
					     	<c:when test="${obj.rowGroupKey==rowGroupkey}">
					     		<td>${obj.content}</td>
					     	</c:when>
					     	<c:otherwise>
					     		<c:set var="rowindex" scope="session" value="${rowindex+1}"/>
					     		</tr><tr><td class="tg-031e">${rowindex}</td><td>${obj.content}</td>
					     	</c:otherwise>
					    </c:choose> 	
				    </c:when>    
				    <c:otherwise>
				    	<c:set var="tableindex" scope="session" value="${tableindex+1}"/>
				    	<c:set var="rowindex" scope="session" value="${1}"/>				    	
				        </table><br><h3>Group ${tableindex}<h3></h1><br><table  class="tg" >	
				        <tr><th class="tg-031e">No.</th><th class="tg-031e" colspan="50"></th></tr>
				        <tr><td class="tg-031e">${rowindex}</td><td class="tg-031e">${obj.content}</td>
				    </c:otherwise>
				</c:choose>			
				<c:set var="tableGroupkey" scope="session" value="${obj.tableGroupKey}"/>
				<c:set var="rowGroupkey" scope="session" value="${obj.rowGroupKey}"/> 
			</c:forEach> 
		</div>		
	</div>
</div>