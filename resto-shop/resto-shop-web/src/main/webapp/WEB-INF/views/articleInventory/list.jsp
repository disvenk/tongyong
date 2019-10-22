<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<div id="control">
	<div class="row form-div" v-if="showform">
		<div class="col-md-offset-3 col-md-6" >
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<span class="caption-subject bold font-blue-hoki">分配店铺</span>
					</div>
				</div>
				<div class="portlet-body">
					<form role="form" class="form-horizontal" >
						<div id="shops">

                            <div class="table-body">
                                <table id="shopList" class="table table-striped table-hover table-bordered "></table>
                            </div>
						</div>
						<div class="text-center">
							<input type="hidden" name="id" v-model="m.id" />
							<input class="btn green" type="button" @click="save" value="分配" />
							<a class="btn default" @click="cancel">取消</a>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="table-div">
		<div class="table-operator">
			<s:hasPermission name="articleInventory/assigned">
			<button class="btn green pull-right" @click="create">分配店铺</button>
			</s:hasPermission>
		</div>
		<div class="clearfix"></div>
		<div class="table-filter"></div>
		<div class="table-body">
			<table id="articleList" class="table table-striped table-hover table-bordered "></table>
		</div>
	</div>
</div>


<script>
	(function(){
		var cid="#control";
		var articleType = {
			1: "单品",
			2: "套餐"
		}
		var allArticles = [];

		var $table = $("#articleList");
		var tb = $table.DataTable({
			"lengthMenu": [[50, 75, 100, 150], [50, 75, 100, "All"]],
			ajax: {
				url: "article/list_all",
				dataSrc: ""
			},

			deferRender: true,
			ordering: false,

			columns: [
				{
					title: "全选 <input type='checkbox' id='checkAll' />",
					data:"id",
					width:'10%',
					createdCell: function (td, tdData) {
						$(td).html("<input type='checkbox' style='width:30px;height:30px' value=\" "+tdData+"\" name= 'artcielCheck' />");
					},
				},
				{
					title: "餐品类别",
					data: "articleFamilyName",
					s_filter: true,
				},
				{
					title: "类型",
					data: "articleType",
					createdCell: function (td, tdData) {
						$(td).html(articleType[tdData]);
					},
					s_filter: true,
					s_render: function (d) {
						return articleType[d];
					}
				},
				{
					title: "名称",
					data: "name",
				},
				{
					title: "价格",
					data: "price",
				},
				{
					title: "粉丝价",
					data: "fansPrice",
					defaultContent: "",
				},
				{
					title: "图片",
					data: "photoSmall",
					defaultContent: "",
					createdCell: function (td, tdData) {
						$(td).html("<img src='" + tdData + "' style='height:40px;width:80px;'/>")
					}
				},

				{
					title: "工作日库存",
					data: "stockWorkingDay",
					defaultContent: "0"
				},
				{
					title: "周末库存",
					data: "stockWeekend",
					defaultContent: "0"
				}
				],


			initComplete: function () {
				var api = this.api();
				api.search('');
				var data = api.data();
				for (var i = 0; i < data.length; i++) {
					allArticles.push(data[i]);
				}

				$("#checkAll").change(function() {

					if($("#checkAll").prop("checked")){
						$("input[name='artcielCheck']").prop("checked", true);

					}else{
						console.log("-----");
						$("input[name='artcielCheck']").prop("checked", false);
					}
				});

					var columnsSetting = api.settings()[0].oInit.columns;
				$(columnsSetting).each(function (i) {
					if (this.s_filter) {
						var column = api.column(i);
						var title = this.title;
						var select = $('<select><option value="">' + this.title + '(全部)</option></select>');
						var that = this;
						column.data().unique().each(function (d) {
							select.append('<option value="' + d + '">' + ((that.s_render && that.s_render(d)) || d) + '</option>')
						});

						select.appendTo($(column.header()).empty()).on('change', function () {
							var val = $.fn.dataTable.util.escapeRegex(
									$(this).val()
							);
							column.search(val ? '^' + val + '$' : '', true, false).draw();
						});
					}
				});
			}
		});


        var C = new Controller(cid,tb);
		var vueObj = new Vue({
			el:"#control",
			mixins:[C.formVueMix],
			data: {
				shopList:[]
			},
            created:function(){
                this.$watch("showform",function(){
                    if(this.showform){
                        App.initUniform();
                        Vue.nextTick(function () {
                            var $shop = $("#shopList");
                            var shop = $shop.DataTable({
                                "lengthMenu": [[-1,5, 10, 15], ["全部",5, 10, 15]],
								"scrollY": "300px",
                                ajax: {
                                    url: "shopDetail/list_without_self",
                                    dataSrc: "data"
                                },

                                deferRender: true,
                                ordering: false,

                                columns: [
                                    {
                                        title: "全选 <input type='checkbox' style='width:20px;height:20px' id='checkShop'   />",
                                        data:"id",
                                        width:'30%',
                                        createdCell: function (td, tdData) {
                                            $(td).html("<input type='checkbox' style='width:20px;height:20px' name='shopType'  value=\" "+tdData+"\"  />");
                                        },
                                    },
                                    {
                                        title: "店铺名称",
                                        data: "name",
                                        s_filter: true,
                                    }
                                ],
                                initComplete: function () {
                                    $("#checkShop").change(function() {

                                        if($("#checkShop").prop("checked")){
                                            $("input[name='shopType']").prop("checked", true);

                                        }else{
                                            console.log("-----");
                                            $("input[name='shopType']").prop("checked", false);
                                        }
                                    });


                                }

                            });
                        })
                    }
                    this.$watch("m",function(){
                        if(this.showform){
                            App.initUniform();
                            App.updateUniform();
                        }
                    },{
                        deep:true
                    });
                });


            },
			methods:{
				create:function(){
					var sum = 0;
					var temp =$(":checkbox[name='artcielCheck']");
					for(var i = 0;i < temp.length;i++){
						if($(temp[i]).prop("checked")){
							sum ++;
						}
					}

				if(sum > 0){
					this.showform = true;
				}else{
					this.showform = false;
					C.errorMsg("未选择分配菜品！");
				}

				},
				save:function(e){
					var that = this;
					var formDom = e.target;



                    var data = {
                        shopList:[],
                        articleList:[]
                    }

                    var temp =$(":checkbox[name='shopType']");
                    for(var i = 0;i < temp.length;i++){
                        if($(temp[i]).prop("checked")){
                            data.shopList.push($(temp[i]).prop("value"));
                        }
                    }

                    if(data.shopList.length == 0){
                        C.errorMsg("未选择分配店铺！");
                        return;
                    }



                    var temp =$(":checkbox[name='artcielCheck']");
                    for(var i = 0;i < temp.length;i++){
                        if($(temp[i]).prop("checked")){
                            data.articleList.push($(temp[i]).prop("value"));
                        }
                    }


                    $.ajax({
                        type: "post",
                        url: "articleInventory/save",
                        data: {shopList:data.shopList.toString(),articleList:data.articleList.toString()},
                        success: function (result) {
                            if (result.success) {
                                that.showform = false;
                                that.m = {};
                                C.simpleMsg("保存成功");
                                tb.ajax.reload(null, false);
                            } else {
                                C.errorMsg(result.message);
                            }
                        },
                        error: function (xhr, msg, e) {
                            var errorText = xhr.status + " " + xhr.statusText + ":" + action;
                            C.errorMsg(errorText);
                        }
                    });



				},
			}
		});
		C.vue = vueObj;

	}());

</script>
