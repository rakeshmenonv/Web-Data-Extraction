<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div data-options="fit:true" class="easyui-panel" style="height:200px;">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'west',split:true,border:true,title:'查询条件',iconCls:'icon-find'"
			style="width: 500px;overflow: hidden;">
			
		</div>
		<div data-options="region:'center',border:true">
			<table id="pageinfoLog_list_dg" style="display: none;"></table>
		</div>
		<!-- <div id="pageinfoLog_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(pageinfoLog_list_create_url,'pageinfoLog_form_inputForm',pageinfoLog_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch(pageinfoLog_list_delete_url,pageinfoLog_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div>  -->
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var pageinfoLog_list_datagrid;
	//列表DataGrid ID
	var pageinfoLog_list_datagrid_id = 'pageinfoLog_list_dg';
	//列表查询表单ID
	var pageinfoLog_list_searchform_id = 'pageinfoLog_list_searchForm';
	//列表toolbar ID
	var pageinfoLog_list_toolbar_id = 'pageinfoLog_list_toolbar';
	//操作链接
	var pageinfoLog_list_create_url =  '${ctx}/audit/create';
	var pageinfoLog_list_update_url =  '${ctx}/audit/update/';
	var pageinfoLog_list_delete_url =  '${ctx}/audit/delete';
	var pageinfoLog_list_view_url =  '${ctx}/audit/urlInfo/';
	var pageinfoLog_list_datagrid_load_url = '${ctx}/audit/findLogList';
	
	//定义相关的操作按钮
	function pageinfoLog_list_actionFormatter(value,row,index){
		 var str = '';	
		 /*str += formatString(
				'<img onclick="updateForm(\'{0}\',\'pageinfo_form_inputForm\',pageinfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				pageinfoLog_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',pageinfoLog_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,pageinfoLog_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';*/
		str += formatString(
				'<img onclick="indexTabsAddTab(\'href\',{title:\'{0}\',url:\'{1}\',iconCls:\'icon-plugin\'})"  src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				'audit-'+row.id,pageinfoLog_list_view_url + row.id);
		str += '&nbsp;';
		return str; 
	}
	
	//DataGrid字段设置
	var pageinfoLog_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    	          			{field : 'url',title : '<spring:message code="pageinfo_url" />',width : 150,align:'center'},
			          			{field : 'CountOf',title : 'count',width : 150,align:'center'},
			          	        {field : 'action',title : '操作',width : 80,align : 'center',formatter : pageinfoLog_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function pageinfoLog_list_loadDataGrid(){		 
		pageinfoLog_list_datagrid = $('#'+pageinfoLog_list_datagrid_id).datagrid({
			url : pageinfoLog_list_datagrid_load_url,
			fit : true,
			border : false,
			fitColumns : true,
			singleSelect : false,
			striped : true,
			pagination : true,
			rownumbers : true,
			idField : 'id',
			pageSize : 15,
			pageList : [ 5, 10,15, 20, 30, 40, 50 ],
			columns : pageinfoLog_list_datagrid_columns,
			toolbar:'#'+pageinfoLog_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+pageinfoLog_list_searchform_id+' table').show();
				$('#'+pageinfoLog_list_datagrid_id).show();
				$('#'+pageinfoLog_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		pageinfoLog_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('pageinfoLog_list_searchBtn','pageinfoLog_list_clearBtn','pageinfo_list_searchForm',pageinfoLog_list_datagrid);
	};
</script>


