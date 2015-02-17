<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$.parser.onComplete = function() {
		parent.$.messager.progress('close');
	};
</script>
<table class="content" style="width:100%;">
	<tr>
		<td class="biao_bt3"><spring:message code="account_loginName" /></td>
		<td>${user.loginName}</td>
	</tr>
	<c:if test="${user.userType==0 }">
		<tr>
			<td class="biao_bt3"><spring:message code="account_name" /></td>
			<td>${ user.name }</td>
		</tr>
	</c:if>

	<c:if test="${user.userType==1 }">
		<tr>
			<td class="biao_bt3"><spring:message code="account_name" /></td>
			<td>${ user.name }</td>
		</tr>
		<tr>
			<td class="biao_bt3">所属帮扶组</td>
			<td><c:forEach items="${group }" var="obj">
					<c:if test="${user.groupId==obj.value }">${obj.name}</c:if>
				</c:forEach></td>
		</tr>
	</c:if>
	<c:if test="${user.userType==2 }">
		<tr>
			<td class="biao_bt3"><spring:message code="account_name" /></td>
			<td>${ user.name }</td>
		</tr>
		<tr>
			<td class="biao_bt3">所属县区</td>
			<td><c:forEach items="${area }" var="obj">
					<c:if test="${user.area==obj.value }">${obj.name
							} </c:if>
				</c:forEach></td>
		</tr>
	</c:if>
	<c:if test="${user.userType==3 }">
		<tr>
			<td class="biao_bt3">企业名称</td>
			<td>${ user.name }</td>
		</tr>
		<tr>
			<td class="biao_bt3">所属县区</td>
			<td><c:forEach items="${area }" var="obj">
					<c:if test="${user.area==obj.value }">${obj.name
							} </c:if>
				</c:forEach></td>
		</tr>
		<tr>
			<td class="biao_bt3">所属帮扶组</td>
			<td><c:forEach items="${group }" var="obj">
					<c:if test="${user.groupId==obj.value }">${obj.name}</c:if>
				</c:forEach></td>
		</tr>
	</c:if>
</table>





