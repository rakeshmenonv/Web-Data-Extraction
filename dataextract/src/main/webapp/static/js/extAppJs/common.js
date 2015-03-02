/*
 * EasyUI 常用工具类，包含了增、删、改、查等常用的通用功能
 */


/**
 * 打开实体表单页面 包括：新增和修改
 * @param url
 *            表单初始化地址
 * @param listDatagrid
 *            表单提交后待刷新的表格
 * @param params
 *            其他参数如：title 弹出窗口标题，width 弹出窗口宽度，height弹出窗口高度
 */
function updateForm(url, formId, listDatagrid, params) {
	var opts = {
		width : 600,
		height : 400,
		title : '信息',
		href : url,
		iconCls : 'icon-application_form_add',
		buttons : [
				{
					text : '保存',
					iconCls : 'icon-save',
					id : 'formSaveBtn',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = listDatagrid;
						var inputForm = parent.$.modalDialog.handler.find('#'
								+ formId);
						var isValid = inputForm.form('validate');
						if (isValid) {
							var file_upload = parent.$.modalDialog.handler
									.find('#file_upload');
							if (file_upload.length > 0) {
								var swfuploadify = file_upload
										.data('uploadify');
								if (swfuploadify.queueData.queueLength > 0) {
									$("#formSaveBtn").linkbutton('disable');
									$("#formCancelBtn").linkbutton('disable');
									file_upload.uploadify('upload', '*');
								} else {
									inputForm.submit();
								}
							} else {
								inputForm.submit();
							}
						}
					}
				}, {
					text : '取消',
					id : 'formCancelBtn',
					iconCls : 'icon-cross',
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}

/**
 * 详细页面
 * 
 * @param url
 *            查看链接
 * @param params
 */
function view(url, params) {
	var opts = {
		width : 1000,
		height : 600,
		title : '详细信息',
		href : url,
		iconCls : 'icon-application_form_magnify',
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-cross',
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	};
	$.extend(opts, params);
	parent.$.modalDialog(opts);
}

/**
 * 删除单条记录
 * @param id
 * @param deleteUrl
 * @param listDatagrid
 */
function deleteOne(id, deleteUrl, listDatagrid) {
	parent.$.messager.confirm('确认', '是否确定删除该记录?', function(r) {
		if (r) {
			$.post(deleteUrl, {
				ids : id
			}, function(data) {
				console.info(data);
				if (data.success) {
					listDatagrid.datagrid('reload'); // reload the user
					// data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}

/**
 * 批量删除
 * @param deleteUrl
 * @param listDatagrid
 */
function deleteBatch(deleteUrl, listDatagrid) {
	var rows = listDatagrid.datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		parent.$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
			if (r) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				var idParam = decodeURIComponent($.param({
					ids : ids
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

/**
 * TreeGrid 删除单条记录
 * @param id
 * @param deleteUrl
 * @param listDatagrid
 */
function treegridDeleteOne(id, deleteUrl, listDatagrid) {
	parent.$.messager.confirm('确认', '是否确定删除该记录?', function(r) {
		if (r) {
			$.post(deleteUrl, {
				ids : id
			}, function(data) {
				if (data.success) {
					listDatagrid.treegrid('reload'); // reload the user data
					$.messager.show({ // show error message
						title : '提示',
						msg : data.message
					});
				} else {
					$.messager.alert('错误', data.message, 'error');
				}
			}, 'JSON');
		}
	});
}

/**
 * 更新修改操作，包含：上报，作废等直接更改信息的操作等。可根据不同需要进行扩展
 * @param url
 * @param type
 * @param listDatagrid
 * @param params
 */
function update(url,type,listDatagrid,params) {
	switch (type) {
	case 'reportOne'://单条上报
		parent.$.messager.confirm('确认', '是否确定上报该记录?', function(r) {
			if (r) {
				$.post(url, params, function(data) {
					if (data.success) {
						listDatagrid.datagrid('reload'); // reload the user
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
		break;
	case 'reportBatch'://批量上报
		var rows = listDatagrid.datagrid('getChecked');
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		var idParam = decodeURIComponent($.param({
			id : ids
		}, true));
		$.extend(idParam, params);
		parent.$.messager.confirm('确认', '您是否要上报当前选中的记录？', function(r) {
			if (r) {
				$.post(reportUrl,idParam, function(data) {
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
				});
			}
		});
		break;
	case 'initialPwd':
		break;
	}

}


/**
 * 查询表单按钮绑定
 * @param searchBtn：查询按钮ID
 * @param clearBtn：
 *            清空按钮ID
 * @param searchForm：查询Form
 *            ID
 * @return
 */
function bindSearchBtn(searchBtn, clearBtn, searchForm, listDatagrid) {
	searchBtn = $('#' + searchBtn).linkbutton({
		iconCls : 'icon-search'
	}).click(
			function() {
				listDatagrid.datagrid('load', infotop.serializeObject($('#'
						+ searchForm)));
			});
	clearBtn = $('#' + clearBtn).linkbutton({
		iconCls : 'icon-no'
	}).click(function() {
		$('#' + searchForm).form('clear');
	});
}

var isReportedFormatter = function(value, row, index) {
	return value == 1 ? '已上报' : '未上报';
};

var isLockFormatter = function(value, row, index) {
	return value == 1 ? '已锁定' : '未锁定';
};

var sexFormatter = function(value, row, index) {
	return value == 1 ? '男' : '女';
};

var isTrueFormatter = function(value, row, index) {
	return value == 1 ? '是' : '否';
};

function newTabView(type, params) {
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
	});
	var index_tabs = top.$('#index_tabs').tabs();
	var opts;
	if (type == '' || type == 'href') {
		opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			href : params.url,
			border : false,
			fit : true
		};
	} else {
		var iframe = '<iframe src="'
				+ params.url
				+ '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true
		};
	}
	if (index_tabs.tabs('exists', opts.title)) {
		index_tabs.tabs('select', opts.title);
		parent.$.messager.progress('close');
	} else {
		index_tabs.tabs('add', opts);
	}
}
