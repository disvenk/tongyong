<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki"> 表单</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" action="brandaccount/updateAccount"
						  @submit.prevent="save">
						<div class="form-body">
							<div class="form-group">
								<label>品牌名称</label>
								<input type="text" class="form-control" name="brandId" v-model="m.brandName" disabled>
							</div>
							<%--<div class="form-group">--%>
							<%--<label>brandSettingId</label>--%>
							<%--<input type="品牌设置id" class="form-control" name="brandSettingId" v-model="m.brandSettingId">--%>
							<%--</div>--%>
							<div class="form-group">
								<label>账户余额</label>
								<input type="text" class="form-control" name="accountBalance" v-model="m.accountBalance"
									   disabled>
							</div>
							<div class="form-group">
								<label>已使用</label>
								<input type="text" class="form-control" name="amountUsed" v-model="m.amountUsed"
									   disabled>
							</div>
							<div class="form-group">
								<label>总共发票</label>
								<input type="text" class="form-control" name="totalInvoiceAmount"
									   v-model="m.totalInvoiceAmount" disabled>
							</div>
							<div class="form-group">
								<label>已开发票</label>
								<input type="text" class="form-control" name="usedInvoiceAmount"
									   v-model="m.usedInvoiceAmount" disabled>
							</div>
							<div class="form-group">
								<label>剩余可开发票</label>
								<input type="text" class="form-control" name="remainedInvoiceAmount"
									   v-model="m.remainedInvoiceAmount" disabled>
							</div>
							<%--<div class="form-group">--%>
							<%--<label>更新时间</label>--%>
							<%--<input type="text" class="form-control" name="updateTime" v-model="m.updateTime">--%>
							<%--</div>--%>

							<div class="form-group">
								<label>需要手动增加的金额</label>
								<input type="number" class="form-control" name="addAccount" placeholder="增加的金额"
									   v-model="addAccount" required>
							</div>

							<div class="form-group">
								<label>密码验证</label>
								<input type="text" class="form-control" name="password" placeholder="谜子口令"
									   required>
							</div>

							<div class="form-group">
								<label>修改人</label>
								<input type="text" class="form-control" name="userName"
									    required>
							</div>

						</div>
						<input type="hidden" name="id" v-model="m.id"/>
						<input class="btn green" type="submit" value="保存"/>
						<a class="btn default" @click="cancel">取消</a>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="brandaccount/add">
				<button class="btn green pull-right" @click="create">新建</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter">&nbsp;</div>
		<div class="table-body">
			<table class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
</div>


<script>
    (function () {
        var cid = "#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax: {
                url: "brandaccount/list_all",
                dataSrc: ""
            },
            columns: [
                {
                    title: "品牌名称",
                    data: "brandName"
                },
                {
                    title: "账户余额",
                    data: "accountBalance",
                },
                {
                    title: "已使用",
                    data: "amountUsed",
                },
                {
                    title: "发票总额",
                    data: "totalInvoiceAmount",
                },
                {
                    title: "已开发票总额",
                    data: "usedInvoiceAmount",
                },
                {
                    title: "剩余可开发票总额",
                    data: "remainedInvoiceAmount",
                },
                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        console.log(rowData);
                        var operator = [
                            <s:hasPermission name="brandaccount/updateAccount">
                            C.createEditBtn(rowData)
                            </s:hasPermission>
                        ];
                        $(td).html(operator);
                    }
                }],
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix]

        });
        C.vue = vueObj;
    }());

</script>
