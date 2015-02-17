<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
#set($S="${") #set($D="s") #set($b="{") 
<div data-options="fit:true" class="easyui-panel">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
			style="height: 65px;overflow: hidden;">
			<form id="$!{bealowerNmae}_list_searchForm" method="post"
				style="width:100%;overflow:hidden;">
				<table class="search_table" style="width: 100%;">
					<tr>
						#set ($l=0) #foreach($field in $!fields) #set($l=$l+1) #if($l>1 && $l<=3)
						<th><spring:message code="$!{bealowerNmae}_$!{field.name}" /></th>
						<td><input type="text" name="search_EQ_$!{field.name}"
							value="$S param.search_EQ_$!{field.name}}"
							id="search_EQ_$!{field.name}" /></td> #end #end
						<th style="width:20%;">&nbsp;<a href="javascript:void(0);"
							id="$!{bealowerNmae}_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="$!{bealowerNmae}_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="$!{bealowerNmae}_list_dg" style="display: none;"></table>
		</div>
		<div id="$!{bealowerNmae}_list_toolbar" style="display: none;">
				<a href="javascript:updateForm($!{bealowerNmae}_list_create_url,'$!{bealowerNmae}_form_inputForm',$!{bealowerNmae}_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch($!{bealowerNmae}_list_delete_url,$!{bealowerNmae}_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div> 
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var $!{bealowerNmae}_list_datagrid;
	//列表DataGrid ID
	var $!{bealowerNmae}_list_datagrid_id = '${bealowerNmae}_list_dg';
	//列表查询表单ID
	var $!{bealowerNmae}_list_searchform_id = '${bealowerNmae}_list_searchForm';
	//列表toolbar ID
	var $!{bealowerNmae}_list_toolbar_id = '${bealowerNmae}_list_toolbar';
	//操作链接
	var $!{bealowerNmae}_list_create_url =  '${ctx}/$!bealowerNmae/create';
	var $!{bealowerNmae}_list_update_url =  '${ctx}/$!bealowerNmae/update/';
	var $!{bealowerNmae}_list_delete_url =  '${ctx}/$!bealowerNmae/delete';
	var $!{bealowerNmae}_list_view_url =  '${ctx}/$!bealowerNmae/view/';
	var $!{bealowerNmae}_list_datagrid_load_url = '${ctx}/$!bealowerNmae/findList';
	
	//定义相关的操作按钮
	function $!{bealowerNmae}_list_actionFormatter(value,row,index){
		var str = '';	
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'$!{bealowerNmae}_form_inputForm\',$!{bealowerNmae}_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				${symbol_dollar}!{bealowerNmae}_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',$!{bealowerNmae}_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,$!{bealowerNmae}_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/view.png" title="查看"/>',
				$!{bealowerNmae}_list_view_url + row.id);
		str += '&nbsp;';
		return str;
	}
	
	//DataGrid字段设置
	var $!{bealowerNmae}_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    #foreach($field in $!fields)
	          #if($!field.name !="id")
					{field : '$!{field.name}',title : '<spring:message code="$!{bealowerNmae}_$!{field.name}" />',width : 150,align:'center'},
		#end#end							
	                    	{field : 'action',title : '操作',width : 80,align : 'center',formatter : $!{bealowerNmae}_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function $!{bealowerNmae}_list_loadDataGrid(){		 
		$!{bealowerNmae}_list_datagrid = $('#'+$!{bealowerNmae}_list_datagrid_id).datagrid({
			url : $!{bealowerNmae}_list_datagrid_load_url,
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
			columns : $!{bealowerNmae}_list_datagrid_columns,
			toolbar:'#'+$!{bealowerNmae}_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+$!{bealowerNmae}_list_searchform_id+' table').show();
				$('#'+$!{bealowerNmae}_list_datagrid_id).show();
				$('#'+$!{bealowerNmae}_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		$!{bealowerNmae}_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('$!{bealowerNmae}_list_searchBtn','$!{bealowerNmae}_list_clearBtn','$!{bealowerNmae}_list_searchForm',$!{bealowerNmae}_list_datagrid);
	};
</script>


