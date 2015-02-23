<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
img {
	cursor: pointer;
}
</style>
<script type="text/javascript"
	src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/plugins/datagrid-groupview.js"></script>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'north',title:'查询条件',border:false,iconCls : 'icon-find'"
			style="height: 13%;">
			<form id="user_list_searchForm" method="post" style="display: none;">
				<table class="search_table" style="width: 100%;">
					<tr>
						<th><spring:message code="account_loginName" />:</th>
						<td><input class="inputmiddle" type="text"
							name="search_LIKE_loginName"
							value="${param.search_LIKE_loginName}" id="search_LIKE_loginName" /></td>
						<th><spring:message code="account_name" />:</th>
						<td><input class="inputmiddle" type="text"
							name="search_LIKE_name" value="${param.search_LIKE_name}"
							id="search_LIKE_name" /></td>
						<th>用户类型:</th>
						<td><select id="search_EQ_userType" name="search_EQ_userType">
								<option value="">请选择</option>
								<option value="0">市级用户</option>
								<option value="1">帮扶组用户</option>
								<option value="2">县级用户</option>
								<option value="3">企业用户</option>
						</select></td>
						<th style="width: 20%;">&nbsp;<a href="javascript:void(0);"
							id="user_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="user_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div>

		<div data-options="region:'center',border:false">
			<table id="user_list_datagrid" style="display: none;"></table>
		</div>
		<!--datagrid上方的工具栏-->
		<div id="user_list_toolbar" style="display: none;">
			<a href="#" class="easyui-menubutton"
				data-options="menu:'#user_add_menu',iconCls:'icon-user_add',plain:true">添加</a>
		</div>

		<div id="user_add_menu" style="display: none;">
			<div data-options="iconCls:'icon-add'">
				<a
					href="javascript:updateForm(user_list_CREATE_URL+'/0','user_form_inputForm',user_list_datagrid,{title:'添加市级用户',width:550,height:300});">普通用户</a>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var user_list_datagrid;
	//定义列表DataGrid ID
	var user_list_datagrid_id = 'user_list_datagrid';
	//定义列表查询表单ID
	var user_list_searchform_id = 'user_list_searchForm';
	//定义列表toolbar ID
	var user_list_toolbar_id = 'user_list_toolbar';
	//定义操作链接
	var user_list_DATAGRID_LOAD_URL = '${ctx}/account/user/findList';
	var user_list_AUTHORIZE_URL = '${ctx}/account/user/authorize/';
	var user_list_CREATE_URL = '${ctx}/register';
	var user_list_RESETPASSWORD_URL = '${ctx}/account/user/resetpassword/';
	var user_list_UPDATE_URL = '${ctx}/account/user/update/';
	var user_list_DELETE_URL = '${ctx}/account/user/delete';
	var user_list_REPORT_URL = '${ctx}/account/user/report';
	var user_list_VIEW_URL = '${ctx}/account/user/view/';

	function userTypeFormatter(value, row, index) {
		switch (value) {
		default:
			return '普通用户';
			break;
		}
	}
	// 定义相关的操作按钮
	function user_list_actionFormatter(value, row, index) {
		var str = '';
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'user_form_inputForm\',user_list_datagrid,{title:\'修改帐号信息\',width:550,height:300});" src="{1}" title="编辑"/>',
				user_list_UPDATE_URL + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';

		str += formatString(
				'<img onclick="deleteOne(\'{0}\',\'{1}\',user_list_datagrid);" src="{2}" title="删除"/>',
				row.id,
				user_list_DELETE_URL,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';

		str += formatString(
				'<img onclick="user_list_authorizeForm(\'{0}\',\'{1}\',\'{2}\',\'{3}\',\'{4}\');" src="{5}" title="授权"/>',
				row.id, '帐号授权', user_list_AUTHORIZE_URL, 'user_form_inputForm',
				index,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/key.png');
		str += formatString(
				'<img onclick="resetPassword(\'{0}\',\'{1}\');" src="{2}" title="重置密码为：123456"/>',
				row.id, user_list_RESETPASSWORD_URL,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/reload.png');
		str += '&nbsp;';

		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				user_list_VIEW_URL + row.id, '{width:550,heigth:300}');
		str += '&nbsp;';

		return str;
	}

	// DataGrid字段设置
	var user_list_datagrid_columns = [ [ {
		field : 'id',
		width : 150,
		hidden : true
	}, {
		field : 'loginName',
		title : '<spring:message code="account_loginName" />',
		width : 150
	}, {
		field : 'name',
		title : '<spring:message code="account_name" />',
		width : 150
	}, {
		field : 'userType',
		title : '用户类型',
		width : 150,
		formatter : userTypeFormatter
	}, {
		field : 'action',
		title : '<spring:message code="menu_operation" />',
		width : 180,
		align : 'center',
		formatter : user_list_actionFormatter
	} ] ];

	/** 初始化DataGrid,加载数据 **/
	function user_list_loadDataGrid() {
		user_list_datagrid = $('#' + user_list_datagrid_id).datagrid({
			url : user_list_DATAGRID_LOAD_URL,
			fit : true,
			border : false,
			fitColumns : false,
			striped : true,
			pagination : true,
			rownumbers : true,
			singleSelect : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 5, 10, 20, 30, 40, 50 ],
			columns : user_list_datagrid_columns,
			toolbar : '#' + user_list_toolbar_id,
			view : groupview,
			groupField : 'userType',
			groupFormatter : function(value, rows) {
				switch (value) {
				default:
					return '普通用户';
					break;
				}
			},
			onLoadSuccess : function() {
				$(this).datagrid('tooltip');
				$('#' + user_list_searchform_id).show();
				$('#' + user_list_toolbar_id).show();
				$('#' + user_list_datagrid_id).show();
				parent.$.messager.progress('close');
			}
		});
	}

	/** 分配角色Form **/
	function user_list_authorizeForm(id, title, url, formName, index) {
		parent.$
				.modalDialog({
					title : title,
					width : 500,
					height : 300,
					href : url + id,
					iconCls : 'icon-application_form_edit',
					buttons : [
							{
								text : '保存',
								iconCls : 'icon-save',
								handler : function() {
									parent.$.modalDialog.openner_dataGrid = user_list_datagrid;
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

	/*重置密码*/
	function resetPassword(id, RESETPASSWORD_URL) {
		parent.$.messager.confirm('确认', '是否确定重置密码?', function(r) {
			if (r) {
				$.post(RESETPASSWORD_URL, {
					id : id
				}, function(result) {
					if (result.success) {
						user_list_datagrid.datagrid('reload'); // reload the user data;
						$.messager.show({ // show error message
							title : '提示',
							msg : result.message
						});
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
				}, 'JSON');
			}
		});
	}

	$.parser.onComplete = function() {
		//加载DataGrid数据
		user_list_loadDataGrid();
		//绑定按钮事件
		bindSearchBtn('user_list_searchBtn', 'user_list_clearBtn',
				'user_list_searchForm', user_list_datagrid);
	};
</script>

