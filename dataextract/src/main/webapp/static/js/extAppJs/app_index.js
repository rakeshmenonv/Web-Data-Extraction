/**
 * 通用布局构造JS，布局命名与中介机构系统index页一致
 */
var index_tabs;
var index_layout;
var index_calendar;

/**
 * 主标签页添加标签项
 * 使用方法：indexTabsAddTab('[href][iframe]',{title:'',url:'',iconCls:''});
 */
var indexTabsAddTab;

$(function() {
	parent.$.messager.progress('close');
	index_layout = $('#index_layout').layout({
		fit : true
	});
	/* 默认关闭日历 */
	index_layout.layout('collapse', 'east');

	index_tabs = $('#index_tabs')
			.tabs(
					{
						fit : true,
						border : false,
						tools : [
								{
									iconCls : 'icon-application_side_contract',
									handler : function() {
										index_tabs.tabs({
											tabPosition : 'left'
										});
									}
								},
								{
									iconCls : 'icon-application_side_expand',
									handler : function() {
										index_tabs.tabs({
											tabPosition : 'right'
										});
									}
								},

								{
									iconCls : 'icon-application_get',
									handler : function() {
										index_tabs.tabs({
											tabPosition : 'top'
										});
									}
								},
								{
									iconCls : 'icon-application_put',
									handler : function() {
										index_tabs.tabs({
											tabPosition : 'bottom'
										});
									}
								},
								{
									iconCls : 'icon-reload',
									handler : function() {
										var tab = index_tabs
												.tabs('getSelected');
										if (tab) {
											var href = tab.panel('options').href;
											if (href) {/* 说明tab是以href方式引入的目标页面 */
												var index = index_tabs
														.tabs(
																'getTabIndex',
																index_tabs
																		.tabs('getSelected'));
												index_tabs
														.tabs('getTab', index)
														.panel('refresh');
											} else {/* 说明tab是以content方式引入的目标页面 */
												var panel = index_tabs.tabs('getSelected').panel('panel');
												var frame = panel
														.find('iframe');
												try {
													if (frame.length > 0) {
														for ( var i = 0; i < frame.length; i++) {
															frame[i].contentWindow.document
																	.write('');
															frame[i].contentWindow
																	.close();
															frame[i].src = frame[i].src;
														}
														if (navigator.userAgent
																.indexOf("MSIE") > 0) {// IE特有回收内存方法
															try {
																CollectGarbage();
															} catch (e) {
															}
														}
													}
												} catch (e) {
												}
											}
										}
									}
								},
								{
									iconCls : 'icon-no',
									handler : function() {
										try {
											var selectedTab = index_tabs
													.tabs('getSelected');
											if (selectedTab) {
												var index = index_tabs.tabs(
														'getTabIndex',
														selectedTab);
												if (index > -1) {
													var tab = index_tabs.tabs(
															'getTab', index);
													if (tab) {
														if (tab
																.panel('options').closable) {
															index_tabs.tabs(
																	'close',
																	index);
														} else {
															$.messager
																	.alert(
																			'提示',
																			'['
																					+ tab
																							.panel('options').title
																					+ ']不可以被关闭！',
																			'error');
														}
													}
												}
											}
										} catch (e) {
										}
									}
								} ]
					});

	index_calendar = $('#index_calendar').calendar({
		fit : true,
		current : new Date(),
		border : false,
		onSelect : function(date) {
			$(this).calendar('moveTo', new Date());
		}
	});

	indexTabsAddTab = function(type, params) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		var opts;
		if (type == '' || type == 'href') {
			opts = {
				title : params.title,
				closable : true,
				iconCls : params.iconCls,
				href : params.url,
				border : false,
				style : {
					padding : "1"
				},
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
	};

	indexTabsUpdateTab = function(type, params) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
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
		index_tabs.tabs('close', opts.title);
		index_tabs.tabs('add', opts);
		parent.$.messager.progress('close');

	};

	indexTabsRefreshTab = function(type, params) {
		var index = index_tabs.tabs('getTabIndex', index_tabs
				.tabs('getSelected'));
		index_tabs.tabs('getTab', index).panel('refresh');
	};

	closecurrentTab = function(type, params) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		var index = index_tabs.tabs('getTabIndex', index_tabs
				.tabs('getSelected'));
		var tab = index_tabs.tabs('getTab', index);
		if (tab) {
			index_tabs.tabs('close', index);
		}
	};
	indexTabAddTab = function(type, title, url, iconCls) {
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
		var opts;
		if (type == '' || type == 'href') {
			opts = {
				title : title,
				closable : true,
				iconCls : iconCls,
				href : url,
				border : false,
				style : {
					padding : "1"
				},
				fit : true
			};
		} else {
			var iframe = '<iframe src="'
					+ params.url
					+ '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
			opts = {
				title : title,
				closable : true,
				iconCls : iconCls,
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
	};
});
