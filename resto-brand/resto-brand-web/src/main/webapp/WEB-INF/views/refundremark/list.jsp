<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
    .switch_box {
        -webkit-user-select:none;
        -moz-user-select:none;
        -ms-user-select:none;
        user-select:none;
    }
    .switch_box .widget_switch_checkbox {
        display:none !important;
    }
    .switch_box .widget_switch_label {
        margin-top: 5px;
        /* background-color: grey; */
        background-color: #ccc;
        display: inline-block;
        position: relative;
        height: 30px;
        line-height: 30px;
        text-indent: 54px;
        color: #fff;
        width: 80px;
        border-radius: 40px !important;
        box-shadow: 0 0 2px black;
    } 
    .switch_box .widget_switch_circle {
        position: absolute;
        display: inline-block;
        /* height: 30px; */
        height: 26px;
        line-height: 26px;
        margin-top: 2px;
        text-align: center;
        width: 36px;
        border-radius: 13px !important;
        border: 2px solid #eee;
        background-color: #fff;
        /* box-shadow:  */
        left: 0;
        -webkit-transition: all 0.3s;
        -moz-transition: all 0.3s;
        -ms-transition: all 0.3s;
        -o-transition: all 0.3s;
        transition: all 0.3s;
    }
    .switch_box .widget_switch_checkbox:checked~.widget_switch_label .widget_switch_circle {
        left: 44px;
    }
    .switch_box .widget_switch_checkbox:checked~.widget_switch_label {
        /* background:lime; */
        background:#3992FB;
        text-indent: 10px;
    }

</style>
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
                    <form role="form" action="{{m.id?'refundremark/modify':'refundremark/create'}}"
                          @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group col-sm-12">
                                <label class="col-md-3 control-label">原因类型：</label>
                                <div class="col-sm-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="type" v-model="m.type" value="1"> 退菜
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="type" v-model="m.type" value="2"> 赠菜
                                    </label>
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-md-3 control-label">{{m.type==2?'赠菜':'退菜'}}原因：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="name" v-model="m.name">
                                </div>
                            </div>

                            <div class="form-group col-sm-12">
                                <label class="col-md-3 control-label">序号：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="sort" v-model="m.sort">
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-md-3 control-label">是否启用：</label>
                                <div class="col-sm-8">
                                    <label class="radio-inline">
                                        <input type="radio" name="state" v-model="m.state" value="1"> 启用
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="state" v-model="m.state" value="0"> 不启用
                                    </label>
                                </div>
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
            <s:hasPermission name="refundremark/add">
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
        var remarkAPI = null; // datatable api
        var cid = "#control";
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax: {
                url: "refundremark/list_all",
                dataSrc: function ( json ) {
                    json = json.map((item) => {
                        // 1：退菜； 2：赠菜
                        var txt = '退菜'
                        if (item.type == 2) {
                            txt = '赠菜'
                        }
                        item.typeName = txt
                        return item;
                    })
                    return json
                }
            },
            'ordering':false,
            columns: [
                {
                    title: "原因类别",
                    data: "typeName",
                    orderable : false,
                    s_filter: true
                },
                {
                    title: "原因",
                    orderable : false,
                    data: "name",
                },
                {
                    title: "序号",
                    data: "sort",
                },
                {
                    title: "状态",
                    data: "state",
                    createdCell: function (td, tdData, rowData) {
                        let checked = '';
                        let txt = '关'
                        if (tdData == 1) {
                            checked = 'checked';
                            txt = '开'
                        }
                        let htmlStr = `
                            <div class="switch_box">
                                <input type="checkbox" \${checked} class="widget_switch_checkbox" id="widget_switch_checkbox\${rowData.id}">
                                <label for="widget_switch_checkbox\${rowData.id}" class="widget_switch_label">
                                    <span class="widget_switch_circle"></span>
                                    <span class="text">\${txt}</span>
                                </label>
                            </div>`;
                        var btn = $(htmlStr);
                        btn.find('input').on('click', function(e) {
                            let txt = '关'
                            rowData.state = 0;                            
                            if (this.checked == true) {
                                txt = '开'
                                rowData.state = 1;
                            }
                            delete rowData.createTime
                            $.ajax({
                                url: "refundremark/modify",
                                type: "post",
                                data: rowData,
                                dataType: "json",
                                success: function (res) {
                                    if (res.success == true) {
                                        btn.find('span.text').text(txt);
                                    }
                                }

                            });
                        })

                        if (tdData == 1) {
                            // $(td).html("开启");
                            $(td).html(btn);
                        } else if (tdData == 0) {
                            // $(td).html("未开启")
                            $(td).html(btn);
                        }
                    }
                },
                {
                    title: "操作",
                    data: "id",
                    createdCell: function (td, tdData, rowData, row) {
                        var operator = [];
                        <s:hasPermission name="refundremark/delete">
                            operator.push(C.createBtn(rowData).html("删除"));
                        </s:hasPermission>
                        var button = $("<button class='btn btn-xs btn-primary'>编辑</button>");
                        button.on('click', function(e) {
                            e.preventDefault() 
                            e.stopPropagation()
                            vueObj.edit(rowData);
                        })
                        // operator.push(C.createBtn(rowData).html("编辑"));
                        operator.push(button);
                        $(td).html(operator);
                    }
                }],
                initComplete: function () {
                	remarkAPI = this.api();
                	remarkTable();
                }
        });

        var C = new Controller(null, tb);
        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix],
            methods: {
                // 新建时 给默认值
                create:function(){
					this.m={type: 1, state: 1};
					this.openForm();
				},
                
            }
        });
        C.vue = vueObj;
        function remarkTable() {
            var api = remarkAPI;
            var columnsSetting = api.settings()[0].oInit.columns;
            $(columnsSetting).each(function (i) {
                if (this.s_filter) {
                    var column = api.column(i);
                    var select = $('<select id=""><option value="">' + this.title + '(全部)</option></select>');
                    column.data().unique().each(function (d) {
                        select.append('<option value="' + d + '">' + d + '</option>')
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
    }());


</script>
