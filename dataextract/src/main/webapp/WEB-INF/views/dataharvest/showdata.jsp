<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
$.parser.onComplete = function() {
	parent. $ .messager.progress('close');	
};
</script>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">		
		<div data-options="region:'center',border:false" id="tabledata">			
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
					     		</tr><tr><td>${rowindex}</td><td>${obj.content}</td>
					     	</c:otherwise>
					    </c:choose> 	
				    </c:when>    
				    <c:otherwise>
				    	<c:set var="tableindex" scope="session" value="${tableindex+1}"/>
				    	<c:set var="rowindex" scope="session" value="${1}"/>				    	
				        </table><br>Table ${tableindex}<br><br><table border='1'>				       
				        <tr><td>${rowindex}</td><td>${obj.content}</td>
				    </c:otherwise>
				</c:choose>			
				<c:set var="tableGroupkey" scope="session" value="${obj.tableGroupKey}"/>
				<c:set var="rowGroupkey" scope="session" value="${obj.rowGroupKey}"/> 
			</c:forEach> 
		</div>		
	</div>
</div>