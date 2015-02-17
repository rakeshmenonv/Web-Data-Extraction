<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
<!--
.thcls {
	background: none repeat scroll 0 0 #DEEEF6;
	color: #004C7C;
	height: 39px;
	text-align: center;
	width: 40%;
	font-size: 15px;
}

.tdcls {
	width: 60%;
}
-->
</style>
<script type="text/javascript">
	$.parser.onComplete = function() {
		//$("#parameter_form_inputForm").validate();
		parent.$.messager.progress('close');

		$('#parameter_form_inputForm').form(
				{
					onSubmit : function() {
						parent.$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
						var isValid = $(this).form('validate');
						if (!isValid) {
							parent.$.messager.progress('close');
						}
						return isValid;
					},
					success : function(result) {
						parent.$.messager.progress('close');
						result = $.parseJSON(result);
						if (result.success) {
							parent.$.modalDialog.openner_dataGrid.treegrid(
									'reload', result.data.parentId);
							parent.$.modalDialog.handler.dialog('close');
							$.messager.show({ // show error message
								title : '提示',
								msg : result.message,
								timeout : 800,
								showType : 'fade',
								style : {
									right : '',
									top : $(window).height() / 2,
									bottom : ''
								}
							});
						} else {
							$.messager.alert('错误', result.message, 'error');
						}
					}
				});

	};
</script>

<form id="parameter_form_inputForm" method="post"
	action="${ctx}/parameter/${action}">
	<input type="hidden" id="id" name="id" value="${parameter.id }" /> <input
		type="hidden" id="uuid" name="uuid" value="${parameter.uuid }" />
	<table class="content" style="width: 100%">
		<tr>
			<td class="thcls">分类编码</td>
			<td class="tdcls"><c:if test="${not empty parameter.category }">
					<input type="text" id="category" name="category"
						value="${parameter.category }" readonly
						class="easyui-validatebox inputmiddle"
						data-options="missingMessage:'分类编码不能为空.',required:true" />
				</c:if> <c:if test="${empty parameter.category }">
					<input type="text" id="category" name="category"
						value="${parameter.category }"
						class="easyui-validatebox inputmiddle"
						data-options="missingMessage:'分类编码不能为空.',required:true" />
				</c:if></td>
		</tr>
		<tr>
			<td class="thcls">二级分类</td>
			<td class="tdcls"><input type="text" id="subcategory"
				name="subcategory" value="${parameter.subcategory }"
				class="inputmiddle" />
		</tr>
		<tr>
			<td class="thcls">参数名称</td>
			<td class="tdcls"><input type="text" id="name" name="name"
				value="${parameter.name }" class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'参数名称不能为空.',required:true" /></td>
		</tr>
		<tr>
			<td class="thcls">参数编码</td>
			<td class="tdcls"><input type="text" id="value" name="value"
				value="${parameter.value }" class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'参数编码不能为空.',required:true" /></td>
		</tr>
		<tr>
			<td class="thcls">参数描述</td>
			<td class="tdcls"><input type="text" id="remark" name="remark"
				value="${parameter.remark }" class="inputmiddle" /></td>
		</tr>
		<tr>
			<td class="thcls">排序编号</td>
			<td class="tdcls"><input type="text" id="sort" name="sort"
				value="${parameter.sort }" class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'排序编号不能为空.',required:true" /></td>
		</tr>
		<tr style="display: none">
			<td class="thcls">父级编号</td>
			<td class="tdcls"><input type=text id="parentId" name="parentId"
				value="${parameter.parentId }"
				class="easyui-validatebox inputmiddle"
				data-options="missingMessage:'父级编号不能为空.',required:true" /></td>
		</tr>
	</table>
</form>
