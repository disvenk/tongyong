var OrderService = new function(){
	this.pushOrderAjax=function(oid,successCbk){
		console.log("hello");
		$.ajax({
			url:"wx/confirmOrder",
			data:{ORDER_ID:oid},
			success:function(result){
				if(isSuccess(result)){
					successCbk&&successCbk();
				}
			}
		});
	}
	
	this.cancelOrderAjax=function(oid,successCbk){
		$.ajax({
			url:"wx/cancelOrder",
			data:{ORDER_ID:oid},
			success:function(result){
				if(isSuccess(result)){
					successCbk&&successCbk();
				}
			}
		});
	}
	
	function isSuccess(r){
		return r.result=="01";
	}
	return this;
}();

var OrderControl = new function(){
	var controllContent = "亲，在您确定要现在下单吗？下单之后，餐品就会开始制作了！";
	this.pushOrder = function(oid,successCbk){
		var orderDialog = new rDialog({
			title:"您要现在下单吗？",
			content:controllContent,
			ok:function(){
				OrderService.pushOrderAjax(oid,function(){
					orderDialog.close();
					successCbk&&successCbk();
				});
				return false;
			},
			okValue:"立即下单",
			close:function(){},
			closeValue:"稍后下单",
		});
		orderDialog.show();
	}
	
	this.cancelOrder=function(oid,successCbk){
		confrimDialog("你确定要取消该订单吗?","取消订单提醒",function(){
			OrderService.cancelOrderAjax(oid,successCbk);
		});
	}
	
	this.flush = function(){
		location.reload();
	}
	function confrimDialog(msg,title,successCbk){
		var dlg = new rDialog({
			title:title,
			content:msg,
			ok:function(){
				successCbk&&successCbk();
			},
			close:function(){},
		});
		dlg.show();
		return dlg;
	}
	
	return this;
}();

var wxToast = function(){
	var temp = '<div id="toast">                          '+
			'	<div class="weui_mask_transparent"></div>       '+
			'	<div class="weui_toast">                        '+
			'		<i class="weui_icon_toast"></i>             '+
			'		<p class="weui_toast_content" i="msg">已完成</p>    '+
			'	</div>                                          '+
			'</div>                                             ';
	
	return {
		success:function(msg,timeout){
			var _$ = $(temp);
			_$.find("[i='msg']").html(msg);
			timeout  = timeout||2000;
			_$.appendTo("body");
			setTimeout(function(){
				_$.remove();
			},timeout);
		}
	}
}();

var rDialog = function(o){
	var temp = 
		'<div class="weui_dialog_confirm">                                                        '+
		'	<div class="weui_mask"></div>                                                         '+
		'	<div class="weui_dialog">                                                             '+
		'		<div class="weui_dialog_hd"><strong class="weui_dialog_title" i="title"></strong></div>     '+
		'		<div class="weui_dialog_bd" i="content"></div>                                                '+
		'		<div class="weui_dialog_ft">                                                      '+
		'			<a href="javascript:;" class="weui_btn_dialog default" i="btn_close"></a>                   '+
		'			<a href="javascript:;" class="weui_btn_dialog primary" i="btn_ok"></a>                   '+
		'		</div>                                                                            '+
		'	</div>                                                                                '+
		'</div>                                                                                   ';
	
	var op={
		content:"",
		title:"",
		ok:function(){},
		okValue:"确认",
		close:function(){},
		closeValue:"取消",
	}
	$.extend(op,o);
	var _$ = $(temp);
	var _ = this;
	
	this.option=op;
	
	this.title=function(html){
		_$.find("[i='title']").html(html);
	}
	
	this.content=function(html){
		_$.find("[i='content']").html(html);
	}
	
	this.buttons=function(html){
		_$.find("[i='buttons']").html(html);
	}
	
	this.show=function(){
		_$.appendTo("body");
	}
	this.close=function(){
		_$.remove();
	}
	
	function getUI(name){
		return _$.find("[i='"+name+"']");
	}
	
	function init(){
		_.title(op.title);
		_.content(op.content);
		var okBtn = getUI("btn_ok");
		var closeBtn = getUI("btn_close");
		if(typeof o.ok == "undefined"){
			okBtn.remove();
		}else{
			okBtn.text(op.okValue);
			okBtn.click(function(){
				var result = o.ok();
				if(!result){
					_.close();
				}
			});
		}
			
		if(typeof o.close=="undefined"){
			getUI("btn_close").remove();
		}else{
			closeBtn.text(op.closeValue);
			closeBtn.click(function(){
				var result = o.close();
				if(!result){
					_.close();
				}
			});
		}
			
		_$.find(".dialog-close").click(function(){
			_.close();
		});
	}
	init();
	return this;
};