Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function showMessage(msg,time){
	var messageDom = $("<div>").css({
		position: 'fixed',
		zIndex: 99999999,
		top: '45%',
		width: '100%',
		textAlign: 'center'
	});
	var textDom = $("<p>").css({
		background: 'rgba(0,0,0,.5)',
		color:'#fff',
		width:"60%",
		margin:'0 auto',
		padding:'10px 30px',
		lineHeight:1.4,
		borderRadius:"5px"
	});
	textDom.text(msg);
	messageDom.html(textDom);
	messageDom.appendTo("body");
	var defaultTime = msg.length/3*300;
	setTimeout(function(){
		messageDom.remove();
	},time||defaultTime);
	return messageDom;
}
function showDialogMessage(msg,time){
	var messageDom = $("<div>").css({
		position: 'fixed',
		zIndex: 99999999,
		height:'100%',
		width: '100%',
		textAlign: 'center',
		background:'rgba(0,0,0,.5)'
	});
	var dlg = $("<div>").css({
		position: "absolute",
		margin: "auto",
	    top: 0,
	    bottom: 0,
	    left: 0,
	    right: 0,
	});
	messageDom.html(dlg);
	var textDom = $("<p>").css({
		position:'absolute',
		background: '#fff',
		color:'#000',
		width:"50%",
		wordBreak:"break-word",
		margin:'auto',
		left: 0,
		right: 0,
		top:"30%",
		padding:'15px 20px',
		lineHeight:1.4,
		borderRadius:"5px"
	});
	textDom.text(msg);
	dlg.html(textDom);
	messageDom.appendTo("body");
	if(time){
		setTimeout(function(){
		messageDom.remove();
		},time);
	}
	return messageDom;
}

$(document).on("ajaxError",function(event,xhr,options){
//	showMessage("服务器错误！",1000);
	console.error("send ajax error",xhr,options)
});
function toTop(){
	$("body").scrollTop(0);
}

function getParam(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}