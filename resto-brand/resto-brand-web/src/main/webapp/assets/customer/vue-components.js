var fileUploadMix = {
	template:'<input type="file" @change="uploadFile">',
	data:function(){
		return {
			types:[".zip"],
			postUrl:"",
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
				$.ajax({
					type: 'POST',
					url:that.postUrl,
					data:formdata,
					contentType:false,
					processData:false
				}).then(
					function (url){
						that.$dispatch("success",url);
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
		}
	}
};

Vue.component("weui-dialog", {
    props: ['show','msg','type'],
    template: '<div class="weui_loading_toast" v-if="show">' +
    '<div class="weui_mask_transparent"></div>' +
    '<div class="weui_toast msg-dialog">' + 
    '<img src="assets/pages/img/loading.gif" class="msgImg" v-if="type && type == 1" />' +
    '<img src="assets/pages/img/correctImg.png" class="msgImg" v-if="type && type == 2"/>' +
    '<p style="top:0px;">{{msg}}</p>' +
    '</div>' +
    '</div>',
    methods: {

    }
});

Vue.component('img-file-upload', {
	props:["cut"],
	//template:'<input type="file" @change="uploadFile" cut={{cut}}>',
	template:'<input type="file" @change="uploadFile">',
	data:function(){
		return {
			types:[".jpg",".png",".gif",".bmp",".pdf"]
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
							that.$dispatch("error","后台报错了，稍后再试试吧！");
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

