<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags" %>
<style>
    .formBox{
        color: #5bc0de;
    }
    .gray{
        color: #5e6672;
    }
</style>

<div id="front" class="row" style="display: none">
    <div class="col-md-12" >
        <div class="portlet light bordered">
            <div class="portlet-title text-center">
                <div class="caption" style="float: none;">
                    <span class="caption-subject bold font-blue-hoki"><strong>您还未开启新版评论</strong></span>
                </div>
            </div>
            <%--<div class="portlet-body">
                <form role="form" class="form-horizontal" action="newAppraise/initAdd" @submit.prevent="save">
                    <div class="form-body">
                    </div>
                    <div class="text-center">
                        <input class="btn green" type="submit" value="是" />&nbsp;&nbsp;&nbsp;
                        <a class="btn default" @click="cancel">否</a>
                    </div>
                </form>
            </div>--%>
        </div>
    </div>
</div>
<div id="control" class="row" style="display: none">
    <div class="col-md-12" >
        <div class="portlet light bordered">
            <div class="portlet-title text-center">
                <div class="caption" style="float: none;">
                    <span class="caption-subject bold font-blue-hoki"><strong>新版评论权重设置</strong></span>
                </div>
            </div>
            <div class="portlet-body">
                <form role="form" class="form-horizontal" action="newAppraise/updateWeight" @submit.prevent="save" id="update">
                    <div class="form-body">
                        <table class="table table-striped table-hover table-bordered" id = "newAppraiseTable">
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        $.post("newAppraise/selectAll",function(result){
            if (result.data!=null){
                $("#control").show();
                $("#newAppraiseTable").append("<tr><th>评论类型</th><th>是否启用</th><th>分数</th></tr>");
                for(var i in result.data){
                    if(result.data[i].state==1){

                        $("#newAppraiseTable").append("<tr><td><input type='hidden' name='id' value='"+result.data[i].id+"'/>"+result.data[i].name+"</td><td>" +
                            "<input type='radio' name='state"+i+"' value='"+result.data[i].state+"' checked='checked' onchange='upperCase(this,"+i+")'>启用&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='state"+i+"' value='"+result.data[i].state+"' onchange='upperCase(this,"+i+")'>禁用" +
                            "</td><td><input style='border: none' type='text' name='score' value='"+result.data[i].score+"'/></td></tr>");
                    }else{
                        $("#newAppraiseTable").append("<tr><td><input type='hidden' name='id' value='"+result.data[i].id+"'/>"+result.data[i].name+"</td><td>" +
                            "<input type='radio' name='state"+i+"' value='"+result.data[i].state+"' onchange='upperCase(this,"+i+")' >启用&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='state"+i+"' value='"+result.data[i].state+"'  checked='checked' onchange='upperCase(this,"+i+")'>禁用" +
                            "</td><td><input style='border: none' type='text' name='score' value='"+result.data[i].score+"'/></td></tr>");
                    }

                }
                $("#update").append("<div class='text-center'><input class='btn green'  id='submit' onclick='update()' type='button' value='确定' />&nbsp;&nbsp;&nbsp;\n" +
                    "<a class='btn default' @click='cancel'>取消</a></div>")
            }else {
                $("#front").show();
            }
        },"json");
    });

    function upperCase(state,i) {
        if(state.value==1){
            var score = $('input[name="state'+i+'"]').parent().next().children();
            score.val(0);//禁用后分數為0
            var b = $('input[name="state'+i+'"]');
            b.attr("value",0);//设置状态
            score.attr("readonly","readonly");
        }else{
            var score = $('input[name="state'+i+'"]').parent().next().children();
            score.removeAttr("readonly");
            var b = $('input[name="state'+i+'"]');
            b.attr("value",1);//设置状态
        }


    }
    function update() {
        var totalRow = 0
        $('#newAppraiseTable tr').each(function() {
            if($(this).find('td:eq(2)').children().val()!=undefined){
                totalRow += parseFloat($(this).find('td:eq(2)').children().val());
            }
        });
        if(totalRow!=100){
            toastr.clear();
            toastr.error("分数设置总和不等于100");
        }else{
            $.post("newAppraise/updateWeight",{data:$("#update").serialize()},function(result){
                toastr.clear();
                if (result.success){
                    toastr.success("设置成功");
                }else{
                    toastr.error("设置失败");
                }
                return;
            });
        }
    }
</script>
