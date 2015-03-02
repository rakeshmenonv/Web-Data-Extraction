<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>航海日志</title>
</head>
<body>
<script>
      var es = new EventSource("dataharvest/log");
      console.info("source::"+es);
      var listener = function (event) {
      console.info("data_value:::"+event.data);
   	  if(event.data) {
      var msg = $.parseJSON(event.data); 
      if(msg.data) {
	  		if ($('#loadingimg').length){
	  			$('#loadingimg').remove();
	  	    }
		  	$(".log-data").append("<p>"+msg.data.id+"</p>");
		    $(".log-data").append("<p>"+msg.data.pageurlinfo.url+"</p>");
			$(".log-data").append("<p>"+msg.data.pageurlinfo.extractedDate+"</p>");
			$(".log-data").append("<p>"+msg.data.content+"</p>");
		}
		if(msg.message) {
		  	$(".log-data").append("<p>"+msg.message+"</p>");			   
		}
	  	$(".log-data").scrollTop($(".log-data").prop('scrollHeight'));
   	   }
     };
     es.addEventListener("open", listener);
     es.addEventListener("message", listener);
     es.addEventListener("error", listener);

</script>
	<div class="log-data" style="height:100%;overflow-y:scroll;margin-left:20px;">
		<div id="loadingimg" style="width:100%;"><center><img style="margin:20px auto;" src="${ctx }/static/images/ajax_loader_blue_128.gif"/></center></div>
	</div>
</body>
</html>
