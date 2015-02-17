<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div id="three">
	<div class="contenttable">
		<h3>
			<spring:message code="$!{bealowerNmae}_title" />
		</h3>
		<div class="contenttable1">
			<table class="content" style="width: 99%;" >
				#foreach($field in $!fields) #if($!field.name !="id")
				<tr>
					<td class="biao_bt3"><spring:message
							code="$!{bealowerNmae}_$!{field.name}" /></td>
					<td>${ $!{bealowerNmae}.$!field.name }</td>
				</tr>
				#end #end
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
$ .parser.onComplete = function() {
	parent.$ .messager.progress('close');
};
</script>



