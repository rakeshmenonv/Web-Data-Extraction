<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
			style="height: 65px;overflow: hidden;">
			<form id="texttaginfo_list_searchForm" method="post"
				style="width:100%;overflow:hidden;">
				<table class="search_table" style="width: 100%;">
					<tr>
						    						<th><spring:message code="texttaginfo_value" /></th>
						<td><input type="text" name="search_EQ_value"
							value="${ param.search_EQ_value}"
							id="search_EQ_value" /></td>   						<th><spring:message code="texttaginfo_PageInfoId" /></th>
						<td><input type="text" name="search_EQ_PageInfoId"
							value="${ param.search_EQ_PageInfoId}"
							id="search_EQ_PageInfoId" /></td>    						<th style="width:20%;">&nbsp;<a href="javascript:void(0);"
							id="texttaginfo_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="texttaginfo_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="texttaginfo_list_dg" style="display: none;"></table>
		</div>
		<div id="texttaginfo_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(texttaginfo_list_create_url,'texttaginfo_form_inputForm',texttaginfo_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch(texttaginfo_list_delete_url,texttaginfo_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div> 
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var texttaginfo_list_datagrid;
	//列表DataGrid ID
	var texttaginfo_list_datagrid_id = 'texttaginfo_list_dg';
	//列表查询表单ID
	var texttaginfo_list_searchform_id = 'texttaginfo_list_searchForm';
	//列表toolbar ID
	var texttaginfo_list_toolbar_id = 'texttaginfo_list_toolbar';
	//操作链接
	var texttaginfo_list_create_url =  '${ctx}/texttaginfo/create';
	var texttaginfo_list_update_url =  '${ctx}/texttaginfo/update/';
	var texttaginfo_list_delete_url =  '${ctx}/texttaginfo/delete';
	var texttaginfo_list_view_url =  '${ctx}/texttaginfo/view/';
	var texttaginfo_list_datagrid_load_url = '${ctx}/texttaginfo/findList';
	
	//定义相关的操作按钮
	function texttaginfo_list_actionFormatter(value,row,index){
		var str = '';	
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'texttaginfo_form_inputForm\',texttaginfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				${symbol_dollar}!{bealowerNmae}_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',texttaginfo_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,texttaginfo_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				texttaginfo_list_view_url + row.id);
		str += '&nbsp;';
		return str;
	}
	
	//DataGrid字段设置
	var texttaginfo_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    	          					{field : 'tag',title : '<spring:message code="texttaginfo_tag" />',width : 150,align:'center'},
			          					{field : 'value',title : '<spring:message code="texttaginfo_value" />',width : 150,align:'center'},
			          					{field : 'PageInfoId',title : '<spring:message code="texttaginfo_PageInfoId" />',width : 150,align:'center'},
			          	                    	{field : 'action',title : '操作',width : 80,align : 'center',formatter : texttaginfo_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function texttaginfo_list_loadDataGrid(){		 
		texttaginfo_list_datagrid = $('#'+texttaginfo_list_datagrid_id).datagrid({
			url : texttaginfo_list_datagrid_load_url,
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
			columns : texttaginfo_list_datagrid_columns,
			toolbar:'#'+texttaginfo_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+texttaginfo_list_searchform_id+' table').show();
				$('#'+texttaginfo_list_datagrid_id).show();
				$('#'+texttaginfo_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		texttaginfo_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('texttaginfo_list_searchBtn','texttaginfo_list_clearBtn','texttaginfo_list_searchForm',texttaginfo_list_datagrid);
	};
</script>


