<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
			style="height: 65px;overflow: hidden;">
			<form id="pageurlinfo_list_searchForm" method="post"
				style="width:100%;overflow:hidden;">
				<table class="search_table" style="width: 100%;">
					<tr>
						    						<th><spring:message code="pageurlinfo_element" /></th>
						<td><input type="text" name="search_EQ_element"
							value="${ param.search_EQ_element}"
							id="search_EQ_element" /></td>   						<th><spring:message code="pageurlinfo_attribute" /></th>
						<td><input type="text" name="search_EQ_attribute"
							value="${ param.search_EQ_attribute}"
							id="search_EQ_attribute" /></td>        						<th style="width:20%;">&nbsp;<a href="javascript:void(0);"
							id="pageurlinfo_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="pageurlinfo_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="pageurlinfo_list_dg" style="display: none;"></table>
		</div>
		<div id="pageurlinfo_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(pageurlinfo_list_create_url,'pageurlinfo_form_inputForm',pageurlinfo_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch(pageurlinfo_list_delete_url,pageurlinfo_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div> 
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var pageurlinfo_list_datagrid;
	//列表DataGrid ID
	var pageurlinfo_list_datagrid_id = 'pageurlinfo_list_dg';
	//列表查询表单ID
	var pageurlinfo_list_searchform_id = 'pageurlinfo_list_searchForm';
	//列表toolbar ID
	var pageurlinfo_list_toolbar_id = 'pageurlinfo_list_toolbar';
	//操作链接
	var pageurlinfo_list_create_url =  '${ctx}/pageurlinfo/create';
	var pageurlinfo_list_update_url =  '${ctx}/pageurlinfo/update/';
	var pageurlinfo_list_delete_url =  '${ctx}/pageurlinfo/delete';
	var pageurlinfo_list_view_url =  '${ctx}/pageurlinfo/view/';
	var pageurlinfo_list_datagrid_load_url = '${ctx}/pageurlinfo/findList';
	
	//定义相关的操作按钮
	function pageurlinfo_list_actionFormatter(value,row,index){
		var str = '';	
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'pageurlinfo_form_inputForm\',pageurlinfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				pageurlinfo_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',pageurlinfo_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,pageurlinfo_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				pageurlinfo_list_view_url + row.id);
		str += '&nbsp;';
		return str;
	}
	
	//DataGrid字段设置
	var pageurlinfo_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    	          					{field : 'url',title : '<spring:message code="pageurlinfo_url" />',width : 150,align:'center'},
			          					{field : 'element',title : '<spring:message code="pageurlinfo_element" />',width : 150,align:'center'},
			          					{field : 'attribute',title : '<spring:message code="pageurlinfo_attribute" />',width : 150,align:'center'},
			          					{field : 'value',title : '<spring:message code="pageurlinfo_value" />',width : 150,align:'center'},
			          					{field : 'extractedDate',title : '<spring:message code="pageurlinfo_extractedDate" />',width : 150,align:'center'},
			          	                    	{field : 'action',title : '操作',width : 80,align : 'center',formatter : pageurlinfo_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function pageurlinfo_list_loadDataGrid(){		 
		pageurlinfo_list_datagrid = $('#'+pageurlinfo_list_datagrid_id).datagrid({
			url : pageurlinfo_list_datagrid_load_url,
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
			columns : pageurlinfo_list_datagrid_columns,
			toolbar:'#'+pageurlinfo_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+pageurlinfo_list_searchform_id+' table').show();
				$('#'+pageurlinfo_list_datagrid_id).show();
				$('#'+pageurlinfo_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		pageurlinfo_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('pageurlinfo_list_searchBtn','pageurlinfo_list_clearBtn','pageurlinfo_list_searchForm',pageurlinfo_list_datagrid);
	};
</script>


