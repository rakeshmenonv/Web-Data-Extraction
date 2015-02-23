<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "/common/taglibs.jsp"%>
<script src="${ctx}/static/js/plugins/echart/esl.js"></script>

<script type="text/javascript">
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
			var mychart = ec.init(document.getElementById('piechart'));
			mychart.setOption({
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
			            data:${piechartdata}
			        }
			    ]
			
			                    
			});
			var mychart1 = ec.init(document.getElementById('piechart1'));
			mychart1.setOption({
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

</script>
<div id="ny_bt1" class="ny_bt" >
	<center><div>${url }</div></center>
	<div id="piechart" style="width:100%; height:300px;">
	</div>
</div>
<div id="ny_bt2" class="ny_bt" >
	
	<div id="piechart1" style="width:100%; height:300px;">
	</div>
</div>