<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki"> 表单</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form role="form" method="post" enctype="multipart/form-data" action="{{m.id?'version/modify':'version/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label>版本号</label>
                                <input type="text" class="form-control" name="versionNo" required v-model="versionNo"  >
                            </div>
                            <div class="form-group" v-if="isEdit">
                                <label>下载地址</label><br/>
                                <span type="text" name="downloadAddress" >{{m.downloadAddress}}</span>
                            </div>
                            <div class="form-group">
                                <input type="hidden" name="file" v-model="file">
                                <input type="file" class="form-control" @change="uploadFile"
                                                 @error="uploadError">                               
                            </div>
                            <div class="form-group" v-if="count>0">
                                <span class="progressBar">
							      	<div id="progressFill"></div>
							    </span>
							    <span class="progressText">进度{{count}}%</span>                                
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">补丁类型：</label>
                                <div >
                                    <label class="radio-inline">
                                        <input type="radio" name="substituteMode" v-model="m.substituteMode" value="0"> 全量
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="substituteMode" v-model="m.substituteMode" value="1"> 增量
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">是否自愿更新：</label>
                                <div>
                                    <label class="radio-inline">
                                        <input type="radio" name="voluntarily" v-model="m.voluntarily" value="0"> 强制
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="voluntarily" v-model="m.voluntarily" value="1"> 非强制
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-group " >
                                    <label class="col-sm-2 control-label">描述：</label>
                                    <div class="col-sm-8" >
									<textarea id="message" name="message" style="height:300px;" v-model="m.message">
									</textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="id" v-model="m.id" />
                        <input class="btn green"  type="submit"  value="保存"/>
                        <a class="btn default" @click="cancel" >取消</a>
                    </form>
                </div>
            </div>
        </div>

        <weui-dialog :show="loadingDialog.show" :msg="loadingDialog.message" :type="loadingDialog.type"></weui-dialog>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <s:hasPermission name="template/add">
                <button class="btn green pull-right" @click="create">新建</button>
            </s:hasPermission>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>

<script>       
    
    (function(){
        var vueObj;
        var cid="#control";
        var C = new Controller(cid,null);
        var option = {
            el:"#control",
            data:{
                showform:false,
                tb:'',
                C:'',
                isEdit:false,
                versionNo:"",
                loadingDialog:{show:false,message:'',type:null},
                timer:null,
              	count:0,
              	types:[".zip"],
            },
            created: function () {
                var that = this;
                this.createTb();
                this.C = new Controller(null,that.tb);
            },
            methods:{
            	showDialog:function(){
            		var that = this;
            		this.timer = setInterval(function(){
                      	$("#progressFill").animate({
	                        width: that.count+"%"
                    	});
                  	},1000)
            	},
            	uploadFile:function(e){
					var that =this;
					var file = e.target.files[0];
					var filename = file.name;
					var timestamp = new Date().getTime();
				  	if(filename.indexOf(".") != -1){
					    var fileNewName = filename.split(".")[0] + timestamp + ".zip";														
					}
				  	console.log(fileNewName);
					if(this.imageNameVailed(filename)){
						var client = new OSS.Wrapper({
						 	region: 'oss-cn-shanghai',//oss地址 ，具体位置见下图
						 	accessKeyId: 'LTAINfbQqw049TwX',//ak
						 	accessKeySecret: 'B2xY7kzv6bkRJi8K1yUsLPYE3bjoD7',//secret
						 	bucket: 'newpos'//oss名字
					 	});
					 	client.list({'max-keys': 10
					      	}).then(function (result) {
					  		console.log(result);
					  		}).catch(function (err) {
					      	console.log(err);
					  	});
					  	
					  	client.multipartUpload(fileNewName, file, {
							progress
					  	}).then(function (result) {
					  		console.log(result);
					  		if(result.res.requestUrls[0].indexOf("?") != -1){
							    that.m.downloadAddress = result.res.requestUrls[0].split("?")[0];														
							}else{
								that.m.downloadAddress = result.res.requestUrls[0];
							}
							console.log(JSON.stringify(that.m.downloadAddress));
			          	}).catch(function (err) {
				            console.log(err);
			          	});
			          	this.showDialog();
					}else{
						this.uploadError("文件类型错误");
					}
				},
				imageNameVailed:function(name){
					if(name.indexOf(".")!=-1){
						// var lastName = name.substring(name.indexOf("."));
		                var split = name.split(".");
		                var lastName="."+split[split.length - 1];
						for(var i in this.types){
							var fix = this.types[i];
							if(lastName==fix){
								return true;
							}
						}
					}
					return false;
				},
                createTb: function () {
                    var that = this;
                    var $table = $(".table-body>table");
                    this.tb = $table.DataTable({
                        'ordering'  :false,
                        ajax : {
                            url : "version/list_all",
                            dataSrc : ""
                        },
                        columns : [
                            {
                                title : "版本号",
                                data : "versionNo",
                            },
                            {
                                title : "下载地址",
                                data : "downloadAddress",
                                createdCell:function(td,tdData,rowData,row){
                                    var a = $("<a href=\""+ rowData.downloadAddress + "\" target=\"_blank\">" + rowData.downloadAddress + "</a>");
                                    $(td).html(a);
                                }
                            },
                            {
                                title : "操作",
                                data : "id",
                                createdCell:function(td,tdData,rowData,row){
                                    var operator=[
                                        <s:hasPermission name="version/delete">
                                        that.createDelBtn(rowData,"version/delete"),
                                        </s:hasPermission>
                                        <s:hasPermission name="version/modify">
                                        that.createEditBtn(rowData),
                                        </s:hasPermission>
                                    ];
                                    $(td).html(operator);
                                }
                            }],
                    });
                },
                createEditBtn: function (rowData){
                    var that = this;
                    var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
                    button.click(function(){
                        that.edit(rowData);
                        showDetails(rowData);
                    });
                    return button;
                },
                createDelBtn : function(rowData){
                    var that=this;
                    var button = $("<button class='btn btn-xs red'>").html("删除");
                    button.click(function(){
                        that.del(rowData);
                    });
                    return button;
                },
                del:function (rowData) {
                    var that=this;
                    console.log(rowData);
                    $.ajax({
                        url:"versionBrand/selectBrandListByVersionId",
                        data:{"versionId":rowData.id},
                        dataType:"json",
                        type:"post",
                        success:function (result) {
                            console.log(result);
                            if(result.data.length > 0){
                                that.C.errorMsg("此版本号已关联相关品牌或店铺，不能删除!");
                            }else{
                                that.C.delConfirmDialog(function(){
                                    that.C.ajax("version/delete",{id:rowData.id},function(result){
                                        if(result.success){
                                            that.tb.ajax.reload();
                                            that.C.simpleMsg("删除成功");
                                        }else{
                                            that.C.errorMsg("删除失败");
                                        }
                                    });
                                });
                            }
                        },
                        error:function (data) {
                            that.C.errorMsg("服务器错误，删除失败，请稍后再试！");
                        }
                    })

                },
                edit:function(rowData){
                    this.m= rowData;
                    this.versionNo=this.m.versionNo;
                    this.isEdit = true;
                    this.showform = true;
                    this.initEditor();
                },
                cancel:function(){
                    this.m={};
                    this.showform = false;
                },
                create:function(){
                    this.m={};
                    this.versionNo=""
                    this.isEdit = false;
                    this.showform = true;
                    this.initEditor();
                },
                save:function(e){
                    var that = this;
                    this.m.versionNo= this.versionNo;
                    var pattern = /^\d+\.\d+\.\d+$/;
                    if(!pattern.test(this.m.versionNo)){
                        that.C.errorMsg("版本号必须是两个小数点");
                        return;
                    }
                    if(!that.m.downloadAddress){
                        that.C.errorMsg("下载地址不能为空,请上传安装包!");
                        return;
                    }
                    var message=$("#message").val();
                    that.m.message=message;
                    if(that.m.id){
                        $.ajax({
                            url:"version/modify",
                            data:that.m,
                            type:"post",
                            dataType:"json",
                            success: function () {
                                that.C.simpleMsg("保存成功");
                                that.cancel();
                                that.tb.ajax.reload();
                            },
                            error: function () {
                                that.C.simpleMsg("服务器错误，保存失败！")
                            }
                        })
                    }else{
                        $.ajax({
                            url:"version/create",
                            data:that.m,
                            type:"post",
                            dataType:"json",
                            success: function () {
                                that.cancel();
                                that.tb.ajax.reload();
                                that.C.simpleMsg("保存成功");
								that.count = 0;
                            },
                            error: function () {
                                that.C.errorMsg("保存失败");
                            }
                        })
                    }
                },
                showSuccess: function (msg,type) {
                	var that = this;
                    this.loadingDialog.show = true;
            		this.loadingDialog.message = msg;
            		this.loadingDialog.type = type;
            		setTimeout(function(){
            			that.loadingDialog.show = false;
            		},2000)
            		this.count = 0;
                },
                uploadError: function (msg) {
                    this.C.errorMsg(msg);
                },
                initEditor : function () {
                    Vue.nextTick(function(){
                        var editor = new wangEditor('message');
                        editor.config.uploadImgFileName = 'file';
                        editor.config.uploadImgUrl = 'upload/file';
                        editor.create();
                    });
                }
            }
        };
        vueObj = C.vueObj(option);
        async function progress(p){
	    	vueObj.count = p.toFixed(2)*100;
	    	if(p === 1){
	    		vueObj.showSuccess("上传成功",2)
	    	}
			console.log('percentage:'+p);	  				 
		}
    }());
    //用于显示描述详情
    function showDetails(obj){
        $(".modal-body").html(obj.message);
        $(".modal").modal();
    }
</script>
