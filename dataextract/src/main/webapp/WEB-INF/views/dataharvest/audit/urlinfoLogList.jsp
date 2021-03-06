<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file = "/common/taglibs.jsp"%>
<script src="${ctx}/static/js/plugins/echart/esl.js"></script>

<div data-options="fit:true" class="easyui-panel" style="height:200px;">
	<div class="easyui-layout" data-options="fit:true">
		<div
			data-options="region:'west',split:true,border:true,title:'&nbsp;<spring:message code="webharvest_toptenurlstatistics" />',iconCls:'icon-chart_organisation'"
			style="width: 500px;overflow: hidden;">
			<center><h2><div style="color:black"><spring:message code="webharvest_top10ExtractedWebsites" /></div></h2></center>
			<div id="piechart3" style="width:100%; height:400px;"></div>
		</div>
		
	<div data-options="region:'center',border:true">
			<table id="pageinfoLog_list_dg" style="display: none;"></table>
		</div>
		<div id="pageinfoLog_list_toolbar" style="display: none;">
				<a href="javascript:deleteBatchByUrl(pageinfoLog_list_deleteGroup_url,pageinfoLog_list_datagrid);" class="easyui-linkbutton"  data-options="iconCls:'icon-remove',plain:false">删除</a>
			</div> 
		
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
	//var pageinfoLog_list_delete_url =  '${ctx}/audit/delete';
	var pageinfoLog_list_delete_url =  '${ctx}/pageurlinfo/delete';
	var pageinfoLog_list_view_url =  '${ctx}/audit/urlInfo/';
	var pageinfoLog_list_datagrid_load_url = '${ctx}/audit/findLogList';
	var pageinfoLog_piechart_url = '${ctx}/audit/coverpage';
	var pageinfoLog_list_deleteGroup_url = '${ctx}/pageurlinfo/deleteGroup';
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
				'<img onclick="indexTabsUpdateTab(\'href\',{title:\'{0}\',url:\'{1}\',iconCls:\'icon-book_next\'})"  src="${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/book_next.png" title="查看"/>',
				'<spring:message code="webharvest_auditDetailed" />',pageinfoLog_list_view_url + row.id);
		str += '&nbsp;';
		str += formatString('<img onclick="deleteOne(\'{0}\',\'{1}\',pageinfoLog_list_datagrid);" src="{2}" title="删除"/>',
                row.url,pageinfoLog_list_deleteGroup_url,'${ctx}/static/js/plugins/jquery-easyui-1.3.4/themes/icons/application_form_delete.png');
		str += '&nbsp;';
		return str; 
	}
	
	//DataGrid字段设置
	var pageinfoLog_list_datagrid_columns = [ [
	                    		{field : 'id',title : '编号',width : 150,checkbox : true,align:'center'},
	    	          			{field : 'url',title : '<spring:message code="webharvest_url" />',width : 150,align:'center'},
			          			{field : 'CountOf',title : '<spring:message code="webharvest_count" />',width : 150,align:'center'},
			          	        {field : 'action',title : '操作',width : 80,align : 'center',formatter : pageinfoLog_list_actionFormatter} 
	                    		] ];
	/** 初始化DataGrid,加载数据 **/		
	function pageinfoLog_list_loadDataGrid(){		 
		pageinfoLog_list_datagrid = $('#'+pageinfoLog_list_datagrid_id).datagrid({
			url : pageinfoLog_list_datagrid_load_url,
			fit : true,
			title:'<spring:message code="webharvest_urlauditlist" />',
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
		bindSearchBtn('pageinfoLog_list_searchBtn','pageinfoLog_list_clearBtn','pageinfoLog_list_searchForm',pageinfoLog_list_datagrid);
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
				
				var loglistchart = ec.init(document.getElementById('piechart3'));
				loglistchart.setOption({
					
					tooltip : {
				        trigger: 'item',
				        formatter: "{a} <br/>{b} : {c} ({d}%)"
				    },
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
				                        x: '25%',
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
				            center: ['50%', '60%'],
				            data:${piechart}
				        }
				    ]
				
				                  
				});
				
			 });
	
	
	function deleteBatchByUrl(deleteUrl, listDatagrid) {
		var rows = listDatagrid.datagrid('getChecked');
		var urls = [];
		if (rows.length > 0) {
			parent.$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
				if (r) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					for (var i = 0; i < rows.length; i++) {
						urls.push(rows[i].url);
					}
					var idParam = decodeURIComponent($.param({
						ids : urls
					}, true));
					$.post(deleteUrl, idParam, function(data) {
						if (data.success) {
							listDatagrid.datagrid('reload');
							listDatagrid.datagrid('uncheckAll').datagrid(
									'unselectAll').datagrid('clearSelections');
							$.messager.show({ // messager信息提示
								title : '提示',
								msg : data.message
							});
						} else {
							$.messager.alert('错误', data.message, 'error');
						}
						parent.$.messager.progress('close');
					}, 'JSON');

				}
			});
		} else {
			$.messager.show({
				title : '提示',
				msg : '请勾选要删除的记录！'
			});
		}
	}

</script>




