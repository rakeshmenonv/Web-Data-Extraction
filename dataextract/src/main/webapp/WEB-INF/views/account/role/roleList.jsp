<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false">
			<table id="role_list_datagrid" style="display: none;"></table>
		</div>
		<!--datagrid上方的工具栏-->
		<div id="role_list_toolbar" style="display: none;">
			<a href="javascript:updateForm(role_list_CREATE_URL,'role_form_inputForm',role_list_datagrid,{title:'添加角色',width:500,height:300});"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add',plain:false">添加</a>

		</div>
	</div>
</div>
<script type="text/javascript">
	var role_list_datagrid;
	//定义列表DataGrid ID
	var role_list_datagrid_id = 'role_list_datagrid';
	//定义列表toolbar ID
	var role_list_toolbar_id = 'role_list_toolbar';
	//定义操作链接
	var role_list_DATAGRID_LOAD_URL = '${ctx}/account/role/findList';
	var role_list_AUTHORIZE_URL = '${ctx}/account/role/authorize/';
	var role_list_CREATE_URL = '${ctx}/account/role/create';
	var role_list_UPDATE_URL = '${ctx}/account/role/update/';
	var role_list_DELETE_URL = '${ctx}/account/role/delete';
	var role_list_VIEW_URL = '${ctx}/account/role/view/';

	// 定义相关的操作按钮
	function role_list_actionFormatter(value, row, index) {
		var str = '';
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'role_form_inputForm\',role_list_datagrid,{title:\'编辑角色\',width:500,height:300});" src="{1}" title="编辑"/>',
				 role_list_UPDATE_URL + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		

		str += formatString(
				'<img onclick="deleteOne(\'{0}\',\'{1}\',role_list_datagrid);" src="{2}" title="删除"/>',
				row.id, role_list_DELETE_URL,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';


		str += formatString(
				'<img onclick="role_list_authorizeForm(\'{0}\',\'{1}\',\'{2}\',\'{3}\',\'{4}\');" src="{5}" title="授权"/>',
				row.id, '帐号授权', role_list_AUTHORIZE_URL, 'role_authorize_form_inputForm',
				index, '${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/key.png');
		

		return str;
	}


	// DataGrid字段设置
	var role_list_list_datagrid_columns = [ [ {
		field : 'id',
		width : 150,
		hidden : true
	}, {
		field : 'name',
		title : '名称',
		width : 150
	}, {
		field : 'remark',
		title : '备注',
		width : 150
	}, {
		field : 'action',
		title : '<spring:message code="menu_operation" />',
		width : 80,
		align : 'center',
		formatter : role_list_actionFormatter
	} ] ];

	/** 初始化DataGrid,加载数据 **/

	function role_list_loadDataGrid() {
		role_list_datagrid = $('#' + role_list_datagrid_id).datagrid({
			url : role_list_DATAGRID_LOAD_URL,
			fit : true,
			border : false,
			fitColumns : false,
			striped : true,
			pagination : true,
			rownumbers : true,
			singleSelect : false,
			idField : 'id',
			pageSize : 10,
			pageList : [ 5, 10, 20, 30, 40, 50 ],
			columns : role_list_list_datagrid_columns,
			toolbar : '#' + role_list_toolbar_id,
			onLoadSuccess : function() {
				$(this).datagrid('tooltip');
				$('#' + role_list_datagrid_id).show();
				parent.$.messager.progress('close');
			}
		});
	}

	/** 修改页面Form **/
	function role_list_authorizeForm(id, title, url, formName, index) {
		parent.$
				.modalDialog({
					title : title,
					width : 500,
					height : 500,
					href : url + id,
					iconCls : 'icon-application_form_edit',
					buttons : [
							{
								text : '保存',
								iconCls : 'icon-save',
								handler : function() {
									parent.$.modalDialog.openner_dataGrid = role_list_datagrid;
									parent.$.modalDialog.row_index = index;
									var inputForm = parent.$.modalDialog.handler
											.find('#' + formName);
									inputForm.submit();
								}
							},
							{
								text : '取消',
								iconCls : 'icon-cross',
								handler : function() {
									parent.$.modalDialog.handler
											.dialog('close');
								}
							} ]
				});
	}

	$.parser.onComplete = function() {
		// 	$('#dg').treegrid('loadData',data);	
		//加载DataGrid数据
		role_list_loadDataGrid();
	};
</script>



