<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
img {
	cursor: pointer;
}
</style>

<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false">
			<table id="permission_list_datagrid" style="display: none;"></table>
		</div>
		<div id="permission_list_toolbar" style="display: none;">
			<a
				href="javascript:updateForm(permission_list_CREATE_URL,'permission_form_inputForm',permission_list_datagrid,{title:'添加权限',width:600,height:300});"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add',plain:false">添加</a>

		</div>
	</div>
</div>

<script type="text/javascript">
	var permission_list_datagrid;
	//定义列表DataGrid ID
	var permission_list_datagrid_id = 'permission_list_datagrid';
	//定义列表toolbar ID
	var permission_list_toolbar_id = 'permission_list_toolbar';
	//定义操作链接
	var permission_list_DATAGRID_LOAD_URL = '${ctx}/account/permission/treeList';
	var permission_list_AUTHORIZE_URL = '${ctx}/account/permission/authorize/';
	var permission_list_CREATE_URL = '${ctx}/account/permission/create';
	var permission_list_UPDATE_URL = '${ctx}/account/permission/update/';
	var permission_list_DELETE_URL = '${ctx}/account/permission/delete';
	var permission_list_REPORT_URL = '${ctx}/account/permission/report';
	var permission_list_VIEW_URL = '${ctx}/account/permission/view/';

	// 定义相关的操作按钮
	function permission_list_actionFormatter(value, row, index) {
		var str = '';
		str += formatString(
				'<img onclick="permission_list_addPermission(\'{0}\',\'{1}\',\'{2}\',\'{3}\',\'{4}\',\'{5}\');" src="{6}" title="增加参数"/>',
				'增加参数', permission_list_CREATE_URL, 'permission_form_inputForm', row.id, 600, 300,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_add.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'permission_form_inputForm\',permission_list_datagrid,{title:\'编辑权限\',width:600,height:300});" src="{1}" title="编辑"/>',
				permission_list_UPDATE_URL + row.id, 
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';

		str += formatString(
				'<img onclick="treegridDeleteOne(\'{0}\',\'{1}\',permission_list_datagrid);" src="{2}" title="删除"/>',
				row.id, permission_list_DELETE_URL,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		return str;
	}

	function permission_list_formatterVlaue(value, row, index) {
		return row.attributes.value;
	}

	function permission_list_formatterPermissionType(value, row, index) {
		if (row.attributes.permissionType == 1) {
			return "系统";
		}
		if (row.attributes.permissionType == 2) {
			return "菜单";
		}
		if (row.attributes.permissionType == 3) {
			return "功能";
		}
	}

	// DataGrid字段设置
	var permission_list_list_datagrid_columns = [ [ {
		field : 'id',
		width : 150,
		hidden : true
	}, {
		field : 'text',
		title : '名称',
		width : 150
	}, {
		field : 'permissionType',
		title : '类型',
		width : 150,
		formatter : permission_list_formatterPermissionType
	}, {
		field : 'value',
		title : '值',
		width : 150,
		formatter : permission_list_formatterVlaue
	}, {
		field : 'action',
		title : '<spring:message code="menu_operation" />',
		width : 80,
		align : 'center',
		formatter : permission_list_actionFormatter
	} ] ];

	/** 初始化DataGrid,加载数据 **/

	function permission_list_loadDataGrid() {
		permission_list_datagrid = $('#' + permission_list_datagrid_id).treegrid({
			url : permission_list_DATAGRID_LOAD_URL,
			idField : 'id',
			treeField : 'text',
			fit : true,
			fitColumns : false,
			pagination : false,
			rownumbers : false,
			border : false,
			columns : permission_list_list_datagrid_columns,
			toolbar : '#' + permission_list_toolbar_id,
			onLoadSuccess : function(row, data) {
				$(this).datagrid('tooltip');
				$('#' + permission_list_toolbar_id).show();
				$('#' + permission_list_datagrid_id).show();
				parent.$.messager.progress('close');
			},
			onBeforeLoad : function(row, param) {
				if (!row) { // load top level rows
					param.id = 0; // set id=0, indicate to load new page rows
				}
			},
			rowStyler : function(row) {
				if (row.attributes.permissionType == 1) {
					return 'color:#6293BB;';
				}
				if (row.attributes.permissionType == 2) {
					return 'color:#629300;';
				}
				if (row.attributes.permissionType == 3) {
					return 'color:#620000;';
				}
			}
		});
	}


	/** 新增页面Form * */
	function permission_list_addPermission(title, createUrl, formName, pid, width, height) {
		parent.$
				.modalDialog({
					width : width,
					height : height,
					title : title,
					href : createUrl + "?pid=" + pid,
					iconCls : 'icon-application_form_add',
					buttons : [
							{
								text : '保存',
								iconCls : 'icon-save',
								id : 'formSaveBtn',
								iconCls : 'icon-save',
								handler : function() {
									parent.$.modalDialog.openner_dataGrid = permission_list_datagrid;
									var inputForm = parent.$.modalDialog.handler
											.find('#' + formName);
									var isValid = inputForm.form('validate');
									if (isValid) {
										$("#formSaveBtn").linkbutton('disable');
										$("#formCancelBtn").linkbutton(
												'disable');
										var file_upload = parent.$.modalDialog.handler
												.find('#file_upload');
										if (file_upload.length > 0) {
											var swfuploadify = file_upload
													.data('uploadify');
											if (swfuploadify.queueData.queueLength > 0) {
												file_upload.uploadify('upload',
														'*');
											} else {
												inputForm.submit();
											}
										} else {
											inputForm.submit();
										}
									}
								}
							},
							{
								text : '取消',
								id : 'formCancelBtn',
								iconCls : 'icon-cross',
								handler : function() {
									parent.$.modalDialog.handler
											.dialog('close');
								}
							} ]
				});
	}
	$.parser.onComplete = function() {
		permission_list_loadDataGrid();
	};
</script>