/**
 * dialog util
 * 封装了 artDialog ，提供了一些常用的confirm提示框
 */
var DialogUtil = new function(){
	this.confirm=function(msg,title,successcbk,cancelcbk){
		var d = new dialog({
			title:title,
			content:msg,
			okValue:"确定",
			cancelValue:"取消",
			ok:function(){
				successcbk&&successckb();
			},
			cancel:function(){
				cancelcbk&&cancelcbk();
			}
		})
	}
	
	this.deleteConfirm=function(successCbk){
		confirm("你确定要删除吗?","警告",successCbk);
	}
	
	this.cancelConfirm=function(successCbk){
		confirm("你确定要取消吗?","提醒",successCbk);
	}
	
}();