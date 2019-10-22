<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="row">
	<div class="col-sm-6">
		<div class="panel panel-success">
		  <div class="panel-heading text-center"><strong>未沽清</strong></div>
		  <div class="panel-body">
		  	<table id="table_notEmpty" class="table table-striped table-bordered table-hover text-center"></table>
		  </div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="panel panel-info">
		  <div class="panel-heading text-center"><strong>已沽清</strong></div>
		  <div class="panel-body">
		  	<table id="table_hasEmpty" class="table table-striped table-bordered table-hover text-center"></table>
		  </div>
		</div>
	</div>
</div>
<script>

// databalse Columns 的默认项
var defalutColumns = [
//     {
// 		title : "编号",
//     	data : "peference",
//     	defaultContent:"",
//     },
	{
		title : "类别",
		data : "articleFamilyName"
	},
	{
		title : "名称",
		data : "name"
	},
	{
		title : "操作",
		data : "id",
		createdCell : function(td, tdData, rowData) {
			var str = ""
			if (rowData.isEmpty == 1) {
				str = "<button type='button' onclick='setEmpty(\""
						+ tdData
						+ "\",0);' class='btn btn-sm btn-info'>不沽清</button>";
			} else {
				str = "<button type='button' onclick='setEmpty(\""
						+ tdData
						+ "\",1);' class='btn btn-success btn-sm'>沽&nbsp;&nbsp;&nbsp;&nbsp;清</button>";
			}
			$(td).html(str);
		}
	}, ]
              	
    //未沽清列表
	var tb_notEmpty = $("#table_notEmpty") .DataTable({
		dom: 'ftp',
		ajax : {
			url : "article/getArticleList",
			dataSrc : "data",
			data : { isEmpty : "0" }
		},
		columns : defalutColumns
	});
	
	//已沽清列表
	var tb_hasEmpty = $("#table_hasEmpty").DataTable({
		dom: 'ftp',
		pageLength:8,
		ajax : {
			url : "article/getArticleList",
			dataSrc : "data",
			data : { isEmpty : "1" }
		},
		columns : defalutColumns
	});

	// 设置是否沽清
	function setEmpty(articleId, isEmpty) {
		$.ajax({
			url : "article/setArticleEmpty",
			data : "articleId=" + articleId + "&isEmpty=" + isEmpty,
			type : "post",
			success : function() {
				tb_notEmpty.ajax.reload();
				tb_hasEmpty.ajax.reload();
			}
		})
	}

	
</script>