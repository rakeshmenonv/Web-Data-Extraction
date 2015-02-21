<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div id="three">
	<div class="contenttable">
		<h3>
			<spring:message code="pagedatainfo_title" />
		</h3>
		<div class="contenttable1">
			<table class="content" style="width: 99%;" >
				 				<tr>
					<td class="biao_bt3"><spring:message
							code="pagedatainfo_tableGroupKey" /></td>
					<td>${ pagedatainfo.tableGroupKey }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pagedatainfo_rowGroupKey" /></td>
					<td>${ pagedatainfo.rowGroupKey }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pagedatainfo_content" /></td>
					<td>${ pagedatainfo.content }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pagedatainfo_type" /></td>
					<td>${ pagedatainfo.type }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pagedatainfo_extractedDate" /></td>
					<td>${ pagedatainfo.extractedDate }</td>
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



