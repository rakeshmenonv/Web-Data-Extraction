jQuery.fn.extend({
    uploader: function(type, script,str,url) {
        $(this).click(function() {
        	 window.open(url+"/picupload/uploadsFlow?tp=image&num=10&jsname="+script+"&str="+str, "uploader", 'height=400,width=620,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
        });
    },addimgbox: function(data, name,url) {
        var datas = data.split("|");
        for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_outer">';
            html += '<div class="photo_img"><a href="' + url + '/basicinfo/showIMG?filePath=' + datas[i] + '" target="_blank"><img src="' + datas[i] + '" onload="photoin(this,120,120)" /></a></div>';
            html += '<input type="hidden" name="' + name + '" value="' + datas[i] + '" />';
            html += '<div class="photo_btn">';
            html += '<input class="btn1" type="button" onclick="$(this).upimgbox()"  value="上移" />';
            html += '<input class="btn1" type="button" onclick="$(this).downimgbox()" value="下移" />';
            html += '<input class="btn1" type="button" value="删除" onclick="$(this).delimgbox()" />';
            html += '</div>';
            html += '</div>';
            $(this).append(html);
        }
    }, editimgbox: function(data,name,url) {
        var datas = data.split("|");
        var urlImage = url + '/static/upload/'
        for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_outer">';
            html += '<div class="photo_img"><a href="' + url + '/basicinfo/showIMG?filePath='+ urlImage + datas[i] + '" target="_blank"><img src="' + urlImage + datas[i] + '" onload="photoin(this,120,120)" /></a></div>';
            html += '<input type="hidden" name="' + name + '" value="' + urlImage + datas[i] + '" />';
            html += '<div class="photo_btn">';
            html += '<input class="btn1" type="button" onclick="$(this).upimgbox()" value="上移" />';
            html += '<input class="btn1" type="button" onclick="$(this).downimgbox()" value="下移" />';
            html += '<input class="btn1" type="button" onclick="$(this).delimgbox2(' + "'delimg'" + ')" value="删除" />';
            html += '</div>';
            html += '</div>';
            $(this).append(html);
        }
    }, showimgbox: function(data,name,url) {
        var datas = data.split("|");
        var urlImage = url + '/static/upload/'
        for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_show">';
            html += '<div class="photo_img"><a href="' + url + '/basicinfo/showIMG?filePath=' + urlImage + datas[i] + '" target="_blank"><img src="' + urlImage + datas[i] + '" onload="photoin(this,120,120)" /></a></div>';
            html += '<input type="hidden" name="' + name + '" value="' + urlImage + datas[i] + '" />';
            html += '</div>';
            $(this).append(html);
        }
    }, showflowimgbox: function(data,url) {
        var datas = data.split("|");
        var urlImage = url + '/static/upload/'
        for (i = 0; i < datas.length; i++) {
            var html = '';
            html += '<div class="photo_flow_show">';
            html += '<div class="photo_flow_img"><a href="' + url + '/basicinfo/showIMG?filePath=' + urlImage + datas[i] + '" target="_blank"><img src="' + urlImage + datas[i] + '" onload="photoin(this,290,290)" /></a></div>';
            html += '</div>';
            $(this).append(html);
        }
    }, delimgbox: function() {
        var m = $(this).parent(".photo_btn").parent(".photo_outer");
        m.remove();
    }, delimgbox2: function(inputname) {
        var m = $(this).parent(".photo_btn").parent(".photo_outer");
        var v = m.children("input").val();
        var nowv = $("#" + inputname).val();
        if (nowv == "") {
            $("#" + inputname).val(v);
        } else {
            $("#" + inputname).val(nowv + "|" + v);
        }
        m.remove();
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
            html += '<input type="hidden" name="' + name + '[]" value="' + datas[i] + '" />';
            html += '</div>';
            $(this).append(html);
        }
    }
});
function closewin() {
    $("#winfacebox").hide();
}
