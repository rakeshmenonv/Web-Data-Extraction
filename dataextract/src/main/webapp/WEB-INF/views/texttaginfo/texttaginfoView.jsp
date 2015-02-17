<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div id="three">
	<div class="contenttable">
		<h3>
			<spring:message code="texttaginfo_title" />
		</h3>
		<div class="contenttable1">
			<table class="content" style="width: 99%;" >
				 				<tr>
					<td class="biao_bt3"><spring:message
							code="texttaginfo_tag" /></td>
					<td>${ texttaginfo.tag }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="texttaginfo_value" /></td>
					<td>${ texttaginfo.value }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="texttaginfo_PageInfoId" /></td>
					<td>${ texttaginfo.PageInfoId }</td>
				</tr>
				   			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
$ .parser.onComplete = function() {
	parent.$ .messager.progress('close');
};
</script>



