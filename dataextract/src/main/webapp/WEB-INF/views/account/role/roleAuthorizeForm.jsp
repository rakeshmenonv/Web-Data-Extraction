<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	var resourceTree;
	$.parser.onComplete = function() {
		parent.$.messager.progress('close');
		resourceTree = $('#resourceTree')
				.tree(
						{
							url : '${ctx}/account/permission/tree',
							parentField : 'pid',
							// 					lines : true,
							// 					panelHeight : '300',
							// 					required : true,
							idField : 'text',
							checkbox : true,
							// 					multiple : true,
							onLoadSuccess : function() {
								var ids = infotop.stringToList('${pids}');
								if (ids.length > 0) {
									for ( var i = 0; i < ids.length; i++) {
										if (resourceTree.tree('find', ids[i])) {
											resourceTree.tree('check',
													resourceTree.tree('find',
															ids[i]).target);
										}
									}
								}
								$('#roleGrantLayout').layout('panel', 'west')
										.panel(
												'setTitle',
												formatString('[{0}]角色可以访问的资源',
														'${role.name}'));
								parent.$.messager.progress('close');
							},
							cascadeCheck : false
						});

		$('#role_authorize_form_inputForm').form(
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
						var checknodes = resourceTree.tree('getChecked');
						var ids = [];
						if (checknodes && checknodes.length > 0) {
							for ( var i = 0; i < checknodes.length; i++) {
								ids.push(checknodes[i].id);
							}
						}
						$('#permissionIds').val(ids);
						return isValid;
					},
					success : function(result) {
						parent.$.messager.progress('close');
						result = $.parseJSON(result);
						if (result.success) {
							parent.$.modalDialog.openner_dataGrid
									.datagrid('reload');
							parent.$.modalDialog.openner_dataGrid.datagrid(
									'uncheckAll').datagrid('unselectAll')
									.datagrid('clearSelections');
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

	function checkAll() {
		var nodes = resourceTree.tree('getChecked', 'unchecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				resourceTree.tree('check', nodes[i].target);
			}
		}
	}
	function uncheckAll() {
		var nodes = resourceTree.tree('getChecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				resourceTree.tree('uncheck', nodes[i].target);
			}
		}
	}
	function checkInverse() {
		var unchecknodes = resourceTree.tree('getChecked', 'unchecked');
		var checknodes = resourceTree.tree('getChecked');
		if (unchecknodes && unchecknodes.length > 0) {
			for ( var i = 0; i < unchecknodes.length; i++) {
				resourceTree.tree('check', unchecknodes[i].target);
			}
		}
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				resourceTree.tree('uncheck', checknodes[i].target);
			}
		}
	}
</script>

<div id="roleGrantLayout" class="easyui-layout"
	data-options="fit:true,border:false">
	<div data-options="region:'west'" title="系统资源"
		style="width: 300px; padding: 1px;">
		<div class="well well-small">
			<form id="role_authorize_form_inputForm" method="post"
				action="${ctx}/account/role/authorize">
				<input type="hidden" name="id" id="id" value="${role.id }" />
				<ul id="resourceTree"></ul>
				<input id="permissionIds" name="permissionIds" type="hidden" />
			</form>
		</div>
	</div>
	<div data-options="region:'center'" title=""
		style="overflow: hidden; padding: 10px;">
		<div class="well well-small">
			<span class="label label-success">${role.name}</span>
			<div>${role.remark}</div>
		</div>
		<div class="well well-large">
			<button class="btn btn-success" onclick="checkAll();">全选</button>
			<br /> <br />
			<button class="btn btn-warning" onclick="checkInverse();">反选</button>
			<br /> <br />
			<button class="btn btn-inverse" onclick="uncheckAll();">取消</button>
		</div>
	</div>
</div>