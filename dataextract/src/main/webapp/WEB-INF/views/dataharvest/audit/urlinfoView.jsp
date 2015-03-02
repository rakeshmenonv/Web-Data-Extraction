<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div data-options="fit:true" class="easyui-panel" style="height:200px;">
	<div class="easyui-layout" data-options="fit:true">
		<%-- <div
			data-options="region:'north',border:false,title:'查询条件',iconCls:'icon-find'"
			style="height: 65px;overflow: hidden;">
			<form id="pageinfoLog_list_searchForm" method="post"
				style="width:100%;overflow:hidden;">
				<table class="search_table" style="width: 100%;">
					<tr>
						    						<th><spring:message code="pageinfo_url" /></th>
						<td><input type="text" name="search_EQ_url"
							value="${ param.search_EQ_url}"
							id="search_EQ_url" /></td>   						<th><spring:message code="pageinfo_id" /></th>
						<td><input type="text" name="search_EQ_id"
							value="${ param.search_EQ_id}"
							id="search_EQ_id" /></td>  						<th style="width:20%;">&nbsp;<a href="javascript:void(0);"
							id="pageinfo_list_searchBtn">查询</a>&nbsp;<a
							href="javascript:void(0);" id="pageinfo_list_clearBtn">清空</a></th>
					</tr>
				</table>
			</form>
		</div> --%>
		<div
			data-options="region:'west',split:true,border:true,title:'&nbsp;<spring:message code="webharvest_lastweekstatistics" />',iconCls:'icon-chart_pie'"
			style="width: 503px;overflow: hidden;">
			<!-- <div id="ny_bt3" class="ny_bt" style="width:95%; height:400px;" >
			
			</div> -->
			 <center><h3><div style="color:black">${url }</div></h3></center>
			<div id="piechartloglist" style="width:100%; height:400px;"></div>
			
		</div>
		<div data-options="region:'center',border:true">
			<table id="urlinfo_list_dg" style="display: none;"></table>
		</div>
		<!-- <div id="pageinfoLog_list_toolbar" style="display: none;">
				<a href="javascript:updateForm(pageinfoLog_list_create_url,'pageinfoLog_form_inputForm',pageinfoLog_list_datagrid,{title:'新增信息'});" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">添加</a> 			
		  	  <a href="javascript:deleteBatch(pageinfoLog_list_delete_url,pageinfoLog_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div>  -->
	</div>
