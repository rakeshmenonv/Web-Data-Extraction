<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<style type="text/css">
img {
	cursor: pointer;
}
</style>

<script type="text/javascript">
//定义列表DataGrid ID
var parameter_list_datagrid_id = 'parameter_list_dg';
//定义列表toolbar ID
var parameter_list_toolbar_id = 'parameter_list_toolbar';
//定义操作链接
var parameter_list_DATAGRID_LOAD_URL = '${ctx}/parameter/treeList';
var parameter_list_CREATE_URL =  '${ctx}/parameter/add';
var parameter_list_UPDATE_URL =  '${ctx}/parameter/update/';
var parameter_list_DELETE_URL =  '${ctx}/parameter/delete/';
var parameter_list_REPORT_URL =  '${ctx}/parameter/report';
var parameter_list_VIEW_URL =  '${ctx}/parameter/view/';

// 定义相关的操作按钮
function parameter_list_actionFormatter(value,row,index){
	var str = '';
	str += formatString(
			'<img onclick="parameter_list_addForm(\'{0}\',\'{1}\',\'{2}\',\'{3}\');" src="{4}" title="增加参数"/>',
			            '增加参数',parameter_list_CREATE_URL,'parameter_form_inputForm',row.id,
	                    '${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_add.png');
	str += '&nbsp;';
	str += formatString(
			'<img onclick="parameter_list_updateForm(\'{0}\',\'{1}\',\'{2}\',\'{3}\',\'{4}\');" src="{5}" title="修改参数"/>',
	                    row.id,'修改参数',parameter_list_UPDATE_URL,'parameter_form_inputForm',index,
	                    '${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
	str += '&nbsp;';
	str += formatString('<img onclick="parameter_list_deletenode(\'{0}\',\'{1}\');" src="{2}" title="删除参数"/>',
	                    row.id,parameter_list_DELETE_URL,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
	return str;
}

function parameter_list_formatterVlaue(value,row,index){
	return row.attributes.value;
}


	function parameter_list_formatterPermissionType(value, row, index) {
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
	var parameter_list_list_datagrid_columns = [ [ {
		field : 'id',
		width : 150,
		hidden:true
	}, {
		field : 'text',
		title : '参数名称',
		width : 150,		
		formatter :function(value,row,index){
			return row.attributes.name;
		} 
	}, {
		field : 'value',
		title : '参数编码',
		width : 150 ,
		formatter :function(value,row,index){
			return row.attributes.value;
		} 
	}, {
		field : 'category',
		title : '参数分类',
		width : 150 ,
		formatter :function(value,row,index){
			return row.attributes.category;
		} 
	},{
		field : 'subcategory',
		title : '二级分类',
		width : 150 ,
		formatter :function(value,row,index){
			return row.attributes.subcategory;
		} 
	}, {
		field : 'sort',
		title : '参数排序',
		width : 50 ,
		formatter :function(value,row,index){
			return row.attributes.sort;
		} 
	},{
		field : 'remark',
		title : '备注',
		width : 150 ,
		formatter :function(value,row,index){
			return row.attributes.remark;
		} 
	},{
		field : 'action',
		title : '<spring:message code="menu_operation" />',
		width : 80,
		align : 'center',
		formatter : parameter_list_actionFormatter 
	} ] ];


	/** 初始化DataGrid,加载数据 **/

	function parameter_list_loadDataGrid() {
		parameter_list_listDatagrid = $('#'+parameter_list_datagrid_id).treegrid({
			url : parameter_list_DATAGRID_LOAD_URL,
			idField : 'id',
			treeField : 'text',
			fit : true,
			fitColumns : true,
			pagination : false,
			rownumbers : true,
			border : false,
			pageSize : 50,
			pageList : [ 5, 10, 20, 30, 40, 50 ],
			columns : parameter_list_list_datagrid_columns,
			toolbar : '#'+parameter_list_toolbar_id,
			onLoadSuccess : function() {
				$('#'+parameter_list_toolbar_id).show();
				$('#'+parameter_list_datagrid_id).show();
				parent.$.messager.progress('close');
			},
			onBeforeLoad : function(row, param) {
				if (!row) { // load top level rows
					param.id = 0; // set id=0, indicate to load new page rows
				}
			}
		});
	}
	
	/** 新增页面Form **/
	function parameter_list_addForm(title,createUrl,formName,pid) { 
		parent.$.modalDialog({
			width : 500,
			height:400,
			title : title,
			href : createUrl+"?pid="+pid,
			iconCls : 'icon-application_form_add',
			buttons : [
					{
						text : '保存',
						iconCls : 'icon-save',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = parameter_list_listDatagrid;						
							var inputForm = parent.$.modalDialog.handler.find('#'+formName);
							inputForm.submit();
						}
					}, {
						text : '取消',
						iconCls : 'icon-cross',
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
		});
	}
	
	/** 修改页面Form **/
	function parameter_list_updateForm(id,title,updateUrl,formName,index) {	
		parent.$.modalDialog({
			title : title,
			width : 500,
			height:400,
			href : updateUrl + id,
			iconCls : 'icon-application_form_edit',
			buttons : [
					{
						text : '保存',
						iconCls : 'icon-save',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = parameter_list_listDatagrid;
							parent.$.modalDialog.row_index = index;
							var inputForm = parent.$.modalDialog.handler.find('#'+formName);
							inputForm.submit();
						}
					}, {
						text : '取消',
						iconCls : 'icon-cross',
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
		});
	}
	
	/**
	 * 删除操作
	 * @param id
	 * @param deleteUrl
	 */
	function parameter_list_deletenode(id,deleteUrl) {		
		parent.$.messager.confirm('确认', '是否确定删除该记录?', function(r) {
			if (r) {
				$.post(deleteUrl, {
					id : id
				}, function(result) {
					//var result = eval('(' + result + ')');
					result = $ .parseJSON(result);	
					if (result.success) {
						listDatagrid.treegrid('reload',result.data); // reload the user data
						$.messager.show({ 
							title : '提示',
							msg : result.message
						});
					} else {
						$.messager.show({ 
							title : '提示',
							msg : '<span style="color:red">'+result.message+'</span>'
						});
					}
				});
			}
		});
	}
	
	

	/** 修改页面Form **/
	function parameter_list_authorizeForm(id, title, url, formName, index) {
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
									parent.$.modalDialog.openner_dataGrid = parameter_list_listDatagrid;
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
		parameter_list_loadDataGrid();
	};
</script>

	<div data-options="fit:true,border:false" class="easyui-panel">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false">
				<table id="parameter_list_dg" style="display: none;"></table>
			</div>
			<!--datagrid上方的工具栏-->
			<div id="parameter_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(parameter_list_CREATE_URL,'parameter_form_inputForm',parameter_list_dg,{title:'增加参数'});" 
				class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">增加参数</a> 			
		  	
			<!-- 	<a href="Javascript:updateForm('增加参数',parameter_list_CREATE_URL,'parameter_form_inputForm','0');"
					class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">增加参数</a>  -->
			</div>
		</div>
	</div>

