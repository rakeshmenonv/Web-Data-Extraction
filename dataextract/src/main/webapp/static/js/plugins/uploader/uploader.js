jQuery.fn.extend({
    uploader: function(type, script,url,rid) {
        $(this).click(function() {
        	 window.open(url+"/picupload/uploads?tp=image&num=10&jsname="+script+"&rid="+rid, "uploader", 'height=400,width=620,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
//            if (type == "image") {
//                window.open("main.php?tp=image&num=1&jsname="+script, "uploader", 'height=250,width=420,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
//            } else if (type == "audio") {
//                window.open("main.php?tp=audio&num=1&jsname="+script, "uploader", 'height=250,width=420,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
//            }else if (type == "all") {
//                window.open("main.php?tp=all&num=1&jsname="+script, "uploader", 'height=200,width=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
//            } else if (type == "images") {
//                window.open("main.php?tp=image&num=10&jsname="+script, "uploader", 'height=400,width=620,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
//            }else if (type == "images2") {
//                window.open("main.php?tp=image&namecode=self&num=10&jsname="+script, "uploader", 'height=400,width=620,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
//            }
        });
    },
    viewimgbox: function(data, name,url) {
    	if(data!=''){
    	var datas = data.split("|");
        for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_outer">';
            html += '<div class="photo_img_view"><a href="' + datas[i] + '" target="_blank"><img src="' + datas[i] + '" onload="photoin(this,198,120)" /></a></div>';
            html += '<input type="hidden" name="' + name + '" value="' + datas[i] + '" />';
            html += '<div class="photo_txt">';
            html += '<div class="title"><input type="text"  name="' + name + '_title" value="小标题" /></div>';
            html += '</div>';
            html += '</div>';
            $(this).append(html);
        }
    	}
    },
    addimgbox: function(data, name,url) {
    	if(data!=''){
    	var datas = data.split("|");
        for (i = 0; i < datas.length; i++) {
        	var html = '';
            html += '<div class="photo_outer">';
            html += '<div class="photo_img"><a href="' +url+ datas[i] + '" target="_blank"><img src="' +url+ datas[i] + '"style="width:130px;height:130px;" /></a></div>';
            html += '</div>';
            $(this).append(html);
        }
    	}
    }, editimgbox: function(data,title,name,url,ids) {
    	if(data!=''){
    	var datas = data.split("|");
    	var ids = ids.split("|");
    	var url=url.split("/");
    	var h="'"+url[1]+"'";
    	for (i = 0; i < datas.length; i++) {
    		alert(datas[i]);
            var html = '';
            html += '<div class="photo_outer">';
            html += '<div class="photo_img"><a href="'+ datas[i] +  '" target="_blank"><img src="' + datas[i] + '" style="width:130px;height:130px;"/></a></div>';
            html+='<center><div class="photo_btn">';
            html+='<input class="btn1" type="button" onclick="$(this).delimgbox2('+ids[i]+','+h+')" value="删除"/>';
            html += '</div></center>';
            html += '</div>';
            $(this).append(html);
        }
       
    	}
    }, showimgbox: function(data,title,name,url) {
    	if(data!=''){
        var datas = data.split("|");
        var titles = title.split("|");
         for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_show">';
            html += '<div class="photo_img"><a href="' + datas[i] +  '" target="_blank"><img src="' + datas[i] + '" style="width:130px;height:130px;" /></a></div>';
            html += '<input type="hidden" name="' + name + '" value="' + datas[i] + '" />';
            html += '<div class="photo_txt">';
            html += '<div class="title">'+titles[i]+'</div>';
            html += '</div>';
            html += '</div>';
            $(this).append(html);
        }
    	}
    }, delimgbox: function() {
        var m = $(this).parent(".photo_btn").parent(".photo_outer");
        m.remove();
    }, delimgbox2: function(ids,url) {
    	 var m = $(this).parent(".photo_btn").parent().parent(".photo_outer");
        var deleteUrl="/"+url+'/attachment/delete';
        parent.$.messager.confirm('确认', '是否确定删除该记录?', function(r) {
    		if (r) {
    			$.post(deleteUrl, {
    				ids : ids
    			}, function(result) {
    				var result = eval('(' + result + ')');
    				if (result.success) {
    					m.remove();
    					$.messager.show({ // show error message
    						title : '提示',
    						msg : result.message
    					});
    				} else {
    					$.messager.alert('错误',result.message,'error');
    				}
    			});
    		}
    	});
    }, upimgbox: function() {
        var m = $(this).parent(".photo_btn").parent(".photo_outer");
        var n = m.prevAll().length;
        if (n > 0) {
            var temp1 = m.html();
            var temp2 = m.prev().html();
            m.prev().html(temp1);
            m.html(temp2);
        }
    }, downimgbox: function() {
        var m = $(this).parent(".photo_btn").parent(".photo_outer");
        var n = m.nextAll().length;
        if (n > 0) {
            var temp1 = m.html();
            var temp2 = m.next().html();
            m.next().html(temp1);
            m.html(temp2);
        }
    },addoneimgbox: function(data, name) {
        var datas = data.split("|");
        for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_outer one">';
            html += '<div class="photo_img"><img src="' + datas[i] + '" onload="photoin(this,120,120)" /></div>';
            html += '</div>';
            $(this).append(html);
        }
    }
});

function photoin(myphoto,W,H){
	alert("fff");
    $(myphoto).removeAttr("width");
    $(myphoto).removeAttr("height");
    var px = myphoto.width;
    var py = myphoto.height;
    var nx;
    var ny;
    if(px / py > W / H){
        if(px>W){
            nx=W;
            ny=nx*(py/px);
        }else{
            nx = px;
            ny = py;
        }
    }else{
        if(py > H){
            ny=H;
            nx=ny*(px/py);
        }else{
            nx = px;
            ny = py;
        }
    }
    myphoto.width = nx;
    myphoto.height = ny;
    $(myphoto).css("marginLeft",(W-nx)/2);
    $(myphoto).css("marginTop",(H-ny)/2);
}
function closewin() {
    $("#winfacebox").hide();
}