</div>
<script type="text/javascript">
	//列表DataGrid
	var urlinfo_list_datagrid;
	//列表DataGrid ID
	var urlinfo_list_datagrid_id = 'urlinfo_list_dg';
	//列表查询表单ID
	var urlinfo_list_searchform_id = 'urlinfo_list_searchForm';
	//列表toolbar ID
	var urlinfo_list_toolbar_id = 'urlinfo_list_toolbar';
	//操作链接
	/* var pageinfoLog_list_create_url =  '${ctx}/pageinfo/create';
	var pageinfoLog_list_update_url =  '${ctx}/pageinfo/update/';
	var pageinfoLog_list_delete_url =  '${ctx}/pageinfo/delete';
	var pageinfoLog_list_view_url =  '${ctx}/pageinfo/view/'; */
	var urlinfo_list_datagrid_load_url = '${ctx}/audit/getUrlInfo/?id=${id}';
	var urlinfo_list_view_url =  '${ctx}/dataharvest/showdata/'; 
	var user_list_stop_url = '${ctx}/audit/stopScheduler/';
	var reschedule_url =  '${ctx}/audit/reschedule/';
	var user_list_search_url = '${ctx}/dataharvest/extracted/';
	var showLogUrl="${ctx}/dataharvest/showlog";
	//定义相关的操作按钮
	function urlinfo_list_actionFormatter(value,row,index){
		 var str = '';	
		 /* str += formatString(
				'<img onclick="updateForm(\'{0}\',\'pageinfo_form_inputForm\',pageinfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="编辑"/>',
				pageinfoLog_list_update_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_edit.png');
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',pageinfoLog_list_datagrid);" src="{2}" title="删除"/>',
		                    row.id,pageinfoLog_list_delete_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;'; */
		str += formatString(
				'<img onclick="view(\'{0}\',\'{1}\');" src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/eye.png" title="查看"/>',
				urlinfo_list_view_url + row.id);
		str += '&nbsp;';
		if(row.jobon){
			
			str += formatString(
					'<img onclick="schedulerEvent(\'{0}\',\'{1}\');" src="{2}" title="停止调度"/>',
					row.id, user_list_stop_url,
					'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/hourglass_delete.png');
			str += '&nbsp;';
		}
		str += formatString(
				'<img onclick="updateForm(\'{0}\',\'reschedule_form_inputForm\',urlinfo_list_datagrid,{title:\'编辑信息\'});" src="{1}" title="重新安排e"/>',
				reschedule_url + row.id,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/hourglass_add.png');
		str += '&nbsp;';
		str += formatString(
				'<img onclick="searchEvent(\'{0}\',\'{1}\');" src="{2}" title="搜索"/>',
				row.id, user_list_search_url,
				'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/search.png');
		str += '&nbsp;';
		return str; 
	}
	function pattern_formatter(value,row,index){
		var str = '';	
		if(row.element){
			str=row.element+'['+row.attribute+'='+row.value+']'
		}
	return str;
	}
		
	//DataGrid字段设置
	var urlinfo_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : false,hidden:true,align:'center'},
	    	          			{field : 'url',title : '<spring:message code="pageinfo_url" />',width : 150,align:'center'},
	    	          			{field : 'pattern',title : '<spring:message code="webharvest_pattern" />',width : 150,align:'center',formatter : pattern_formatter},
			          			{field : 'extracted_date',title : '<spring:message code="webharvest_extractedDate" />',width : 150,align:'center'},
			          	        {field : 'action',title : '操作',width : 80,align : 'center',formatter : urlinfo_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function urlinfo_list_loadDataGrid(){		 
		urlinfo_list_datagrid = $('#'+urlinfo_list_datagrid_id).datagrid({
			url : urlinfo_list_datagrid_load_url,
			fit : true,
			title:'<spring:message code="webharvest_individualurlaudit" />',
			border : false,
			fitColumns : true,
			singleSelect : true,
			striped : true,
			pagination : true,
			rownumbers : true,
			idField : 'id',
			pageSize : 15,
			pageList : [ 5, 10,15, 20, 30, 40, 50 ],
			columns : urlinfo_list_datagrid_columns,
			toolbar:'#'+urlinfo_list_toolbar_id,
			onLoadSuccess : function() {	
				$(this).datagrid('tooltip');
				$('#'+urlinfo_list_searchform_id+' table').show();
				$('#'+urlinfo_list_datagrid_id).show();
				$('#'+urlinfo_list_toolbar_id).show();
				parent. $ .messager.progress('close');
			}
		});
	}
	function schedulerEvent(id, ACTIONURL) {
		parent.$.messager.confirm('确认', '你肯定要停下来？', function(r) {
			if (r) {
				$.post(ACTIONURL, {
					id : id
				}, function(result) {
					if (result.success) {
						
						urlinfo_list_datagrid.datagrid('reload'); // reload the user data;
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
	function searchEvent(id, ACTIONURL) {
		parent.$.messager.confirm('确认', '你确定要搜索吗？', function(r) {
			if (r) {
				showLog(showLogUrl);
				$.post(ACTIONURL, {
					id : id
				}, function(result) {
					if (result.success) {
						//result = $ .parseJSON(result);
						$('#formSaveBtn').linkbutton({disabled : false});
						$('#formSaveBtn').bind('click', function(){
							 parent.$.modalDialog.handler.dialog('close');						
							 indexTabsUpdateTab('href',{title:'<spring:message code="webharvest_extracteddata" />',url:'${ctx}/dataharvest/showdata/'+result.data.id,iconCls:'icon-table_multiple'});
						});
						urlinfo_list_datagrid.datagrid('reload'); // reload the user data;
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
	$ .parser.onComplete = function() {
		//加载DataGrid数据
		urlinfo_list_loadDataGrid();	
		//绑定按钮事件
		bindSearchBtn('urlinfo_list_searchBtn','urlinfo_list_clearBtn','pageinfo_list_searchForm',urlinfo_list_datagrid);
	};
	require.config({
		paths:{
			echarts:'${ctx}/static/js/plugins/echart/echarts',
			'echarts/chart/bar' : '${ctx}/static/js/plugins/echart/echarts-map',
			'echarts/chart/line' : '${ctx}/static/js/plugins/echart/echarts-map',
			'echarts/chart/map' : '${ctx}/static/js/plugins/echart/echarts-map',
		}
	});
	require(
			[
			 'echarts',
			 'echarts/chart/bar',
			 'echarts/chart/line',
			 'echarts/chart/map',
			 ],
			 
			 function(ec)
			 {
				
				var mychart1 = ec.init(document.getElementById('piechartloglist'));
				mychart1.setOption({
					/* tooltip : {
				        trigger: 'item',
				        formatter: "{a} <br/>{b} : {c} ({d}%)"
				    }, */
				      toolbox: {
				        show : true,
				        feature : {
				            mark : {show: true},
				            dataView : {show: true, readOnly: false},
				            magicType : {
				                show: true, 
				                type: ['pie', 'funnel'],
				                option: {
				                    funnel: {
				                        x: '-25%',
				                        width: '50%',
				                        funnelAlign: 'left',
				                        max: 1548
				                    }
				                }
				            },  
				            restore : {show: true},
				            saveAsImage : {show: true}
				        }
				    }, 
				    calculable : true,
				    series : [
				        {
				            name:'访问来源',
				            type:'pie',
				            radius : '55%',
				            center: ['50%', '55%'],
				            data:${piechartdata}
				        }
				    ]
				
				                  
				});
				
			 });
	function showLog(url,params) {
		var opts = {
			width : 900,
			height : 600,
			title : '信息',
			href : url,
			iconCls : 'icon-application_form_add',
			buttons : [
					{
						text : '<spring:message code="webharvest_next" />',
						iconCls : 'icon-note_go',
						disabled : true,
						id : 'formSaveBtn',
						handler : function() {
							
						}
					}]
		};
		$.extend(opts, params);
		parent.$.modalDialog(opts);

	}
</script>


