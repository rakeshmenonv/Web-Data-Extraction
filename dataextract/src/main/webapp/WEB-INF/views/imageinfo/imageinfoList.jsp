<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
			style="height: 65px;overflow: hidden;">
			<form id="imageinfo_list_searchForm" method="post"
				style="width:100%;overflow:hidden;">
				<table class="search_table" style="width: 100%;">
					<tr>
						    						<th><spring:message code="imageinfo_localImageUrl" /></th>
						<td><input type="text" name="search_EQ_localImageUrl"
							value="${ param.search_EQ_localImageUrl}"
							id="search_EQ_localImageUrl" /></td>   						<th><spring:message code="imageinfo_PageInfoId" /></th>
						<td><input type="text" name="search_EQ_PageInfoId"
							value="${ param.search_EQ_PageInfoId}"
							id="search_EQ_PageInfoId" /></td>    						<th style="width:20%;">&nbsp;<a href="javascript:void(0);"
							id="imageinfo_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="imageinfo_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="imageinfo_list_dg" style="display: none;"></table>
		</div>
		<div id="imageinfo_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(imageinfo_list_create_url,'imageinfo_form_inputForm',imageinfo_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch(imageinfo_list_delete_url,imageinfo_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div> 
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var imageinfo_list_datagrid;
	//列表DataGrid ID
	var imageinfo_list_datagrid_id = 'imageinfo_list_dg';
	//列表查询表单ID
	var imageinfo_list_searchform_id = 'imageinfo_list_searchForm';
	//列表toolbar ID
	var imageinfo_list_toolbar_id = 'imageinfo_list_toolbar';
	//操作链接
	var imageinfo_list_create_url =  '${ctx}/imageinfo/create';
	var imageinfo_list_update_url =  '${ctx}/imageinfo/update/';
	var imageinfo_list_delete_url =  '${ctx}/imageinfo/delete';
	var imageinfo_list_view_url =  '${ctx}/imageinfo/view/';
	var imageinfo_list_datagrid_load_url = '${ctx}/imageinfo/findList';
	
	//定义相关的操作按钮
	function imageinfo_list_actionFormatter(value,row,index){
		var str = '';	
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'imageinfo_form_inputForm\',imageinfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				${symbol_dollar}!{bealowerNmae}_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',imageinfo_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,imageinfo_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				imageinfo_list_view_url + row.id);
		str += '&nbsp;';
		return str;
	}
	
	//DataGrid字段设置
	var imageinfo_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    	          					{field : 'SourceImageUrl',title : '<spring:message code="imageinfo_SourceImageUrl" />',width : 150,align:'center'},
			          					{field : 'localImageUrl',title : '<spring:message code="imageinfo_localImageUrl" />',width : 150,align:'center'},
			          					{field : 'PageInfoId',title : '<spring:message code="imageinfo_PageInfoId" />',width : 150,align:'center'},
			          	                    	{field : 'action',title : '操作',width : 80,align : 'center',formatter : imageinfo_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function imageinfo_list_loadDataGrid(){		 
		imageinfo_list_datagrid = $('#'+imageinfo_list_datagrid_id).datagrid({
			url : imageinfo_list_datagrid_load_url,
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
			columns : imageinfo_list_datagrid_columns,
			toolbar:'#'+imageinfo_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+imageinfo_list_searchform_id+' table').show();
				$('#'+imageinfo_list_datagrid_id).show();
				$('#'+imageinfo_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		imageinfo_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('imageinfo_list_searchBtn','imageinfo_list_clearBtn','imageinfo_list_searchForm',imageinfo_list_datagrid);
	};
</script>


