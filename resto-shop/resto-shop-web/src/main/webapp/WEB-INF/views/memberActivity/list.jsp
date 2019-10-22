<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
	            <div class="portlet-title">
	                <div class="caption">
	                    <span class="caption-subject bold font-blue-hoki">会员折扣</span>
	                </div>
	            </div>
	            <div class="portlet-body">
	            	<form role="form"  id = "memberActivity" action="{{m.id?'memberActivity/modify':'memberActivity/create'}}" @submit.prevent="save">
						<div class="form-group">
						    <label>活动名称</label>
						    <input type="text" class="form-control" name="name" v-model="m.name" required="required">
						</div>
						<div class="form-group">
						    <label>活动折扣(0-1之间)</label>
						    <input type="text" class="form-control" id ="disconut" name="disconut" v-model="m.disconut" required="required" >
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label">是否开启：</label>
							<div class="col-sm-6 radio-list">
								<label class="radio-inline">
									<input type="radio" name="type" v-model="m.type" value="0"> 不开启
								</label>
								<label class="radio-inline">
									<input type="radio" name="type" v-model="m.type" value="1"> 开启
								</label>
							</div>
						</div>
						<input type="hidden" name="id" v-model="m.id" />
						<input class="btn green"  type="submit"  value="保存" id="saveBrandUser" >
						<a class="btn default" @click="cancel" >取消</a>
					</form>
	            </div>
	        </div>
		</div>
	</div>

	<div class="modal fade" id="orderDetail" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							@click="closeModal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<strong>录入会员</strong>
					</h4>
				</div>
				<div class="modal-body">
					<form class="form-inline" @submit.prevent="inputCustomer">
						<div class="form-group">
							<div class="input-group">
								<input type="text" class="form-control"  v-model="telephone" id="telephone" required="required" placeholder="手机号">
							</div>
						</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="submit" class="btn btn-primary">录入</button>
					</form>
				</div>
				<div class="table-scrollable">
					<table id="customerTable" class="table table-striped table-hover table-bordered "></table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-block btn-primary" data-dismiss="modal" @click="closeModal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="table-div">
		<div class="table-operator">
			<button class="btn green pull-right" @click="create">添加活动</button>
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
		var cid="#control";
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax : {
				url : "memberActivity/list_all",
				dataSrc : ""
			},
			columns : [
                {
                    title : "活动名称",
                    data : "name",
		    	},
                {
                    title : "活动折扣",
                    data : "disconut",
                },
                {
                    title : "是否开启",
                    data : "type",
                    createdCell:function (td,tdData) {
                        if (tdData == 1) {
                            $(td).html("是");
                        }else{
                            $(td).html("否");
						}
                    }
                },
                {
                    title : "创建时间",
                    data : "createTime",
                    createdCell:function (td,tdData) {
                        var data = new Date(tdData).format("yyyy-MM-dd hh:mm:ss");
                        $(td).html(data);
                    }
                },
                {
                    title : "修改时间",
                    data : "updateTime",
                    createdCell:function (td,tdData) {
                        if (tdData != null){
                            var data = new Date(tdData).format("yyyy-MM-dd hh:mm:ss");
                            $(td).html(data);
						}else{
                            $(td).html("--");
						}
                    }
                },
                {
                    title:"操作",
                    data:"id",
                    createdCell:function (td,tdData,rowData,row) {
                        var button = $("<button class='btn btn-xs btn-success'>绑定会员</button>");
                        button.click(function () {
                            vueObj.showModel(tdData);
                        });
                        var operator=[
                            C.createEditBtn(rowData),
                            button
                        ];
                        $(td).html(operator);
                    }
                }
			],
		});

        var $customerTable = $("#customerTable");
        var customerTable = $customerTable.DataTable({
            lengthMenu: [ [10, 50, 100, 150], [10, 50, 100, "All"] ],
            columns : [
                {
                    title : "电话号码",
                    data : "telephone",
                },
                {
                    title : "微信昵称",
                    data : "nickname"
                },
                {
                    title : "折扣",
                    data : "discount"
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function (td,tdData,rowData) {
                        var button = $("<button class='btn btn-xs btn-danger'>删除</button>");
                        button.click(function () {
                            vueObj.deleteCustomer(tdData, rowData.telephone);
                        });
                        $(td).html(button);
                    }
                }
            ],
        });
		
	 var C = new Controller(null,tb);
		 var vueObj = new Vue({
			el:"#control",
			data:{
                showform: false,
                telephone : null,
				id : null,
				telephones : {},
				m : {
                    type : 1
				}
			},
			mixins:[C.formVueMix],
			methods:{
			    save : function () {
			        var that = this;
					var reg = /^[0-1]$|^00?\.(?:0[1-9]|[1-9][0-9]?)$/;
					if (!reg.test(that.m.disconut)){
					    toastr.clear();
					    toastr.error("活动折扣请输入0-1之间的数字，可保留小数点后两位数字");
					    return;
					}
					var action = $("#memberActivity").attr("action");
					$.post(action, $("#memberActivity").serialize(), function (result) {
						if (result.success){
						    toastr.clear();
						    toastr.success("保存成功");
                            that.showform = false;
                            that.m = {type : 1};
                            tb.ajax.reload();
						}else{
                            toastr.clear();
                            toastr.error("保存失败");
						}
                    });
                },
				create : function(){
                     this.showform = true;
                     this.m = {
                         type : 1
					 };
				},
                edit : function (model) {
                    this.showform = true;
                    this.m = model;
                },
                showModel : function (id) {
				    var that = this;
				    that.id = id;
					$.post("memberActivity/selectCustomer",{id : id}, function (result) {
						if (result.success){
						    //清空当前id在telephones对应的属性的值
						    that.telephones[id] = "";
                            customerTable.clear();
                            customerTable.rows.add(result.data).draw();
                            for (index in result.data){
                                that.telephones[id] = that.telephones[id] + result.data[index].telephone + ",";
							}
						}
                    });
					$("#orderDetail").modal();
                },
                inputCustomer : function () {
				    console.log("所有手机号记录：" + JSON.stringify(this.telephones));
				    console.log("当前手机号：" + this.telephone);
				    var that = this;
				    var flg = false;
				    //查看当前添加的手机号是否存在
				    for (tele in that.telephones){
				        var str = that.telephones[tele];
				        if (str.indexOf(that.telephone) != -1){
                            flg = true;
						}
					}
				    //验证手机号是否正确
                    var reg = /^((13[\d])|(15[0-35-9])|(18[\d])|(145)|(147)|(17[0135678]))\d{8}$/;
                    if (!reg.test(that.telephone)){
                        toastr.clear();
                        toastr.error("手机号格式错误，请输入正确格式的手机号");
                        return;
					}else if (flg){
                        toastr.clear();
                        toastr.error("该手机号已存在于折扣活动当中，请勿重复添加");
                        return;
					}
					$.post("memberActivity/inputCustomer", {telephone : that.telephone, activityId : that.id}, function (result) {
						if (result.success){
						    toastr.clear();
						    toastr.success("录入会员成功");
						    that.showModel(that.id);
						}else{
                            toastr.clear();
                            if (result.message){
                                toastr.error(result.message);
							}else {
                                toastr.error("录入会员失败");
                            }
						}
                    });
                },
                deleteCustomer : function (id, telephone) {
				    var that = this;
				    C.confirmDialog("确认删除该会员？", "提示", function () {
                        $.post("memberActivity/deleteMemberActivityThing", {id: id}, function (result) {
                            if (result.success){
                                toastr.clear();
                                toastr.success("删除成功");
                                that.showModel(that.id);
                            }else{
                                toastr.clear();
                                toastr.error("删除失败");
                            }
                        });
                    });
                }
			}
			
		});
		
		 C.vue=vueObj; 
	}());
	
</script>
