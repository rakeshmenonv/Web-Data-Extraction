<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<script>
amenu.open("v_hm",true);
$(document).ready(function() {
		$("#collapseOne").addClass("in");
		$("#collapseOne").css("height", "auto");
		$("#role").addClass("current");
		// 按钮事件

		//删除按钮事件
		$("#deleteBtn").click(function() {
			var str = "";
			var obj = $(this);
			var href = "${ctx}${deletePath}";
			$("input[name='objid']:checkbox").each(function() {
				if ($(this).attr("checked")) {
					str += $(this).val() + ","
				}
			})

			var ids = str.split(",");
			if (str.length > 0) {
				str = str.substring(0, str.length - 1);
				if (window.confirm('确认删除选择的条记录吗？')) {
					$.post(href, {
						'ids' : str
					}, function() {
						window.location.reload();
					});
				}

			} else {
				alert("请选择要删除的记录！");
			}
		});
	});
	
</script>
<style>
.glossymenu div.submenu ul li a.role {
	color: green;
}
</style>
<script>
$(document).ready(
		function() {
			ddaccordion.expandone('submenuheader',0);
		});
$(document).ready(function() {
	
	$.validator.addMethod("nospace",function(value,element){
		return value.indexOf(" ")<0 && value !="";
	},"No Spaces Allowed"); 
	
	$(document).ready(function() {
		$.validator.addMethod("english",function(value,element){
			return this.optional(element)||/^[-a-zA-Z0-9_.\-]+$/i.test(value);
		},"only english characters are allowed");
				//聚焦第一个输入框
		$("#name").focus();
		//为inputForm注册validate函数
		$("#inputForm").validate({
			rules : {
		
				name : {
					required : true,
					 nospace : true,
					 english:true						 									
				  }
				 
			}
		  
});
});
});
</script>
<SCRIPT type="text/javascript">	var setting = {
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback: {
			onCheck: onCheck
		}
	};
var zTreeObj; 
$(document).ready(function() {
	$("#max_menu_system").addClass("active");
	$("#max_menu_system").addClass("main-nav-arrow");
	$("#sub-menu-system").removeClass("hidden");
	$("#min_menu_role").addClass("active");
	//聚焦第一个输入框
	
	zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, ${jsonData});
	
	submitCheckedNodes();
});

	function onCheck(e, treeId, treeNode) {
		submitCheckedNodes();
	}	
	function submitCheckedNodes(treeNode) {  
	    var nodes = new Array();  
	    //取得选中的结点  
	    nodes = zTreeObj.getCheckedNodes(true);  
	    var str = "";  
	    for (i = 0; i < nodes.length; i++) {  
	        if (str != "") {  
	            str += ",";  
	        }  
	        str += nodes[i].id;  
	    }  
	    $("#pids").attr("value",str);
	} 
</script>
<script>
function keycheck()
{
	var name=$("#name").val();
	
	var ret=false;	
	nodes = zTreeObj.getCheckedNodes(true);  
    if(nodes.length!=0)
			{
			ret = true;
			}
		else
			{
			alert("Atleast have to select one role");
			}
return ret;
}
</script>
</head>
<body>
	<div id="ny_right">
		<div id="ny_right1">
			<div id="weizhi">
				<ul>
					<li>
						<img src="${ctx}/static/images/home.jpg" />&nbsp;
						<a href="${ctx}/account/user" class="link">
						<spring:message code="menu_accountManagement" /></a>/
						<spring:message code="menu_roleManagement" />
					</li>
				</ul>
			</div>
			<br>
			<form:form id="inputForm" modelAttribute="role" action="${ctx}/account/role/${action}" method="post" class="form-horizontal">
			<input type="hidden" name="id" id="id" value="${ role.id}" />
			<table class="form-table">
					<tr>
						<td><spring:message code="account_roleName" />:</td>
						<td style="text-align: left;"><input type="text" id="name" disabled name="name" value="${role.name}" /></td>
					</tr>
					<tr>
						<td><spring:message code="account_roleSelect" />:</td>
						<td style="text-align: left;"><input type="hidden" id="pids" name="pids" value="${pids }" />
							<div id="test" style="border: 1px solid #DDD; width: 410px;">
								<ul id="treeDemo" class="ztree" style="width: 400px; height: 300px; overflow-y: scroll; overflow-x: auto;"></ul>
							</div>
						</td>
					</tr>
				</table>
			</div>
		<div class="tab-bottom">
			<button class="btnimg" onclick="history.back()"><img src="${ctx}/static/images/rollback.png" /></button>
		</div>
		</form:form>
	</div>
	</div>
</body>
</html>









