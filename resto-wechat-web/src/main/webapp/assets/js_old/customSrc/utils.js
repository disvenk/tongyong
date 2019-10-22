$(document).ready(function(){
	
	$(".dialog-close").click(function(){
		$(this).parents(".custom-dialog").hide();
	});

});

var loading_div_default = $("<div></div>");
loading_div_default.css({
	margin: 0,
	padding: 0,
	position: "absolute",
	top: 0,
	height: "100%",
	width: "100%",
	background: "rgba(0,0,0,0.3)",
	textAlign: "center",
	color: "#FFF",
	paddingTop: "20%",
	cursor: "wait",
	zIndex: 99999999
});
var defaultDivClass = "utils-loading-div-class";
loading_div_default.addClass(defaultDivClass);
loading_div_default.html("<img src='assets/img/loadding.gif' />");
function loading(el) {
	var div = loading_div_default.clone();
	div.height($(el).height());
	$(el).append(div);

}

function loaded(el) {
	$(el).find("." + defaultDivClass).remove();
}

function showMessage(msg,time){
	var messageDom = $("<div>").css({
		position: 'fixed',
		zIndex: 99999999,
		padding: '15px',
		top: '50%',
		width: '100%',
		textAlign: 'center'
	});
	var textDom = $("<span>").css({
		background: 'rgba(0,0,0,.5)',
		color:'#fff',
		padding:'10px 30px',
		borderRadius: '3px'
	});
	textDom.text(msg);
	messageDom.html(textDom);
	messageDom.appendTo("body");
	setTimeout(function(){
		messageDom.animate({opacity:0},500,function(){
			messageDom.remove();
		});
	},time||800);
}

$.fn.setAjaxParam= function(obj){
	$(this).find("*[data-key]").each(function(){
		var dom = $(this);
		var type = dom.data("type");
		var key = dom.data("key");
		var value = obj[key];
		switch (type){
			case "IMG":
				try{
					if(value.indexOf("http://")==-1&&value.indexOf("https://")==-1){
						value = IMAGE_SERVICE+value;
					}	
				}catch(e){
					value="";
				}
				dom.attr("src",value);
				break;
			case "DATE":
				var date = new Date(value);
				var format = dom.data("format")||"yyyy-MM-dd hh:mm";
				var dateStr = date.format(format);
				dom.html(dateStr);
				break;
			default:
				if(value==undefined){
					value="";
				}
				dom.html(value);
				break;
		}
	});
}

function cAjax(url,data,callback,errorcallback){
	if(!errorcallback){
		errorcallback = function(x, t, e){
			log.error(x,t,e);
			showMessage("出错了！"+x+t+e);
		}
	}
	
	$.ajax({
		type:"post",
		url:url,
		data:data,
		async:true,
		success:callback,
		error:errorcallback,
//		timeout:5000
	});
}

function HashMap() {
	var size = 0;
	var entry = new Object();

	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	}

	this.get = function(key) {
		return this.containsKey(key) ? entry[key] : null;
	}

	this.remove = function(key) {
		if (this.containsKey(key) && (delete entry[key])) {
			size--;
		}
	}

	this.containsKey = function(key) {
		return (key in entry);
	}

	this.containsValue = function(value) {
		for (var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	}

	this.getValues = function() {
		var values = new Array();
		for (var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	}

	this.getKeys = function() {
		var keys = new Array();
		for (var prop in entry) {
			keys.push(prop);
		}
		return keys;
	}

	this.getSize = function() {
		return size;
	}

	this.clear = function() {
		size = 0;
		entry = new Object();
	}
}



function getParam(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

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