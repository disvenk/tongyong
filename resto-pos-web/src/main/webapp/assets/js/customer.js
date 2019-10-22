/**
 * custom utils 
 * @author Diamond
 * 2015年9月15日 
 */
var cUtils = customerUtils();

function customerUtils(){
	return {
		flush:function(){
			location.reload();
		}
	};
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
 
$(".img-preview").click(function(){
	var d = new Dialog();
	d.InnerHtml ="<img src='"+$(this).attr("src")+"' style='height:100%'/>";
	d.Title="图片预览";
	d.Height = 500;
	d.show();
	d.Width="30";
});

$("form").find("[data-dchecked]").each(function(){
	$(this).find("[value='"+$(this).data("dchecked")+"']").prop("checked",true);
});
if($().datetimepicker){
	$('.date-picker.day').datetimepicker({
		minView:"month",
		maxView:"month",
		autoclose:true,
		format:"yyyy-mm-dd",
		startView:"month",
		language:"zh-CN"
	});

	$('.date-picker.time').datetimepicker({
		minView:"hour",
		maxView:"hour",
		autoclose:true,
		format:"hh:ii",
		startView:"hour",
		language:"zh-CN"
	});

	$('.date-picker').attr("readonly","readonly");
}

