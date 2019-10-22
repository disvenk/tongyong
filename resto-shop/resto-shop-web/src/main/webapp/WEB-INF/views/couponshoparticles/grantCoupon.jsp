<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style type="text/css">
	
</style>

<div role="tabpanel" class="tab-pane" id="personalLoans">
	<a class="btn btn-info ajaxify" href="couponshoparticles/list" style="margin-bottom: 30px;">
    	<span class="glyphicon glyphicon-circle-arrow-left"></span>返回
    </a>
    <form class="form-inline">    	
        <div class="form-group">
            <label>查询用户</label>
            <input type="text" style="margin-left: 10px;" class="form-control" v-model="phone" placeholder="请录入手机号">
            <button type="button" style="margin-left: 10px;" class="btn btn-success" @click="searchInfo">查询</button>
            <button type="button" style="margin-left: 10px;" class="btn btn-primary" @click="grantCoupon">发放</button>
        </div>
    </form>
    <br/><br/>
    <table id="personalLoansTable" class="table table-striped table-bordered table-hover"
           style="width: 100%;">
    </table>
</div>

<script>
	var couponId = ${param.couponId};
	console.log(couponId);
	new Vue({
        el : "#personalLoans",
        data : {
        	personalLoansTable : {}, 	//个人发放datatables对象
        	phone: "",					//录入查询的手机号
        	personId: false
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.personalLoansTable=$("#personalLoansTable").DataTable({
	                lengthMenu: [ [100, 50, 10], [100, 50, 10] ],
	                order: [[ 6, "desc" ]],
	                columns : [
	                    {
	                        title : "用户类型",
	                        data : "customerType",
	                        orderable : false,
	                        s_filter: true
	                    },
	                    {
	                        title : "储值",
	                        data : "isValue",
	                        orderable : false,
	                        s_filter: true
	                    },
	                    {
	                        title : "昵称",
	                        data : "nickname",
	                        orderable : false
	                    },
	                    {
	                        title : "性别",
	                        data : "sex",
	                        orderable : false,
	                        s_filter: true
	                    },
	                    {
	                        title : "手机号",
	                        data : "telephone",
	                        orderable : false
	                    },
	                    {
	                        title : "生日",
	                        data : "birthday"
	                    },
	                    {
	                        title : "订单总数",
	                        data : "orderCount"
	                    },
	                    {
	                        title:"订单总额" ,
	                        data:"orderMoney"
	                    },
	                    {
	                        title:"平均消费金额" ,
	                        data:"avgOrderMoney"
	                    }
	                ]
	            });
         	},
         	searchInfo: function () {
         		var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
                if(this.phone == ""){
                	toastr.error('请填写手机号');
                	return;
                }else if(!myreg.test(this.phone)){
                	toastr.error('请输入有效的手机号码');
                	return;
                }
                this.selectFunction();                
         	},
         	selectFunction : function () {
         		var that = this;
                toastr.clear();
                toastr.success("查询中...");
                try{
  	      			$.ajax({					
		                url : 'newcustomcoupon/selectCustomer',
		                type: "post",
		                data : {
							text: that.phone
	                    },
		                success : function(result) {
	                		if (result.success) {
	                			console.log(JSON.stringify(result.data));
	                            that.personId = false;
	                            if(result.data != null){
	                                that.personId = true;
	                            }
	                            //清空表格
	                            that.personalLoansTable.clear();
	                            that.personalLoansTable.rows.add(result.data).draw();
	                            toastr.success("查询成功");
	                        }else {
	                            toastr.error("查询失败");
	                        }
	                	}
	            	})
                }catch(e){
                    toastr.clear();
                    toastr.error("系统异常，请刷新重试");
                }                              
         	},
         	grantCoupon:function(){
        	    var that = this;
        	    if (!that.personId){
                    toastr.clear();
                    toastr.error("暂无发放用户");
                    return;
                }
    			$.ajax({					
	                url : 'couponshoparticles/grantCoupon',
	                type: "post",
	                data : {
                    	couponId:couponId,
						phone: that.phone
                    },
	                success : function(res) {
	                    if(res.success){
                            toastr.success("发放成功!");
                        }else {
                            toastr.error(res.message);
                        }

                	}
            	})
    		},
        },
        created : function() {
            this.initDataTables();
        }
    })    
	
</script>