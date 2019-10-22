//弹窗组件
Vue.component("weui-dialog", {
    props: ['show','msg','type'],
    template: '<div class="weui_loading_toast" v-if="show">' +
    '<div class="weui_mask" @click="close"></div>' +
    '<div class="weui_to msg-di">' +		
		'<img src="images/correctImg.png" class="msgImg" v-if="type && type == 1" />' +
		'<img src="images/errorMsg.png" class="msgImg" v-if="type && type == 2" />' +
    	'<p>{{msg}}</p>' +
    '</div>' +
    '</div>',
    methods: {
        close: function () {
        	console.log(this.type);
            this.show = false;
        }
    }
});