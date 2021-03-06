﻿<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
			style="height: 65px;overflow: hidden;">
			<form id="pagedatainfo_list_searchForm" method="post"
				style="width:100%;overflow:hidden;">
				<table class="search_table" style="width: 100%;">
					<tr>
						    						<th><spring:message code="pagedatainfo_rowGroupKey" /></th>
						<td><input type="text" name="search_EQ_rowGroupKey"
							value="${ param.search_EQ_rowGroupKey}"
							id="search_EQ_rowGroupKey" /></td>   						<th><spring:message code="pagedatainfo_content" /></th>
						<td><input type="text" name="search_EQ_content"
							value="${ param.search_EQ_content}"
							id="search_EQ_content" /></td>        						<th style="width:20%;">&nbsp;<a href="javascript:void(0);"
							id="pagedatainfo_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="pagedatainfo_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="pagedatainfo_list_dg" style="display: none;"></table>
		</div>
		<div id="pagedatainfo_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(pagedatainfo_list_create_url,'pagedatainfo_form_inputForm',pagedatainfo_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch(pagedatainfo_list_delete_url,pagedatainfo_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div> 
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var pagedatainfo_list_datagrid;
	//列表DataGrid ID
	var pagedatainfo_list_datagrid_id = 'pagedatainfo_list_dg';
	//列表查询表单ID
	var pagedatainfo_list_searchform_id = 'pagedatainfo_list_searchForm';
	//列表toolbar ID
	var pagedatainfo_list_toolbar_id = 'pagedatainfo_list_toolbar';
	//操作链接
	var pagedatainfo_list_create_url =  '${ctx}/pagedatainfo/create';
	var pagedatainfo_list_update_url =  '${ctx}/pagedatainfo/update/';
	var pagedatainfo_list_delete_url =  '${ctx}/pagedatainfo/delete';
	var pagedatainfo_list_view_url =  '${ctx}/pagedatainfo/view/';
	var pagedatainfo_list_datagrid_load_url = '${ctx}/pagedatainfo/findList';
	
	//定义相关的操作按钮
	function pagedatainfo_list_actionFormatter(value,row,index){
		var str = '';	
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'pagedatainfo_form_inputForm\',pagedatainfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				pagedatainfo_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',pagedatainfo_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,pagedatainfo_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				pagedatainfo_list_view_url + row.id);
		str += '&nbsp;';
		return str;
	}
	
	//DataGrid字段设置
	var pagedatainfo_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    	          					{field : 'tableGroupKey',title : '<spring:message code="pagedatainfo_tableGroupKey" />',width : 150,align:'center'},
			          					{field : 'rowGroupKey',title : '<spring:message code="pagedatainfo_rowGroupKey" />',width : 150,align:'center'},
			          					{field : 'content',title : '<spring:message code="pagedatainfo_content" />',width : 150,align:'center'},
			          					{field : 'type',title : '<spring:message code="pagedatainfo_type" />',width : 150,align:'center'},
			          					{field : 'extractedDate',title : '<spring:message code="pagedatainfo_extractedDate" />',width : 150,align:'center'},
			          	                    	{field : 'action',title : '操作',width : 80,align : 'center',formatter : pagedatainfo_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function pagedatainfo_list_loadDataGrid(){		 
		pagedatainfo_list_datagrid = $('#'+pagedatainfo_list_datagrid_id).datagrid({
			url : pagedatainfo_list_datagrid_load_url,
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
			columns : pagedatainfo_list_datagrid_columns,
			toolbar:'#'+pagedatainfo_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+pagedatainfo_list_searchform_id+' table').show();
				$('#'+pagedatainfo_list_datagrid_id).show();
				$('#'+pagedatainfo_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		pagedatainfo_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('pagedatainfo_list_searchBtn','pagedatainfo_list_clearBtn','pagedatainfo_list_searchForm',pagedatainfo_list_datagrid);
	};
</script>


