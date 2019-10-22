function uploadFile(inputDom,url,filename,aftersuccess,aftererror){
	var watting = new dialog({title:"数据正在处理中...",width:200});
	watting.showModal();
	console.log(url,filename);
	var formdata = new FormData(); 
	var files = inputDom.files;
	formdata.append(filename,files[0]);
	$.ajax({
		 type: 'POST', 
		 url:url,
		 data:formdata,
		 contentType:false,
		 processData:false,
		 success:function(result){
			if(result.success){
				var resDialog= new dialog({title:"上传完成",width:500});
				resDialog.show();
			}
			aftersuccess&&aftersuccess(result);
		 },
		 error:C.ajaxError,
		 complete:function(){
			 watting.remove();
		 }
	});
}