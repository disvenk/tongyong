

/**
 * 打开位置
 * @param {Object} latitude  经度
 * @param {Object} longitude 纬度
 * @param {Object} name 地名
 * @param {Object} address 地址
 */
function wxOpenLocation(latitude,longitude,name,address){
	wx.openLocation({
		latitude: latitude, // 纬度，浮点数，范围为90 ~ -90
		longitude: longitude, // 经度，浮点数，范围为180 ~ -180。
		name: name, // 位置名
		address: address, // 地址详情说明
		scale: 20, // 地图缩放级别,整形值,范围从1~28。默认为最大
		infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
	});	
}

/**
 * 设置分享到朋友圈按钮内容
 * @param {String} title
 * @param {String} link
 * @param {String} imgUrl
 * @param {Function} successCallback
 * @param {Function} cancelCallback
 */
function wxShareToMoments(title,link,imgUrl,successCallback,cancelCallback){
	wx.onMenuShareTimeline({
	    title: title, // 分享标题
	    link: link, // 分享链接
	    imgUrl: imgUrl, // 分享图标
	    success: function () {
	    	if(successCallback){
	    		 successCallback();
	    	}
	    },
	    cancel: function () { 
	    	if(cancelCallback){
	    		cancelCallback();
	    	}
	    }
	});	
}

