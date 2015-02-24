<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
 <script>
if (!!window.EventSource) {
       
	   console.log("Event source available");
	   var source = new EventSource('dataharvest/log');
	  
	   source.addEventListener('message', function(e) {

		   //console.info(e.data);
		   var msg = $.parseJSON(e.data);
		  	if(msg.data) {
			  	$(".log-data").append("<p>"+msg.data.id+"</p>");
			    $(".log-data").append("<p>"+msg.data.pageurlinfo.url+"</p>");
				$(".log-data").append("<p>"+msg.data.pageurlinfo.extractedDate+"</p>");
				$(".log-data").append("<p>"+msg.data.content+"</p>");
			}
		  	var contentdiv=$(".log-data");
		  //	contentdiv.scrollTop = contentdiv.scrollHeight;
		  	$(".log-data").scrollTop($(".log-data").prop('scrollHeight'));
			//contentdiv.scrollTop(contentdiv[0].scrollHeight-contentdiv.height());
		});

	   source.addEventListener('open', function(e) {
		  	console.log("Connection was opened.");
	   }, false);

	   source.addEventListener('error', function(e) {
		  	if (e.readyState == EventSource.CLOSED) {
	            console.log("Connection was closed.");
	        } else {
	        	
	            //console.log(e.readyState);
	        }
	   }, false);
	} else {
		 
	        console.log("No SSE available");
	}
	

	
</script>
<div class="log-data" style="height:100%;overflow-y:scroll;"></div>
</body>
</html>
