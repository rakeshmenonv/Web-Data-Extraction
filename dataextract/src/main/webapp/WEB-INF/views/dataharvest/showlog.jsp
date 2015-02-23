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
		   //console.log(e.data);
		   var event = $.parseJSON(e.data);
		   var eventvalue = $.parseJSON(event.data);
		   console.log(eventvalue);
		   
		   if(event.success == true) {
			   
			    if(eventvalue.id != null){
			    $(".log-data").append("<p>"+eventvalue.id+"</p>");
			    $(".log-data").append("<p>"+eventvalue.pageurlinfo.url+"</p>");
				$(".log-data").append("<p>"+eventvalue.pageurlinfo.extractedDate+"</p>");
				$(".log-data").append("<p>"+eventvalue.content+"</p>");
			    }else{
			    	 $(".log-data").append("<p>Progress..</p>");
			    	 $(".log-data").append("<p>progress....</p>");
			    	 
			    }
		   }
		   else if(event.success == false){
			    e.target.close();  
		   }
		  
		
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
<div class="log-data"></div>
</body>
</html>