/**
 * 2015年10月28日 
 * @author Diamond
 */
toastr.options = {
  "closeButton": true,
  "debug": false,
  "positionClass": "toast-top-right",
  "onclick": null,
  "showDuration": "500",
  "hideDuration": "500",
  "timeOut": "3000",
  "extendedTimeOut": "500",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}


$(document).on("ajaxError",function(event,xhr,options){
//	showMessage("服务器错误！",1000);
	console.error("send ajax error",xhr,options)
});

$(document).on("ajaxComplete",function(){
	Vue.nextTick(function(){
		App.updateUniform();
	});
});

/**
 * option{
 * 	el: vue控制的el标签
 *  
 * }
 */
var Controller = function(controlId,datatable){
	var _C = this;
	var cid = controlId;
	var tb = datatable;
	this.currentDialog = null;
	this.ajaxError= function(xhr){
		var errorDialog = new dialog({
			title:xhr.status+" "+xhr.statusText,
			content:xhr.responseText,
			width:$(window).width()*.7
		});
		errorDialog.show();
		console.error("error:",xhr);
	}
	
	this.ajax = function(url,data,successcallback,errorcallback,type){
		if(!successcallback){
			successcallback=function(result){
				console.log("默认成功处理方式",result);
			}
		}
		if(!errorcallback){
			errorcallback = function(xhr){
				_C.ajaxError(xhr);
			}
		}
		$.ajax({
			url:url,
			data:data,
			type:type||"post",
			success:successcallback,
			error:errorcallback,
			complete:function(){
				App.stopPageLoading();	
			}
		});
	}
	
	this.ajaxForm =  function(form,success,error,type){
    	_C.ajax($(form).attr("action"),$(form).serialize(),success,error,type||"post");
    } 
	
	this.ajaxFormEx = function (form,options){
		var defaults = {
			success:null,
			error:null,
			aftersuccess:null,
			aftererror:null,
			formaction:null,
		};
		//如果options参数是方法的话，就默认将 成功后的回调方法指向options这个方法
		if(typeof options== "function"){
			options  = $.extend(defaults,{aftersuccess:options});
		}else{ //其他情况，覆盖默认选项
			options  = $.extend(defaults,options);
		}
		form = $(form);
		form.attr('action',options.formaction||form.attr('action'));
		_C.ajaxForm(form,function(result){
			if(result.success){
				if(typeof options.success=="function"){
					options.success(result,form);
				}else{
					_C.simpleMsg("操作成功");
				}
				options.aftersuccess&&options.aftersuccess(result,form);
			}else{
				if(typeof options.error=="function"){
					options.error(result,form);	
				}else{
					if(result.data){
						_C.failedMsgAddToInput(form,result.data);
					}else{
						_C.errorMsg(result.message);
					}
				}
				options.aftererror&&options.aftererror(result,form);
			}
		},function(xhr,msg,e){
			var errorText = xhr.status+" "+xhr.statusText+":"+form.attr("action");
			_C.errorMsg(errorText);
		});
		
	}
	
	/**
	 * 加载表单，并绑定默认表单 错误提示 和 相关处理
	 */
	this.loadForm = function loadForm(options){

		var defaults = {
			title:"表单",
			url:"",
			formDom:"",
			data:null,
			success:null,
			error:null,
			aftersuccess:null,
			aftererror:null,
			autoclose:true,
			formaction:null,
			afterload:null,
			width:400
		};
		if(typeof options=="string"){
			defaults.url=options;
		}
		options  = $.extend(defaults,options);
		
		App.startPageLoading();
		var formDialogId = "dialog_id_"+new Date().getTime();
		var formDialog = new dialog({
			id:formDialogId,
			width:options.width,
			title:options.title,
			onremove:function(){
				_C.currentDialog = null;
			}
		});
		_C.currentDialog = formDialog;
		if(options.url){
			_C.ajax(options.url,options.data,function(res){
				var formDom = $(res);
				bindFormDom(formDom);
			});
		}else if(options.formDom){
			bindFormDom(options.formDom);
		}
		function bindFormDom(formDom){
			App.stopPageLoading();
			if(options.formaction){
				formDom.attr("action",options.formaction);
			}
			if(options.afterload){
				options.afterload(formDom);
			}
			formDom.attr("data-dialogid",formDialogId);
			formDialog.content(formDom);
			formDialog.showModal();
			handlerDatePicker();
			var formNode = $(formDialog.node);
			formNode.find("select").each(function(){
				var defaultData = $(this).data("selected");
				defaultData&&$(this).val(defaultData).trigger("change");
			});
			formDialog.button([{
				value:"关闭",
				callback:function(){}
			},{
				value:"提交",
				callback:function(){
					formDialog.statusbar("");
					var form = formNode.find("form");
					_C.ajaxForm(form,function(result){
						if(result.success){
							if(options.success){
								options.success(result,form,formDialog);
							}else{
								formDialog.statusbar('<span style="color:green;">提交成功</span>');
								formDialog.remove();
								_C.simpleMsg("提交成功");
							}
							options.aftersuccess&&options.aftersuccess(result,form,formDialog);
						}else{
							if(options.error){
								options.error(result,form,formDialog);	
							}else{
								if(result.data){
									failedMsgAddToInput(form,result.data);
								}else{
									formDialog.statusbar('<span class="vailderror">'+result.message+'</span>');
								}
							}
							options.aftererror&&options.aftererror(result,form,formDialog);
						}
					},function(xhr,msg,e){
						var errorText = xhr.status+" "+xhr.statusText+":"+form.attr("action");
						formDialog.statusbar('<span style="color:red">'+errorText+'</span>');
					});
					return false;
				},
				autofocus:true
			}]);
			App.initAjax();
		}
		return formDialog;
	}
	
	this.handlerFormAjax = function(form,options){
		var defaults = {
			url:options,
			success:null,
			error:null,
			aftersuccess:null,
			aftererror:null,
			formaction:null,
		};
		options  = $.extend(defaults,options);
		form.submit(function(e){
			debugger
			e.preventDefault();
			form.attr('action',options.formaction||form.attr('action'));
			_C.ajaxFormEx(form,options);
		});
	}
	
	function failedMsgAddToInput(form,failedData){
		var errorClass="text-danger";
		for(var i in failedData){
			var errMsg = failedData[i];
			var n =0;
			for(var name in errMsg){
				var input = form.find("[name='"+name+"']");
				if(input.length>0&&n++==0){
					input.focus();
				}
				var vailderror = input.parent().find("span."+errorClass);
				if(vailderror.length==0){
					vailderror = $("<span>"+errMsg[name]+"</span>").addClass(errorClass);
					input.parent().append(vailderror);	
				}else{
					vailderror.html(errMsg[name]);
					vailderror.show();
				}
				input.change(function(e){
					$(this).parent().find('span.'+errorClass).hide();
					$(this).unbind(e);
				})
			}
		}
	}
	
	var handlerDatePicker=function(){
		if($().datetimepicker){
			$(".datetime-picker").prop("readonly",true).css({
				background:"none",
				cursor:"pointer"
			});
			$(".datetime-picker").each(function(){
				var options = {	autoclose:true,language:"zh-CN"	};
				var dom = $(this);
				if(dom.hasClass("time")){
					options.minView="hour";
					options.format="yyyy-mm-dd hh:ii";
				}else{ 
					options.minView="month";  //默认时间选择器都是 选择日期 
					options.format="yyyy-mm-dd"
				}
				if(dom.hasClass("now")){
					options.startDate=new Date();
				}
				$(this).datetimepicker(options);
				
			})
		}
	}
	
	this.handlerAutoComplete= function(){
		 if ($().autocomplete) {
			 $(".autocomplete2me").each(function(){
				var id =$(this).attr("id");
				var dataDiv = $("[data-autocompletefor='"+id+"']");
				var dataSource = [];
				dataDiv.children().each(function(){
					var d = $(this).text();
					dataSource.push(d);
				});
				$(this).autocomplete({
					source:dataSource,
				});
			 });
		 }
	}
	
	this.exportExcel = function(url){
		_C.simpleMsg("正在导出..");
		action = action||"list_export";
		var iframe=$("<iframe hidden>").attr("src",url);
		iframe.appendTo("body");
		iframe.load(function(){
			_C.simpleMsg("数据导出异常");
			iframe.remove();
		});
	}
	
	/**
	 * 创建表单按钮
	 * o{
	 *  name:string,       按钮名称
	 *  style:classString, 按钮样式
	 *  **以下下全部对应loadForm 方法参数选项**
	 * 	data:object,    数据
	 *  url:string,     表单URL
	 *  formaction:string, 表单动作 
	 *  afterload:function,  加载成功后回调
	 *  aftersuccess:function  成功回调  
	 * }
	 */

    this.createFormBtn=function(o){
        var classString = o.style||"btn btn-xs blue";
        var button = $("<button>");
        button.addClass(classString);
        button.html(o.name);
        o.title=o.name;
        button.click(function(){
            _C.loadForm(o);
        });
        return button;
    }


	this.createContactBtn=function(){
		var button = $("<button class='btn btn-xs btn-success ajaxify'>关联菜品</button>");
		button.click(function(){
			if(_C.vue){
				_C.vue.showContactArticle();
			}
		});
		return button;
	}
	
	this.createDelBtn = function(value,delUrl,successCbk){
		var button = $("<button class='btn btn-xs red'>").html(name||"删除");
		button.click(function(){
			_C.delConfirmDialog(function(){
				_C.ajax(delUrl,{id:value},function(result){
					if(result.success){
						tb.ajax.reload();
						_C.simpleMsg("删除成功");
					}else{
						_C.errorMsg(result.message,"删除失败");
					}
				});
			});
		});
		return button;
	}

    this.createDelSupplier = function(value,delUrl,checkIsEffectSupplierUrl,successCbk){
        var button = $("<button class='btn btn-xs red'>").html(name||"删除");
        button.click(function() {
            $.get(checkIsEffectSupplierUrl+'?supplierId='+value,function (result) {
                _C.confirmDialog((result.message == null) ? "你确定要删除吗?":result.message + ",你确定要删除吗?", "提醒", function () {
                    _C.ajax(delUrl, {id: value}, function (result) {
                        if (result.success) {
                            tb.ajax.reload();
                            _C.simpleMsg("删除成功");
                        } else {
                            _C.errorMsg(result.message, "删除失败");
                        }
                    });
                });
            });
        })
        return button;
    }


    this.systemButton = function(delUrl,obj,alertName){ //执行ajax，返回
                _C.ajax(delUrl,obj,function(result){
                    if(result.success){
                        tb.ajax.reload();
                        _C.simpleMsg(alertName[0]);
                    }else{
                        _C.errorMsg(result.message,alertName[1]);
                    }
                });
        // return button;
    }
    this.systemButtonNo = function(yn,alertName){
    	if(yn=='success'){
            tb.ajax.reload();
            _C.simpleMsg(alertName);
		}else if(yn=='error') {
            _C.errorMsg(alertName);
		}
    }
	this.createBtn = function(model,url,urlData){
		var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
		button.click(function(){
			if(_C.vue){
				_C.vue.platform(model);
			}else{
				_C.loadForm({
					url:url,
					data:{id:model},
					formaction:urlData,
					aftersuccess:function(){
						tb.ajax.reload();
					}
				});
			}
		});
		return button;
	}
    this.createEditBtn = function(model,url,urlData){
		//专供自定义消息
		if(model.bigOpen == undefined){
            var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
		}else if(!model.bigOpen){
            var button = $("<button disabled='true' class='btn btn-xs btn-primary'>编辑</button>");
		}else {
            var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
		}

        button.click(function(){
            if(_C.vue){
            	//console.log(JSON.stringify(model));
                _C.vue.edit(model);
            }else{
                _C.loadForm({
                    url:url,
                    data:{id:model},
                    formaction:urlData
                });
            }
        });
        return button;
    }

    this.createScmBomEditBtn = function(model,url,urlData){
        var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
        button.click(function() {

            if(_C.vue){
                _C.vue.edit(model);
            }else{
                _C.loadForm({
                    url:url,
                    data:{id:model},
                    formaction:urlData
                });
            }



        })
        return button;
    }

	this.showFind = function(model,url,urlData){
		var button = $("<button class='btn btn-xs btn-primary'>查看下级</button>");
		button.click(function(){
			if(_C.vue){
				_C.vue.showFind(model);
			}else{
				_C.loadForm({
					url:url,
					data:{id:model},
					formaction:urlData
				});
			}
		});
		return button;
	}


    this.createFind = function(model,url,urlData){
        var button = $("<button class='btn btn-xs btn-primary'>新增下级</button>");
        button.click(function(){
            if(_C.vue){
                _C.vue.createFind(model);
            }else{
                _C.loadForm({
                    url:url,
                    data:{id:model},
                    formaction:urlData
                });
            }
        });
        return button;
    }
    //
	this.findBtn = function(model,url,urlData){
		var button = $("<button class='btn btn-xs btn-primary'>查看</button>");
		button.click(function(){
			if(_C.vue){
				_C.vue.showDetails(model);
			}else{
				_C.loadForm({
					url:url,
					data:{id:model},
					formaction:urlData
				});
			}
		});
		return button;
	}


    this.findCommonBtn = function(model,btnName,url,urlData){
        var button = $("<button class='btn btn-xs btn-primary'>"+btnName+"</button>");
        button.click(function(){
            if(_C.vue){
                _C.vue.showDetails(model);
            }else{
                _C.loadForm({
                    url:url,
                    data:{id:model},
                    formaction:urlData
                });
            }
        });
        return button;
    }
    this.createApproveBtn = function(model,url,urlData){
		debugger
		console.info("----model:"+model)
        var button = $("<button class='btn btn-xs red'>审核</button>");
        button.click(function(){
            if(_C.vue){
                _C.vue.approve(model);
            }else{
                _C.loadForm({
                    url:url,
                    data:{id:model},
                    formaction:urlData
                });
            }
        });
        return button;
    }

	this.createConfirmDialogBtn=function(opt){
		var o= {
				style:"btn btn-xs red",
				title:"",
				text:"",
				name:"",
				yes:function(){},
				no:function(){},
		};
		$.extend(o,opt);
		var button = $("<button>",{"class":o.style}).html(o.name);
		button.click(function(){
			_C.confirmDialog(o.text,o.title,o.yes,o.no);
		});
	}
	this.delConfirmDialog = function(yes,no){
		_C.confirmDialog("你确定要删除吗?","提醒",yes,no);
	}

	this.confirmDialog=function(text,title,successcbk,cancelcbk){
		var width = text.length*16;
		width = width>200?width:200;
		var cDialog = new dialog({
			title:title||"",
			content:text||"",
			width:width,
			ok:function(){
				if(typeof successcbk=="function"){
					successcbk();
				}
			},
			cancel:function(){
				if(typeof cancelcbk=="function"){
					cancelcbk();
				}	
			}
		});
		cDialog.showModal();
	}
	
	this.simpleMsg = function(message){
		toastr.clear();
		toastr.success(message);
	}
	
	this.errorMsg = function(message,title){
		toastr.clear();
		toastr.error(message);
	}
	
	this.closeDialog = function(dlg){
		removeDialog(dlg,0);
	}
	
	this.removeDialog = function(dlg,time){
		if(time>0){
			setTimeout(function(){
				dlg.remove();
			},time);
		}else{
			dlg.remove();
		}
	} 	
	
	this.formVueMix={
		data:function(){
			return {
				m:{},
				showform:false
			}
		},
		created:function(){
			this.$watch("showform",function(){
				if(this.showform){
					App.initUniform();
				}
			});
			this.$watch("m",function(){
				if(this.showform){
					App.initUniform();
					App.updateUniform();
				}
			},{
				deep:true
			});
		},
		methods:{
			openForm:function(){
				this.showform = true;
			},
			closeForm:function(){
				this.m={};
				this.showform = false;
			},
			cancel:function(){
				this.m={};
				this.closeForm();
			},
			create:function(){
				this.m={};
				this.openForm();
			},
			edit:function(model){
				this.m= model;
				this.openForm();
			},
			save:function(e){
				var that = this;
				var formDom = e.target;
				_C.ajaxFormEx(formDom,function(){
					that.cancel();
					tb.ajax.reload();
				});
			},
		}
	};
	
	if(cid){
		this.vueObj = function(option){
			var o = {
					el:cid,
					data:{
						m:{},
						showform:false
					},
					methods:{
						openForm:function(){
							this.showform = true;
						},
						closeForm:function(){
							this.m={};
							this.showform = false;
						},
						cancel:function(){
							this.m={};
							this.closeForm();
						},
						create:function(){
							this.m={};
							this.openForm();
						},
						edit:function(model){
							this.m= model;
							this.openForm();
						},
						save:function(e){
							var that = this;
							var formDom = e.target;
							_C.ajaxFormEx(formDom,function(){
								that.cancel();
								tb.ajax.reload();
							});
						},
					},
				};
			if(option){
				o = $.extend(o,option);
			}
            Vue.use(VueDragging);
			var vue = new Vue(o);
			_C.vue = vue;
			return vue;
		}
	}
	
	return this;
};