Vue.component('img-file-upload', {
	props:["cut"],
	//template:'<input type="file" @change="uploadFile" cut={{cut}}>',
	template:'<input type="file" @change="uploadFile">',
	data:function(){
		return {
			types:[".jpg",".png",".gif",".bmp"]
		}
	},
	methods:{
		uploadFile:function(e){
			var that =this;
			var obj = e.target;
			var file = obj.files[0];
			var filename = file.name;
			if(this.imageNameVailed(filename)){
				var formdata = new FormData(); 
				formdata.append("file",file);
				//var cut = $(that).attr("cut");
				var cut = this.cut;
				if(cut=="false"){//不压缩   (如过未申明type属性，则默认为压缩)
					formdata.append("type","false");
					console.log("不压缩");
				}else{
					formdata.append("type","true");
					console.log("压缩");
				}
				$.ajax({
					 type: 'POST', 
					 url:"upload/file",
					 data:formdata,
					 contentType:false,
					 processData:false
				}).then(
					function (result){
						if(result.success){
							that.$dispatch("success",result.data);
						}else{
							that.$dispatch("error","图片上传失败，请压缩后再上传！");
						}
					},
					function (){
						that.$dispatch("error","文件上传失败");
					}
				);
			}else{
				that.$dispatch("error","文件类型错误")
			}
		},
		imageNameVailed:function(name){
			if(name.indexOf(".")!=-1){
				var lastName = name.substring(name.indexOf("."));
				for(var i in this.types){
					var fix = this.types[i];
					if(lastName==fix){
						return true;
					}
				}
			}
			return false;
		}
	}
});
