<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div id="three">
	<div class="contenttable">
		<h3>
			<spring:message code="hyperlinkinfo_title" />
		</h3>
		<div class="contenttable1">
			<table class="content" style="width: 99%;" >
				 				<tr>
					<td class="biao_bt3"><spring:message
							code="hyperlinkinfo_linkName" /></td>
					<td>${ hyperlinkinfo.linkName }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="hyperlinkinfo_linkUrl" /></td>
					<td>${ hyperlinkinfo.linkUrl }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="hyperlinkinfo_PageInfoId" /></td>
					<td>${ hyperlinkinfo.PageInfoId }</td>
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



