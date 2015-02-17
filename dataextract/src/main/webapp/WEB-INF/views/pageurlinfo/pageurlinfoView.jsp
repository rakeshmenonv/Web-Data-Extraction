<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div id="three">
	<div class="contenttable">
		<h3>
			<spring:message code="pageurlinfo_title" />
		</h3>
		<div class="contenttable1">
			<table class="content" style="width: 99%;" >
				 				<tr>
					<td class="biao_bt3"><spring:message
							code="pageurlinfo_url" /></td>
					<td>${ pageurlinfo.url }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pageurlinfo_element" /></td>
					<td>${ pageurlinfo.element }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pageurlinfo_attribute" /></td>
					<td>${ pageurlinfo.attribute }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pageurlinfo_value" /></td>
					<td>${ pageurlinfo.value }</td>
				</tr>
				  				<tr>
					<td class="biao_bt3"><spring:message
							code="pageurlinfo_extractedDate" /></td>
					<td>${ pageurlinfo.extractedDate }</td>
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



