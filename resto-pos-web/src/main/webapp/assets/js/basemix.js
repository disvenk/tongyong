var mode = {1: "堂食", 2: "外卖", 3: "外带"};

var subpage = "${subpage}";

var callOrderTask = [];

var posmix = {
    template: '<div">                                                                                          ' +
    '   <choice-printer :show.sync="choicePrinterDialog.show" :oid="choicePrinterDialog.oid" :printers="choicePrinterDialog.printers" ></choice-printer>	' +
    '   <history-modal :show.sync="historyOrdersDialog.show" :title="historyOrdersDialog.title" :historyorders="historyOrdersDialog.historyorders"></history-modal>	' +
    '   <other-paymodel :show.sync="otherPayModelDialog.show" :ordermoney="otherPayModelDialog.orderMoney" :oid="otherPayModelDialog.oid"></other-paymodel>	' +
    '   <refund-articlemodel :show.sync="refundArticleModelDialog.show" :list="refundArticleModelDialog.refundList" :order="refundArticleModelDialog.order" :tangshi="refundArticleModelDialog.tangshi"></refund-articlemodel>	' +
    '   <refund-turntablemodel :show.sync="turnTableModelDialog.show" :order="turnTableModelDialog.order" :tangshi="turnTableModelDialog.tangshi":shop.sync="turnTableModelDialog.shop"></refund-turntablemodel>	' +
    '   <shanhui-dialog :show.sync="shanHuiDialog.show" :order.sync="shanHuiDialog.order"></shanhui-dialog> ' +
    '   <pos-discount-dialog :show.sync="posDiscountDialog.show" :order.sync="posDiscountDialog.order"></pos-discount-dialog> ' +
    '   <add_article_dialog :show.sync="addArticleDialog.show" :order.sync="addArticleDialog.order"></add_article_dialog>' +
    '   <msg-modal :show.sync="MsgModalDialog.show" :title="MsgModalDialog.title" :msg="MsgModalDialog.msg"></msg-modal> ' +
    '   <order-settlement :show.sync="orderSettlementDialog.show" :order.sync="orderSettlementDialog.order" :brandsetting.sync="orderSettlementDialog.brandsetting" :shop.sync="orderSettlementDialog.shop" :orderlist.sync="orderSettlementDialog.orderlist" :oldorder.sync="orderSettlementDialog.oldorder" :payvalue.sync="orderSettlementDialog.payvalue" :remainvalue.sync="orderSettlementDialog.remainvalue" :couponvalue.sync="orderSettlementDialog.couponvalue" :usecouponvalue.sync="orderSettlementDialog.usecouponvalue" :couponid.sync="orderSettlementDialog.couponid"></order-settlement> ' +
    '	<div class="row order-operator" >                                                                                       ' +
    '		<div class="col-sm-4 padding-0 full-height border-1">                                                              ' +
    '			<div class="order-detailed">                                                                                   ' +
    '				<div class="title">                                                                                        ' +
    '					<h4>订单信息' +
    '                   <span class="pull-right"><button v-if="brandSetting.openPosDiscount==1&&shop.openPosDiscount==1&&order.orderState==1&&order.payType==1&&order.payMode!=3&&order.payMode!=4" class=" btn-primary" style="margin-right: 10px" @click="getDiscount(order)">折扣</button>' +
    '                   <button v-if="brandSetting.turntable == 1 && shop.isTurntable==1 && order.id != null" class=" btn-primary" style="margin-right: 10px" @click="turnTable(order)">换桌</button>' +
    ' 					<span v-if="brandSetting.isPosPlus&&order.orderState==1&&order.payType==1&&order.allowContinueOrder" id="addArticle"><button class=" btn-primary" @click="addArticle(order)">加菜</button></span>' +
    '                   </span></h4> ' +
    '				</div>                                                                                                     ' +
    '				<div class="description">' +
    '                   <p> <span>点单方式:</span><span v-if="order.id != null"><span v-if="order.customerId==0">pos点单</span><span v-if="order.customerId!=0">微信点单</span></span></p> '+ '                                                                                 ' +
    '					<p> <span>订单编号:</span><span v-if="outFood">{{order.orderId}}</span> <span v-if="!outFood">{{order.serialNumber}}</span></p>        ' +
    '					<p>	<span>下单门店:</span> <span >{{order.shopName}}</span></p>                                            ' +
    '					<p>	<span>下单时间:</span><span v-if="outFood && order.createdAt">{{new Date(order.createdAt).format("yyyy-MM-dd hh:mm")}}</span> <span v-if="!outFood && order.createTime">{{new Date(order.createTime).format("yyyy-MM-dd hh:mm")}}</span></p>                                            ' +
    '					<p>	<span>顾客电话:</span> <span v-if="outFood">{{order.phoneList}}</span> <span v-if="!outFood">{{order.customer.telephone}}</span></p>                                           ' +
    '					<p>	<span v-if="!outFood">就餐模式:</span> <span>{{modeText}}</span></p>                                  ' +
    '					<p>	<span v-if="outFood">顾客姓名:</span> <span>{{order.consignee}}</span></p>                                  ' +
    '					<p>	<span v-if="outFood">送货地址:</span> <span>{{order.address}}</span></p>                                  ' +
    '					<p v-if="showTableNumber && !outFood">	<span>关联餐位:</span> <span id="turnNumber">{{order.tableNumber}}</span></p>                                  ' +
    '					<p v-if="order.id != null && !outFood">	<span>POS折扣:</span> <span>￥{{order.orderPosDiscountMoney == null ? 0 : order.orderPosDiscountMoney}}</span></p>                                  ' +
    '					<p v-if="order.id != null && !outFood">	<span>会员折扣:</span> <span>￥{{order.memberDiscountMoney == null? 0 : order.memberDiscountMoney}}</span></p>                                  ' +
        //'                   <p v-if="showCustomerCount">	<span>就餐人数:</span> <span>{{order.customerCount}}</span></p>                                                                                                                                                                                             ' +
    '				</div>                                                                                                     ' +
    // '				<div class="title" v-if="editHoufu" >                                                                                        ' + //这是后付模式走的代码现该模式已废弃，为避免混乱特将代码注掉  2017-11-04 wtl edit
    //     //'					<h4 v-if="unFinish"><span>餐品详情</span> <span class="pull-right" id="changeArticle"><button class=" btn-primary" v-if="!editChange" @click="changeOrderItem">编辑</button><button class=" btn-primary" @click="saveChangeOrder" v-if="editChange" style="margin-right: 10px">保存</button><button  class=" btn-primary" v-if="editChange" @click="cancelChange">取消</button><button v-if="brandSetting.turntable == 1 && shop.isTurntable==1 && order.id != null && order.allowContinueOrder==1" class=" btn-primary" style="margin-right: 10px" @click="turnTable(order)">换桌</button><button v-if="brandSetting.openPosReminder == 1 && order.id != null" class=" btn-primary" style="margin-left: 10px" @click="reminder">催菜</button></span></h4>' +
    // '					<h4 v-if="unFinish"><span>餐品详情</span> <span class="pull-right" id="changeArticle"><button class=" btn-primary" v-if="!editChange" @click="changeOrderItem">编辑</button><button class=" btn-primary" @click="saveChangeOrder" v-if="editChange" style="margin-right: 10px">保存</button><button  class=" btn-primary" v-if="editChange" @click="cancelChange">取消</button>' +
    // //'<button v-if="brandSetting.turntable == 1 && shop.isTurntable==1 && order.id != null" class=" btn-primary" style="margin-right: 10px" @click="turnTable(order)">换桌</button>' +
    // '<button v-if="brandSetting.openPosReminder == 1 && order.id != null" class=" btn-primary" style="margin-left: 10px" @click="reminder">催菜</button></span></h4>' +
    // '					<p v-if="unFinish"><span>餐品名称</span> <span class="pull-right" id="itemNum">数量/份</span></p>                    ' +
    // '					<h4 v-if="!unFinish"><span>餐品名称</span> <span class="pull-right" id="itemNum">数量/份</span></h4>                    ' +
    // '					<div class="article-items" id="articleBody">                                                                            ' +
    // '						<p style="height:30px;vertical-align:middle " v-if="brandSetting.isUseServicePrice == 1 && shop.isUseServicePrice == 1 && order.orderItems" id="{{brandSetting.id}}"  @click="selectServicePrice(shop,order,0)" ><span>{{shop.serviceName}}</span><span class="pull-right" v-if="!editChange || (editChange && selectedId != brandSetting.id)">{{order.customerCount}}</span><span v-if="brandSetting.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span></p>                                         ' +
    // '						<p style="height:30px;vertical-align:middle " v-if="!outFood" v-for="item in order.orderItems" id="{{item.id}}" @click="selectServicePrice(item,order,1)"><span>{{item.articleName}}</span><span v-if="!editChange || (editChange && selectedId != item.id)" class="pull-right">{{item.count}}</span><span v-if="item.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" readonly focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span></p>                                         ' +
    // '						<p v-if="outFood" v-for="item in order.details"><span>{{item.name}}</span><span class="pull-right">{{item.quantity}}</span></p>                                         ' +
    // '						<div v-for="child in childList">' +
    // '						<hr/>                                         ' +
    // '						<p  style="height:30px;vertical-align:middle " v-for="childItem in child.orderItems" id="{{childItem.id}}" @click="selectServicePrice(childItem,child,1)"><span>{{childItem.articleName}}</span><hidden id="{{childItem.id}}pId" value="{{child.id}}"/><span v-if="!editChange || (editChange && selectedId != childItem.id)" class="pull-right">{{childItem.count}}</span><span v-if="childItem.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" readonly focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span></p>                                         ' +
    // '                   </div>                                         ' +
    // '					</div>                                                                                                 ' +
    // '				</div>' +
    '				<div class="title" v-if="!editHoufu" >                                                                                        ' +
    '					<h4 v-if="!outFood && !unPrint" ><span>餐品详情</span> <span v-if="order.orderState > 1 && order.productionStatus >= 2 " class="pull-right" id="changeArticle"><button  class=" btn-primary"  @click="refundArticle(order)">退菜</button></span>' +
    //' 					<span v-if="brandSetting.isPosPlus&&order.orderState==1&&order.payType==1&&order.allowContinueOrder" class="pull-right" style="margin-left: 20px" id="addArticle"><button class=" btn-primary" @click="addArticle(order)">加菜</button></span>' +
        //' 					<span v-if="order.orderState == 1 && order.payMode != 3 && order.payMode != 4" class="pull-right" id="changeArticle"><button  class=" btn-primary" v-if="!editChange" @click="changeOrderItem">编辑</button><button  class=" btn-primary" @click="saveChangeOrder" v-if="editChange" style="margin-right: 10px">保存</button><button  class=" btn-primary" v-if="editChange" @click="cancelChange">取消</button></span><span class="pull-right"><button v-if="brandSetting.turntable == 1 && shop.isTurntable==1 && order.id != null && order.allowContinueOrder==1" class=" btn-primary" style="margin-right: 10px" @click="turnTable(order)">换桌</button><button v-if="brandSetting.openPosReminder == 1 && order.id != null" class=" btn-primary" style="margin-right: 10px" @click="reminder">催菜</button></span></h4>                    ' +
    ' 					<span v-if="order.orderState == 1 && order.payMode != 3 && order.payMode != 4" class="pull-right" id="changeArticle"><button  class=" btn-primary" v-if="!editChange" @click="changeOrderItem">编辑</button><button  class=" btn-primary" @click="saveChangeOrder" v-if="editChange" style="margin-right: 10px">保存</button><button  class=" btn-primary" v-if="editChange" @click="cancelChange">取消</button></span>' +
    '                   <span class="pull-right">' +
    //'<button v-if="brandSetting.openPosDiscount==1&&shop.openPosDiscount==1&&order.orderState==1&&order.payType==1&&order.payMode!=3&&order.payMode!=4" class=" btn-primary" style="margin-right: 10px" @click="getDiscount(order)">折扣</button>' +
    //'                   <button v-if="brandSetting.turntable == 1 && shop.isTurntable==1 && order.id != null" class=" btn-primary" style="margin-right: 10px" @click="turnTable(order)">换桌</button>' +
    '                   <button v-if="brandSetting.openPosReminder == 1 && order.id != null" class=" btn-primary" style="margin-right: 10px" @click="reminder">催菜</button></span></h4>                    ' +
    '					<h4 v-if="outFood || unPrint" ><span>餐品详情</span> <span class="pull-right" id="changeArticle"></span></h4>                    ' +
    '					<div id="laoyin">' +
    '					<p><input  type="hidden" style="width:40px;" /><span>餐品名称</span> <span class="pull-right" id="itemNum">数量/份</span></p>                    ' +
        // '					<h4 v-if="!unFinish"><span>餐品名称</span> <span class="pull-right" id="itemNum">数量/份</span></h4>                    ' +
    '					<div class="article-items" v-if="order.orderState == 1 || outFood">                                                                            ' +
    '						<p style="height:30px;vertical-align:middle " v-for="service in order.serviceList" id="{{service.id}}"  @click="selectServicePrice(service,order,service.type)"><span>{{service.name}}</span><span class="pull-right" v-if="!editChange || (editChange && selectedId != service.id)">{{service.count}}</span><span v-if="service.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span></p>                                         ' +
    '						<p style="height:30px;vertical-align:middle " v-if="!outFood" v-for="item in order.orderItems" id="{{item.id}}" @click="selectServicePrice(item,order,1)"><span>{{item.articleName}}</span><span v-if="!editChange || (editChange && selectedId != item.id)" class="pull-right">{{item.count}}</span><span v-if="item.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" readonly focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span></p>                                         ' +
    '						<p v-if="outFood" v-for="item in order.details"><span>{{item.name}}</span><span class="pull-right">{{item.quantity}}</span></p>                                         ' +
    '					<div  class="title"  v-if="!outFood">	<h4 ><span>加菜详情</span></h4>' +
    '					<div v-for="child in childList">' +
    '						<p  style="height:30px;vertical-align:middle " v-for="childItem in child.orderItems" id="{{childItem.id}}" @click="selectServicePrice(childItem,child,1)"><span>{{childItem.articleName}}</span><hidden id="{{childItem.id}}pId" value="{{child.id}}"/><span v-if="!editChange || (editChange && selectedId != childItem.id)" class="pull-right">{{childItem.count}}</span><span v-if="childItem.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" readonly focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span></p>                                         ' +
    '   				</div>' +
    '                   	</div> ' +
    '					</div>                                                                                                 ' +
    '					<div class="article-items" v-if="order.orderState > 1">                                                                            ' +
    '						<p style="height:30px;vertical-align:middle " v-for="service in order.serviceList" id="{{service.id}}"  @click="selectServicePrice(service,order,service.type)"><input   type="checkbox" v-if="!unPrint" style="width:40px;"/><span>{{service.name}}</span><span class="pull-right" >{{service.count}}</span></p>                                         ' +
    '						<p style="height:30px;vertical-align:middle;  "  v-if="!outFood" v-for="item in order.orderItems"  id="{{item.id}}" @click="selectServicePrice(item,order,1)">' +
    '					<label style="padding-left: 40px;" v-if="item.type==4"></label> <input v-if="item.type != 4 && !unPrint" type="checkbox"   style="width:40px;"/><span >{{item.articleName}}</span><span v-if="!editChange || (editChange && selectedId != item.id)" class="pull-right">{{item.count}}</span><span v-if="item.id == selectedId && editChange" class="pull-right"><button  style="background-color: red;width:20px;height:20px;margin-right: 5px;" @click="cutChangeNumber" ><label style="color:#ffffff">-</label></button><input style="width:25px;height:20px" readonly focus value="{{selectedNumber}}"><button @click="addChangeNumber" style="background-color: red;height:20px;width:20px;margin-left: 5px" ><label style="color: #ffffff">+</label></button></span>' +
    '					</p>' +
    '						<p v-if="outFood" v-for="item in order.details"><span>{{item.name}}</span><span class="pull-right">{{item.quantity}}</span></p>                                         ' +
    '					</div>' +
    '					<div  class="title"  v-if="((!outFood && !unPrint   ) || bossModel) && order.orderState > 1" >	<h4 ><span>加菜详情</span></h4>' +
    '					<div v-for="child in childList">' +
    '						<p style="height:30px;vertical-align:middle "  v-for="childItem in child.orderItems" id="{{childItem.id}}"   @click="selectServicePrice(childItem,child,1)">' +
    '						<label style="padding-left: 40px;" v-if="childItem.type==4"></label><input v-if="childItem.type != 4"   type="checkbox" style="width:40px;"/><span >{{childItem.articleName}}</span><span  class="pull-right">{{childItem.count}}</span>' +
    '						</p></div>                                      ' +
    '                   </div> ' +
    '				<div  class="title"  v-if="((!outFood && !unPrint   ) || bossModel) && order.orderState > 1 " >	<h4 ><span>退菜详情</span></h4>' +
    '				<div >' +
    '				<p style="height:30px;vertical-align:middle " v-for="service in order.serviceList" v-if="order.baseCustomerCount != service.count">' +
    '				<label style="padding-left: 40px;"></label><span >{{service.name}}</span><span  class="pull-right">{{order.baseCustomerCount - service.count}}</span></p>' +
    '						<p style="height:30px;vertical-align:middle " v-for="refund in order.orderItems"  v-if="refund.refundCount > 0" >' +
    '				<label style="padding-left: 40px;"></label><span >{{refund.articleName}}</span><span  class="pull-right">{{refund.refundCount}}</span>' +
    '				</p></div>                                         ' +
    '						<div v-for="child in childList"><p style="height:30px;vertical-align:middle "  v-for="childItem in child.orderItems" v-if="childItem.refundCount > 0" >' +
    '				<label style="padding-left: 40px;"></label><span >{{childItem.articleName}}</span><span  class="pull-right">{{childItem.refundCount}}</span>' +
    '				</p> </div>' +
    '                   </div> ' +
    '				</div>' +
    '				</div>' +
    '			</div>' +
    '		</div>' +

    '		<div class="col-sm-8" id="iscroll_div2" style="overflow-y: scroll;height: 100%;">' +
    '			<table class="table table-hover call-number-table" id="new-order-table">' +
    '				<thead>' +
    '					<tr>' +
    '						<th v-if="bossModel && showPayState">状态</th>' +
    '						<th v-if="outFood">订单来源</th>' +
    '						<th v-if="outFood">手机号码</th>' +
    '						<th v-if="!outFood">交易码</th>' +
    '						<th v-if="showTableNumber && !outFood">桌号</th>' +
    '						<th>菜品数</th>' +
    '						<th v-if="orderMoney">订单额</th>' +
    '						<th colspan="2">' +
    '							<input v-model="keyWord" class="form-control" id="searchNumber">' +
//	'							<img src="assets/img/clear.png" class="clearImg" alt="关闭" @click="cleanKeyWord" v-if="keyWord" style="right:20px;bottom:initial;"/>'+
    '						</th>' +
    '						<th></th>' +
    '					</tr>' +
    '				</thead>' +

    '				<tbody style="overflow-y:scroll;" > ' +
    '					<tr   v-for="o in orderlist | filterBy keyWord in \'verCode\' \'tableNumber\' \'phoneList\'"   @click="showOrderInfo(o.id)"  id={{o.id}}>' +
    '						<td v-if="o.orderMode == 6 && o.orderState > 1 && showPayState" style="color:#4BAE4F;font-size:16px;">已支付</td> ' +
    '						<td v-if="showPayState && o.orderMode == 6 && o.orderState == 1 && o.payMode != 3 && o.payMode != 4 && o.payMode != 5 && o.payMode != 6" ><span class="label label-danger">未支付</span></td>' +
    '						<td v-if="showPayState && o.orderMode == 6 && o.orderState == 1 && (o.payMode == 3 || o.payMode == 4 || o.payMode == 5 || o.payMode == 6 )"><span class="label label-warning">{{orderPayType[o.payMode]}}支付</span></td>' +
    '						<td v-if="outFood">{{platformType[o.type]}}</td>' +
    '						<td v-if="outFood">{{o.phoneList}}</td>' +
    '                       <td v-if="outFood">{{o.sum}}</td>' +
    '						<td v-if="outFood">{{o.totalPrice}}</td>' +
    '						<td v-if="!outFood">{{o.verCode}}<span v-if="o.childCount != 0">（{{o.childCount}}）</span>{{o.parentOrderId&&\'(加)\'}}</td>' +
    '						<td v-if="showTableNumber">{{o.tableNumber}}</td>' +
    '						<td ><span v-if="o.totalCount">{{o.totalCount}}</span><span v-if="!o.totalCount">{{o.articleCount}}</span></td>' +
    '						<td v-if="o.amountWithChildren != 0.0 && o.amountWithChildren != null">{{o.amountWithChildren}}</td>' +
    '						<td v-if="o.amountWithChildren == 0.0 || o.amountWithChildren == null">{{o.orderMoney}}</td>' +
    '						<td v-if="cancelBtn"><button id="{{o.id}}CancelBtn" class="btn btn-danger" @click.stop="cancelOrder(o)">拒绝</button></td>' +
    '                       <td v-if="o.type==4"><button id="{{o.id}}CancelBtn" class="btn btn-danger" @click.stop="cancelOrder(o)">拒绝</button></td>' +
    '                       <td v-if="o.type!=4"></td>' +
        // '                        暂时将所有模式下的订单都显示：打印厨打和打印总单的按钮。    lmx    begin'+
        // '						<td v-if="printBtn"><button class="btn btn-primary" @click.stop="printOrderTicket(o.id)">打印总单</button></td>' +
        // '                       	<td v-if="printKitchenBtn"><button class="btn btn-primary" @click.stop="printKitchenTicket(o.id)">打印厨打</button></td>' +
    '						<td><button class="btn btn-primary" id="{{o.id}}zongdan"    @click.stop="printOrderTicket(o.id)">总单</button></td>' +
    '                       	<td><button class="btn btn-primary" id="{{o.id}}chuda"  @click.stop="printKitchenTicket(o.id)">厨打</button></td>' +
        // '                    暂时将所有模式下的订单都显示：打印厨打和打印总单的按钮。    lmx    end'+
    '						<td v-if="bossModel && showPayState && o.payMode == 3  && o.orderState == 1"><button class="btn btn-warning" @click.stop="changePayMode(o)">现金</button></td>' +
    '						<td v-if="bossModel && showPayState && o.payMode == 4  && o.orderState == 1"><button class="btn btn-warning" @click.stop="changePayMode(o)">银联</button></td>' +
    '						<td v-if="bossModel && showPayState && (o.payMode == 3 || o.payMode == 4 || o.payMode == 6) && o.orderState == 1"><button class="btn btn-warning" @click.stop="confirmOrderPos(o)">确认</button></td>' +
    '						<td v-if="bossModel && showPayState && o.payMode == 5 && o.orderState == 1"><button class="btn btn-warning" @click="confirmOrderShanHui(o)">确认</button></td>' +
    '                   	    <td v-if="otherPayModeBtn"><button class="btn btn-primary" @click.stop="otherPayMode(o)">其他支付方式</button></td>' +
    '						<td v-if="callBtn"><button class="btn btn-info" @click.stop="callNumber(o)">叫号</button></td>' +
    '						<td v-if="o.orderMode == 6 && o.orderBefore == null && o.payType == 1 && o.orderState == 1 && (o.payMode == 0 || o.payMode == 1 || o.payMode == 2) && shop.openPosPayOrder == 1"><button class="btn btn-primary" @click="orderSettlement(o)">结算</button></td>' +
    '					</tr>' +
    '				</tbody>' +
    '			</table>' +
    '		</div>' +
    '	</div>' +
    '</div>',
    components: {
        "other-paymodel": {
            props: ["show", "ordermoney", "oid"],
            template: '<div class="modal" style="display:block" v-if="show">' +
            '	<div class="modal-dialog">' +
            '		<div class="modal-content">' +
            '			<div class="modal-header">' +
            '				<button type="button" class="close" @click="close"><span >&times;</span></button>' +
            '				<h4 class="modal-title text-center"><strong>使用其他支付方式</strong></h4>' +
            '			</div>' +
            '			<div class="modal-body">' +
            '				<table class="table table-bordered" style="font-size:18px;margin-bottom:0px;">				' +
            '					<tr v-for="model in payModels" @click="choseModel(model.id)">			' +
            '						<td>			' +
            '							<div class="radio" style="margin-left:50px;">				' +
            '								<label>			' +
            '									<input type="radio" name="payModel" value="{{model.id}}" v-model="payModel"><strong>{{model.val}}</strong>' +
            '								</label>	' +
            '							</div>		' +
            '						</td>			' +
            '					</tr>			' +
            '					<tr class="bg-info">	' +
            '						<td><div class="text-center"><strong>应收金额：{{ordermoney}}</strong></div></td>	' +
            '					</tr>	' +
            '				</table>	' +
            '			</div>' +
            '			<div class="modal-footer">' +
            '				<div class="row">' +
            '					<div class="col-md-6"><button type="button" class="btn btn-primary btn-block" @click="confirmOrder">确定</button></div>' +
            '					<div class="col-md-6"><button type="button" class="btn btn-default btn-block" @click="close">关闭</button>' +
            '				</div>' +
            '			</div>' +
            '		</div>' +
            '	</div>' +
            '</div>',
            data: function () {
                return {
                    payModels: [//支付类型集合
                        {"id": "1", "val": "其他支付"},
//        			             {"id":"1","val":"现金支付"},
//        			             {"id":"2","val":"支付宝"},
//        			             {"id":"3","val":"银行卡"},
                    ],
                    payModel: "1",//当前支付类型
                }
            },
            methods: {
                choseModel: function (model) {
                    this.payModel = model;
                },
                confirmOrder: function () {
                    this.$dispatch("confirm-order-event", this.payModel, this.oid);
                    this.close();
                },
                close: function () {
                    this.show = false;
                }
            }
        },
        "refund-turntablemodel": {
            props: ["show", "order", "tangshi","shop"],
            template: '<div class="modal" style="display:block;overflow-y:scroll;" v-if="show">' +
            '	<div class="modal-dialog" >' +
            '		<div class="modal-content">' +
            '			<div class="modal-header">' +
            '				<button type="button" class="close" @click="close"><span >&times;</span></button>' +
            '				<h3 class="modal-title text-center"><strong>更换桌号</strong></h3>' +
            '			</div>' +
            '			<div class="modal-body">' +
            '               <p style="margin-left: 130px;font-size:x-large;"><b>当前桌号：</b><span style="color: red;">{{order.tableNumber}} </span></p>'+
            '               <br/>' +
            '               <p style="margin-left: 130px;font-size:x-large;"><b>更换桌号：</b>' +
            '                   <input type="hidden" id="order_id" name="order_id" value="{{order.id}}">' +
            '                   <input type="hidden" id="serialNumber" name="serialNumber" value="{{order.serialNumber}}">' +
            '                   <input type="hidden" id="createTime" name="createTime" value="{{order.createTime}}">' +
            '                   <input type="hidden" id="shopDetailId" name="shopDetailId" value="{{order.shopDetailId}}">' +
            '                   <input type="hidden" id="brandId" name="brandId" value="{{order.brandId}}">' +
            '                   <input type="hidden" id="oldtableNumber" name="oldtableNumber" value="{{order.tableNumber}}">' +
            '                   <input type="text" required="true" id="tableNumber" name="tableNumber" style="width:230px;border-radius:5px;" placeholder="请输入更换后的桌号"/>' +
            '               </p>'+
            '			</div>' +
            '			<div class="modal-footer">' +
            '				<div class="row">' +
            '					<div class="col-md-6"><button type="button" style="width: 80px;margin-left: 150px;" class="btn btn-default btn-block" @click="close">取消</button>' +
            '					<div class="col-md-6"><button type="button" style="margin-left: 300px;position: fixed;width: 80px;margin-top: -34px;" class="btn btn-primary btn-block" @click="refundOrder">确定</button></div>' +
            '				</div>' +
            '			</div>' +
            '		</div>' +
            '	</div>' +
            '</div>',
            data: function () {
                return {
                    isMealFee: false,
                    click: false,
                    tableNumber: null,
                }
            },
            methods: {

                choseModel: function (model) {
                    this.payModel = model;
                },
                close: function () {
                    this.show = false;
                },
                refundOrder: function () {
                    var that = this;
                    if (that.click) {
                        return;
                    }
                    that.click = true;
                    if ($('#tableNumber').val() != '') {
                        if($('#tableNumber').val()==$('#oldtableNumber').val()){
                            toastr.error("桌号相同");
                        }else
                            updateTurnTable($('#tableNumber').val(),$('#order_id').val(), function (result) {
                                if (result.success) {
                                    that.show = false;
                                    toastr.success("更换桌号成功！");
                                    that.click = false;
                                    //换桌打印
                                    //debugger
                                    turntable($('#order_id').val(),$('#brandId').val(),$('#tableNumber').val(),$('#serialNumber').val(),$('#createTime').val(),$('#shopDetailId').val(),$('#oldtableNumber').val(), function (result) {
                                        if (result.success){
                                            if (result.data.length > 0) {
                                                toastr.success("换桌成功，正在打印换桌信息");
                                                // if(that.shop.isPosNew == 1){
                                                //
                                                //     for (var i = 0; i < result.data.length; i++) {
                                                //         debugger;
                                                //         printPlusNew(result.data[i]);
                                                //     }
                                                // }else{
                                                    printPlus(result.data,function () {
                                                        setTimeout(function(){
                                                            location.reload();
                                                        },1000)
                                                    }, function (msg) {
                                                        toastr.clear();
                                                        toastr.error(msg);
                                                    });
                                                // }

                                            }else{
                                                toastr.error("换桌成功，但无打印信息请检查换桌设置");
                                                setTimeout(function(){
                                                    location.reload();
                                                },1000)
                                            }
                                        }else{
                                            toastr.error("换桌打印失败");
                                            setTimeout(function(){
                                                location.reload();
                                            },1000)
                                        }
                                    });
                                } else {
                                    toastr.error("更换桌号失败！");
                                    that.click = false;
                                }
                            });
                    }
                    else {
                        that.click = false;
                        toastr.error("请输入更换桌号！");
                    }
                }
            },
            created: function () {
                this.$watch("show", function () {
                    $("#tableNumber").addClass("returnKey");
                    $(".returnKey").ioskeyboard({
                        keyboardRadix: 60,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                        keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
                        keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                        clickeve: true,//是否绑定元素click事件
                        colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                        colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                        colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
                    });

                    var Uarry = $("#keyboard_5xbogf8c li");//获取所有的li元素
                    var nowNumber = "";
                    $("#keyboard_5xbogf8c li").bind("click", function () {	//点击事件
                        var count = $(this).index();	//获取li的下标
                        var result = Uarry.eq(count).text();
                        if ("←" == result) {
                            var count = nowNumber;
                            if (count.length > 0) {
                                count = count.substring(0, count.length - 1);
                                nowNumber = count;
                            }
                        } else {
                            if (nowNumber == 0) {
                                nowNumber = result;
                            } else {
                                nowNumber += result;
                            }
                        }
                        that.returnPassword = nowNumber;
                    })
                    $("#tableNumber").blur(function () {
                        $("#keyboard_5xbogf8c").css({"display": "none"});
                    });
                });
            },
        },
        "refund-articlemodel": {
            props: ["show", "list", "order", "tangshi"],
            template: '<div class="modal" style="display:block;overflow-y:scroll;" v-if="show">' +
            '	<div class="modal-dialog" >' +
            '		<div class="modal-content">' +
            '			<div class="modal-header">' +
            '				<button type="button" class="close" @click="close"><span >&times;</span></button>' +
            '				<h4 class="modal-title text-center"><strong>确认退菜</strong></h4>' +
            '			</div>' +
            '			<div class="modal-body">' +
            '				<table class="table table-bordered" style="font-size:18px;margin-bottom:0px;">				' +
            '<tr><th>菜品名称</th><th>单价</th><th>退菜数量</th></tr>' +
            '					<tr  v-for="refund in refundList" >			' +
            '						<td>{{refund.name}}		</td>			' +
            '						<td>{{refund.unitPrice.toFixed(2)}}					</td>			' +
            '						<td><button style="width:40px;color: #ffffff;background-color: #ff0000;" @click="minusCount(refund)"  >-</button> <input readonly style=" text-align: center; width:40px;margin-right: 5px;margin-left: 3px" value="{{refund.count}}" /><button style="width:40px;color: #ffffff;background-color: #ff0000;"  @click="addCount(refund)">+</button>			</td>			' +
            '					</tr>			' +
            '<tr><td colspan="3">退菜总额:￥{{refundMoney}}</td></tr>' +
            '<tr><td colspan="3" v-if="isMealFee">{{mealFeeName}}:￥{{extraPrice}}</td></tr>' +
            '<tr><td>退菜原因:</td><td><select name="AppStakeholder" class="form-control AppStakeholder" @change="setRefundRemark(this)"><option v-for="refundRemark in refundRemarkList" :value="refundRemark" >{{refundRemark.name}}</option></select></td><td><input type="text" required="true" id="" v-model="remarkSupply" /></td></tr>' +
            '<tr><td>退菜口令:</td><td colspan="2"><input type="password" required="true" id="pwd" v-model="returnPassword" /></td></tr>' +
            '<tr v-if="refundType == 1"><td>退款方式:</td><td><label><input name="refundType" type="radio" value="1" checked/>线下退现金</label></td></tr>' +
            '<tr v-if="refundType == null"><td>退款方式:</td><td><label><input name="refundType" type="radio" value="0"/>退还余额</label><label><input name="refundType" type="radio" value="1"/>线下退现金 </label> </td></tr>' +
            '				</table>	' +
                //'<span style="color: red; font-size: 20px;" v-if="order.payMode == 3 || order.payMode == 4">应退还现金：￥{{refundMoney}}</span>' +
            '			</div>' +
            '			<div class="modal-footer">' +
            '               <span style="color:red;text-align: center;display: block;" v-if="order.orderState == 11">* 此订单已领取评论红包</span>' +
            '				<div class="row">' +
            '					<div class="col-md-6"><button type="button" class="btn btn-primary btn-block" @click="refundOrder">确定</button></div>' +
            '					<div class="col-md-6"><button type="button" class="btn btn-default btn-block" @click="close">关闭</button>' +
            '				</div>' +
            '			</div>' +
            '		</div>' +
            '	</div>' +
            '</div>',
            data: function () {
                return {
                    refundList: [],
                    refundMoney: 0.00,
                    isMealFee: false,
                    mealFeeName: null,
                    extraPrice: 0.00,
                    mealFeePrice: 0.00,
                    click: false,
                    returnPassword: null,
                    refundRemarkList: [],
                    remarkSupply:null,
                    refundRemark:{},
                    refundRemarkMap: new HashMap(),
                    refundType: 0,
                }
            },
            methods: {
                setRefundRemark:function(){
                    var vs =  $(".AppStakeholder").find("option:selected").text();
                    this.refundRemark = this.refundRemarkMap.get(vs);
                },
                choseModel: function (model) {
                    this.payModel = model;
                },
                confirmOrder: function () {
                    this.$dispatch("confirm-order-event", this.payModel, this.order.id);
                    this.close();
                },
                close: function () {
                    this.show = false;
                    this.refundList = [];
                },
                minusCount: function (refund) {
                    if (refund.count > 1) {
                        refund.count--;
                        this.refundMoney = parseFloat(this.refundMoney) - parseFloat(refund.unitPrice);

                        if (this.isMealFee) {
                            this.extraPrice = parseFloat(this.extraPrice) - parseFloat(refund.mealFeeNumber) * parseFloat(this.mealFeePrice);
                            this.extraPrice = this.extraPrice.toFixed(2);
                            this.refundMoney = parseFloat(this.refundMoney) - parseFloat(refund.mealFeeNumber) * parseFloat(this.mealFeePrice);
                        }
                        this.refundMoney = this.refundMoney.toFixed(2);
                    }
                },
                addCount: function (refund) {
                    if (refund.maxCount > refund.count) {
                        refund.count++;
                        this.refundMoney = parseFloat(this.refundMoney) + parseFloat(refund.unitPrice);

                        if (this.isMealFee) {
                            this.extraPrice = parseFloat(this.extraPrice) + parseFloat(refund.mealFeeNumber) * parseFloat(this.mealFeePrice);
                            this.extraPrice = this.extraPrice.toFixed(2);
                            this.refundMoney = parseFloat(this.refundMoney) + parseFloat(refund.mealFeeNumber) * parseFloat(this.mealFeePrice);
                        }
                        this.refundMoney = this.refundMoney.toFixed(2);
                    }

                },
                refundOrder: function () {
                    var that = this;
                    if(that.refundType == null && ($("input[name='refundType']:checked").val() == 0 || $("input[name='refundType']:checked").val() == 1)){
                        that.refundType = $("input[name='refundType']:checked").val();
                    }else if(that.refundType == null){
                        toastr.error("请选择退款方式！");
                        return;
                    }
                    if (that.click) {
                        return;
                    }
                    if (that.refundMoney <= 0) {
                        toastr.error("退菜金额小于0！");
                        return;
                    }
                    that.click = true;

                    if ($('#pwd').val() != '') {
                        getBrandUser($('#pwd').val(), function (result) {
                            if (result.success) {
                                var orderItem = [];
                                var orderId = that.refundList[0].orderId;

                                for (var i = 0; i < that.refundList.length; i++) {
                                    var num = that.refundList[i].mealFeeNumber == null ? 0 : that.refundList[i].mealFeeNumber;
                                    var extarPrice = 0;
                                    if (that.isMealFee) {
                                        extarPrice = parseFloat(that.mealFeePrice) * that.refundList[i].count * num;
                                    }
                                    var item = {
                                        id: that.refundList[i].id,
                                        type: that.refundList[i].type,
                                        count: that.refundList[i].count,
                                        unitPrice: that.refundList[i].unitPrice,
                                        orderId: that.refundList[i].orderId,
                                        extraPrice: extarPrice
                                    };
                                    orderItem.push(item);
                                }

                                var order = {
                                    id: that.order.id,
                                    refundMoney: that.refundMoney,
                                    orderItems: orderItem,
                                    refundRemark: that.refundRemark,
                                    remarkSupply: that.remarkSupply,
                                    refundType: that.refundType
                                };
                                refundOrder(order, function (result) {
                                    var task = result.data;
                                    if (result.success) {
                                        if (that.order.id != null) {
                                            that.$dispatch("fresh-finish-order-event", that.order.id);
                                        } else {
                                            that.$dispatch("fresh-finish-order-event", orderId);
                                        }

                                        that.show = false;
                                        toastr.success("退菜成功！");
                                        that.click = false;

                                        if (task != null && task.length != 0) {
                                            printPlus(task, function () {
                                                toastr.success("打印成功");
                                            }, function (msg) {
                                                toastr.error(msg);
                                            });
                                        }
                                        updateReceiptOrderNumber(that.order.serialNumber, function (result) {
                                            if (result.success) {
                                                console.log("更新发票信息成功")
                                            }
                                        })
                                    } else {
                                        toastr.error(result.message);
                                        that.click = false;
                                    }
                                });
                            } else {
                                toastr.error("退菜口令有误！");
                                that.click = false;
                            }
                        });
                    }
                    else {
                        that.click = false;
                        toastr.error("请输入退菜口令！");
                    }
                }
            },
            created: function () {
                var that = this;
                checkMealFee(function (result) {
                    if (result.success) {
                        that.isMealFee = true;
                        that.mealFeeName = result.data.mealFeeName;
                        that.mealFeePrice = result.data.mealFeePrice;
                    } else {
                        that.isMealFee = false;
                    }
                });
                that.remarkSupply = null;
                getRefundRemarkList(function (res){
                    if (res.success) {
                        that.isMealFee = true;
                        that.refundRemarkList = res.data;
                        that.refundRemark = that.refundRemarkList[0];
                        for(var i = 0; i < res.data.length; i++){
                            that.refundRemarkMap.put(res.data[i].name, res.data[i]);
                        }
                    }
                });
                this.$watch("show", function () {
                    var that = this;
                    //判断订单退款方式   0 线上退 （包含余额）  1 线下退     null 自定义
                    if(this.order.customerId == 0 || (this.order.isPosPay == 1 && (this.order.payMode == 1 || this.order.payMode == 2))){
                        this.refundType = 1;
                    }else if(this.order.payMode == 3 || this.order.payMode == 4){
                        this.refundType = null;
                    }else{
                        this.refundType = 0;
                    }
                    this.refundList = this.list;
                    this.returnPassword = null;     //POS端退菜功能，退菜后会不记录退菜口令
                    var sum = 0;
                    this.refundMoney = 0.00;
                    if (this.isMealFee && !this.tangshi) {
                        this.isMealFee = true;
                    } else {
                        this.isMealFee = false;
                    }
                    for (var i = 0; i < this.refundList.length; i++) {
                        var num = this.refundList[i].mealFeeNumber == null ? 0 : this.refundList[i].mealFeeNumber;
                        sum += parseFloat(this.mealFeePrice) * this.refundList[i].count * num;
                        this.refundMoney = parseFloat(this.refundList[i].unitPrice * this.refundList[i].count) + parseFloat(this.refundMoney);
                    }
                    this.extraPrice = sum.toFixed(2);
                    if (this.isMealFee) {
                        this.refundMoney = parseFloat(this.refundMoney.toFixed(2)) + parseFloat(sum);
                    }
                    this.refundMoney = this.refundMoney.toFixed(2);

                    $("#pwd").addClass("returnKey");
                    $(".returnKey").ioskeyboard({
                        keyboardRadix: 60,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                        keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
                        keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                        clickeve: true,//是否绑定元素click事件
                        colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                        colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                        colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
                    });

                    var Uarry = $("#keyboard_5xbogf8c li");//获取所有的li元素
                    var nowNumber = "";
                    $("#keyboard_5xbogf8c li").bind("click", function () {	//点击事件
                        var count = $(this).index();	//获取li的下标
                        var result = Uarry.eq(count).text();
                        if ("←" == result) {
                            var count = nowNumber;
                            if (count.length > 0) {
                                count = count.substring(0, count.length - 1);
                                nowNumber = count;
                            }
                        } else {
                            if (nowNumber == 0) {
                                nowNumber = result;
                            } else {
                                nowNumber += result;
                            }
                        }
                        that.returnPassword = nowNumber;
                    })
                    $("#pwd").blur(function () {
                        $("#keyboard_5xbogf8c").css({"display": "none"});
                    });
                });
            },
        },
        "shanhui-dialog": {
            props: ["show", "order"],
            template: '<div class="modal" style="display:block" v-if="show">' +
            '  <div class="modal-dialog">' +
            '    <div class="modal-content">' +
            '      <div class="modal-header">' +
            '        <h3 class="modal-title text-center"><strong>大众点评订单确认</strong></h3>' +
            '      </div>' +
            '      <div class="modal-body">' +
            '       <form class="form-horizontal" @submit.prevent="checkShanHuiOrder">' +
            '           <div class="form-group">' +
            '               <label class="col-sm-3 control-label">大众点评订单号</label>' +
            '               <div class="col-sm-8 input-group">' +
            '                   <input type="text" class="form-control numkeyboard" required v-model.trim="shanhuiOrderId" placeholder="请输入大众点评订单号"/>' +
            '                   <div class="input-group-addon" @click="checkShanHuiOrder()">校验</div>' +
            '               </div>' +
            '           </div>' +
            '       </form>' +
            '       <table class="table table-bordered" v-if="paymentInfo && paymentInfo.data">' +
            '           <tr v-if="paymentInfo.data.vendorOrderId">' +
            '               <td colspan="4" class="text-danger text-center">该大众点评订单已被消费，请确认后重新录入</td>' +
            '           </tr>' +
            '           <tr v-if="!paymentInfo.data.vendorOrderId && !paymentInfo.checkVal">' +
            '               <td colspan="4" class="text-danger text-center">订单应收金额和闪惠支付金额不符，请核对后进行操作~</td>' +
            '           </tr>' +
            '           <tr>' +
            '               <td>消费总额</td><td>￥{{paymentInfo.data.originalAmount}}</td>' +
            '               <td>不可优惠价格</td><td>￥{{paymentInfo.data.vendorNoDiscountAmount}}</td>' +
            '           </tr>' +
            '           <tr>' +
            '               <td>折扣金额</td><td>￥{{paymentInfo.data.vendorDiscountAmount}}</td>' +
            '               <td>手机号</td><td>{{paymentInfo.data.phoneNo}}</td>' +
            '           </tr>' +
            '           <tr>' +
            '               <td>闪惠实付</td><td>￥{{paymentInfo.data.userPayAmount}}</td>' +
            '               <td>订单状态</td><th :class="{\'text-danger\':paymentInfo.data.status!=10}">{{getOrderState()}}</th>' +
            '           </tr>' +
            '           <tr :class="{success:paymentInfo.checkVal,danger:!paymentInfo.checkVal}">' +
            '               <th colspan="4">应收总额：￥{{paymentInfo.payValue}}</th>' +
            '           </tr>' +
            '       </table>' +
            '       <p class="text-danger text-center" v-else>{{paymentInfo.error.message}}</p>' +
            '      </div>' +
            '      <div class="modal-footer">' +
            '           <p class="text-info" style="float: left;">请核对好金额后再点击确认收款~</p>' +
            '           <button type="button" class="btn btn-default"  @click="close">关闭</button>' +
            '           <button type="button" class="btn btn-primary" v-if="paymentInfo && paymentInfo.data && !paymentInfo.data.vendorOrderId && paymentInfo.data.status==10" @click="saveShanHuiOrder">确认支付</button>' +
            '      </div>' +
            '    </div>' +
            '  </div>' +
            '</div>',
            data: function () {
                return {
                    shanhuiOrderId: null,
                    paymentInfo: null,
                }
            },
            watch: {
                show: function (newValue, oldValue) {
                    if (newValue) {
                        this.paymentInfo = null;
                        this.shanhuiOrderId = null;
                        //绑定 js 键盘
                        $(".numkeyboard").ioskeyboard({
                            keyboardRadix: 80,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                            keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
                            keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                            clickeve: true,//是否绑定元素click事件
                            colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                            colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                            colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
                        });
                        $("input").blur(function () {//隐藏 js键盘
                            $("#keyboard_5xbogf8c").css({"display": "none"});
                        });
                    }
                }
            },
            methods: {
                checkShanHuiOrder: function () {
                    var that = this;
                    this.paymentInfo = null;
                    checkShanHuiOrder(this.order.id, this.shanhuiOrderId, function (result) {
                        that.paymentInfo = result;
                    });
                },
                getOrderState: function () {
                    var status = this.paymentInfo.data.status;
                    if (status == 10) {
                        return "已支付";
                    } else if (status == 20) {
                        return "未支付";
                    } else if (status == 30) {
                        return "已退款";
                    }
                },
                saveShanHuiOrder: function () {
                    var that = this;
                    if (this.paymentInfo && this.paymentInfo.data) {
                        saveShanhuiOrder(this.order.id, this.paymentInfo.data.serialNumber, function (res) {
                            if (res.success) {
                                confirmOrderPos(that.order.id, function (result) {
                                    if (result.success) {
                                        toastr.success("确认订单成功");
                                        that.show = false;
                                        that.$dispatch("remove-order", that.order);
                                    }
                                });
                            } else {
                                that.paymentInfo = null;
                                toastr.error(res.message || "网络异常，请稍候再试~");
                            }
                        });
                    } else {
                        toastr.error("请输入有效的大众点评订单号~");
                    }
                },
                close: function () {
                    this.show = false;
                }
            }
        },
        "order-settlement": {
            props: ["show", "order", "brandsetting", "shop", "orderlist", "oldorder", "payvalue", "remainvalue", "couponvalue", "usecouponvalue", "couponid"],
            template: '<div class="modal" style="display:block" v-if="show">' +
            '<div class="weui_dialog_alert">' +
            '<div class="weui_mask"></div>' +
            '<div class="weui_dialog_pay" style="font-size: 20px;">' +
            '<div class="full-height">' +
            '<div class="pay_header">' +
            '<div class="order_middle"><h2>结算订单</h2></div>' +
            '<img src="assets/img/close.png" class="closeImg" alt="关闭" @click="close"/>' +
            '</div>' +
            '<div class="pay_body">' +
            '<div class="pay_content_left">' +
            '<div style="text-align: left;position: relative;margin-top: 10px;">' +
            '<div style="font-size: 24px;font-weight: bold;">订单金额:￥{{order.orderMoney}}</div>' +
            '<div>桌号:{{order.tableNumber == null ? "0" : order.tableNumber}}</div>' +
            '<div>交易码:{{order.verCode}}</div>' +
            '<div>就餐人数:{{order.customerCount == null ? "-" : order.customerCount}}</div>' +
            '<div>下单时间:{{new Date(order.createTime).format("yyyy-MM-dd hh:mm")}}</div>' +
            '<div>扣除余额:<font color="red">-￥{{remainvalue}}</font><span v-if="subscribe==0" style="margin-left:20px; color:red">(未关注公众号)</span></div>' +
            '<div>扣除优惠券:<font color="red">-￥{{couponvalue}}</font></div>' +
            '<p style="font-size:20px;font-weight:bold;">支付方式:</p>' +
            '</div>' +
            '<div class="flex-container-pay">' +
            '<div class="payMentBtn" :class="{active:payMode == 1 || defaultWeChat}" @click="selectPayMode(1)" v-if="shop.openPosWeChatPay==1">' +
            '<img class="payImgIcon" src="assets/img/wxpay.png" alt="微信支付" />' +
            '<span>微信支付</span>' +
            '</div>' +
            '<div class="payMentBtn" :class="{active:payMode == 2 || defaultAliPay}" @click="selectPayMode(2)" v-if="brandsetting.aliPay==1 && shop.openPosAliPay==1 && order.distributionModeId != 3">' +
            '<img class="payImgIcon" src="assets/img/ali.png" alt="支付宝支付" />' +
            '<span>支付宝</span>' +
            '</div>' +
            '<div class="payMentBtn" :class="{active:payMode == 3 || defaultUnionPay}" @click="selectPayMode(3)" v-if="brandsetting.openUnionPay==1 && shop.openPosUnionPay==1">' +
            '<img class="payImgIcon" src="assets/img/bankPay.png" alt="银联支付" />' +
            '<span>银联支付</span>' +
            '</div>' +
            '<div class="payMentBtn" :class="{active:payMode == 4 || defaultMoneyPay}" @click="selectPayMode(4)" v-if="brandsetting.openMoneyPay==1 && shop.openPosMoneyPay==1">' +
            '<img class="payImgIcon" src="assets/img/cash.png" alt="现金支付" />' +
            '<span>现金支付</span>' +
            '</div >' +
            '<div class="payMentBtn" :class="{active:payMode == 5 || defaultShanhuiPay}" @click="selectPayMode(5)" v-if="brandsetting.openShanhuiPay==1 && shop.openPosShanhuiPay==1">' +
            '<img class="payImgIcon" src="assets/img/people.png" alt="大众点评" />' +
            '<span>大众点评</span>' +
            '</div>' +
            '<div class="payMentBtn" :class="{active:payMode == 6 || defaultIntegralPay}" @click="selectPayMode(6)" v-if="brandsetting.integralPay==1 && shop.openPosIntegralPay==1">' +
            '<img class="payImgIcon" src="assets/img/corn.png" alt="会员支付" />' +
            '<span>会员支付</span>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="pay_content_right">' +
            '<div class="pay_input">' +
            '<span>应付:&nbsp;&nbsp;￥{{payvalue}}</span>' +
            '</div>' +
            '<div class="pay_input">' +
            '<span>已收:</span>' +
            '<input type="text" class="form-control payText" disabled="disabled" v-model="money" ">' +
            '</div>' +
            '<div class="pay_input">' +
            '<span>找零:</span>' +
            '<input type="text" class="form-control payText" disabled="disabled" :class="{activeRed:muchMoney < 0}" v-model="muchMoney.toFixed(2)">' +
            '</div>' +
            '<div class="keys">' +
            '<span @click="getValueAdd(10)">10</span>' +
            '<span @click="getValue(1)">1</span>' +
            '<span @click="getValue(2)">2</span>' +
            '<span @click="getValue(3)">3</span>' +
            '<span @click="getValueAdd(50)">50</span>' +
            '<span @click="getValue(4)">4</span>' +
            '<span @click="getValue(5)">5</span>' +
            '<span @click="getValue(6)">6</span>' +
            '<span @click="getValueAdd(100)">100</span>' +
            '<span @click="getValue(7)">7</span>' +
            '<span @click="getValue(8)">8</span>' +
            '<span @click="getValue(9)">9</span>' +
            '<span @click="getValueAdd(500)">500</span>' +
            '<span @click="getValue(00)">00</span>' +
            '<span @click="getValue(0)">0</span>' +
            '<span @click="getValue(\'.\')">.</span>' +
            '</div>' +
            '<div class="keysText">' +
            '<span @click="back">←</span>' +
            '<span class="clear" @click="clearMoney">清空</span>' +
            '<span class="bill" @click="payOrder">结账</span>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>',
            data: function () {
                return {
                    money: "",
                    muchMoney: 0,
                    defaultWeChat: false,
                    defaultAliPay: false,
                    defaultUnionPay: false,
                    defaultMoneyPay: false,
                    defaultShanhuiPay: false,
                    defaultIntegralPay: false,
                    payMode: 0,
                    disabled: false,
                    isSetMoney: true,
                    subscribe: 1
                }
            },
            methods: {
                back: function () {
                    if (!this.isSetMoney) {
                        if (this.money == "") {
                            return;
                        }
                        if (this.money.length == 1) {
                            this.money = "";
                            this.muchMoney = 0;
                            return;
                        }
                        this.money = this.money.substr(0, this.money.length - 1);
                        var mm = parseFloat(this.money) - parseFloat(this.payvalue);
                        this.muchMoney = mm;
                    }
                },
                clearMoney: function () {
                    if (!this.isSetMoney) {
                        this.money = "";
                        this.muchMoney = 0;
                    }
                },
                getValueAdd: function (m) {
                    if (!this.isSetMoney) {
                        if (this.money == "") {
                            this.money = parseFloat(m) + "";
                        } else {
                            this.money = parseFloat(this.money) + parseFloat(m) + "";
                        }

                        var mm = parseFloat(this.money) - parseFloat(this.payvalue);
                        this.muchMoney = mm;
                    }
                },
                getValue: function (m) {
                    if (!this.isSetMoney) {
                        //不能出现2个.
                        if ("." == m && (this.money + "").indexOf(".") > -1) {
                            return false;
                        }
                        //小数点.之前要有数字
                        if ("." == m && (this.money + "").length == 0) {
                            return false;
                        }
                        //如果存在小数点.  小数点末尾不能超过2位
                        if ((this.money + "").indexOf(".") > -1 && ((this.money + "").length - (this.money + "").indexOf(".")) > 2) {
                            return false;
                        }
                        m = m + "";
                        this.money = this.money + "";
                        this.money = this.money + m;
                        var mm = parseFloat(this.money) - parseFloat(this.payvalue);
                        this.muchMoney = mm;
                    }
                },
                payOrder: function () {
                    var that = this;
                    if (that.payMode == 0) {
                        toastr.clear();
                        toastr.error("请选择支付方式");
                        return;
                    }
                    if (that.money == "") {
                        toastr.clear();
                        toastr.error("请输入已收金额");
                        return;
                    }
                    if (that.money < that.payvalue) {
                        toastr.clear();
                        toastr.error("已收金额小于应付金额");
                        return;
                    }
                    if (!that.disabled) {
                        payOrder({
                            "orderId": that.order.id,
                            "payMode": that.payMode,
                            "couponId": that.couponid,
                            "payValue": that.money,
                            "giveChange": that.muchMoney,
                            "remainValue": that.remainvalue,
                            "couponValue": that.couponvalue
                        }, function (result) {
                            if (result.success) {
                                toastr.clear();
                                toastr.success("订单结算成功");
                                that.orderlist.$remove(that.oldorder);
                                that.$dispatch("clean-show-order-info");
                            } else {
                                that.payMode = 0;
                                toastr.clear();
                                if (result.message != null) {
                                    toastr.error(result.message);
                                } else {
                                    toastr.error("订单结算失败");
                                }
                            }
                            that.disabled = false;
                            that.show = false;
                        });
                    }
                    that.disabled = true;
                },
                close: function () {
                    this.show = false;
                    this.payMode = 0;
                },
                selectPayMode: function (payMode) {
                    this.payMode = payMode;
                    if (payMode == 4) {
                        this.isSetMoney = false;
                    } else {
                        this.money = this.payvalue + "";
                        this.muchMoney = 0;
                        this.isSetMoney = true;
                    }
                    this.defaultWeChat = false;
                    this.defaultAliPay = false;
                    this.defaultUnionPay = false;
                    this.defaultMoneyPay = false;
                    this.defaultShanhuiPay = false;
                    this.defaultIntegralPay = false;
                },
                getDefaultPayMode: function () {
                    var brandSetting = this.brandsetting;
                    var shop = this.shop;
                    if (brandSetting.openMoneyPay == 1 && shop.openPosMoneyPay == 1) {
                        this.defaultMoneyPay = true;
                        this.payMode = 4;
                        this.isSetMoney = false;
                    } else if (brandSetting.openUnionPay == 1 && shop.openPosUnionPay == 1) {
                        this.defaultUnionPay = true;
                        this.payMode = 3;
                    } else if (this.defaultMoneyPay == false && this.defaultUnionPay == false) {
                        this.defaultWeChat = true;
                        this.payMode = 1;
                    } else if (brandSetting.aliPay == 1 && shop.openPosAliPay == 1) {
                        this.defaultAliPay = true;
                        this.payMode = 2;
                    } else if (brandSetting.openShanhuiPay == 1 && shop.openPosShanhuiPay == 1) {
                        this.defaultShanhuiPay = true;
                        this.payMode = 5;
                    } else if (brandSetting.integralPay == 1 && shop.openPosIntegralPay == 1) {
                        this.defaultIntegralPay = true;
                        this.payMode = 6;
                    } else {
                        this.isSetMoney = false;
                    }
                }
            },
            created: function () {
                var that = this;
                this.$watch("show", function () {
                    if (that.show) {
                        checkCustomerSubscribe(that.order.customerId, function (result) {
                            if(result.success){
                                if(result.data == 1){
                                    if(that.usecouponvalue > 0){
                                        that.remainvalue += that.couponvalue;
                                        that.couponvalue = 0;
                                        that.couponid = "";

                                    }
                                    that.subscribe = 1;
                                    that.money = that.payvalue + "";
                                    that.muchMoney = 0;
                                    that.getDefaultPayMode();
                                }else if(result.data == 0){
                                    that.payvalue += that.remainvalue;
                                    that.remainvalue = 0;
                                    if(that.usecouponvalue > 0){
                                        that.remainvalue += that.couponvalue;
                                        that.couponvalue = 0;
                                        that.couponid = "";
                                    }
                                    that.subscribe = 0;
                                    that.money = that.payvalue + "";
                                    that.muchMoney = 0;
                                    that.getDefaultPayMode();
                                }
                            }else{
                                toastr.error(result.message);
                                that.show = false;
                            }
                        });
                    }
                });
            }
        },
        "pos-discount-dialog":{
            props: ["show", "order"],
            template:'<div class="weui_dialog_alert" style="display:block" v-if="show">' +
            '<div class="weui_mask"></div>' +
            '<div class="weui_dialog_pay" style="font-size: 20px;width: 65%;">' +
            '<div class="full-height">' +
            '<div class="pay_header" style="font-size: 30px;height: 13%;">' +
            '<div class="order_middle"><span>折扣</span></div>' +
            '<img src="assets/img/close.png" class="closeImg" alt="关闭" @click="close"/>' +
            '</div>' +
            '<div class="pay_body" style="height: 64%;margin: 0 4%;">' +
            '<div class="pay_content_left" >' +

            '<div class="pay_input" style="font-weight: initial;text-align: left;margin-top:20px;">' +
            '<span>打折:</span>' +
            '<input type="test" class="form-control payText" v-model="discount" placeholder="100%" @click="choiceInputValue(2)" style="font-size:24px;width:40%;border: 1px solid #bbb;float:right;">' +
            '</div>' +
            '<div class="pay_input" style="font-weight: initial;text-align: left;">' +
            '<span>折扣金额:</span>' +
            '<input type="test" class="form-control payText" v-model="eraseMoney" placeholder="0元" @click="choiceInputValue(1)" style="font-size:24px;width:40%;border: 1px solid #bbb;float:right;">' +
            '</div>' +
            '<div class="pay_input" style="font-weight: initial;text-align: left;">' +
            '<span >不参与打折金额:</span>' +
            '<input type="test" class="form-control payText" v-model="noDiscountMoney" placeholder="0元" @click="choiceInputValue(3)" style="font-size:24px;width:40%;border: 1px solid #bbb;float:right;margin:initial;">' +
            '</div>' +
            '<div style="text-align:left;margin-top:20px;">' +
            '<span >订单总额:</span>' +
            '<span style="position:absolute;right:0px;"><i style="font-size:28px;color:#04be01;">{{orderMoney}}</i>元</span>' +
            '</div>' +
            '<div>' +
            '<span class="payDiscountInfor">优惠金额:</span>' +
            '<span class="payDiscountInfor" style="text-align: right;"><i style="font-size:28px;color:#04be01;">{{(orderMoney - discountMoney).toFixed(2)}}</i>元</span>' +
            '</div>' +
            '<div>' +
            '<span class="payDiscountInfor">应付金额:</span>' +
            '<span class="payDiscountInfor" style="text-align: right;"><i style="font-size:28px;color:#cc0000;">{{discountMoney.toFixed(2)}}</i>元</span>' +
            '</div>' +
//          '<div class="flex-container-discount">'+
//	            '<button class="btn btn-default" @click="getValueSet(90)">9折</button>' +
//	            '<button class="btn btn-default" @click="getValueSet(88)">88折</button>' +
//	            '<button class="btn btn-default" @click="getValueSet(80)">8折</button>' +
//          '</div>' +
//          '<div class="flex-container-discount">'+
//	            '<button class="btn btn-default" @click="getValueSet(70)">7折</button>' +
//	            '<button class="btn btn-default" @click="getValueSet(60)">6折</button>' +
//	            '<button class="btn btn-default" @click="getValueSet(50)">5折</button>' +
//          '</div>' +

            '</div>' +
            '<div class="pay_content_right">' +
//          '<div class="pay_input" style="font-weight: initial;text-align: right;">' +
//          '<span>折扣率:</span>' +
//          '<input type="test" class="form-control payText" v-model="discount" placeholder="100%" style="font-size:24px;width:50%;border: 1px solid #bbb;">' +
//          '</div>' +
            '<div class="keyNum">' +
            '<span @click="getValue(1)">1</span>' +
            '<span @click="getValue(2)">2</span>' +
            '<span @click="getValue(3)">3</span>' +
            '<span @click="getValue(4)">4</span>' +
            '<span @click="getValue(5)">5</span>' +
            '<span @click="getValue(6)">6</span>' +
            '<span @click="getValue(7)">7</span>' +
            '<span @click="getValue(8)">8</span>' +
            '<span @click="getValue(9)">9</span>' +
            '<span @click="getValue(\'.\')">.</span>' +
            '<span @click="getValue(0)">0</span>' +
            '<span @click="back">←</span>' +
            '</div></div></div>' +
            '<p style="color:#a8a8a8;font-size:16px;text-align: center;">(编辑菜品后需重新设置折扣方案)</p>'	+
            '<div class="discountBtn">' +
            '<button class="btn btn-default" @click="close">取消</button>' +
            '<button class="btn btn-primary" style="margin-left: -40%;" @click="posDisCount">确定</button>' +
            '</div></div></div></div> ',
            data: function () {
                return {
                    discount: "",
                    discountMoney: 0,
                    orderMoney: 0,
                    eraseMoney: "",
                    noDiscountMoney: "",
                    choiceInput:null
                }
            },
            created: function () {
                var that = this;
                this.$watch("show", function () {
                    that.discount = "";
                    that.eraseMoney = "";
                    that.noDiscountMoney = "";
                    if(that.order.posDiscount == 1 && that.order.eraseMoney == 0 && that.order.noDiscountMoney == 0){
                        that.orderMoney = that.order.amountWithChildren != 0 ? that.order.amountWithChildren : that.order.orderMoney;
                    }else{
                        that.orderMoney = that.order.baseOrderMoney != undefined ? that.order.baseOrderMoney : that.order.amountWithChildren != 0 ? that.order.amountWithChildren : that.order.orderMoney;
                    }
                    that.getDiscountMoney();
                });
            },
            methods: {
                choiceInputValue:function(type){
                    this.choiceInput = type;
                },
                close: function () {
                    this.show = false;
                },
                back: function () {
                    if(this.choiceInput == 1){
                        if(this.eraseMoney.indexOf("元") > -1){
                            this.eraseMoney = this.eraseMoney.substring(0, this.eraseMoney.length - 1);
                        }
                        if(this.eraseMoney.length == 1){
                            this.eraseMoney = "";
                        }else if(this.eraseMoney.length > 1){
                            this.eraseMoney = this.eraseMoney.substr(0, this.eraseMoney.length - 1) + "元";
                        }
                    }else if(this.choiceInput == 2){
                        if(this.discount.indexOf("%") > -1){
                            this.discount = this.discount.substring(0, this.discount.length - 1);
                        }
                        if (this.discount == "") {
                            this.getDiscountMoney();
                            return;
                        }
                        if (this.discount.length == 1) {
                            this.getDiscountMoney();
                            this.discount = "";
                            return;
                        }
                        this.discount = this.discount.substr(0, this.discount.length - 1) + "%";
                    }else if(this.choiceInput == 3){
                        if(this.noDiscountMoney.indexOf("元") > -1){
                            this.noDiscountMoney = this.noDiscountMoney.substring(0, this.noDiscountMoney.length - 1);
                        }
                        if(this.noDiscountMoney.length == 1){
                            this.noDiscountMoney = "";
                        }else if(this.noDiscountMoney.length > 1){
                            this.noDiscountMoney = this.noDiscountMoney.substr(0, this.noDiscountMoney.length - 1) + "元";
                        }
                    }
                    this.getDiscountMoney();
                },
                getValueSet:function(e){
                    this.discount = e + "%";
                },
                getValue:function(e){
                    var that = this;
                    if(that.choiceInput == null){
                        toastr.error("请选择要输入的位置！");
                        return false;
                    }
                    if(that.choiceInput == 2){
                        if(that.discount.indexOf("%") > -1){
                            that.discount = that.discount.substring(0, that.discount.length - 1);
                        }
                        if(that.discount.length > 4){
                            toastr.error("小数点后最多保留2位！");
                            that.discount = that.discount + "%";
                            return false;
                        }
                        if(that.discount == ""){
                            if(e == "."){
                                that.discount = "0."
                            }else{
                                that.discount = e + "%";
                            }
                        }else{
                            if(parseFloat(that.discount) < 10){
                                if(e=="." && that.discount.indexOf(".") > -1){
                                    toastr.error("折扣不可以出现两次小数点！");
                                    that.discount = that.discount + "%";
                                    return false;
                                }else{
                                    that.discount = that.discount +"" + e + "%";
                                }
                            }else{
                                if(e=="." && that.discount.indexOf(".") == -1){
                                    that.discount = that.discount + e + "%";
                                }else if(e=="." && that.discount.indexOf(".") > -1){
                                    toastr.error("折扣不可以出现两次小数点！");
                                    that.discount = that.discount + "%";
                                    return false;
                                }else if(that.discount.indexOf(".") > -1){
                                    that.discount = that.discount + e + "%";
                                }else if(that.discount == "10" && e == 0){
                                    that.discount = that.discount +"" + e + "%";
                                }else{
                                    toastr.error("折扣不可以超过100！");
                                    that.discount = that.discount + "%";
                                    return false;
                                }
                            }
                        }
                        this.getDiscountMoney();
                    }else if(that.choiceInput == 1){
                        if(that.eraseMoney.indexOf("元") > -1){
                            that.eraseMoney = that.eraseMoney.substring(0, that.eraseMoney.length - 1);
                        }
                        if(e=="." && that.eraseMoney.indexOf(".") > -1){
                            toastr.error("金额不可以出现两次小数点！");
                            that.eraseMoney = that.eraseMoney + "元";
                            return false;
                        }else if(e=="." && that.eraseMoney == ""){
                            that.eraseMoney = "0.元";
                        }else if(e!="." && that.eraseMoney.indexOf(".") > -1){
                            if(that.eraseMoney.length - that.eraseMoney.indexOf(".") > 2) {
                                toastr.error("最多2位小数");
                                that.eraseMoney = that.eraseMoney + "元";
                                return false;
                            }else{
                                that.eraseMoney = that.eraseMoney + e + "元";
                                this.getDiscountMoney();
                            }
                        }else{
                            that.eraseMoney = that.eraseMoney + e + "元";
                            this.getDiscountMoney();
                        }
                    }else if(that.choiceInput == 3){
                        if(that.noDiscountMoney.indexOf("元") > -1){
                            that.noDiscountMoney = that.noDiscountMoney.substring(0, that.noDiscountMoney.length - 1);
                        }
                        if(e=="." && that.noDiscountMoney.indexOf(".") > -1){
                            toastr.error("金额不可以出现两次小数点！");
                            that.noDiscountMoney = that.noDiscountMoney + "元";
                            return false;
                        }else if(e=="." && that.noDiscountMoney == ""){
                            that.noDiscountMoney = "0.元";
                        }else if(e!="." && that.noDiscountMoney.indexOf(".") > -1){
                            if(that.noDiscountMoney.length - that.noDiscountMoney.indexOf(".") > 2){
                                toastr.error("最多2位小数");
                                that.noDiscountMoney = that.noDiscountMoney + "元";
                                return false;
                            }else{
                                that.noDiscountMoney = that.noDiscountMoney + e + "元";
                                this.getDiscountMoney();
                            }
                        }else{
                            that.noDiscountMoney = that.noDiscountMoney + e + "元";
                            this.getDiscountMoney();
                        }
                    }
                },
                getDiscountMoney:function(){
                    if(this.eraseMoney != ""){
                        this.eraseMoney = this.eraseMoney.substring(0, this.eraseMoney.length - 1);
                    }
                    if(this.noDiscountMoney != ""){
                        this.noDiscountMoney = this.noDiscountMoney.substring(0, this.noDiscountMoney.length - 1);
                    }
                    if(this.discount != ""){
                        this.discount = this.discount.substring(0, this.discount.length - 1);
                    }
                    this.discountMoney = (parseFloat(this.orderMoney) - parseFloat(this.eraseMoney == "" ? 0 : this.eraseMoney) - parseFloat(this.noDiscountMoney == "" ? 0 : this.noDiscountMoney)) * parseFloat(this.discount == "" ? 100 : this.discount) / 100 + parseFloat(this.noDiscountMoney == "" ? 0 : this.noDiscountMoney);
                    if(this.eraseMoney != ""){
                        this.eraseMoney = this.eraseMoney + "元";
                    }
                    if(this.noDiscountMoney != ""){
                        this.noDiscountMoney = this.noDiscountMoney + "元";
                    }
                    if(this.discount != ""){
                        this.discount = this.discount + "%";
                    }
                    if((parseFloat(this.eraseMoney != "" ? this.eraseMoney : 0) + parseFloat(this.noDiscountMoney != "" ? this.noDiscountMoney : 0)) > (parseFloat(this.orderMoney)/parseFloat(this.order.posDiscount) + parseFloat(this.order.eraseMoney) + parseFloat(this.order.noDiscountMoney))){
                        toastr.error("抹去金额加上不参与折扣的金额不应该大于订单实际支付金额！");
                        this.back();
                        return false;
                    }
                },
                posDisCount:function(){
                    var that = this;
                    if((parseFloat(that.eraseMoney != "" ? that.eraseMoney : 0) + parseFloat(that.noDiscountMoney != "" ? that.noDiscountMoney : 0)) > (parseFloat(that.orderMoney)/parseFloat(that.order.posDiscount) + parseFloat(that.order.eraseMoney) + parseFloat(that.order.noDiscountMoney))){
                        toastr.error("抹去金额加上不参与折扣的金额不应该大于订单实际支付金额！");
                        return false;
                    }
                    if(that.discount.indexOf("%") > -1){
                        that.discount = that.discount.substring(0, that.discount.length - 1);
                    }
                    if(that.eraseMoney.indexOf("元") > -1){
                        that.eraseMoney = that.eraseMoney.substring(0, that.eraseMoney.length - 1);
                    }
                    if(that.noDiscountMoney.indexOf("元") > -1){
                        that.noDiscountMoney = that.noDiscountMoney.substring(0, that.noDiscountMoney.length - 1);
                    }
                    posDisCountAjax(that.order.id, (parseFloat(that.discount != "" ? that.discount : 100) / 100).toFixed(4), that.eraseMoney != "" ? that.eraseMoney : 0, that.noDiscountMoney != "" ? that.noDiscountMoney : 0, 0, (that.orderMoney - that.discountMoney).toFixed(2),function (result) {
                        if(result.success){
                            that.show = false;
                            window.location.reload();
                            toastr.success("订单折扣成功");
                        }else{
                            toastr.error(result.message);
                            that.show = false;
                        }
                    });
                }
            }
        },
        "add_article_dialog": {
            props: ["show", "order"],
            template: '<div class="weui_dialog_alert" style="display:block" v-if="show">' +
            '<article_detail :show.sync="articleDetail.show" :article="articleDetail.article" :type="articleDetail.type" :uuid="articleDetail.uuid"></article_detail>' +
            '<order_middle :show.sync="orderMiddle.show" :order.sync="orderMiddle.order" :brandsetting.sync="brandSetting" :shop.sync="shop"></order_middle>' +
            '<div class="weui_mask"></div>' +
            '<div class="weui_dialog_dish" style="font-size: 16px;">' +
            '<div class="full-height">' +
            '<img src="assets/img/close.png" style="z-index:9" class="closeImg" alt="关闭" @click="close" />' +
            '<div class="weui_header">' +
            '<div id="myModalLabel" style="font-size: 24px;text-align: left;margin-left:30px;">' +
            '<span v-if="order.distributionModeId == 3" style="margin-right:15px;">[外带]</span>' +
            '<span v-if="order.distributionModeId == 1" style="margin-right:15px;">[堂食]</span>' +
            '<span>交易码：{{order.verCode}}</span>' +
            '<span style="margin-left: 30px;" v-if="order.orderMode !=2">桌号：{{order.tableNumber==null?"---":order.tableNumber}}</span>' +
            '<span style="margin-left: 30px;" v-if="order.orderMode !=2">人数：{{order.customerCount==null?"---":order.customerCount}}</span>' +
            '<div class="form-search">' +
            '<input type="text" id="searchDishName" class="form-control" placeholder="请输入编号或关键词" v-model="searchNameLike">' +
            '<img src="assets/img/clear.png" class="clearImg" alt="关闭" @click="clearSearchName" v-if="searchNameLike" />' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="weui_body">' +
            '<div class="edit-panel">' +
            '<div class="container" id="scroll-wapper">' +
            '<div class="col-xs-10" id="tableList">' +
            '<table class="table table-bordered" style="max-width: initial;margin-bottom: initial;">' +
            '<thead>' +
            '<tr>' +
            '<th style="text-align:center;">类型</th>' +
            '<th style="text-align:center;">餐品名称</th>' +
            '<th style="text-align:center;">数量</th>' +
            '<th style="text-align:center;">小计</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody>' +
            '<tr v-for="cart in cart.items" id="{{cart.uuid}}" @click="selectedCartItem(cart)" v-bind:class="{active:cartItem.id == cart.id && cartItem.uuid == cart.uuid}">' +
            '<td>{{cart.familyName}}</td>' +
            '<td>{{cart.name}}<span v-if="(cart.type==3&&cart.open) || (cart.data.recommendList!=null&&cart.data.recommendList.items.length>0)" @click="selectMealItems(cart)"><img src="assets/img/openMealItem.png" style="width:20px;heigth:20px;" /></span><span v-if="cart.type==3&&!cart.open" @click="selectMealItems(cart)"><img src="assets/img/closeMealItem.png" style="width:20px;heigth:20px;" /></span></td>' +
            '<td id="{{cart.id}}" v-if="!editArticleBtn || cartItem.id != cart.id">{{cart.number}}</td>' +
            '<td id="{{cart.id}}" v-if="editArticleBtn && cartItem.id == cart.id"><input class="cartNum" type="text" v-model="cart.number" style="width:30px;text-align:center;" /></td>' +
            '<td v-if="cart.data.fansPrice==null">{{cart.number * cart.data.price}}</td>' +
            '<td v-if="cart.data.fansPrice!=null">{{(cart.number * cart.data.fansPrice).toFixed(2)}}</td>' +
            '</tr>' +
            '</tbody>' +
            '</table>' +
            '</div>' +
            '</div>' +
            '<div class="flex-container">' +
            '<button type="button" class="btn btn-danger" @click="delArticleNumber">删除</button>' +
            '<button type="button" class="btn btn-info" v-if="!editArticleBtn" @click="editArticleNumber">编辑</button>' +
            '<button type="button" class="btn btn-info" v-if="editArticleBtn" @click="saveArticleNumber">保存</button>' +
            '<button type="button" class="btn btn-info" @click="editArticleInfo(null,1)"> ＋ </button>' +
            '<button type="button" class="btn btn-info" @click="editArticleInfo(null,2)"> — </button>' +
            '</div>' +
            '<div class="flex-container-count">' +
            '<span>小计</span>' +
            '<span>共{{cart.totalNumber}}份</span>' +
            '<span v-if="order.memberDiscount != null && order.memberDiscount != 1">￥{{((cart.totalPrice.toFixed(2)==-0.00?0:cart.totalPrice)*order.memberDiscount).toFixed(2)}}<span>(会员折扣：{{order.memberDiscount * 100}}%)</span></span>' +
            '<span v-if="order.memberDiscount != null && order.memberDiscount == 1">￥{{cart.totalPrice.toFixed(2)==-0.00?0:cart.totalPrice.toFixed(2)}}</span>' +
            '<span v-if="order.memberDiscount == null">￥{{cart.totalPrice.toFixed(2)==-0.00?0:cart.totalPrice.toFixed(2)}}</span>' +
            '</div>' +
            '<div class="flex-container-btn">' +
//          '<button type="button" class="btn btn-default">取消</button>' +
//          '<button type="button" class="btn btn-info">打印</button>' +
//          '<button type="button" class="btn btn-info">结算</button>' +
            '<button type="button" class="btn btn-info" v-if="order.orderMode == 2" @click="createOrderTv(order)">结算</button>' +
            '<button type="button" class="btn btn-info" v-if="order.orderMode == 6" @click="createOrderBoss(order)">立即下单</button>' +
            '</div>' +
            '<img src="assets/img/arror.png" alt="" class="arrorImg" @click="scrollToDown" />' +
            '<img src="assets/img/arror.png" alt="" class="arrorImg" style="transform: rotate(180deg);top: 25%;" @click="scrollToUp" />' +
            '</div>' +
            '<div class="add-panel">' +
            '<div class="dishButton">' +
            '<p v-for="article in articleListByFamily | filterBy searchNameLike in \'name\' \'peference\' \'initials\' " @click="addArticleInfo(article)">' +
            '<img src="assets/img/current.png" alt="" v-if="article.imgBtn" class="currentImg" />' +
            '<span class="add-dishName">{{article.name}}</span>' +
            '<span class="add-dishNumber">{{article.peference == null ? "" : article.peference}}</span>' +
            '<span class="add-dishMoney" v-if="article.isEmpty == 1" style="color: red">已售罄</span>' +
            '<span class="add-dishMoney" v-if="article.isEmpty != 1">￥{{article.fansPrice==null?article.price:article.fansPrice}}</span>' +
            '</p>' +
            '</div>' +
            '<img src="assets/img/arror.png" alt="" class="arrorImg" @click="nextPage" />' +
            '<img src="assets/img/arror.png" alt="" class="arrorImg" style="transform: rotate(180deg);top: 25%;" @click="previousPage" />' +
            '</div>' +
            '<div class="add-panel-other">' +
            '<div>' +
            '<div class="dishNameBtn">' +
            '<button type="button" :class="{active:familyNow.id == family.id}" v-for="family in familyList" @click="findArticlesByFamily(family)">{{family.name}}</button>' +
            '</div>' +
            '</div>' +
            '<img src="assets/img/arror.png" alt="" class="arrorImg" style="top:60%;width:45px;height:45px;" @click="nextFamilyPage" />' +
            '<img src="assets/img/arror.png" alt="" class="arrorImg" style="transform: rotate(180deg);top: 10%;width:45px;height:45px;" @click="previousFamilyPage" />' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div> ',
            data: function () {
                return {
                    articleMap: new HashMap(),
                    articleDetail: {show: false, article: null, type: 0, uuid: null},
                    orderMiddle: {show: false, order: {}},
                    familyList: [],
                    articleListByFamily: [],
                    addOrder: [],
                    //shopCart: [],
                    cart: [],
                    familyNow: {},            //当前选中的菜品分类
                    cartItem: {},             //当前选中的已选餐品  左侧
                    pageNumber: 0,            //当前分类餐品的页码  右侧
                    pageCount: 30,
                    pageFamilyNumber: 0,        //菜品分类分页的页码
                    pageFamilyCount: 10,        //菜品分类分页的数目
                    editArticleBtn: false,
                    scrollHeight: 0,
                    myScroll: null,
                    searchNameLike: null,
                    shop: null,
                    brandSetting : null
                }
            },
            watch: {
                'show': function (newVal, oldVal) {
                    if (newVal) {
                        var that = this;
                        Vue.nextTick(function () {
                            that.myScroll = new IScroll("#scroll-wapper", {
                                click: true
                            });
                        });
                        var activeinputele = "";
                        $("#searchDishName").addClass("numkeyboard");
                        $(".numkeyboard").ioskeyboard({
                            keyboardRadix: 60,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                            keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
                            keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                            clickeve: true,//是否绑定元素click事件
                            colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                            colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                            colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
                        });
                        $("#searchDishName").blur(function () {
                            activeinputele = "";
                            $("#keyboard_5xbogf8c").css({"display": "none"});
                        });

                        var Uarry = $("#keyboard_5xbogf8c li");//获取所有的li元素

                        $("#keyboard_5xbogf8c li").bind("click", function () {	//点击事件
                            var count = $(this).index();	//获取li的下标
                            var result = Uarry.eq(count).text();
                            if ("←" == result) {
                                var inputtext = activeinputele;
                                if (inputtext.length > 0) {
                                    inputtext = inputtext.substring(0, inputtext.length - 1);
                                    activeinputele = inputtext;
                                }
                            } else if ("Clear" == result) {
                                activeinputele = "";
                            } else if ("Exit" == result || "CapsLock" == result || "-" == result || "_" == result || "." == result || "/" == result || "/." == result || "-_" == result) {
                                activeinputele = activeinputele;
                            } else {
                                activeinputele += result;
                            }
                            that.searchNameLike = activeinputele;
                        })
                    }
                },
                'editArticleBtn': function (newVal, oldVal) {
                    if (newVal) {
                        var that = this;
                        setTimeout(function () {
                            $(".cartNum").focus()
                        }, 500);
                        $(".cartNum").focus(function () {
                            this.select();
                        });
                        $(".cartNum").addClass("numkey");
                        $(".numkey").numkeyboard({
                            keyboardRadix: 80,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                            keyboardRadixMin: 50,//键盘大小的最小值，默认为60，实际大小为564X198
                            keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                            clickeve: true,//是否绑定元素click事件
                            colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                            colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                            colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
                        });
                        $(".cartNum").blur(function () {
                            $("#keyboard_number").css({"display": "none"});
                            $(".cartNum").removeClass("numkey");
                        });
                        var Uarry = $("#keyboard_number li");//获取所有的li元素
                        var nowCount = "";
                        $("#keyboard_number li").bind("click", function () {	//点击事件
                            var count = $(this).index();	//获取li的下标
                            var result = Uarry.eq(count).text();
                            if ("←" == result) {
                                var count = nowCount;
                                if (count.length > 0) {
                                    count = count.substring(0, count.length - 1);
                                    nowCount = count;
                                }
                            } else if ("关闭" == result) {
                                nowCount = 0;
                                $("#keyboard_number").css({"display": "none"});
                            } else {
                                if (nowCount == 0) {
                                    nowCount = result;
                                } else {
                                    nowCount += result;
                                }
                            }
                            that.cartItem.number = nowCount;
                        })
                    }
                }
            },
            events: {
                "close-dialog": function () {
                    this.show = false;
                },
                "add-shopcart": function (article, type, uuid) {
                    //编辑菜品之前  优先关闭所有套餐的套餐单品
                    var that = this;
                    for (var f = 0; f < this.cart.items.length; f++) {
                        if (this.cart.items[f].type == 3) {
                            $("." + this.cart.items[f].uuid).remove();
                            this.cart.items[f].open = true;
                        }
                        if (this.cart.items[f].data.recommendList != null && this.cart.items[f].data.recommendList.items.length > 0) {
                            $("." + this.cart.items[f].uuid).remove();
                            this.cart.items[f].open = true;
                        }
                    }
                    //如果是编辑餐品保存   去除原先的  在进行添加  用uuid来判断是否是编辑还是添加
                    for (var i = 0; i < this.cart.items.length; i++) {
                        var c = this.cart.items[i];
                        if (c.uuid == uuid && c.id == article.id) {
                            if (c.type == 3) {
                                this.cart.items.splice(i, 1);
                                this.cart.totalNumber -= parseInt(c.number);
                                this.cart.totalPrice -= parseFloat(c.data.finalPrice) * parseInt(c.number);
                                this.cart.originPrice -= parseFloat(c.data.finalPrice) * parseInt(c.number);
                                this.cart.canheNumber -= c.data.mealFeeNumber * parseInt(c.number);
                            } else {
                                this.cart.items.splice(i, 1);
                                this.cart.totalNumber -= parseInt(c.data.number);
                                this.cart.totalPrice -= parseFloat(c.data.fansPrice == null ? c.data.price : c.data.fansPrice) * parseInt(c.data.number);
                                this.cart.originPrice -= parseFloat(c.data.price) * parseInt(c.data.number);
                                this.cart.canheNumber -= c.data.mealFeeNumber * parseInt(c.data.number);
                            }
                        }
                    }
                    var item = {};
                    if (type == 2) {
                        for (var j = 0; j < article.articlePrices.length; j++) {
                            if (article.articlePrices[j].click) {
                                item.name = article.name + article.articlePrices[j].name;
                            }
                        }
                    } else if (type == 5) {
                        item.name = article.name;
                        for (var j = 0; j < article.units.length; j++) {
                            for (var l = 0; l < article.units[j].details.length; l++) {
                                if (article.units[j].details[l].click) {
                                    item.name = item.name + "(" + article.units[j].details[l].name + ")";
                                }
                            }
                        }
                    } else {
                        item.name = article.name;
                    }
                    item.data = article;
                    item.id = article.id;
                    item.type = type;
                    if (type == 5) {
                        item.number = article.number;
                    } else {
                        item.number = 1;
                    }
                    item.uuid = this.uuid();
                    item.open = true;
                    item.familyName = this.familyNow.name;
                    item.current_working_stock = article.currentWorkingStock;
                    this.cart.totalNumber += parseInt(item.number);
                    if (type == 3) {
                        this.cart.totalPrice += parseFloat(article.finalPrice) * parseInt(item.number);
                        this.cart.originPrice += parseFloat(article.finalPrice) * parseInt(item.number);
                    } else {
                        this.cart.totalPrice += parseFloat(article.fansPrice == null ? article.price : article.fansPrice) * parseInt(item.number);
                        this.cart.originPrice += parseFloat(article.price) * parseInt(item.number);
                    }
                    this.cart.canheNumber += article.mealFeeNumber * parseInt(item.number);
                    this.cart.items.push(item);
                    this.editArticleImgBtn(article, true);
                    this.cartItem = item;
                    Vue.nextTick(function () {
                        that.myScroll.refresh();
                        that.myScroll.scrollToElement(document.querySelector('#scroll-wapper tbody tr:last-child'), 500, null, null, IScroll.utils.ease.quadratic);
                    });
                }
            },
            methods: {
                uuid: function () {
                    var s = [];
                    var hexDigits = "0123456789abcdef";
                    for (var i = 0; i < 36; i++) {
                        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
                    }
                    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
                    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
                    s[8] = s[13] = s[18] = s[23] = "-";

                    var uuid = s.join("");
                    return uuid;
                },
                selectMealItems: function (cart) {
                    var that = this;
                    if (cart.open) {
                        var $tr = "";
                        if (cart.type == 3) {
                            for (var i = 0; i < cart.data.mealAttrs.length; i++) {
                                var mealAttr = cart.data.mealAttrs[i];
                                for (var j = 0; j < mealAttr.currentItem.length; j++) {
                                    var $td = '<tr class=\"' + cart.uuid + '\"><td></td><td>' + mealAttr.currentItem[j].name + '</td><td>1</td><td>-</td></tr>';
                                    $tr = $tr + $td;
                                }
                            }
                        } else if (cart.data.recommendList != null && cart.data.recommendList.items.length > 0) {
                            for (var i = 0; i < cart.data.recommendList.items.length; i++) {
                                var item = cart.data.recommendList.items[i];
                                if (item.number > 0) {
                                    var $td = '<tr class=\"' + cart.uuid + '\"><td></td><td>' + item.articleName + '</td><td>' + item.number + '</td><td>-</td></tr>';
                                    $tr = $tr + $td;
                                }
                            }
                        }

                        $("#" + cart.uuid).after($tr);
                        cart.open = false;
                        Vue.nextTick(function () {
                            that.myScroll.refresh();
                            that.myScroll.scrollToElement(document.querySelector('#scroll-wapper tbody tr:last-child'), 500, null, null, IScroll.utils.ease.quadratic);
                        });
                    } else {
                        $("." + cart.uuid).remove();
                        cart.open = true;
                        Vue.nextTick(function () {
                            that.myScroll.refresh();
                            that.myScroll.scrollToElement(document.querySelector('#scroll-wapper tbody tr:last-child'), 500, null, null, IScroll.utils.ease.quadratic);
                        });
                    }
                },
                selectedCartItem: function (cart) {
                    this.cartItem = cart;
                },
                clearSearchName: function () {
                    var that = this;
                    that.searchNameLike = '';
                },
                findArticlesByFamily: function (family) {
                    var that = this;
                    that.familyNow = family;
                    that.pageNumber = 0;
                    getArticleListByFamily(family.id, that.pageNumber, that.pageCount, function (result) {
                        for (var k = 0; k < result.length; k++) {
                            if (result[k].name.length > 5) {
                                result[k].name = result[k].name.substr(0, 5) + "...";
                            }
                            if (that.shop.posPlusType == 1) {
                                result[k].fansPrice = null;
                            }
                            result[k].imgBtn = false;
                            for (var m = 0; m < that.cart.items.length; m++) {
                                if (result[k].id == that.cart.items[m].id) {
                                    result[k].imgBtn = true;
                                }
                            }
                        }
                        that.articleListByFamily = result;
                    });
                },
                close: function () {
                    this.show = false;
                },
                addArticleInfo: function (article) {
                    var that = this;
                    article = this.findArticleById(article.id);
                    if (article == null) {
                        toastr.error("请等待菜品加载完毕！");
                        return false;
                    }
                    //已售罄的不可点击
                    if (!article.currentWorkingStock || article.isEmpty == 1) {
                        return false;
                    }
                    //关闭所有套餐的套餐单品
                    for (var f = 0; f < that.cart.items.length; f++) {
                        if (that.cart.items[f].type == 3) {
                            $("." + that.cart.items[f].uuid).remove();
                            that.cart.items[f].open = true;
                        }
                    }
                    //编辑 套餐 新规格 老规格 推荐餐包的时候  弹窗
                    if (article.mealAttrs != null && article.mealAttrs.length > 0 && article.articleType == 2) {          //套餐
                        //构造套餐的默认值   以及 差价
                        article.finalPrice = article.fansPrice == null ? article.price : article.fansPrice;
                        for (var i = 0; i < article.mealAttrs.length; i++) {
                            var mealAttr = article.mealAttrs[i];
                            mealAttr.currentItem = [];
                            if(mealAttr.mealItems != null && mealAttr.mealItems.length > 0){
                                for (var j = 0; j < mealAttr.mealItems.length; j++) {
                                    if (mealAttr.mealItems[j].isDefault && mealAttr.choiceType == 0) { //必选 并且有默认值
                                        article.finalPrice += mealAttr.mealItems[j].priceDif;
                                        mealAttr.currentItem.push(mealAttr.mealItems[j]);
                                        mealAttr.mealItems[j].click = true;
                                    } else {
                                        mealAttr.mealItems[j].click = false;
                                    }
                                }
                            }
                        }
                        article.finalPrice = article.finalPrice * article.discount / 100;
                        this.articleDetail.article = JSON.stringify(article);
                        this.articleDetail.type = 3;
                        this.articleDetail.uuid = null;
                        this.articleDetail.show = true;
                        return false;
                    }
                    if (article.articlePrices != null && article.articlePrices.length > 0) {                              //老规格
                        //article.finalPrice = article.fansPrice == null ? article.price : article.fansPrice;
                        //for(var i = 0; i < article.articlePrices.length; i++){
                        //    article.articlePrices[i].click = false;
                        //}
                        //article.finalPrice = article.finalPrice * article.discount / 100;
                        //this.articleDetail.article = JSON.stringify(article);
                        //this.articleDetail.type = 2;
                        //this.articleDetail.show = true;
                        toastr.error("暂不支持添加此类餐品！");
                        return false;
                    }
                    if (article.units != null && article.units.length > 0) {                                              //新规格
                        article.finalPrice = article.fansPrice == null ? article.price : article.fansPrice;
                        for (var i = 0; i < article.units.length; i++) {
                            article.units[i].unitNumber = 0;
                            for (var j = 0; j < article.units[i].details.length; j++) {
                                article.units[i].details[j].click = false;
                            }
                        }
                        article.finalPrice = article.finalPrice * article.discount / 100;
                        article.number = 1;
                        this.articleDetail.article = JSON.stringify(article);
                        this.articleDetail.type = 5;
                        this.articleDetail.show = true;
                        return false;
                    }
                    if (article.recommendId != null && article.recommendId != "") {                                       //推荐餐包
                        //article.finalPrice = article.fansPrice == null ? article.price : article.fansPrice;
                        //article.finalPrice = article.finalPrice * article.discount / 100;
                        //article.recommendList = {items: [], count: null};
                        //getRecommends(article.id, function(obj){
                        //    article.recommendList.items = [];
                        //    article.recommendList.choiceType = obj.choiceType;
                        //    article.recommendList.count = obj.count;
                        //    if (obj.articles.length > 0) {
                        //        for (var i = 0; i < obj.articles.length; i++) {
                        //            //var art = that.findArticleById(obj.articles[i].articleId);
                        //            article.recommendList.items.push({
                        //                articleId: obj.articles[i].articleId,
                        //                price: obj.articles[i].price,
                        //                articleName: obj.articles[i].articleName,
                        //                number: 0,
                        //                maxCount: obj.articles[i].maxCount,
                        //                //currentWorkingStock: art.currentWorkingStock,
                        //                show: true
                        //            });
                        //        }
                        //    }
                        //});
                        //this.articleDetail.article = JSON.stringify(article);
                        //this.articleDetail.type = 6;
                        //this.articleDetail.show = true;
                        toastr.error("暂不支持添加此类餐品！");
                        return false;
                    }
                    var flag = true;
                    //无限点击加菜
                    for (var i = 0; i < this.cart.items.length; i++) {
                        if (article.id == this.cart.items[i].id) {
                            flag = false;
                            this.editArticleInfo(article, null);
                        }
                    }
                    if (flag) {
                        //添加单品
                        if (article.articlePrices.length == 0) {
                            var item = {};
                            item.name = article.name;
                            item.data = article;
                            item.id = article.id;
                            item.type = 1;
                            item.number = 1;
                            item.familyName = this.familyNow.name;
                            item.current_working_stock = article.currentWorkingStock;
                            this.cart.totalNumber += parseInt(1);
                            this.cart.totalPrice += parseFloat(article.fansPrice == null ? article.price : article.fansPrice);
                            this.cart.originPrice += parseFloat(article.price);
                            this.cart.canheNumber += article.mealFeeNumber;
                            this.cart.items.push(item);
                            this.editArticleImgBtn(article, true);
                            this.cartItem = item;
                        }
                    }
                    Vue.nextTick(function () {
                        that.myScroll.refresh();
                        that.myScroll.scrollToElement(document.querySelector('#scroll-wapper tbody tr:last-child'), 500, null, null, IScroll.utils.ease.quadratic);
                    });
                },
                editArticleInfo: function (article, number) {
                    if (this.editArticleBtn) {
                        toastr.error("请先保存完正在编辑的餐品！");
                        return;
                    }
                    //套餐不能修改数量
                    if (this.cartItem.type == 3) {
                        return false;
                    }
                    //存在推荐餐包不能进行修改数量
                    if (this.cartItem.data.recommendList != null && this.cartItem.data.recommendList.items.length > 0) {
                        return false;
                    }
                    //编辑菜品之前  优先关闭所有套餐 推荐餐包  的套餐单品
                    for (var f = 0; f < this.cart.items.length; f++) {
                        if (this.cart.items[f].type == 3) {
                            $("." + this.cart.items[f].uuid).remove();
                            this.cart.items[f].open = true;
                        }
                        if (this.cart.items[f].data.recommendList != null && this.cart.items[f].data.recommendList.items.length > 0) {
                            $("." + this.cart.items[f].uuid).remove();
                            this.cart.items[f].open = true;
                        }
                    }
                    if (article == null) {         //只做加减1个菜品的时候赋值
                        article = this.cartItem.data;
                        if (number == 1) {
                            number = parseInt(this.cartItem.number) + parseInt(1);
                        } else if (number == 2) {
                            number = parseInt(this.cartItem.number) - parseInt(1);
                        }
                        if (article == null) {
                            toastr.error("请选择编辑的菜品");
                        }
                    }
                    for (var i = 0; i < this.cart.items.length; i++) {
                        if (article.articlePrices == null) {
                            article = this.findArticleById(article.id);
                        }
                        if (this.cart.items[i].id == article.id && this.cart.items[i].uuid == this.cartItem.uuid) {
                            if (number == null) {
                                number = parseInt(this.cart.items[i].number) + 1;
                                this.cartItem = this.cart.items[i];
                            }
                            if (number == 0) {
                                this.cart.totalNumber -= parseInt(this.cart.items[i].number);
                                this.cart.totalPrice -= this.cart.items[i].number * parseFloat(article.fansPrice == null ? article.price : article.fansPrice);
                                this.cart.originPrice -= this.cart.items[i].number * parseFloat(article.price);
                                this.cart.canheNumber -= this.cart.items[i].number * article.mealFeeNumber;
                                this.cart.items.splice(i, 1);
                                this.editArticleImgBtn(article, false);
                                if (this.cart.items.length > 0) {
                                    this.cartItem = this.cart.items[this.cart.items.length - 1];
                                } else {
                                    this.cartItem = [];
                                }
                            } else {
                                var count = number - this.cartItem.number;
                                if (this.cartItem.type == 5) {
                                    article.finalPrice = parseFloat(article.finalPrice) / parseInt(article.number) * (parseInt(article.number) + parseInt(count));
                                    article.number += parseInt(count);
                                }
                                this.cart.items[i].name = this.cartItem.name;
                                this.cart.items[i].data = article;
                                this.cart.items[i].id = article.id;
                                this.cart.items[i].type = this.cartItem.type;
                                this.cart.items[i].number = number;
                                this.cart.items[i].familyName = this.familyNow.name;
                                this.cart.items[i].current_working_stock = article.currentWorkingStock;
                                this.cart.totalNumber += parseInt(count);
                                this.cart.totalPrice += count * parseFloat(article.fansPrice == null ? article.price : article.fansPrice);
                                this.cart.originPrice += count * parseFloat(article.price);
                                this.cart.canheNumber += count * article.mealFeeNumber;
                                this.editArticleImgBtn(article, true);
                                this.cartItem = this.cart.items[i];
                                if (this.cart.totalNumber == 0) {
                                    this.cart.items.splice(i, 1);
                                    this.editArticleImgBtn(article, false);
                                    if (this.cart.items.length > 0) {
                                        this.cartItem = this.cart.items[this.cart.items.length - 1];
                                    } else {
                                        this.cartItem = [];
                                    }
                                }
                            }
                        }
                    }
                },
                editArticleNumber: function () {
                    if (this.cartItem.number == null) {
                        toastr.error("请选择添加的菜品");
                    } else {
                        //编辑菜品
                        if (this.cartItem.type > 1) {   //套餐 出现弹窗
                            if (this.cartItem.type == 3) {
                                this.cartItem.data.finalPrice = parseFloat(this.cartItem.data.finalPrice) / parseInt(this.cartItem.number);
                            } else {
                                this.cartItem.data.finalPrice = parseFloat(this.cartItem.data.finalPrice) / parseInt(this.cartItem.data.number);
                            }
                            this.articleDetail.article = JSON.stringify(this.cartItem.data);
                            this.articleDetail.type = this.cartItem.type;
                            this.articleDetail.uuid = this.cartItem.uuid;
                            this.articleDetail.show = true;
                        } else {
                            this.editArticleBtn = true;
                        }
                    }
                },
                delArticleNumber: function () {
                    if (this.editArticleBtn) {
                        toastr.error("请先保存完正在编辑的餐品！");
                        return;
                    }
                    //删除购物车前  优先关闭所有套餐的套餐单品
                    for (var f = 0; f < this.cart.items.length; f++) {
                        if (this.cart.items[f].type == 3) {
                            $("." + this.cart.items[f].uuid).remove();
                            this.cart.items[f].open = true;
                        }
                        if (this.cart.items[f].data.recommendList != null && this.cart.items[f].data.recommendList.items.length > 0) {
                            $("." + this.cart.items[f].uuid).remove();
                            this.cart.items[f].open = true;
                        }
                    }
                    for (var i = 0; i < this.cart.items.length; i++) {
                        if (this.cartItem.id == this.cart.items[i].id && this.cartItem.uuid == this.cart.items[i].uuid) {
                            var article = this.findArticleById(this.cartItem.id);
                            //如果是套餐 计算差价
                            if (this.cartItem.type == 3 && this.cartItem.uuid == this.cart.items[i].uuid) {
                                // if (article.fansPrice != null) {
                                //     article.finalPrice = this.cartItem.data.finalPrice;
                                // } else {
                                //     article.price = this.cartItem.data.price;
                                // }
                            } else if (this.cartItem.type == 3 && this.cartItem.uuid != this.cart.items[i].uuid) {
                                continue;
                            }
                            this.cart.totalNumber -= parseInt(this.cartItem.number);
                            if (this.cartItem.type == 2) {
                                for (var k = 0; k < this.cartItem.data.articlePrices.length; k++) {
                                    var a = this.cartItem.data.articlePrices[k];
                                    if (a.click) {
                                        this.cart.totalPrice -= this.cartItem.number * parseFloat(a.fansPrice == null ? a.price : a.fansPrice);
                                        this.cart.originPrice -= this.cartItem.number * parseFloat(a.price);
                                    }
                                }
                            } else if (this.cartItem.type == 5) {
                                this.cart.totalPrice -= this.cartItem.number * parseFloat(this.cartItem.data.finalPrice / this.cartItem.data.number);
                                this.cart.originPrice -= this.cartItem.number * parseFloat(this.cartItem.data.finalPrice / this.cartItem.data.number);
                            } else if (this.cartItem.type == 3){
                                this.cart.totalPrice -= this.cartItem.number * parseFloat(this.cartItem.data.finalPrice);
                                this.cart.originPrice -= this.cartItem.number * parseFloat(this.cartItem.data.finalPrice);
                            } else {
                                this.cart.totalPrice -= this.cartItem.number * parseFloat(article.fansPrice == null ? article.price : article.fansPrice);
                                this.cart.originPrice -= this.cartItem.number * parseFloat(article.price);
                            }
                            this.cart.canheNumber -= this.cartItem.number * article.mealFeeNumber;
                            this.cart.items.splice(i, 1);
                            this.editArticleImgBtn(this.cartItem.data, false);
                        }
                    }
                    if (this.cart.items.length > 0) {
                        this.cartItem = this.cart.items[this.cart.items.length - 1];
                    } else {
                        this.cartItem = {};
                    }
                },
                saveArticleNumber: function () {
                    var re = /^[0-9]\d*$/;
                    this.cart.totalNumber = 0;
                    this.cart.totalPrice = 0;
                    this.cart.originPrice = 0;
                    this.cart.canheNumber = 0;
                    for (var i = 0; i < this.cart.items.length; i++) {
                        if (this.cart.items[i].number == 0) {
                            //this.cart.items.splice(i, 1);
                            //this.editArticleImgBtn(this.cartItem.data, false);
                            toastr.error("菜品个数应为大于0的整数！");
                            return;
                        } else {
                            if (!re.test(this.cart.items[i].number)) {
                                toastr.error("菜品个数应为大于0的整数！");
                                return;
                            }
                            var article = this.findArticleById(this.cart.items[i].id);
                            this.cart.totalNumber += parseInt(this.cart.items[i].number);
                            this.cart.totalPrice += this.cart.items[i].number * parseFloat(article.fansPrice == null ? article.price : article.fansPrice);
                            this.cart.originPrice += this.cart.items[i].number * parseFloat(article.price);
                            this.cart.canheNumber += this.cart.items[i].number * article.mealFeeNumber;
                        }
                    }
                    this.editArticleBtn = false;
                },
                //显示选中当前图片的右上勾图案
                editArticleImgBtn: function (article, boo) {
                    for (var n = 0; n < this.articleListByFamily.length; n++) {
                        if (this.articleListByFamily[n].id == article.id) {
                            this.articleListByFamily[n].imgBtn = boo;
                        }
                    }
                },
                findArticleById: function (id) {
                    return this.articleMap.get(id);
                },
                createOrderBoss: function (order) {
                    if (this.editArticleBtn) {
                        toastr.error("请先保存完正在编辑的餐品！");
                        return;
                    }
                    if (this.cart.items.length <= 0) {
                        toastr.error("请先添加餐品！");
                        return;
                    }
                    var orderForm = {};
                    orderForm.tableNumber = order.tableNumber;
                    orderForm.parentOrderId = order.id;
                    orderForm.verCode = order.verCode;
                    orderForm.customerCount = order.customerCount;
                    orderForm.customerId = order.customerId;
                    orderForm.distributionModeId = 1;
                    orderForm.orderItems = [];
                    orderForm.useAccount = false;
                    for (var i = 0; i < this.cart.items.length; i++) {
                        var item = this.cart.items[i];
                        var orderItem;
                        if (item.data.recommendList != null && item.data.recommendList.items.length > 0) {
                            for (var k = 0; k < item.data.recommendList.items.length; k++) {
                                var recommend = item.data.recommendList.items[k];
                                if (recommend.number > 0) {
                                    var recommendResult = {
                                        articleId: recommend.articleId,
                                        count: recommend.number,
                                        type: 6,
                                        recommendId: item.data.recommendId,
                                        parentId: item.data.id
                                    }
                                    orderForm.orderItems.push(recommendResult);
                                }
                            }
                        }

                        if (item.type == 5) {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: item.type,
                                name: item.name,
                                price: item.data.fansPrice == null ? item.data.price : item.data.fansPrice
                            };
                        } else if (item.type == 6) {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: 1
                            };
                        } else if (item.type == 3) {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: item.type
                            };
                        } else {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: item.type
                            };
                        }
                        if (item.type == 3) {
                            orderItem.mealItems = [];
                            for (var n = 0; n < item.data.mealAttrs.length; n++) {
                                var mealAttr = item.data.mealAttrs[n];
                                for (var k = 0; k < mealAttr.currentItem.length; k++) {
                                    orderItem.mealItems.push(mealAttr.currentItem[k].id);
                                }
                            }
                        }
                        //保存此菜品当前时间段的折扣,用于和后台做对比（1：无规格单品，2：老规格单品，3：套餐主品，5：新规格单品）
                        if (item.type == 1 || item.type == 2 || item.type == 3 || item.type == 5) {
                            if (item.type == 2) {
                                //var id = item.id.substr(0, item.id.indexOf('@'));
                                orderItem.discount = this.findArticleById(item.id).discount;
                            } else {
                                orderItem.discount = this.findArticleById(item.id).discount;
                            }
                        }
                        orderForm.orderItems.push(orderItem);
                    }
                    orderForm.waitMoney = 0;
                    orderForm.waitId = null;
                    orderForm.servicePrice = 0;
                    orderForm.payMode = 0;
                    if(order.memberDiscount != null){
                        orderForm.paymentAmount = (this.cart.totalPrice * order.memberDiscount).toFixed(2);
                        orderForm.memberDiscount = order.memberDiscount;
                        orderForm.totalPrice = (this.cart.totalPrice * order.memberDiscount).toFixed(2);
                    }else{
                        orderForm.paymentAmount = this.cart.totalPrice.toFixed(2);
                        orderForm.memberDiscount = order.memberDiscount;
                        orderForm.totalPrice = this.cart.totalPrice.toFixed(2);
                    }
                    orderForm.payType = 1;
                    this.show = false;
                    saveOrderForm(orderForm, function (result) {
                        if (result.success) {
                            toastr.success("下单成功");
                        } else {
                            toastr.error(result.message);
                        }
                    });
                },
                createOrderTv: function (order) {
                    if (this.editArticleBtn) {
                        toastr.error("请先保存完正在编辑的餐品！");
                        return;
                    }
                    if (this.cart.items.length <= 0) {
                        toastr.error("请先添加餐品！");
                        return;
                    }
                    var orderForm = {};
                    orderForm.verCode = order.verCode;
                    orderForm.tableNumber = order.verCode;
                    orderForm.distributionModeId = order.distributionModeId;
                    orderForm.orderItems = [];
                    orderForm.useAccount = false;
                    for (var i = 0; i < this.cart.items.length; i++) {
                        var item = this.cart.items[i];
                        var orderItem;
                        if (item.data.recommendList != null && item.data.recommendList.items.length > 0) {
                            for (var k = 0; k < item.data.recommendList.items.length; k++) {
                                var recommend = item.data.recommendList.items[k];
                                if (recommend.number > 0) {
                                    var recommendResult = {
                                        articleId: recommend.articleId,
                                        count: recommend.number,
                                        type: 6,
                                        recommendId: item.data.recommendId,
                                        parentId: item.data.id
                                    }
                                    orderForm.orderItems.push(recommendResult);
                                }
                            }
                        }
                        if (item.type == 5) {
                            console.log(JSON.stringify(item));
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: item.type,
                                name: item.name,
                                price: item.data.fansPrice == null ? item.data.price : item.data.fansPrice
                            };
                        } else if (item.type == 6) {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: 1
                            };
                        } else if (item.type == 3) {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: item.type
                            };
                        } else {
                            orderItem = {
                                articleId: item.id,
                                count: item.number,
                                type: item.type
                            };
                        }
                        if (item.type == 3) {
                            orderItem.mealItems = [];
                            for (var n = 0; n < item.data.mealAttrs.length; n++) {
                                var mealAttr = item.data.mealAttrs[n];
                                for (var k = 0; k < mealAttr.currentItem.length; k++) {
                                    orderItem.mealItems.push(mealAttr.currentItem[k].id);
                                }
                            }
                        }
                        //保存此菜品当前时间段的折扣,用于和后台做对比（1：无规格单品，2：老规格单品，3：套餐主品，5：新规格单品）
                        if (item.type == 1 || item.type == 2 || item.type == 3 || item.type == 5) {
                            if (item.type == 2) {
                                //var id = item.id.substr(0, item.id.indexOf('@'));
                                orderItem.discount = this.findArticleById(item.id).discount;
                            } else {
                                orderItem.discount = this.findArticleById(item.id).discount;
                            }
                        }
                        orderForm.orderItems.push(orderItem);
                    }
                    orderForm.waitMoney = 0;
                    orderForm.waitId = null;
                    orderForm.servicePrice = 0;
                    orderForm.totalPrice = this.cart.totalPrice;
                    orderForm.payType = 0;
                    orderForm.payMode = 0;
                    orderForm.orderMode = 2;
                    orderForm.articleCount = this.cart.totalNumber;
                    this.orderMiddle.show = true;
                    this.orderMiddle.order = orderForm;
                },
                previousPage: function () {
                    var that = this;
                    if (this.pageNumber == 0) {
                        toastr.error("已经是第一页了");
                        return;
                    } else {
                        this.pageNumber--;
                    }
                    getArticleListByFamily(this.familyNow.id, this.pageNumber * this.pageCount, this.pageCount, function (res) {
                        for (var k = 0; k < res.length; k++) {
                            if (res[k].name.length > 5) {
                                res[k].name = res[k].name.substr(0, 5) + "...";
                            }
                            if (that.shop.posPlusType == 1) {
                                res[k].fansPrice = null;
                            }
                            res[k].imgBtn = false;
                            for (var p = 0; p < that.cart.items.length; p++) {
                                if (that.cart.items[p].id == res[k].id) {
                                    res[k].imgBtn = true;
                                }
                            }
                        }
                        that.articleListByFamily = res;
                    });
                },
                nextPage: function () {
                    var that = this;
                    this.pageNumber++;
                    getArticleListByFamily(this.familyNow.id, this.pageNumber * this.pageCount, this.pageCount, function (res) {
                        for (var k = 0; k < res.length; k++) {
                            if (res[k].name.length > 5) {
                                res[k].name = res[k].name.substr(0, 5) + "...";
                            }
                            if (that.shop.posPlusType == 1) {
                                res[k].fansPrice = null;
                            }
                            res[k].imgBtn = false;
                            for (var p = 0; p < that.cart.items.length; p++) {
                                if (that.cart.items[p].id == res[k].id) {
                                    res[k].imgBtn = true;
                                }
                            }
                        }
                        that.articleListByFamily = res;
                        if (res.length == 0) {
                            toastr.error("已经是最后一页了");
                            that.previousPage();
                        }
                    });
                },
                previousFamilyPage: function () {
                    var that = this;
                    if (this.pageFamilyNumber == 0) {
                        toastr.error("已经是第一页了");
                        return;
                    } else {
                        this.pageFamilyNumber--;
                    }
                    getFamilyBySort(this.pageFamilyNumber * this.pageFamilyCount, this.pageFamilyCount, function (result) {
                        that.familyNow = result[0];
                        for (var j = 0; j < result.length; j++) {
                            if (result[j].name.length > 5) {
                                result[j].name = result[j].name.substr(0, 4) + "...";
                            }
                        }
                        that.familyList = result;
                        getArticleListByFamily(that.familyList[0].id, 0, that.pageCount, function (res) {
                            for (var k = 0; k < res.length; k++) {
                                if (res[k].name.length > 5) {
                                    res[k].name = res[k].name.substr(0, 5) + "...";
                                }
                                if (that.shop.posPlusType == 1) {
                                    res[k].fansPrice = null;
                                }
                                res[k].imgBtn = false;
                                for (var p = 0; p < that.cart.items.length; p++) {
                                    if (that.cart.items[p].id == res[k].id) {
                                        res[k].imgBtn = true;
                                    }
                                }
                            }
                            that.articleListByFamily = res;
                        });
                    });
                },
                nextFamilyPage: function () {
                    var that = this;
                    this.pageFamilyNumber++;
                    getFamilyBySort(that.pageFamilyNumber * that.pageFamilyCount, that.pageFamilyCount, function (result) {
                        if (result.length == 0) {
                            toastr.error("已经是最后一页了");
                            that.previousFamilyPage();
                        } else {
                            that.familyNow = result[0];
                            for (var j = 0; j < result.length; j++) {
                                if (result[j].name.length > 5) {
                                    result[j].name = result[j].name.substr(0, 4) + "...";
                                }
                            }
                            that.familyList = result;
                            getArticleListByFamily(that.familyList[0].id, 0, that.pageCount, function (res) {
                                for (var k = 0; k < res.length; k++) {
                                    if (res[k].name.length > 5) {
                                        res[k].name = res[k].name.substr(0, 5) + "...";
                                    }
                                    if (that.shop.posPlusType == 1) {
                                        res[k].fansPrice = null;
                                    }
                                    res[k].imgBtn = false;
                                    for (var p = 0; p < that.cart.items.length; p++) {
                                        if (that.cart.items[p].id == res[k].id) {
                                            res[k].imgBtn = true;
                                        }
                                    }
                                }
                                that.articleListByFamily = res;
                            });
                        }
                    });
                },
                scrollToDown: function () {
                    var that = this;
                    Vue.nextTick(function () {
                        that.myScroll.scrollToElement(document.querySelector('#scroll-wapper tbody tr:last-child'), 500, null, null, IScroll.utils.ease.quadratic);
                    });
                },
                scrollToUp: function () {
                    var that = this;
                    Vue.nextTick(function () {
                        that.myScroll.scrollToElement(document.querySelector('#scroll-wapper'), 500, null, null, IScroll.utils.ease.quadratic);
                    });
                },
                discount: function (price, discount) {
                    if (price && discount) {
                        return parseFloat((price * discount / 100).toFixed(2));
                    } else {
                        return price;
                    }
                }
            },
            created: function () {
                var that = this;
                this.$watch("show", function () {
                    if (this.show) {
                        that.cart = {
                            items: [],
                            totalNumber: 0,
                            totalPrice: 0,
                            originPrice: 0,
                            canheNumber: 0
                        };
                        //that.shopCart = [];
                        that.editArticleBtn = false;
                        that.cartItem = {};
                        that.pageNumber = 0;
                        that.pageFamilyNumber = 0;
                        that.cartItem = {};
                        that.familyNow = {};
                        that.familyList = [];
                        that.searchNameLike = '';
                        getArticleList(function (r) {
                            for (var i = 0; i < r.length; i++) {
                                if (that.shop.posPlusType == 1) {
                                    r[i].fansPrice = null;
                                }
                                that.articleMap.put(r[i].id, r[i]);
                            }
                        });
                        getShopInfo(function (result) {
                            that.shop = result;
                            getFamilyBySort(that.pageFamilyNumber, that.pageFamilyCount, function (result) {
                                that.familyNow = result[0];
                                for (var j = 0; j < result.length; j++) {
                                    if (result[j].name.length > 5) {
                                        result[j].name = result[j].name.substr(0, 4) + "...";
                                    }
                                }
                                that.familyList = result;
                                getArticleListByFamily(that.familyList[0].id, 0, that.pageCount, function (res) {
                                    for (var k = 0; k < res.length; k++) {
                                        if (res[k].name.length > 5) {
                                            res[k].name = res[k].name.substr(0, 5) + "...";
                                        }
                                        if (res[k].discount != null && res[k].discount != 100) {
                                            res[k].fansPrice = that.discount(res[k].fansPrice, res[k].discount);
                                            res[k].price = that.discount(res[k].price, res[k].discount);
                                        }
                                        if (that.shop.posPlusType == 1) {
                                            res[k].fansPrice = null;
                                        }
                                        res[k].imgBtn = false;
                                    }
                                    that.articleListByFamily = res;
                                });
                            });
                        });
                        getBrandSetting(function (result) {
                            that.brandSetting = result;
                        });
                    }
                });
            },
            components: {
                "article_detail": {
                    props: ["show", "article", "type", "uuid"],
                    template: '<div class="modal" style="display:block" v-if="show">' +
                    '<div class="weui_dialog_alert">' +
                    '<div class="weui_mask"></div>' +
                    '<div class="weui_dialog_mealAttrs">' +
                    '<div class="full-height">' +
                    '<span class="meal_title">{{art.name}}</span>' +
                    '<div class="mealAttrs_main" id="scroll-mealAttrs">' +
                    '<div>' +
                        //套餐
                    '<div class="mealAttrs_chlid" v-if="type==3" v-for="mealAttr in art.mealAttrs">' +
                    '<span class="mealAttrs_title">{{mealAttr.name}}({{mealAttr.choiceType == 0 ? "必选" + mealAttr.choiceCount : "任选"}}，已选{{mealAttr.currentItem.length}}):</span>' +
                    '<p v-for="mealItem in mealAttr.mealItems" :class="{active:mealItem.click == true}" @click="selectMealItem(mealAttr,mealItem)">' +
                    '<span class="mealAttrs_childName">{{mealItem.name}}</span>' +
                    '<span class="mealAttrs_childMoney" v-if="mealItem.priceDif > 0">+{{mealItem.priceDif}}元</span>' +
                    '</p>' +
                    '</div>' +
                        //老规格
                    '<div class="mealAttrs_chlid" v-if="type==2">' +
                    '<p v-for="articlePrice in art.articlePrices" :class="{active:articlePrice.click == true}" @click="selectArticlePrice(articlePrice)">' +
                    '<span class="mealAttrs_childName">{{art.name}}{{articlePrice.name}}</span>' +
                    '<span class="mealAttrs_childMoney" v-if="articlePrice.price > 0"> {{articlePrice.fansPrice==null?articlePrice.price:articlePrice.fansPrice}}元</span>' +
                    '</p>' +
                    '</div>' +
                        //新规格
                    '<div class="mealAttrs_chlid" v-if="type==5" v-for="unit in art.units">' +
                    '<div class="mealAttrs_title" style="font-size: 20px;">' +
                    '<span>{{unit.name}}</span>' +
                    '<span>({{unit.choiceType == 0 ? "必选1": "任选"}}，已选{{unit.unitNumber}}):</span>' +
                    '</div>' +
                    '<p v-for="detail in unit.details" v-if="detail.isUsed==1" :class="{active:detail.click == true}" @click="selectUnit(unit,detail,art.units)">' +
                    '<span class="middle">' +
                    '<i class="mealAttrs_childName">{{detail.name}}</i>' +
                    '<i class="mealAttrs_childMoney" v-if="detail.price > 0">+{{detail.price}}元</i>	' +
                    '</span>' +
                    '</p>' +
                    '</div>' +
                        //推荐餐包
                    '<div class="mealAttrs_chlid" v-if="art.recommendList!=null && art.recommendList.items.length>0">' +
                    '<p v-for="item in art.recommendList.items" :class="{active:item.number > 0}">' +
                    '<span class="mealAttrs_childName">{{item.articleName}}</span>' +
                    '<span @click="editRecommendItemNumber(0,item)">+++</span>' +
                    '<span if="item.number > 0">{{item.number}}</span>' +
                    '<span @click="editRecommendItemNumber(1,item)">---</span>' +
                    '<span class="mealAttrs_childMoney" v-if="item.price > 0"> + {{item.price}}元</span>' +
                    '</p>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +

                        //加入购物车  出去新规格
                    '<div class="flex-container-mealAttrs" v-if="type!=5">' +
                    '	<span>共<i style="color:#0083C9;font-style: normal;">1</i>份</span>' +
                    '	<span>小计<i style="color:#0083C9;font-style: normal;">￥ {{art.finalPrice.toFixed(2)}}</i></span>' +
                    '	<button type="button" class="btn btn-info" @click="addToShopCart(art)">{{uuid != null ? "确定" : "加入购物车"}}</button>' +
                    '	<button type="button" class="btn btn-default" @click="close" style="margin-right: 20px;">取消</button>' +
                    '</div>' +
                        // <!--新规格小计-->
                    '<div class="flex-container-mealAttrs" v-if="type==5">' +
                        //'<div v-if="hasNoodle" style="font-weight: bold;font-size: 20px;margin-bottom: 5px;">红烧牛肉面(中份)(加葱)(微辣)</div>'+
                    '<span v-if="hasNoodle">' +
                    '<img class="newAttrImg" @click="minusNum" src="assets/img/minus.png" alt="减" />' +
                    '<i style="font-size: 20px;margin: 0px 10px;">{{art.number}}</i>' +
                    '<img class="newAttrImg" @click="addNum" src="assets/img/plus.png" alt="加" />' +
                    '</span>' +
                    '<span>小计<i style="color:#0083C9;font-style: normal;">￥ {{priceEnd.toFixed(2)}}</i></span>' +
                    '<button type="button" class="btn btn-info" style="margin-right: 20px;" @click="addToShopCart(art)">{{uuid != null ? "确定" : "加入购物车"}}</button>' +
                    '<button type="button" class="btn btn-default" @click="close" style="margin-right: 20px;">取消</button>' +
                    '</div>' +

                    '<img src="assets/img/arror.png" alt="" class="arrorImg" @click="scrollMealDown" />' +
                    '<img src="assets/img/arror.png" alt="" class="arrorImg" style="transform: rotate(180deg);top: 25%;" @click="scrollMealUp" />' +
                    '</div>' +
                    '<img src="assets/img/close.png" class="closeImg" alt="关闭" @click="close" style="width: 30px;height: 30px;"/>' +
                    '</div>' +
                    '</div>' +
                    '</div> ',
                    data: function () {
                        return {
                            art: {},
                            mealScroll: null,
                            hasNoodle: false,
                            priceEnd: 0,
                        }
                    },
                    methods: {
                        close: function () {
                            this.hasNoodle = false;
                            this.show = false;
                        },
                        minusNum: function () {
                            if (this.art.number == 1) {
                                toastr.error("菜品数量不能为0");
                                return false;
                            }
                            this.art.number--;
                            this.priceEnd = this.art.finalPrice * this.art.number;
                        },
                        addNum: function () {
                            this.art.number++;
                            this.priceEnd = this.art.finalPrice * this.art.number;
                        },
                        selectMealItem: function (mealAttr, mealItem) {
                            if (mealAttr.choiceType == 0) {
                                if (mealItem.click) {
                                    return;
                                }
                                this.art.finalPrice -= mealAttr.currentItem[0].priceDif;
                                mealAttr.currentItem.splice(0, 1);
                                mealAttr.currentItem.push(mealItem);
                                this.art.finalPrice = this.art.finalPrice + (mealItem.priceDif * this.art.discount / 100);
                            } else {
                                if (mealItem.click) {
                                    mealItem.click = false;
                                    for (var i = 0; i < mealAttr.currentItem.length; i++) {
                                        if (mealAttr.currentItem[i].id == mealItem.id) {
                                            this.art.finalPrice = this.art.finalPrice - (mealAttr.currentItem[i].priceDif * this.art.discount / 100);
                                            mealAttr.currentItem.splice(i, 1);
                                        }
                                    }
                                } else {
                                    mealItem.click = true;
                                    mealAttr.currentItem.push(mealItem);
                                    this.art.finalPrice = this.art.finalPrice + (mealItem.priceDif * this.art.discount / 100);
                                }
                            }
                            //重组已选套餐属性下的单品
                            for (var i = 0; i < mealAttr.mealItems.length; i++) {
                                var flag = false;
                                for (var j = 0; j < mealAttr.currentItem.length; j++) {
                                    if (mealAttr.currentItem[j].id == mealAttr.mealItems[i].id) {
                                        flag = true;
                                    }
                                }
                                if (flag) {
                                    mealAttr.mealItems[i].click = true;
                                } else {
                                    mealAttr.mealItems[i].click = false;
                                }
                            }
                        },
                        selectArticlePrice: function (articlePrice) {
                            for (var i = 0; i < this.art.articlePrices.length; i++) {
                                this.art.articlePrices[i].click = false;
                            }
                            articlePrice.click = true;
                            this.art.finalPrice = articlePrice.fansPrice == null ? articlePrice.price : articlePrice.fansPrice;
                        },
                        selectUnit: function (unit, detail, units) {
                            if (unit.choiceType == 0) {
                                for (var i = 0; i < unit.details.length; i++) {
                                    if (unit.details[i].click) {
                                        this.art.finalPrice -= unit.details[i].price;
                                        this.priceEnd = this.art.finalPrice * this.art.number;
                                    }
                                    unit.details[i].click = false;
                                }
                                unit.unitNumber = 1;
                                this.art.finalPrice += detail.price;
                                this.priceEnd = this.art.finalPrice * this.art.number;
                                detail.click = true;
                            } else if (unit.choiceType == 1) {
                                if (detail.click) {
                                    this.art.finalPrice -= detail.price;
                                    this.priceEnd = this.art.finalPrice * this.art.number;
                                } else {
                                    this.art.finalPrice += detail.price;
                                    this.priceEnd = this.art.finalPrice * this.art.number;
                                }
                                detail.click = !detail.click;
                                var unitNumber = 0;
                                for (var i = 0; i < unit.details.length; i++) {
                                    if (unit.details[i].click) {
                                        unitNumber++;
                                    }
                                }
                                unit.unitNumber = unitNumber;
                            }
                            //判断如何units里面有click是true的则hasNoodle为true
                            this.hasNoodle = false;
                            for (var m = 0; m < units.length; m++) {
                                for (var n = 0; n < units[m].details.length; n++) {
                                    if (units[m].details[n].click) {
                                        this.hasNoodle = true;
                                        return;
                                    }
                                }
                            }
                        },
                        editRecommendItemNumber: function (type, item) {
                            if (type == 0) {
                                item.number += 1;
                                this.art.finalPrice += item.price;
                            } else if (type == 1) {
                                if (item.number == 0) {
                                    toastr.error("该单品尚未选择无法减去！");
                                    return;
                                } else {
                                    item.number -= 1;
                                    this.art.finalPrice -= item.price;
                                }
                            }
                        },
                        addToShopCart: function (art) {
                            if (art.fansPrice == null) {
                                art.price = art.finalPrice.toFixed(2);
                            } else {
                                art.fansPrice = art.finalPrice.toFixed(2);
                            }
                            if (this.type == 2) {
                                var flag = true;
                                for (var i = 0; i < this.art.articlePrices.length; i++) {
                                    if (this.art.articlePrices[i].click) {
                                        flag = false;
                                    }
                                }
                                if (flag) {
                                    toastr.error("请选择餐品规格！");
                                    return;
                                }
                            } else if (this.type == 5) {
                                var flag = true;
                                for (var i = 0; i < art.units.length; i++) {
                                    var unit = art.units[i];
                                    if (unit.choiceType == 0) {
                                        for (var j = 0; j < unit.details.length; j++) {
                                            var detail = unit.details[j];
                                            if (detail.click) {
                                                flag = false;
                                            }
                                        }
                                        if (flag) {
                                            toastr.error("请选择餐品规格！");
                                            return;
                                        }
                                    }
                                }
                                art.finalPrice = this.priceEnd;
                            }
                            this.$dispatch("add-shopcart", art, this.type, this.uuid);
                            this.close();
                        },
                        scrollMealUp: function () {
                            var that = this;
                            Vue.nextTick(function () {
                                that.mealScroll.scrollToElement(document.querySelector('#scroll-mealAttrs .mealAttrs_chlid:first-child'), 500, null, null, IScroll.utils.ease.quadratic);
                            });
                        },
                        scrollMealDown: function () {
                            var that = this;
                            Vue.nextTick(function () {
                                that.mealScroll.scrollToElement(document.querySelector('#scroll-mealAttrs .mealAttrs_chlid:last-child'), 500, null, null, IScroll.utils.ease.quadratic);
                            });
                        },
                    },
                    created: function () {
                        var that = this;
                        this.$watch("show", function () {
                            if (that.show) {
                                that.art = JSON.parse(that.article);
                                if (that.type == 5) {
                                    that.priceEnd = that.art.finalPrice * that.art.number;
                                    if (that.art.number > 0) {
                                        that.hasNoodle = true;
                                    }
                                }
                                Vue.nextTick(function () {
                                    that.mealScroll = new IScroll("#scroll-mealAttrs", {
                                        probeType: 2,
                                        click: true,
                                    });
                                });
                            }
                        });
                    }
                },
                "order_middle": {
                    props: ["show", "order", "brandsetting", "shop"],
                    template: '<div class="modal" style="display:block" v-if="show">' +
                    '<div class="weui_dialog_alert">' +
                    '<div class="weui_mask"></div>' +
                    '<div class="weui_dialog_pay" style="font-size: 24px;">' +
                    '<div class="full-height">' +
                    '<div class="pay_header">' +
                    '<div class="order_middle">结算订单</div>' +
                    '<img src="assets/img/close.png" class="closeImg" alt="关闭" @click="closePayDialog"/>' +
                    '</div>' +
                    '<div class="pay_body">' +
                    '<div class="pay_content_left">' +
                    '<div style="text-align: left;position: relative;height: 50%;margin-top: 15px;">' +
                    '<div style="font-size: 30px;font-weight: bold;">订单金额:￥{{order.totalPrice.toFixed(2)}}</div>' +
                    '<div>交易码:{{order.verCode}}</div>' +
                    '<div>餐品数量:{{order.articleCount}}</div>' +
                    '<p style="font-size:20px;font-weight:bold;">支付方式:</p>' +
                    '</div>' +
                    '<div class="flex-container-pay">' +
                    '<div class="payMentBtn" :class="{active:order.payMode == 1 || defaultWeChat}" @click="selectPayMode(1)" v-if="shop.openPosWeChatPay==1">' +
                    '<img class="payImgIcon" src="assets/img/wxpay.png" alt="微信支付" />' +
                    '<span>微信支付</span>' +
                    '</div>' +
                    '<div class="payMentBtn" :class="{active:order.payMode == 2 || defaultAliPay}" @click="selectPayMode(2)" v-if="brandsetting.aliPay==1 && shop.openPosAliPay==1">' +
                    '<img class="payImgIcon" src="assets/img/ali.png" alt="支付宝支付" />' +
                    '<span>支付宝</span>' +
                    '</div>' +
                    '<div class="payMentBtn" :class="{active:order.payMode == 3 || defaultUnionPay}" @click="selectPayMode(3)" v-if="brandsetting.openUnionPay==1 && shop.openPosUnionPay==1">' +
                    '<img class="payImgIcon" src="assets/img/bankPay.png" alt="银联支付" />' +
                    '<span>银联支付</span>' +
                    '</div>' +
                    '<div class="payMentBtn" :class="{active:order.payMode == 4 || defaultMoneyPay}" @click="selectPayMode(4)" v-if="brandsetting.openMoneyPay==1 && shop.openPosMoneyPay==1">' +
                    '<img class="payImgIcon" src="assets/img/cash.png" alt="现金支付" />' +
                    '<span>现金支付</span>' +
                    '</div >' +
                    '<div class="payMentBtn" :class="{active:order.payMode == 5 || defaultShanhuiPay}" @click="selectPayMode(5)" v-if="brandsetting.openShanhuiPay==1 && shop.openPosShanhuiPay==1">' +
                    '<img class="payImgIcon" src="assets/img/people.png" alt="大众点评" />' +
                    '<span>大众点评</span>' +
                    '</div>' +
                    '<div class="payMentBtn" :class="{active:order.payMode == 6 || defaultIntegralPay}" @click="selectPayMode(6)" v-if="brandsetting.integralPay==1 && shop.openPosIntegralPay==1">' +
                    '<img class="payImgIcon" src="assets/img/corn.png" alt="会员支付" />' +
                    '<span>会员支付</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="pay_content_right">' +
                    '<div class="pay_input">' +
                    '<span>已收:</span>' +
                    '<input type="text" class="form-control payText" disabled="disabled" v-model="money">' +
                    '</div>' +
                    '<div class="pay_input">' +
                    '<span>找零:</span>' +
                    '<input type="text" class="form-control payText" disabled="disabled" :class="{activeRed:muchMoney < 0}" v-model="muchMoney.toFixed(2)">' +
                    '</div>' +
                    '<div class="keys">' +
                    '<span @click="getValueAdd(10)">10</span>' +
                    '<span @click="getValue(1)">1</span>' +
                    '<span @click="getValue(2)">2</span>' +
                    '<span @click="getValue(3)">3</span>' +
                    '<span @click="getValueAdd(50)">50</span>' +
                    '<span @click="getValue(4)">4</span>' +
                    '<span @click="getValue(5)">5</span>' +
                    '<span @click="getValue(6)">6</span>' +
                    '<span @click="getValueAdd(100)">100</span>' +
                    '<span @click="getValue(7)">7</span>' +
                    '<span @click="getValue(8)">8</span>' +
                    '<span @click="getValue(9)">9</span>' +
                    '<span @click="getValueAdd(500)">500</span>' +
                    '<span @click="getValue(00)">00</span>' +
                    '<span @click="getValue(0)">0</span>' +
                    '<span @click="getValue(\'.\')">.</span>' +
                    '</div>' +
                    '<div class="keysText">' +
                    '<span @click="back">←</span>' +
                    '<span class="clear" @click="clearMoney">清空</span>' +
                    '<span class="bill" @click="saveOrder(order)">结账下单</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>',
                    data: function () {
                        return {
                            money: "",
                            muchMoney: 0,
                            defaultWeChat: false,
                            defaultAliPay: false,
                            defaultUnionPay: false,
                            defaultMoneyPay: false,
                            defaultShanhuiPay: false,
                            defaultIntegralPay: false,
                            disabled: true
                        }
                    },
                    methods: {
                        back: function () {
                            if (!this.disabled) {
                                if (this.money == "") {
                                    return;
                                }
                                if (this.money.length == 1) {
                                    this.money = "";
                                    this.muchMoney = 0;
                                    return;
                                }

                                this.money = this.money.substr(0, this.money.length - 1);
                                var mm = parseFloat(this.money) - parseFloat(this.order.totalPrice);
                                this.muchMoney = mm;
                            }
                        },
                        clearMoney: function () {
                            if (!this.disabled) {
                                this.money = "";
                                this.muchMoney = 0;
                            }
                        },
                        closePayDialog: function () {
                            this.show = false;
                        },
                        selectPayMode: function (payMode) {
                            if (payMode == 4) {
                                this.disabled = false;
                            } else {
                                this.money = this.order.totalPrice + "";
                                this.muchMoney = 0;
                                this.disabled = true;
                            }
                            this.order.payMode = payMode;
                            this.defaultWeChat = false;
                            this.defaultAliPay = false;
                            this.defaultUnionPay = false;
                            this.defaultMoneyPay = false;
                            this.defaultShanhuiPay = false;
                            this.defaultIntegralPay = false;
                        },
                        getValueAdd: function (m) {
                            if (!this.disabled) {
                                if (this.money == "") {
                                    this.money = parseFloat(m) + "";
                                } else {
                                    this.money = parseFloat(this.money) + parseFloat(m) + "";
                                }

                                var mm = parseFloat(this.money) - parseFloat(this.order.totalPrice);
                                this.muchMoney = mm;
                            }
                        },
                        getValue: function (m) {
                            if (!this.disabled) {
                                //不能出现2个.
                                if ("." == m && (this.money + "").indexOf(".") > -1) {
                                    return false;
                                }
                                //小数点.之前要有数字
                                if ("." == m && (this.money + "").length == 0) {
                                    return false;
                                }
                                //如果存在小数点.  小数点末尾不能超过2位
                                if ((this.money + "").indexOf(".") > -1 && ((this.money + "").length - (this.money + "").indexOf(".")) > 2) {
                                    return false;
                                }
                                m = m + "";
                                this.money = this.money + "";
                                this.money = this.money + m;
                                var mm = parseFloat(this.money) - parseFloat(this.order.totalPrice);
                                this.muchMoney = mm;
                            }
                        },
                        saveOrder: function (order) {
                            var that = this;
                            if (that.order.payMode == 0) {
                                toastr.clear();
                                toastr.error("请选择支付方式");
                                return;
                            }
                            if (this.muchMoney < 0) {
                                toastr.clear();
                                toastr.error("订单金额不符");
                                return;
                            }
                            if (this.money == "") {
                                toastr.clear();
                                toastr.error("请输入付款金额");
                                return;
                            }
                            if (parseFloat(this.money) <= 0) {
                                toastr.clear();
                                toastr.error("请输入付款金额");
                                return;
                            }
                            order.paymentAmount = this.money;
                            order.giveChange = this.muchMoney.toFixed(2);

                            saveOrderForm(order, function (result) {
                                if (result.success) {
                                    toastr.success("下单成功");
                                    that.show = false;
                                    if (that.shop.isPosNew == 1) {
                                        receiveMoneyNew(result.data.id, function (result) {
                                            PrintService.send(JSON.stringify(result), function (result) {
                                                if (result != -5) {
                                                    toastr.clear();
                                                    toastr.success("开启钱箱");
                                                }

                                            });
                                        })
                                    } else {
                                        receiveMoney(result.data.id, function (result) {
                                            callbackObj.openCashDrawer(JSON.stringify(result));
                                        })
                                    }

                                    that.$dispatch("close-dialog");

                                } else {
                                    toastr.error(result.message);
                                }
                            });
                        },
                        getDefaultPayMode: function () {
                            var brandSetting = this.brandsetting;
                            var shop = this.shop;
                            if (brandSetting.openMoneyPay == 1 && shop.openPosMoneyPay == 1) {
                                this.defaultMoneyPay = true;
                                this.order.payMode = 4;
                                this.disabled = false;
                            } else if (brandSetting.openUnionPay == 1 && shop.openPosUnionPay == 1) {
                                this.defaultUnionPay = true;
                                this.order.payMode = 3;
                            } else if (this.defaultMoneyPay == false && this.defaultUnionPay == false) {
                                this.defaultWeChat = true;
                                this.order.payMode = 1;
                            } else if (brandSetting.aliPay == 1 && shop.openPosAliPay == 1) {
                                this.defaultAliPay = true;
                                this.order.payMode = 2;
                            } else if (brandSetting.openShanhuiPay == 1 && shop.openPosShanhuiPay == 1) {
                                this.defaultShanhuiPay = true;
                                this.order.payMode = 5;
                            } else if (brandSetting.integralPay == 1 && shop.openPosIntegralPay == 1) {
                                this.defaultIntegralPay = true;
                                this.order.payMode = 6;
                            } else {
                                this.disabled = false;
                            }
                        }
                    },
                    created: function () {
                        var that = this;
                        this.$watch("show", function () {
                            if (that.show) {
                                that.money = that.order.totalPrice.toFixed(2) + "";
                                that.muchMoney = 0;
                                that.getDefaultPayMode();
                            }
                        });
                    }
                }
            }
        },
        "choice-printer": {
            props: ["printers", "show", "oid"],
            template: '<div class="modal" style="display:block" v-if="show">                                                                                                                 ' +
            '  <div class="modal-dialog">                                                                                                        ' +
            '	<div class="modal-content">                                                                                                      ' +
            '	  <div class="modal-header">                                                                                                     ' +
            '		<button type="button" class="close" @click="close"><span >&times;</span></button> ' +
            '		<h4 class="modal-title">找到多个打印机</h4>                                                                                     ' +
            '	  </div>                                                                                                                         ' +
            '	  <div class="modal-body" style="max-height: 500px; overflow: auto;">                                                                                                       ' +
            '<div>' +
            '		 <button v-for="o in printers" @click="choiceprinter(o)" class="btn btn-block" :class="{\'btn-success\':$index%2==1,\'btn-info\':$index%2==0}" style="text-align:left;margin-bottom:10px;">' +
            '<div class="flex-row"><div class="flex-1">{{$index+1}}.{{o.name}} -- {{o.ip}}:{{o.port}}</div> ' +
            '<div>&nbsp;</div>' +
            '</button>   ' +
            '</div>' +
            '	  </div>                                                                                                                         ' +
            '	  <div class="modal-footer">                                                                                                     ' +
            '		<button type="button" class="btn btn-default" @click="close">关闭</button>                                            ' +
            '	  </div>                                                                                                                         ' +
            '	</div>                                                                                                                           ' +
            '  </div>                                                                                                                            ' +
            '</div> ',
            methods: {
                choiceprinter: function (p) {
                    this.$dispatch("choice-printer-event", p, this.oid);
                    this.close();
                },
                close: function () {
                    this.show = false;
                }
            }
        },
        "history-modal": {
            props: ["show", "title", "historyorders"],
            template: '<div class="modal" style="display:block" v-if="show">' +
            '  <div class="modal-dialog">' +
            '    <div class="modal-content">' +
            '      <div class="modal-header">' +
            '        <button type="button" class="close" @click="close"><span >&times;</span></button>' +
            '        <h4 class="modal-title text-center"><strong>{{title}}</strong></h4>' +
            '      </div>' +
            '      <div class="modal-body">' +
            '		<div  id="history_iScroll" style="height:300px">' +
            '      	<table class="table table-striped">' +
            '	      <thead>' +
            '	        <tr>' +
            '	          <th>交易码</th>' +
            '	          <th>菜品总数</th>' +
            '	          <th>操作</th>' +
            '	        </tr>' +
            '	      </thead>' +
            '	      <tbody style="font-size: 22px">' +
            '	        <tr v-for="order in historyorders">' +
            '	          <td>{{order.verCode}}{{order.parentOrderId&&\'(加)\'}}</td>' +
            '	          <td>{{order.articleCount}}</td>' +
            '	          <td><button class="btn btn-info btn-block" @click="printOne(order)">打&nbsp;印</button></td>' +
            '	        </tr>' +
            '	      </tbody>' +
            '	    </table>' +
            '		</div>' +
            '      </div>' +
            '      <div class="modal-footer">' +
            '      	<div class="col-md-2"><button type="button" class="btn btn-bolck btn-danger" @click="close">不处理</button></div>' +
            '      	<div class="col-md-8 col-md-offset-2"><button type="button" class="btn btn-block btn-primary" @click="printAll">打印全部</button></div>' +
            '      </div>' +
            '    </div>' +
            '  </div>' +
            '</div>',
            watch: {
                "show": function (val, oldVal) {
                    if (val) {
                        this.iScrollInit();
                    }
                }
            },
            methods: {
                printOne: function (order) {
                    this.$dispatch("history-order-print-event", order);
                    if (this.historyorders.length == 0) {
                        this.close();
                    }
                },
                printAll: function () {
                    this.$dispatch("history-order-print-all-event");
                    this.close();
                },
                close: function () {
                    this.show = false;
                },
                iScrollInit: function () {
                    var that = this;
                    Vue.nextTick(function () {
                        $("#history_iScroll").css("overflow", "hidden");
                        that.orderListiScroll = new IScroll("#history_iScroll", {
                            scrollbars: true,
                            interactiveScrollbars: true,
                            shrinkScrollbars: 'clip',
                            scrollbars: 'custom'
                        });
                        document.addEventListener('touchmove', function (e) {
                            e.preventDefault();
                        }, false);
                    })
                }
            }
        },
        "msg-modal": {
            props: ["show", "title", "msg"],
            template: '<div class="modal" style="display:block" v-if="show">' +
            '  <div class="modal-dialog">' +
            '    <div class="modal-content">' +
            '      <div class="modal-header">' +
            '        <h4 class="modal-title" style="font-size:30px;"><strong>{{title}}</strong></h4>' +
            '      </div>' +
            '      <div class="modal-body" style="font-size:30px;height: 240px;">' +
            '			<strong>{{{msg}}}' +
                // '<button style="float:right;" @click="closeExe">退出</button>' +
            '</strong>' +
            '      </div>' +
            '    </div>' +
            '  </div>' +
            '</div>',
            methods: {
                closeExe: function () {

                    window.close();
                },
            }
        }
    },
    computed: {
        modeText: function () {
            return mode[this.order.distributionModeId];
        }
    },
    data: function () {
        return {
            orderlist: [],
            order: {},
            lastTime: 0,
            printTask: [],
            errorTask:[],
            printerList:[],
            websocket:null,
            serverIp:"",
            localInfo:{}, //云巴本地信息
            onlineServer : [], //在线设备
            offlineServer : [], //不在线设备
            loginBegin : true,
            aliasList : [],
            loginOther: false,
            printOrderList: new HashMap(),
            errorOrderId: [],
            printPlatformTask: [],
            cancelBtn: true,
            callBtn: true,
            printBtn: false,
            orderMoney: true,//只有后付款模式显示订单金额，且显示的是订单原价。
            otherPayModeBtn: false,
            printKitchenBtn: false,
            showTableNumber: false,
            ws: null,
            wsHeartbeatId: null,
            shop: null,
            brandSetting: null,
            choicePrinterDialog: {oid: "", show: false, printers: []},
            historyOrdersDialog: {historyorders: [], title: "历史订单", show: false},
            otherPayModelDialog: {oid: "", order: {}, orderList: [], orderMoney: 0, show: false},//其他支付方式弹框
            refundArticleModelDialog: {refundList: [], show: false, orderId: null, tangshi: true, order: null},//退菜弹框
            turnTableModelDialog: { show: false, orderId: null, tangshi: true, order: null, shop:null},//换桌弹框
            posDiscountDialog: { show: false, order: null},//换桌弹框
            addArticleDialog: {show: false, order: null},//加菜弹框
            MsgModalDialog: {show: false, title: "", msg: ""},//后台发送的提示指令
            shanHuiDialog: {show: false, order: {}}, //闪惠确定付款的弹窗
            orderSettlementDialog: {
                show: false,
                order: {},
                brandsetting: {},
                shop: {},
                orderlist: [],
                oldorder: {},
                payvalue: 0,
                remainvalue: 0,
                couponvalue: 0,
                usecouponvalue: 0,
                couponid: ""
            }, //订单结算弹窗
            orderListiScroll: {},//新增订单里列表的 滑动 对象
            showCustomerCount: false,//是否显示就餐人数
            outFood: false, //如果是外卖的话就是true
            keyWord: null,
            errorOrderTask: null,//自动打印异常订单对象
            childList: [],
            selectedId: null,
            selectedItem:null,
            baseSelectedNumber : 0, //编辑菜品前的数量， 防止前后值相等的情况去请求
            selectedNumber: 0,
            editChange: false,
            editHoufu: false,
            unFinish: false,
            selectedOrderId: null,
            selectedType: null, //0,10,11,12是服务费 1是菜品,
            hasCalled: false,
            refundList: [], //退菜列表
            socketConnTimes: 5,	//socket 断线连接次数
            bossModel: false, //大boss模式,
            currentOrderId: null, //选中的订单id,
            showPayState: false,
            canCallOrder: true,
            platformType: ["未知", "饿了么外卖", "美团外卖", "百度外卖","R+"],
            orderPayType: {0: "余额", 1: "微信", 2: "支付宝", 3: "银联", 4: "现金", 5: "闪惠", 6: "会员"},
            shopTvConfig:{},
        };
    },
    created: function () {

        this.getBrandSetting();
        this.getServerIp();
        //美团心跳监测 上报
        this.reportHeartbeat();

        //this.getPosOrder();
        this.iScrollInit();
        // $("#conn_ok").show();
        // $("#conn_break").hide();
    },
    ready: function () {
        var that = this;
        $("#searchNumber").addClass("passWordKey");
        $(".passWordKey").numkeyboard({
            keyboardRadix: 60,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
            keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
            keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
            clickeve: true,//是否绑定元素click事件
            colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
            colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
            colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
        });
        var Uarry = $("#keyboard_number li");//获取所有的li元素
        var nowNumber = "";
        $("#keyboard_number li").bind("click", function () {	//点击事件
            var count = $(this).index();	//获取li的下标
            var result = Uarry.eq(count).text();
            if ("←" == result) {
                var count = nowNumber;
                if (count.length > 0) {
                    count = count.substring(0, count.length - 1);
                    nowNumber = count;
                }
            } else {
                if (nowNumber == 0) {
                    nowNumber = result;
                } else {
                    nowNumber += result;
                }
            }
            that.keyWord = nowNumber;
        })
        $("#searchNumber").focus(function () {
            $("#keyboard_number").css({"display": "block"});
        });
        $("#searchNumber").blur(function () {
            $("#keyboard_number").css({"display": "none"});
//          $("body #keyboard_number:last-child").remove();
        });

    },
    events: {
        "choice-printer-event": function (p, oid) {
            console.log("选择打印机打印:", p.id, oid);
            this.printTicketBase(oid, p.id);
        },
        "remove-order": function (order) {
            this.orderlist.$remove(order);
        },
        "history-order-print-event": function (order) {
            this.orderlist.push(order);
            this.orderlist.sort(this.keysert('serialNumber', true))//排序，新订单显示在最上面
            this.printTask.push(order);
            this.historyOrdersDialog.historyorders.$remove(order);
            this.refreshIScroll();
        },
        "history-order-print-all-event": function () {
            var historyorders = this.historyOrdersDialog.historyorders;
            for (var i = 0; i < historyorders.length; i++) {
                this.orderlist.push(historyorders[i]);
                this.printTask.push(historyorders[i]);
            }
            this.orderlist.sort(this.keysert('serialNumber', true))//排序，新订单显示在最上面
            this.historyOrdersDialog.historyorders = [];
            this.refreshIScroll();
        },
        "confirm-order-event": function (payModel, oid) {
            var that = this;
            var order = this.otherPayModelDialog.order;
            //this.orderlist.$remove(order);//移除当前订单

            var removeList = this.otherPayModelDialog.orderList;//需要移除订单的集合
            console.log(removeList);
            // return ;
            var orderId = order.id;
            if (order.parentOrderId) {
                orderId = order.parentOrderId;
            }
            confirmOrder(orderId, function (result) {
                if (result.success) {
                    for (var i in removeList) {//移除
                        console.log(removeList[i].verCode, removeList[i].id);
                        that.orderlist.$remove(removeList[i]);
                    }
                    toastr.success("确认订单成功");
                    //window.location.reload();
                    //that.keyWord = "";
                } else {
                    toastr.error("确认订单失败！请确认网络是否存在异常！");
                }
            })
        },
        "show-create-order-pos": function (order) {
            this.addArticleDialog.show = true;
            this.addArticleDialog.order = order;
        },
        "fresh-finish-order-event": function (orderId) {
            var that = this;
            that.refundList = [];
            listHistoryOrder(function (olist) {
                that.orderlist = olist;
            });
            getOrderInfo(orderId, function (order) {
                that.order = order;
                // if (that.order.orderMode == 5) { //如果是后付模式
                getChildItem(orderId, function (result) {
                    that.childList = [];
                    that.childList = result;

                });
                // }
            });
        },
        "clean-show-order-info": function () {
            this.order = {};
        }
    },
    methods: {
        iScrollInit: function () {
            if (!this.modeCode) {//非验证码下单模式，需设置触屏滑动
                console.log("设置触屏滑动");
                var that = this;
                Vue.nextTick(function () {
                    setTimeout(function () {
                        $("#iscroll_div").height($("body").height() - $("#articleFamily").height() - 80);
                        $("#iscroll_div").css("overflow", "hidden");
                        that.orderListiScroll = new IScroll("#iscroll_div", {
                            scrollbars: true,
                            interactiveScrollbars: true,
                            shrinkScrollbars: 'clip',
                            scrollbars: 'custom'
                        });
                        document.addEventListener('touchmove', function (e) {
                            e.preventDefault();
                        }, false);
                    }, 1000)
                })
            }
        },
        getServerIp:function(){
            var that = this;
            getServerIp(function (data) {
                that.serverIp = data.serverInfo.serverIp;

            });
        },
        getBrandSetting: function () {
            var that = this;
            getBrandSetting(function (data) {
                that.brandSetting = data;
                getShopInfo(function (result) {
                    that.shop = result;
                    that.showCustomerCount = data.isUseServicePrice == 1 && result.isUseServicePrice == 1 ? true : false;
                    console.log(that.showCustomerCount);
                    if (that.shop.shopMode == 2) {
                        getShopTvConfig(function(r){
                            that.shopTvConfig = r;
                            that.startTV();
                        });
                    }
                });
            });
        },
        refreshIScroll: function () {//更新滑动高度
            var that = this;
            Vue.nextTick(function () {
                that.orderListiScroll.refresh();
            });
        },
        getPosOrder: function () {//得到 POS 中新列表订单中，未处理的订单，防止页面刷新后，新订单被移除。
            var that = this;
            getPosOrder(function (data) {
                for (var i = 0; i < data.length; i++) {
                    var temp = data[i];
                    that.orderlist.push(temp);//已打印，但未操作的订单（拒绝，叫号等）
                }
            })
        },
        getPrinterList: function () {
            var that = this;
            printList(function (result) {
                that.printerList = result;
                for (var i = 0; i < that.printerList.length; i++) {
                    that.printerList[i].taskList = [];
                }
                that.printPrinterOrder();
            });
        },
        getChildItem: function () {
            return this.childMap.getSize();
        },
        otherPayMode: function (order) {
            var orderList = [];
            var orderMoney = 0;
            //计算当前选中的订单
            orderList.push(order);
            orderMoney += order.orderMoney;//计算订单原价
            //循环订单列表，匹配对应的订单
            if (order.parentOrderId) {
                for (var i in this.orderlist) {
                    var o = this.orderlist[i];
                    if (order.parentOrderId == o.id) {//查找 父 订单
                        orderList.push(o);
                        orderMoney += o.orderMoney;
                    }
                    if (order.parentOrderId == o.parentOrderId && order.id != o.id) {//查找 同级 追加的 订单,不包含当前订单
                        orderList.push(o);
                        orderMoney += o.orderMoney;
                    }
                }
            } else {
                orderMoney = order.amountWithChildren == 0.0 ? order.orderMoney : order.amountWithChildren;
            }
            this.otherPayModelDialog.oid = order.id;
            this.otherPayModelDialog.order = order;
            this.otherPayModelDialog.orderList = orderList;
            this.otherPayModelDialog.orderMoney = orderMoney.toFixed(2);
            this.otherPayModelDialog.show = true;
        },
        cancelOrder: function (order) {
            var that = this;

            if (confirm("你确定要取消订单吗？")) {
                cancelOrderPos(order.id, function (result) {
                    if (result.success) {
                        var removeOrders = [];
                        removeOrders.push(order);//将当前订单添加到移除列表
                        for (var i in that.orderlist) {
                            var o = that.orderlist[i];
                            if (order.id == o.parentOrderId) {//将子订单添加到移除列表
                                removeOrders.push(o);
                            }
                        }
                        for (var i in removeOrders) {
                            that.orderlist.$remove(removeOrders[i]);
                        }
                        toastr.success("取消订单成功");
                    } else {
                        toastr.error("取消订单失败");
                    }
                });
                if(that.shop.shopMode == 2){
                    var data = {
                        type : "remove",
                        code : order.verCode,
                        orderId:order.id,
                        data:null
                    }
                    that.sendTvMsg(JSON.stringify(data));
                }
            }
        },
        confirmOrderPos: function (order) {
            var that = this;
            if (confirm("确认已收款？")) {
                confirmOrderPos(order.id, function (result) {
                    that.orderlist.$remove(order);
                    if (result.success) {
                        toastr.success("确认订单成功");
                    }
                });
            }
        },
        confirmOrderShanHui: function (order) {
            this.shanHuiDialog.show = true;
            this.shanHuiDialog.order = order;
        },
        reminder : function () {
            toastr.clear();
            var that = this;
            if (that.refundList == null || that.refundList.length == 0){
                toastr.error("请选择要催单的菜品");
                return;
            }
            // alert(JSON.stringify(that.refundList));
            if (that.selectedType == 0 || that.selectedType == 10 || that.selectedType == 11 || that.selectedType == 12){
                toastr.error("您选择的是服务费，请重新选择");
                return;
            }
            var selectIds = "";
            var orderId = "";
            for (var index in that.refundList){
                selectIds = selectIds + that.refundList[index].id + ",";
                orderId = that.refundList[index].orderId;
            }
            // alert(selectIds);
            if (confirm("是否确认催菜？")){
                reminder(selectIds, orderId, function (result) {
                    if (result.success){
                        if (result.data.length > 0) {
                            toastr.success("催菜成功，正在打印菜品信息");
                            if(that.shop.isPosNew == 1){
                                for (var i = 0; i < result.data.length; i++) {
                                    printPlusNew(result.data[i]);
                                }
                            }else{
                                printPlus(result.data, function () {
                                }, function (msg) {
                                    toastr.clear();
                                    toastr.error(msg);
                                });
                            }

                        }else{
                            toastr.error("催菜成功，但无打印信息请检查菜品设置");
                        }
                    }else{
                        toastr.error("催菜失败");
                    }
                });
            }
        },
        orderSettlement: function (order) {
            var that = this;
            posPayOrderGetCustomer(order.id, function (result) {
                that.orderSettlementDialog.show = true;
                that.orderSettlementDialog.oldorder = order;
                that.orderSettlementDialog.order = result.order;
                that.orderSettlementDialog.brandsetting = that.brandSetting;
                that.orderSettlementDialog.shop = that.shop;
                that.orderSettlementDialog.orderlist = that.orderlist;
                that.orderSettlementDialog.payvalue = result.payValue;
                that.orderSettlementDialog.remainvalue = result.remainValue;
                that.orderSettlementDialog.couponvalue = result.couponValue;
                that.orderSettlementDialog.usecouponvalue = result.useCouponValue;
                if (result.couponValue > 0) {
                    that.orderSettlementDialog.couponid = result.couponId;
                }
            });
        },
        changePayMode: function (o) {
            var that = this;
            changeMode(o.id, function () {
                toastr.success("支付方式变更成功");
                that.orderlist = [];
                that.getPosOrder();
            });
        },
        printOrderTicket: function (oid) {
            var that = this;
            getPrinterByType(2, function (printers) {
                console.log("打印小票", oid);
                if (printers.length == 1) {
                    console.log("11111111111111");
                    that.printTicketBase(oid, printers[0].id);
                } else if (printers.length > 1) {
                    console.log("222222222222222");
                    that.choicePrinterDialog.show = true;
                    that.choicePrinterDialog.printers = printers;
                    that.choicePrinterDialog.oid = oid;
                } else {
                    toastr.error("没有找到前台打印机，请检查后台是否配置正确");
                }
            });

        },
        printKitchenTicket: function (oid) {
            var posNew = this.shop.isPosNew;


            // if (isPrintSupport()) {
            toastr.info("正在打印...");
            if (this.outFood) {
                printHungerKitchenReceipt(oid, function (result) {
                    if (result.success) {
                        if (posNew == 1) {
                            for (var i = 0; i < result.data.length; i++) {
                                printPlusNew(result.data[i],function(){},function(){},"delivery");
                            }

                        } else {
                            printPlus(result.data);
                        }

                        // toastr.success("打印订单成功");
                    } else {
                        toastr.error(result.message);
                    }
                });
            } else {
                printKitchenReceipt(oid, function (result) {
                    if (result.success) {
                        if (posNew == 1) {
                            for (var i = 0; i < result.data.length; i++) {
                                printPlusNew(result.data[i]);
                            }

                        } else {
                            printPlus(result.data);
                        }

                        // toastr.success("打印订单成功");
                    } else {
                        toastr.error(result.message);
                    }
                });
            }

            // } else {
            //     toastr.error("不支持打印插件！");
            // }

        },
        printTicketBase: function (oid, printerId) {
            var pos = this.shop.isPosNew;
            if (this.outFood) {
                printHungerReceipt(oid, printerId, function (task) {
                    if (pos == 1) {
                        printPlusNew(task, function () {
                            toastr.success("开始打印...");

                        }, function (msg) {
                            toastr.error(msg);
                        });
                    } else {
                        printPlus([task], function () {
                            toastr.success("打印成功");
                        }, function (msg) {
                            toastr.error(msg);
                        });
                    }
                });

            } else {

                printReceipt(oid, printerId, function (task) {
                    if (pos == 1) {
                        printPlusNew(task, function () {
                            toastr.success("开始打印...");
                        }, function (msg) {
                            toastr.error(msg);
                        });
                    } else {
                        printPlus([task], function () {
                            toastr.success("打印成功");
                        }, function (msg) {
                            toastr.error(msg);
                        });
                    }

                });


            }

        },
        callNumber: function (o) {
            var that = this;
            try {
                $.post("order/getOrderItems", {"orderId": o.id}, function (result) {
                    var articleItem = [];

                    for (var i = 0; i < result.data.length; i++) {
                        var item = {
                            articleName: result.data[i].articleName.length > 10 ? result.data[i].articleName.substring(0,9) + "..." : result.data[i].articleName,
                            count: result.data[i].count
                        }
                        articleItem.push(item);
                    }
                    console.log(articleItem);
                    var data = {
                        type: "call",
                        code: o.verCode,
                        orderId: o.id,
                        data: articleItem,
                        createTime: new Date()
                    }
                    callOrderTask.push(data);
                })
            } catch (exception) {
                var data = {
                    type: "call",
                    code: o.verCode,
                    orderId: o.id,
                    data: null
                }
                callOrderTask.push(data);
            }

            if (that.canCallOrder) {
                that.canCallOrder = false;
                setTimeout(function () {
                    that.canCallOrder = true;
                }, 5000);
                callNumberRequest(o.id, function (result) {
                    if (result.success) {
                        toastr.success("叫号成功");
                        $("#" + o.id + "CancelBtn").attr("disabled", "disabled");
                        $("#" + o.id).addClass("success");
                        if (!that.hasCalled) {
                            setTimeout(function () {
                                //一分钟后移除此订单
                                that.orderlist.$remove(o);
                            }, 30000);
                        }
                    } else {
                        toastr.error(result.message);
                    }
                });
            }

            //}
        },
        keysert: function (key, desc) {
            return function (a, b) {
                return desc ? ~~(a[key] < b[key]) : ~~(a[key] > b[key]);
            }
        },
//      cleanKeyWord: function () {
//          this.keyWord = "";
//      },
        cutChangeNumber: function () {
            if (this.selectedNumber > 0) {
                this.selectedNumber = this.selectedNumber - 1;
            }

        },
        addChangeNumber: function () {
            this.selectedNumber = parseInt(this.selectedNumber) + 1;

        },
        changeOrderItem: function () {
            if(this.selectedItem != null && this.selectedItem.type == 3){
                toastr.error("套餐不可修改");
                return;
            }
            if (this.selectedId == null) {
                toastr.error("请选择要修改的订单项");
            } else {
                this.editChange = true;
                this.selectedNumber = $('#' + this.selectedId + " span:eq(1)").text();
                this.baseSelectedNumber = this.selectedNumber;
            }

        },
        refundArticle: function (order) {
            var that = this;
            if (that.refundList.length == 0) {
                toastr.error("请选择要退的菜品");
                return;
            }

            if (order.distributionModeId == 1) {
                that.refundArticleModelDialog.tangshi = true;
            } else if (order.distributionModeId == 3) {
                that.refundArticleModelDialog.tangshi = false;
            }
            that.refundArticleModelDialog.refundList = that.refundList;
            that.refundArticleModelDialog.show = true;
            that.refundArticleModelDialog.order = order;

        },
        getDiscount: function(order){
            this.posDiscountDialog.show = true;
            this.posDiscountDialog.order = order;
        },
        turnTable: function (order) {
            var that = this;

            if (order.distributionModeId == 1) {
                that.turnTableModelDialog.tangshi = true;
            } else if (order.distributionModeId == 3) {
                that.turnTableModelDialog.tangshi = false;
            }
            that.turnTableModelDialog.shop = that.shop;
            that.turnTableModelDialog.show = true;
            that.turnTableModelDialog.order = order;

        },
        addArticle: function (order) {
            this.addArticleDialog.show = true;
            this.addArticleDialog.order = order;
        },
        selectServicePrice: function (obj, order, v) {
            if (this.unPrint) {
                return;
            }
            var id = obj.id;
            var orderId = order.id;
            if (v == 1 && obj.type == 4) {//如果选择的是菜品并且是套餐子项直接return
                return;
            }

            // if(order.orderState == 1 && order.payMode != 3 && order.payMode != 4
            //   && v== 1 && obj.type == 3){
            //     return;
            // }


            if (this.editChange) { //如果是编辑菜品直接return
                return;
            }

            var basePrice;
            var item = order.orderItems;
            if (obj.type == 3) {
                basePrice = obj.unitPrice;
                for (var i = 0; i < item.length; i++) {
                    if (item[i].parentId == obj.id) {
                        obj.unitPrice += item[i].unitPrice;
                    }
                }
            }
            //要退菜的对象
            var unitPriceNow = obj.unitPrice;
            if(obj.type == 0 && order.memberDiscount != null){
                unitPriceNow = obj.unitPrice * order.memberDiscount;
            }
            var refund = {
                id: id,
                orderId: orderId,
                type: v,
                count: obj.count,
                maxCount: obj.count,
                unitPrice: unitPriceNow
            };
            if (v == 0 || v == 10 || v == 11 || v == 12){ //如果服务费
                refund.name = obj.name;
                refund.mealFeeNumber = 0;
            }else{ //菜品
                refund.name = obj.articleName;
                refund.mealFeeNumber = obj.mealFeeNumber == null ? 0 : obj.mealFeeNumber;
            }

            if (obj.type == 3) { //如果是套餐类型
                obj.unitPrice = basePrice;
            }


            for (var i = 0; i < this.refundList.length; i++) { //循环退菜集合中的数据，如果产生重复则从集合中删除
                if (this.refundList[i].id == id) {
                    this.refundList.$remove(this.refundList[i]);
                }
            }
            if ($('#' + id + " input").prop("checked")) { //如果原来的选中的则改为不选中
                $('#' + id + " input").prop("checked", false);

            } else {//如果原来的没选中的则将他改为选中， 并且如果退菜数量大于0则将其加入到退菜集合
                $('#' + id + " input").prop("checked", true);
                if (refund.count > 0) {
                    this.refundList.push(refund);
                }

            }

            $('#articleBody p').css("background-color", "#FFFFFF")
            $('#' + id).css("background-color", "#e5e5e5");
            this.selectedId = id;
            this.selectedItem = obj;
            this.selectedOrderId = orderId;
            if (v == 1) { //是菜品
                this.selectedType = v;
            } else{ //服务费
                this.selectedType = v;
            }
            //$(item).css("background-color", "#e5e5e5").siblings().css("background-color", "#FFFFFF");
        },
        cancelChange: function () {
            this.editChange = false;
            //this.selectedId = null;
            this.selectedNumber = 0;
        },
        saveChangeOrder: function () {
            var that = this;
            if (confirm("保存后不可恢复，请谨慎操作")) {

                if (that.selectedNumber == that.baseSelectedNumber){ //如果编辑前后值未发现改变
                    toastr.clear();
                    toastr.error("编辑前后菜品数量一致");
                    that.editChange = false;
                    that.selectedNumber = 0;
                    that.baseSelectedNumber = 0;
                    return;
                }

                if ((that.selectedType == 0 || that.selectedType == 10 || that.selectedType == 11 || that.selectedType == 12) && that.selectedNumber == 0) { //要修改的是服务费
                    toastr.error("额外费用不可为空");
                    that.editChange = false;
                    //this.selectedId = null;
                    that.selectedNumber = 0;
                    return;
                }

                saveChange(that.selectedOrderId, that.selectedNumber, that.selectedId, that.selectedType, function (result) {
                    if (result.success) {
                        getOrderInfo(that.currentOrderId, function (order) {
                            that.order = order;
                            // if (that.order.orderMode == 5) { //如果是后付模式
                            getChildItem(that.currentOrderId, function (result) {
                                that.childList = [];
                                that.childList = result;
                                that.editChange = false;
                                //this.selectedId = null;
                                that.selectedNumber = 0;
                                that.orderlist = [];
                                that.getPosOrder();
                            });
                        });
                        toastr.info("修改成功");
                        if (result.data != null) {
                            printPlus(result.data, function () {
                                toastr.success("打印成功");
                            }, function (msg) {
                                toastr.error(msg);
                            });
                        }
                    } else {
                        toastr.error(result.message);
                        that.editChange = false;
                        //this.selectedId = null;
                        that.selectedNumber = 0;
                    }
                });
            }
        },

        reportHeartbeat: function () {
            var that = this;
            checkPlatformSetting(function (result) {
                if (result.success) {
                    that.reportHealth();
                }
            });

        },
        reportHealth: function () {
            var that = this;
            reportHeartbeat();
            setTimeout(function () {
                that.reportHealth();
            }, 30000);
        },
        showOrderInfo: function (oid) {
            Vue.nextTick(function () {
                var choiceId = "#" + oid
                //设置当前tr为灰色并设置其他tr为另一种颜色
//                $(choiceId).css("background-color", "#e5e5e5").siblings().css("background-color", "#FFFFFF");
                $(choiceId).css("background-color", "#e5e5e5").siblings().css("background-color", "#FFFFFF");
            })
            var that = this;
            that.currentOrderId = oid;
            if (!that.outFood) {
                getOrderInfo(oid, function (order) {
                    that.order = order;
                    // if (that.order.orderMode == 5) { //如果是后付模式

                    getChildItem(oid, function (result) {
                        that.childList = [];
                        that.childList = result;
                    });
                    // }
                });


            } else {
                getOutFoodInfo(oid, function (order) {
                    that.order = order;
                });
            }
            that.selectedId = null;
            that.refundList = [];

        },
        startGetNewOrder: function () {
            console.log("开始连接云巴服务");
            var that = this;
            getServerIp(function(result){
                console.log("得到云巴相关配置：" + JSON.stringify(result));
                that.localInfo = result;
                that.connWebsock();
            })

        },
        startTV: function () {
            var that = this;
            that.connTVWebsock();

        },
        removePaySuccessOrder: function (order) {
            //模式5(后付款模式),从订单列表中移除已支付的订单
            var removeList = [];
            for (var i in this.orderlist) {//查找当前订单
                var o = this.orderlist[i];
                if (order.id == o.id) {
                    removeList.push(o);
                }
            }
            this.order = null;
            if (order.parentOrderId) {
                for (var i in this.orderlist) {
                    var o = this.orderlist[i];
                    if (order.parentOrderId == o.id) {//查找 父 订单
                        removeList.push(o);
                    }
                    if (order.parentOrderId == o.parentOrderId && order.id != o.id) {//查找 同级 追加的 订单,不包含当前订单
                        removeList.push(o);
                    }
                }
            } else {
                for (var i in this.orderlist) {
                    var o = this.orderlist[i];
                    if (order.id == o.parentOrderId) {//查找子订单
                        removeList.push(o);
                    }
                }
            }
            console.log("要移除的订单集合：", removeList);
            for (var i in removeList) {//移除
                console.log("移除的当前订单", removeList[i].verCode, removeList[i]);
                this.orderlist.$remove(removeList[i]);
            }

        },
        getOrderFamily: function (order) {//从当前订单列表中获取订单相关联的   this.orderlist
            var orderFamily = {};
            orderFamily.parent = order;//将当前订单默认设置为父订单
            var children = [];
            if (order.parentOrderId) {
                for (var i in this.orderlist) {
                    var o = this.orderlist[i];
                    if (order.parentOrderId == o.id) {//查找 父 订单
                        orderFamily.parent = o;
                    }
                    if (order.parentOrderId == o.parentOrderId) {//查找 同级 追加的 订单，包含当前订单
                        children.push(o);
                    }
                }
            } else {
                for (var i in this.orderlist) {
                    var o = this.orderlist[i];
                    if (order.id == o.parentOrderId) {//查找子订单
                        children.push(o);
                    }
                }
            }
            orderFamily.children = children;
            return orderFamily;
        },
        connWebsock: function () {
            console.log("开始连接YB服务器");
            //初始换连接信息
            var customerId = this.localInfo.customerId;
            var appKey = this.localInfo.appKey;
            var that = this;
            //创建云巴实例
            var yunba = new Yunba({server: 'http://sock.yunba.io', port: 3000, appkey: appKey});
            //连接云巴Socket
            yunba.init(function (success) {
                console.log("连接云巴Socket" + (success ? "成功" : "失败"));
                if (success) {
                    //连接socket成功，连接消息服务器
                    that.connectByCustomId(yunba, customerId);
                }
            }, function () {
                console.log("与云巴Socket断开连接");
                //连接断开，重新连接
                that.resultCheck(false);
            });
        },
        connectByCustomId : function (yunba, customerId) {
            var that = this;
            yunba.connect_by_customid(customerId, function (success, msg, sessionId) {
                console.log("连接云巴消息服务器"+ (success ? "成功，sessionId："+ sessionId +"" : "失败，" +
                    "错误信息："+ msg +"" +""));
                if (success) {
                    //设置当前设备别名，每个店铺在每个集群的别名是唯一的
                    yunba.set_alias({'alias': customerId}, function (data) {
                        console.log("设置别名："+ customerId +""+ (data.success ? "成功" : "失败，错误信息："+ data.msg +"" +""));
                    });
                    //订阅当前别名
                    yunba.subscribe({'topic': customerId}, function (success, msg) {
                        console.log("订阅别名：" + customerId + ""+ (success ? "成功" : "失败，错误信息："+ msg +"" +""));
                    });
                    //订阅公共频道
                    yunba.subscribe({'topic': that.localInfo.publicTopic}, function (success, msg) {
                        console.log("订阅公共频道：" + that.localInfo.publicTopic + ""+ (success ? "成功" : "失败，错误信息："+ msg +"" +""));
                    });
                    //查询R+通讯服务器在线状态
                    that.getYunBaServerAlias(yunba);
                    //云巴接收到消息
                    yunba.set_message_cb(function (data) {
                        console.log("收到推送的消息" + JSON.stringify(data));
                        if (data.presence != null) {
                            that.presenceMessage(data.presence);
                        } else{
                            that.setMessageCb(data.msg);
                        }
                    });
                }
            });
        },
        getYunBaServerAlias : function (yunba) {
            var that = this;
            //获取到当前集群公共频道下所有的别名用户
            yunba.get_alias_list(that.localInfo.publicTopic, function (success, data) {
                if (success) {
                    console.log("获取到公共：" + that.localInfo.publicTopic + "下的用户-->" + JSON.stringify(data))
                    data.alias.forEach(function (alias) {
                        //获取到R+通讯服务器的别名
                        if (alias.indexOf(that.localInfo.publicTopic) != -1) {
                            console.log("R+通讯服务器：" + alias)
                            that.aliasList.push(alias);
                        }
                    });
                    if (that.aliasList.length == 0){
                        that.resultCheck(false);
                        console.log("未查找到当前公共频道：" + that.localInfo.publicTopic + "存在对应的R+服务器，设备设置掉线");
                    }else {
                        for (var index in that.aliasList) {
                            that.subscribePresence(yunba, that.aliasList[index]);
                            //检查当前R+服务器在线状态
                            yunba.get_state(that.aliasList[index], function (data) {
                                if (data.success) {
                                    if (data.data == "online" && that.loginBegin) {
                                        that.onlineServer.push(that.aliasList[index]);
                                    } else if (data.data != "online" && data.data != "join" && that.loginBegin) {
                                        that.offlineServer.push(that.aliasList[index]);
                                    }
                                }
                                //检查完成
                                if ((index + 1) == that.aliasList.length) {
                                    //至少保证有一台R+通讯服务器在线, 否则断开连接
                                    if (that.onlineServer.length > 0) {
                                        that.resultCheck(true);
                                    } else {
                                        that.resultCheck(false);
                                    }
                                    that.loginBegin = false;
                                    console.log("在线服务器：" + that.onlineServer.join(",") + "---"
                                        + "离线服务器：" + that.offlineServer.join(","));
                                    that.aliasList = [];
                                }
                                console.log("查询R+服务器在线状态：" + JSON.stringify(data));
                            });
                        }
                    }
                } else {
                    //重新连接
                    that.resultCheck(false);
                    console.log("获取别名信息出错：" + data.error_msg);
                }
            });
        },
        presenceMessage : function (presence) {
            //接收到实时消息，所监听的R+通讯服务器有上下线变化
            if (presence.action == "online") {
                console.log("设备：" + presence.alias + "上线");
                this.onlineServer.push(presence.alias);
                this.offlineServer = arrayRemove(this.offlineServer, presence.alias);
            }else if (presence.action == "offline") {
                console.log("设备：" + presence.alias + "离线");
                this.offlineServer.push(presence.alias);
                this.onlineServer  = arrayRemove(this.onlineServer, presence.alias);
            }
            //至少保证有一台R+通讯服务器在线, 否则断开连接
            if (this.onlineServer.length > 0) {
                this.resultCheck(true);
            }else{
                this.resultCheck(false);
            }
        },
        setMessageCb : function (msg) {
            //接收到推送消息
            var data = JSON.parse(msg);
            if (data.dataType == "current") {//后台推送的 实时订单
                console.log("接受到了后台发送的新增订单的消息" + data);
                if (data.orderMode == 6) {
                    this.orderlist = [];
                    this.getPosOrder();
                } else {
                    if(data.distributionModeId!=2){
                        this.orderlist.push(data);
                    }
                }
                this.orderlist.sort(this.keysert('serialNumber', false))//排序，新订单显示在最下面
                this.printTask.push(data);
                this.refreshIScroll();
            } else if (data.dataType == "confirm") {
                console.log("接收到后台推送的现金、银联、积分、闪惠支付订单：" + data);
                //先付模式下选择现金、银联、积分、闪惠确认后再执行打单
                if (data.orderState == 1) {
                    this.orderlist.push(data);
                }else if (data.orderState == 2) {
                    this.printTask.push(data);
                }else {
                    this.printTask.push(data);
                }
            } else if (data.dataType == "platform") { //第三方订单
                console.log("接收到外卖订单消息:" + data)
                this.printPlatformTask.push(data);
            } else if (data.dataType == "money") {
                $('#orderCount').text(data.orderCount);
                $('#orderTotal').text(data.orderTotal == null ? 0 : data.orderTotal);
            }else if(data.dataType == "receipt") {
                console.log("返回发票管理打印类型:"+data.dataType);
                $.post("order/receiptOrderPosPrints",{orderNumber:data.orderNumber},function (result) {
                    if (result.success){
                        printPlus(result.data,function () {
                        }, function (msg) {
                            toastr.clear();
                            toastr.error(msg);
                        });
                    }
                });
                $.post("order/getPosReceiptList",{orderNumber:data.orderNumber},function(result){
                    Hub.$emit('change', data);
                });
            }else if (data.dataType == "badAppraisePrintOrder") {
                //接收到差评订单
                badAppraisePrintOrder(data.id, function (data) {
                    printPlus(data);
                    toastr.success("差评订单已打印");
                }, function (errorMessage) {
                    //提示错误信息
                    toastr.error(errorMessage);
                });
            }
        },
        subscribePresence : function (yunba, topic) {
            yunba.subscribe_presence({'topic': topic}, function (success, msg) {
                console.log("监听设备：" + topic + ""+ (success ? "成功" : "失败，错误信息："+ msg+"" +""));
            });
        },
        resultCheck : function (success) {
            if (success) {
                toastr.clear();
                toastr.info("连接服务器成功");
                $("#conn_ok").show();
                $("#conn_break").hide();
                this.socketConnTimes = 5;
                this.MsgModalDialog.show = false;
            } else {
                //操作出错，此时让pos重新连接
                if (this.socketConnTimes > 0) {
                    this.socketConnTimes--;
                    this.loginError(1);
                    this.wsOnclose();
                } else {
                    //自动连接数超过5则连接超时
                    this.loginError(2);
                }
            }
        },
        loginError : function (type) {
            this.MsgModalDialog.show = true;
            var msg = "<span>";
            var title = "断线提醒";
            if (type == 1){
                //type为1则表示弹出自动重连的model
                msg = msg + "您已断开连接，系统正在自动重连...</span><br/><span>若长时间连接失败，请手动重新登录。";
            }else{
                //type为2则表示弹出重新登录的model
                msg = msg + "<a href='" + $("base").attr("href") + "'>连接超时，请重新登录</a></span><br/><span>";
                title = "连接失败";
            }
            msg = msg + "</span><br/>";
            this.MsgModalDialog.title = title;
            this.MsgModalDialog.msg = msg;
            $("#conn_ok").hide();
            $("#conn_break").show();
        },
        connTVWebsock: function () {
            var that = this;
            if(that.shop.tvIp == null && that.shop.tvIp == ''){
                return;
            }
            $('#tvState').css("display","");
            $('#tvStatus').css("display","");
            $('#tvStatus').text("未连接");
            try{
                // var readyState = new Array("正在连接", "已建立连接", "正在关闭连接"
                //     , "已关闭连接");
                var host = "ws://"+that.shop.tvIp+":8000";
                that.websocket = new WebSocket(host);
                that.websocket.onopen = function(){
                    console.log("连接成功------");
                    var orderNew = [];
                    if(that.orderlist != null && that.orderlist.length > 0){
                        for(var oo = 0; oo < that.orderlist.length; oo++){
                            var o = {
                                orderId: that.orderlist[oo].id,
                                code: that.orderlist[oo].verCode
                            };
                            if(that.orderlist[oo].productionStatus < 3){
                                orderNew.push(o);
                            }
                        }
                    }
                    var data = {
                        type : "open",
                        orderNew: orderNew,
                        shopTvConfig : that.shopTvConfig
                    }
                    that.sendTvMsg(JSON.stringify(data));
                    $('#tvStatus').text("已连接");
                    // msg.innerHTML += "<p>Socket状态： " + readyState[websocket.readyState] + "</p>";
                }
                that.websocket.onmessage = function(event){
                    var that = this;
                    console.log("接收到消息");
                    var msg = JSON.parse(event.data);
                    console.log(JSON.stringify(msg));
                    if (msg.type == "scan") {//后台推送的 实时订单
                        $.post("order/getOrderItemsBySerialNumber", {"serialNumber": msg.data}, function (result) {
                            console.log(JSON.stringify(result));
                            var articleItem = [];

                            for (var i = 0; i < result.data.length; i++) {
                                var item = {
                                    articleName: result.data[i].articleName.length > 10 ? result.data[i].articleName.substring(0,9) + "..." : result.data[i].articleName,
                                    count: result.data[i].count
                                }
                                articleItem.push(item);
                            }
                            console.log(articleItem);
                            var data = {
                                type: "call",
                                code: result.data[0].verCode,
                                orderId: result.data[0].orderId,
                                data: articleItem,
                                createTime: new Date()
                            }
                            callOrderTask.push(data);
                        })
                    }
                    // msg.innerHTML += "<p>接收信息： " + event.data + "</p>";
                }
                that.websocket.onclose　= function(){
                    $('#tvStatus').text("断开连接");

                    console.log("关闭")
                    // msg.innerHTML += "<p>Socket状态： " + readyState[websocket.readyState] + "</p>";
                }
                that.websocket.onerror　= function(){
                    $('#tvStatus').text("断开连接");
                    console.log("关闭")

                    // msg.innerHTML += "<p>Socket状态： " + readyState[websocket.readyState] + "</p>";
                }
                // msg = document.getElementById("msg");
                // msg.innerHTML += "<p>Socket状态： " + readyState[websocket.readyState] + "</p>";
            }catch(exception){

                $('#tvStatus').text("断开连接");

                // msg.innerHTML += "<p>有错误发生</p>";
            }
        },
        sendTvMsg: function (txt) {
            if (this.websocket != null) {
                this.websocket.send(txt);

            }

        },
        wsOnopen: function () {
            $("#conn_ok").show();
            $("#conn_break").hide();
            this.MsgModalDialog.show = false;
            console.log("开始连接");
            var data = this.getJsonData("check", null);
            this.ws.send(data);
            console.log("链接成功");
            toastr.info("连接服务器成功");
            //每隔 30s 进行心跳检测
            this.wsHeartbeatId = window.setInterval(this.wsHeartbeat, 30000);
        },
        wsOnmessage: function (event) {
            var data = JSON.parse(event.data);
            console.log(JSON.stringify(data));
            if (data.dataType == "current") {//后台推送的 实时订单
                console.log("接受到了后台发送的新增订单的消息" + data.id);
                if (data.orderMode == 5 && data.parentOrderId != null) {
                    this.orderlist = [];
                    this.getPosOrder();
                } else if (data.orderMode == 6) {
                    this.orderlist = [];
                    this.getPosOrder();
                } else {
                    if(data.distributionModeId!=2){
                        this.orderlist.push(data);
                    }
                }
                this.orderlist.sort(this.keysert('serialNumber', false))//排序，新订单显示在最下面
                this.printTask.push(data);
                this.refreshIScroll();
            } else if (data.dataType == "nopay") {
                if (data.orderState == 1 && data.productionStatus != 2) {
                    this.orderlist.push(data);
                } else if (data.orderState == 1 && data.productionStatus == 2) {
                    this.printTask.push(data);
                } else if (data.orderState == 2) {
                    this.printTask.push(data);
                }
            } else if (data.dataType == "history") {//后台推送的 历史订单
//                console.log(data.orders);
//                for(var i in data.orders){
//                	this.pushOrderToHistoryList(data.orders[i]);
//                }
                if (this.modeHouFu) {//如果是后付款模式，则修改标题为  【未打印订单】
                    this.historyOrdersDialog.title = "未打印订单";
                }
                this.historyOrdersDialog.historyorders = data.orders;
                this.historyOrdersDialog.show = true;
            } else if (data.dataType == "command") {//后台推送指令
                this.executeCommand(data.operation);
            } else if (data.dataType == "paySuccess" && this.modeHouFu) {//模式5(后付款模式)订单支付后，移除pos端的订单列表
                this.removePaySuccessOrder(data.orders[0]);
            } else if (data.dataType == "platform") { //第三方订单
                this.printPlatformTask.push(data);
            } else if (data.dataType == "money") {
                $('#orderCount').text(data.orderCount);
                $('#orderTotal').text(data.orderTotal == null ? 0 : data.orderTotal);
            }else if(data.dataType == "receipt") {
                console.log("返回发票管理打印类型:"+data.dataType);
                $.post("order/receiptOrderPosPrints",{orderNumber:data.orderNumber},function (result) {
                    if (result.success){
                        printPlus(result.data,function () {
                            // toastr.success("发票打印成功");
                        }, function (msg) {
                            toastr.clear();
                            toastr.error(msg);
                        });
                    }
                });
                $.post("order/getPosReceiptList",{orderNumber:data.orderNumber},function(result){
                    Hub.$emit('change', data);
                });

            }else if (data.dataType == "badAppraisePrintOrder") {
                //接收到差评订单
                badAppraisePrintOrder(data.id, function (data) {
                    printPlus(data);
                    toastr.success("差评订单已打印");
                }, function (errorMessage) {
                    //提示错误信息
                    toastr.error(errorMessage);
                });
            }
        },
        wsOnclose: function () {
            // if (!this.MsgModalDialog.show) {//如果当前已被挤掉，则不再进行重新连接
            this.onlineServer = [];
            this.offlineServer = [];
            toastr.info("与服务器的连接断开,正在自动重连，剩余连接次数：" + this.socketConnTimes);
            window.setTimeout(this.connWebsock, 3000);
            // }
        },
        wsOnerror: function () {
            window.setTimeout(this.connWebsock, 1000);
            console.log("ws出错");
            //当报错时，停止心跳检测
            window.clearInterval(this.wsHeartbeatId);
        },
        wsHeartbeat: function () {//心跳
            this.ws.send(this.getJsonData("heartbeat", null));
        },
        ping: function () {
            var that = this;
            $.ajax({
                type: "POST",
                url: "ping",
                timeout: 3000,
                success: function (result) {
                    console.log("ping---成功")
                    console.log(result);
                    if (result == null || result == "") {//如果返回的没有值，则自动登录
                        that.xlogin();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("ping --失败");
                    setTimeout(function () {
                        that.ping();
                    }, 2000);
                }
            });
        },
        xlogin: function () {
            var that = this;
            var data = {
                "loginname": localStorage.getItem("userName"),
                "password": localStorage.getItem("userPwd")
            };
            $.ajax({
                type: "POST",
                url: "xlogin",
                data: data,
                success: function (result) {
                    console.log("登录成功：" + result);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    //登录失败，跳转到登录页面
                    window.location.href = $("base").attr("href");
                }
            });
        },
        executeCommand: function (operation) {
            if ("exit" == operation) {
                this.MsgModalDialog.show = true;
                this.MsgModalDialog.title = "提示";
                this.MsgModalDialog.msg = "当前账号已在其他客户端登录！请退出客户端！";
                this.loginOther = true;
            }
        },
        getJsonData: function (type, data) {
            var dataAPI = {};
            dataAPI.shopId = shopId;
            dataAPI.client = "pos";
            dataAPI.type = type;
            dataAPI.data = data;
            console.log(brandName);
            console.log(shopName);
            dataAPI.brandName = brandName;
            dataAPI.shopName = shopName;
            dataAPI.brandId = brandId;
            return JSON.stringify(dataAPI);
        },

        setTaskIntoPrinter: function (task) {
            var that = this;
            for (var i = 0; i < task.length; i++) {
                for (var k = 0; k < that.printerList.length; k++) {
                    if (task[i].TICKET_TYPE == "RestaurantLabel" || task[i].TICKET_TYPE == "DeliveryLabel") {
                        //如果是贴纸
                        if (task[i].PORT == that.printerList[k].port) {
                            that.printerList[k].taskList.push(task[i]);
                        }
                    } else {
                        if (task[i].IP == that.printerList[k].ip) {

                            that.printerList[k].taskList.push(task[i]);
                        }
                    }
                }
            }


        },
        printPrinterOrder: function () {
            var that = this;
            for (var i = 0; i < that.printerList.length; i++) {
                if (that.printerList[i].ticketType == 1) {
                    that.taskPrinting(that.printerList[i].taskList, 4000, "label");
                } else {
                    that.taskPrinting(that.printerList[i].taskList, 500, "ticket");

                }

            }
        },
        taskPrinting: function (tt, time, type) {
            var that = this;
            setInterval(function () {
                var task = tt.shift();
                if (task) {

                    var result;
                    if (type == "ticket") {
                        if(that.shop.isPosNew == 1){
                            printPlusNew(task, function () {
                                toastr.success("开始打印...");

                            }, function (msg) {
                                toastr.error(msg);
                            });
                        }else{
                            result = callbackObj.printTicket(JSON.stringify(task));
                        }

                    } else {
                        if(that.shop.isPosNew == 1){
                            printPlusNew(task, function () {
                                toastr.success("开始打印...");

                            }, function (msg) {
                                toastr.error(msg);
                            });
                        }else{
                            result = callbackObj.printLabel(JSON.stringify(task));
                        }

                    }

                    if (!result) {
                        that.errorTask.push(task);
                    }
                }
            }, time);
        },
        startErrorTask:function(){
            var that = this;
            var task = that.errorTask.shift();
            if(task){
                var result;
                if (type == "ticket") {
                    if(that.shop.isPosNew == 1){
                        printPlusNew(task, function () {
                            toastr.success("开始打印...");

                        }, function (msg) {
                            toastr.error(msg);
                        });
                    }else{
                        result = callbackObj.printTicket(JSON.stringify(task));
                    }

                } else {
                    if(that.shop.isPosNew == 1){
                        printPlusNew(task, function () {
                            toastr.success("开始打印...");

                        }, function (msg) {
                            toastr.error(msg);
                        });
                    }else{
                        result = callbackObj.printLabel(JSON.stringify(task));
                    }

                }

                if (!result) {
                    that.errorTask.push(task);
                }
            }
            setTimeout(function () {
                that.startErrorTask();
            }, 3000);
        },
        startPrinterTask: function () {
            var task = this.printTask.shift();
            var platFormTask = this.printPlatformTask.shift();
            var that = this;
            if (task) {
                printOrderAll(task.id, function (printData) {
                    if (printData != null) {
                        that.setTaskIntoPrinter(printData);
                        printSuccess(task.id, function (result) {
                            if (result.success) {
                                that.orderlist.sort(that.keysert('serialNumber', true))//排序，新订单显示在最上面
                                toastr.success("打印成功");
                            }
                        });
                        if (that.shop.shopMode == 2) {
                            getOrderInfo(task.id, function (order) {
                                if(order.distributionModeId != 2){
                                    var data = {
                                        type: "new",
                                        code: order.verCode,
                                        orderId: order.id,
                                        data: null
                                    }
                                    that.sendTvMsg(JSON.stringify(data));
                                }
                            });
                        }
                    } else {
                        toastr.error("未找到订单项");
                    }
                });

            }

            if (platFormTask) {
                printPlatformOrderAll(platFormTask.id, platFormTask.type, function (printData) {
                    if (printData != null) {
                        that.setTaskIntoPrinter(printData);
                    } else {
                        toastr.error("未找到订单项");
                    }
                    printUpdate(platFormTask.id, function (result) {
                        if (result.success) {
                            console.log("更新production_status,外卖模式设置为4")
                        }
                    });
                });

            }

            setTimeout(function () {
                that.startPrinterTask();
            }, 3000);
        },
        startCallOrder: function () {
            var that = this;
            var task = callOrderTask.shift();
            if(task){
                that.sendTvMsg(JSON.stringify(task));
                setTimeout(function () {
                    that.sendTvMsg(JSON.stringify(task));
                }, 5000);
                setTimeout(function () {
                    that.startCallOrder();
                }, 10000);
            }else{
                setTimeout(function () {
                    that.startCallOrder();
                }, 1000);
            }
        },
        startGetErrorOrder: function () {
            var that = this;
            this.errorOrderTask = setTimeout(function () {
                listErrorOrder(function (data) {
                    $(data).each(function (i, item) {
                        var check = true;
                        if(item.distributionModeId==2){
                            for (var i = 0; i < that.printPlatformTask.length; i++) {
                                if (that.printPlatformTask[i].id == item.id) {
                                    check = false;
                                }
                            }
                            if (check) {
                                that.printPlatformTask.push(item);
                            }
                        }else{
                            for (var i = 0; i < that.printTask.length; i++) {
                                if (that.printTask[i].id == item.id) {
                                    check = false;
                                }
                            }
                            if (check) {
                                that.printTask.push(item);
                            }
                        }
                        var history = that.historyOrdersDialog.historyorders;
                        for (var i = 0; i < history.length; i++) {
                            if (history[i].id == item.id) {
                                that.historyOrdersDialog.historyorders.$remove(history[i]);
                            }
                        }

                    })
                });
                listPlatFormErrorOrder(function (data){
                    $(data).each(function (i, item) {
                        var check = true;
                        for (var i = 0; i < that.printPlatformTask.length; i++) {
                            if (that.printPlatformTask[i].id == item.id) {
                                check = false;
                            }
                        }
                        if (check) {
                            that.printPlatformTask.push(item);
                        }
                    })
                });
                that.startGetErrorOrder();
            }, 40000);
        }
    }
};

var printMix = {
    template: '<shop-modal :show.sync="shopPrinterDialog.show"></shop-modal>'
    + '<h2 class="text-center"><strong>结算报表</strong></h2><br/>'
    + '<div class="row" id="searchTools"><div class="col-md-12"> <form class="form-inline">'
    + '<div class="form-group" style="margin-right: 50px;"> <label for="beginDate">开始时间：</label>'
    + '<input type="text" class="form-control form_datetime" id="beginDate" readonly="readonly">'
    + '</div> <div class="form-group" style="margin-right: 50px;"><label for="endDate">结束时间：</label>'
    + '<input type="text" class="form-control form_datetime" id="endDate" readonly="readonly">'
    + '</div>' + ' <button type="button" class="btn btn-primary" id="searchReport">查询报表</button>'
        //+ ' &nbsp;' + ' <button type="button" class="btn btn-primary" id="shopReport">下载报表</button>'
    + ' &nbsp;<button type="button" class="btn btn-primary" v-if="hasCleanShopOrder"  @click.stop="printOrderTicket()">日报小票</button> '
    + ' &nbsp;<button type="button" class="btn btn-success" v-if="hasCleanShopOrder"  @click.stop="sendCleanShopMessage()">日报短信</button> '
    + ' <button type="button" class="btn btn-success" v-if="!hasCleanShopOrder"  @click="cleanShopOrder()">结&nbsp;&nbsp;店</button> </form>'
    + ' <br/> <div> </div><br/>  <br/> <div> <ul class="nav nav-tabs" role="tablist" id="ulTab">'
    + '<li role="presentation" class="active">' + ' <a href="#dayReport" aria-controls="dayReport" role="tab" data-toggle="tab"><strong>营业收入报表</strong>'
    + '</a>' + ' </li> <li role="presentation"><a href="#revenueCount" aria-controls="revenueCount" role="tab"'
    + '    data-toggle="tab"><strong>菜品销售报表</strong></a></li>' + ' </ul>' + ' <div class="tab-content">'
    + '<div role="tabpanel" class="tab-pane active" id="dayReport">' + ' <div class="panel panel-success">'
    + ' <div class="panel-heading text-center">  <strong style="margin-right:100px;font-size:22px">收入条目'
    + ' </strong>  </div>   <div class="panel-body">' + ' <table id="dayReportTable" class="table table-striped table-bordered table-hover"'
    + ' width="100%"></table></div> </div>  </div> <div role="tabpanel" class="tab-pane" id="revenueCount">'
    + '<div class="panel panel-primary" style="border-color:write;"> <div class="panel panel-info">'
    + '<div class="panel-heading text-center"> <strong style="margin-right:100px;font-size:22px">菜品销售记录</strong>'
    + ' </div><div class="panel-body"><table id="articleSaleTable" class="table table-striped table-bordered table-hover"'
    + ' width="100%"></table> </div>  </div> </div> </div> </div> </div></div>',
    components: {
        "choice-printer": {
            props: ["printers", "show", "oid"],
            template: '<div class="modal" style="display:block" v-if="show">                                                                                                                 ' +
            '  <div class="modal-dialog">                                                                                                        ' +
            '	<div class="modal-content">                                                                                                      ' +
            '	  <div class="modal-header">                                                                                                     ' +
            '		<button type="button" class="close" @click="close"><span >&times;</span></button> ' +
            '		<h4 class="modal-title">找到多个打印机</h4>                                                                                     ' +
            '	  </div>                                                                                                                         ' +
            '	  <div class="modal-body" style="max-height: 500px; overflow: auto;">                                                                                                       ' +
            '<div>' +
            '		 <button v-for="o in printers" @click="choiceprinter(o)" class="btn btn-block" :class="{\'btn-success\':$index%2==1,\'btn-info\':$index%2==0}" style="text-align:left;margin-bottom:10px;">' +
            '<div class="flex-row"><div class="flex-1">{{$index+1}}.{{o.name}} -- {{o.ip}}:{{o.port}}</div> ' +
            '<div>&nbsp;</div>' +
            '</button>   ' +
            '</div>' +
            '	  </div>                                                                                                                         ' +
            '	  <div class="modal-footer">                                                                                                     ' +
            '		<button type="button" class="btn btn-default" @click="close">关闭</button>                                            ' +
            '	  </div>                                                                                                                         ' +
            '	</div>                                                                                                                           ' +
            '  </div>                                                                                                                            ' +
            '</div> ',
            methods: {
                choiceprinter: function (p) {
                    this.$dispatch("choice-printer-event", p, this.oid);
                    this.close();
                },
                close: function () {
                    this.show = false;
                }
            }
        },
        "history-modal": {
            props: ["show", "historyorders"],
            template: '<div class="modal" style="display:block" v-if="show">' +
            '  <div class="modal-dialog">' +
            '    <div class="modal-content">' +
            '      <div class="modal-header">' +
            '        <button type="button" class="close" @click="close"><span >&times;</span></button>' +
            '        <h4 class="modal-title text-center"><strong>历史订单</strong></h4>' +
            '      </div>' +
            '      <div class="modal-body">' +
            '      	<table class="table table-striped">' +
            '	      <thead>' +
            '	        <tr>' +
            '	          <th>交易码</th>' +
            '	          <th>菜品总数</th>' +
            '	          <th>操作</th>' +
            '	        </tr>' +
            '	      </thead>' +
            '	      <tbody style="font-size: 22px">' +
            '	        <tr v-for="order in historyorders">' +
            '	          <td>{{order.verCode}}</td>' +
            '	          <td>{{order.articleCount}}</td>' +
            '	          <td><button class="btn btn-info btn-block" @click="printOne(order)">打&nbsp;印</button></td>' +
            '	        </tr>' +
            '	      </tbody>' +
            '	    </table>' +
            '      </div>' +
            '      <div class="modal-footer">' +
            '      	<div class="col-md-2"><button type="button" class="btn btn-bolck btn-danger" @click="close">不处理</button></div>' +
            '      	<div class="col-md-8 col-md-offset-2"><button type="button" class="btn btn-block btn-primary" @click="printAll">打印全部</button></div>' +
            '      </div>' +
            '    </div>' +
            '  </div>' +
            '</div>',
            methods: {
                printOne: function (order) {
                    this.$dispatch("history-order-print-event", order);
                    if (this.historyorders.length == 0) {
                        this.close();
                    }
                },
                printAll: function () {
                    this.$dispatch("history-order-print-all-event");
                    this.close();
                },
                close: function () {
                    this.show = false;
                }
            }
        },
//      结店弹出框
        "shop-modal": {
            props: ["show"],
            template: '<div class="modal" style="display:block;" v-if="show">' +
            '  <div class="modal-dialog" style="height:500px;Width:400px">' +
            '    <div class="modal-content">' +
            '      <div class="modal-body">' +
            '			<div class="title" style="text-align:center">' +
            '				<button type="button" class="close" @click="cancelAll"><span style="font-size: 35px">&times;</span></button>' +
            '				<h4 style="border-top: none;margin-bottom: 20px">是否结店</h4>' +
            '			</div>' +
            '      	<table class="table table-striped">' +
            '	       &nbsp;&nbsp;&nbsp;线下支付金额：&nbsp;&nbsp;&nbsp;<input type="text"  name="enterTotal" id="enterTotal" class="numkeyboard" value="0" />&nbsp;&nbsp;&nbsp;元</br>' +
            '          <span style="float:left; width:120px">&nbsp</span><span style="color:red;display:none;font-size:8px;" id="spanid1">线下支付金额为必填字段</span></br> ' +
            '	   	   &nbsp;&nbsp;&nbsp;线下消费笔数：&nbsp;&nbsp;&nbsp;<input type="text"  name="enterCount" id="enterCount" class="numkeyboard" value="0" />&nbsp;&nbsp;&nbsp;笔</br>' +
            '          <span style="float:left; width:120px">&nbsp</span><span style="color:red;display:none;font-size:8px;" id="spanid2">线下消费笔数为必填字段</span></br> ' +
            '          &nbsp;&nbsp;&nbsp;外卖订单笔数：&nbsp;&nbsp;&nbsp;<input type="text"  name="deliveryOrders" id="deliveryOrders" class="numkeyboard" value="0"/>&nbsp;&nbsp;&nbsp;笔</br>' +
            '          <span style="float:left; width:120px">&nbsp</span><span style="color:red;display:none;font-size:8px;" id="reveal1">外卖订单笔数为必填字段</span></br> ' +
            '          &nbsp;&nbsp;&nbsp;外卖订单金额：&nbsp;&nbsp;&nbsp;<input type="text"  name="orderBooks" id="orderBooks" class="numkeyboard" value="0"/>&nbsp;&nbsp;&nbsp;元</br>' +
            '          <span style="float:left; width:120px">&nbsp</span><span style="color:red;display:none;font-size:8px;" id="reveal2">外卖订单金额为必填字段</span></br> ' +
            '        &nbsp;&nbsp;&nbsp;线下就餐人数：&nbsp;&nbsp;&nbsp;<input type="text"  name="numGuest" id="numGuest" class="numkeyboard" value="0" />&nbsp;&nbsp;&nbsp;人' +
            '	    </table>' +
            '      </div>' +
            '      <div class="modal-footer" style="border-top: none;font-size:15px;padding-top: 1px">' +
            '      	<div class="col-md-5"><button type="button" class="btn btn-bolck btn-danger" @click="cancelAll" style="padding: 8px 36px;">取消</button></div>' +
            '      	<div class="col-md-3 col-md-offset-2"><button type="button" class="btn btn-block btn-primary" @click="shopclose()" style="padding: 8px 36px;width: 170%" whi>确认</button></div>' +
            '      </div>' +
            '    </div>' +
            '  </div>' +
            '</div>',
            data: function () {
                return {
                    shopPrinterDialog: {show: false},
                    hasCleanShopOrder: false,
                };
            },
            watch: {
                "show": function (val, oldVal) {
                    if (val) {
                        this.initKeyBoard();
                    }
                }
            },
            methods: {
                shopclose: function () {//结店弹出框,录取两个值后,将所有未消费的订单，进行退单处理
                    this.$dispatch("history-shop-close-event");
                    var that = this;
                    var a = /^[0-9]+(\.[0-9]{2})?$/;//判断只能取两位小数点
                    var b = /^\+?(0|[1-9][0-9]*)$/;//判断是否是整数（包括0）
                    var enterTotal = document.getElementById("enterTotal").value;
                    var enterCount = document.getElementById("enterCount").value;
                    var numGuest = document.getElementById("numGuest").value;
                    var deliveryOrders = document.getElementById("deliveryOrders").value;
                    var orderBooks = document.getElementById("orderBooks").value;
                    var spanid1 = document.getElementById("spanid1");
                    var spanid2 = document.getElementById("spanid2");
                    /*cleanShopOrder(function (result) {
                     that.hasCleanShopOrder = true;
                     toastr.info("退单成功！");
                     });*/

                    if (enterTotal == null || enterTotal == "") {
                        spanid1.style.display = '';
                    } else {
                        spanid1.style.display = 'none';
                    }
                    if (enterCount == null || enterCount == "") {
                        spanid2.style.display = '';
                    } else {
                        spanid2.style.display = 'none';
                    }
                    console.log(deliveryOrders);
                    console.log(orderBooks);
                    var reveal1 = document.getElementById("reveal1");
                    var reveal2 = document.getElementById("reveal2");
                    if (deliveryOrders == null || deliveryOrders == "") {
                        reveal1.style.display = '';
                    } else {
                        reveal1.style.display = 'none';
                    }
                    if (orderBooks == null || orderBooks == "") {
                        reveal2.style.display = '';
                    } else {
                        reveal2.style.display = 'none';
                    }
                    //为避免前面数据还是没有填写
                    if (enterTotal == null || enterTotal == "") {
                        spanid1.style.display = '';
                    } else {
                        spanid1.style.display = 'none';
                    }

                    if (enterCount == null || enterCount == "") {
                        spanid2.style.display = '';
                    } else {
                        spanid2.style.display = 'none';
                    }

                    if (deliveryOrders == null || deliveryOrders == "") {
                        reveal1.style.display = '';
                    } else {
                        reveal1.style.display = 'none';
                    }

                    if (!b.test(enterCount)) {
                        toastr.error("线下消费笔数只能是正整数");
                    } else if (!a.test(enterTotal)) {
                        toastr.error("线下支付金额只能保留两位");
                    } else if (!b.test(deliveryOrders)) {
                        toastr.error("外卖订单笔数只能是正整数");
                    } else if (!a.test(orderBooks)) {
                        toastr.error("外卖订单金额只能保留两位");
                    } else {
                        console.log(enterTotal, enterCount, numGuest, deliveryOrders, orderBooks);
                        console.log(beginClosingDate);
                        debugger;
                        var createTime = new Date(beginClosingDate + " 00:00:00").getTime();
                        //toastr.info("退单成功！");
                        var offLineOrder = {
                            "enterTotal": enterTotal,
                            "enterCount": enterCount,
                            "deliveryOrders": deliveryOrders,
                            "orderBooks": orderBooks,
                            "numGuest": numGuest,
                            "createDate":createTime
                        }
                        cleanShopOrder(offLineOrder, function (restult) {
                            if (restult.success) {
                                that.cleanShopOrder = false;
                                that.$dispatch("my-shop-close-event");//掉用外部方法，将 hasCleanShopOrder = true
                                that.show = false;
                                toastr.info("结店成功！")
                            }else{
                                toastr.clear();
                                toastr.error("结店失败，录入线下数据出错");
                            }
                        })
//                    });
                    }
                },
                cancelAll: function () {
                    this.show = false;
                    $("#keyboard_5xbogf8c").css({"display": "none"});//隐藏键盘
                },
                initKeyBoard: function () {
                    //绑定 js 键盘
                    $(".numkeyboard").ioskeyboard({
                        keyboardRadix: 80,//键盘大小基数，实际大小比为9.4，即设置为100时实际大小为940X330
                        keyboardRadixMin: 40,//键盘大小的最小值，默认为60，实际大小为564X198
                        keyboardRadixChange: true,//是否允许用户改变键盘大小,该功能仅能完美支持Chrome26；仅当keyboardRadixMin不小于60时才较好支持Safari内核浏览器
                        clickeve: true,//是否绑定元素click事件
                        colorchange: false,//是否开启按键记忆功能，如果开启，将随着按键次数的增加加深相应按键的背景颜色
                        colorchangeStep: 1,//按键背景颜色改变步伐，采用RBG值，默认为RGB(255,255,255),没按一次三个数字都减去步伐值
                        colorchangeMin: 154//按键背影颜色的最小值，默认为RGB(154,154,154)
                    });
                    $("#enterTotal").focus();
                    $("input").blur(function () {//隐藏 js键盘
                        $("#keyboard_5xbogf8c").css({"display": "none"});
                    });
                }
            }
        },
    },
    computed: {
        modeText: function () {
            return mode[this.order.distributionModeId];
        }
    },
    data: function () {
        return {
            orderlist: [],
            order: {},
            lastTime: 0,
            printTask: [],
            cancelBtn: true,
            callBtn: true,
            printBtn: false,
            showTableNumber: false,
            ws: null,
            choicePrinterDialog: {oid: "", show: false, printers: []},
            historyOrdersDialog: {historyorders: [], show: false},
            shopPrinterDialog: {show: false},//结店弹出框
            hasCleanShopOrder: false
        };
    },
    created: function () {
    },
    events: {
        "choice-printer-event": function (p, oid) {
            console.log("选择打印机打印:", p.id, oid);
            this.printTicketBase(oid, p.id);
        },
        "history-order-print-event": function (order) {
            this.orderlist.push(order);
            this.printTask.push(order);
            this.historyOrdersDialog.historyorders.$remove(order);
        },
        "history-order-print-all-event": function () {
            var historyorders = this.historyOrdersDialog.historyorders;
            for (var i = 0; i < historyorders.length; i++) {
                this.orderlist.push(historyorders[i]);
                this.printTask.push(historyorders[i]);
            }
            this.historyOrdersDialog.historyorders = [];
        },
//        结店后调用
        "history-shop-close-event": function () {
            this.show = true;//将弹出框show状态改为true
        },
        "my-shop-close-event": function () {
            this.hasCleanShopOrder = true;//结店成功后得到日常小票（关闭结店功能）
        }
    },
    methods: {
        cancelOrder: function (o) {
            var that = this;
            if (confirm("你确定要取消订单吗？")) {
                cancelOrderPos(o.id, function (result) {
                    if (result.success) {
                        toastr.success("取消订单成功");
                        that.orderlist.$remove(o);
                    } else {
                        toastr.error("取消订单失败");
                    }
                });
            }
        },
        printOrderTicket: function () {
            //if (isPrintSupport()) {
            //toastr.info("正在打印...");
            //printOrderAllAndSuccess('09e834cd82894600ae7a5a2f2044db20', function (result) {
            //    if (result.success) {
            //        toastr.success("打印订单成功");
            //        window.location.reload();
            //    } else {
            //        toastr.error(result.message);
            //    }
            //});
            printTotal({beginDate: beginClosingDate, endDate: endClosingDate}, function (result) {
                if (result.success) {
                    printPlus(result.data);
                    toastr.success("打印订单成功");
                } else {
                    toastr.error(result.message);
                }
            });
            //} else {
            //    toastr.error("不支持打印插件！");
            //}
        },
        sendCleanShopMessage : function () {
            sendCleanShopMessage(function (result) {
                if (result.success){
                    toastr.clear();
                    toastr.success("发送日结短信成功，请注意查收");
                }else{
                    toastr.clear();
                    if (result.message){
                        toastr.error(result.message);
                    }else {
                        toastr.error("发送日结短信失败");
                    }
                }
            });
        },
        printTicketBase: function () {
            var that = this;
            getPrinterByType(2, function (printers) {
                //console.log("打印小票", oid);
                if (printers.length == 1) {
                    printTotal(printers[0].id, function (task) {
                        printPlus([task], function () {
                            toastr.success("打印成功");
                        }, function (msg) {
                            toastr.error(msg);
                        });
                    });

                } else if (printers.length > 1) {
                    that.choicePrinterDialog.show = true;
                    that.choicePrinterDialog.printers = printers;
                    that.choicePrinterDialog.oid = oid;
                } else {
                    toastr.error("没有找到前台打印机，请检查后台是否配置正确");
                }
            });
        },

        cleanShopOrder: function () {//结店关闭事件

            beginClosingDate = $("#beginDate").val();
            endClosingDate = $("#endDate").val();
            if(beginClosingDate != endClosingDate){
                toastr.error("请选择一天进行结店。。");
                return false;
            }

            this.shopPrinterDialog.show = true;//弹出框调用
//                if (confirm("是否要结店？")) {
//                    var that = this;
//                    toastr.info("正在退单！");
//                    cleanShopOrder(function (result) {
//                        that.hasCleanShopOrder = true;
//                        toastr.info("退单成功！");
//                    });
//                }
        },
        callNumber: function (o) {
            var that = this;
            callNumberRequest(o.id, function (result) {
                if (result.success) {
                    toastr.success("叫号成功");
                    that.orderlist.$remove(o);
                } else {
                    toastr.error(result.message);
                }
            });
        },
        showOrderInfo: function (oid) {
            var that = this;
            getOrderInfo(oid, function (order) {
                that.order = order;
            });
        },
        startGetNewOrder: function () {
            this.connWebsock();
        },
        connWebsock: function () {
            console.log("老版本连接socket");
            var that = this;
            var url = $("base").attr("href") + "sockjs/orderServer";

            console.log("----：" + url);

            this.ws = new SockJS(url);
            this.ws.onopen = function (event) {
                that.wsOnopen()
            };
            this.ws.onmessage = function (event) {
                that.wsOnmessage(event)
            };
            this.ws.onclose = function (event) {
                console.log("-0---colose");
                that.wsOnclose()
            };
            this.ws.onerror = function (evt) {
                that.wsOnerror()
            };
        },
        wsOnopen: function () {
            console.log("开始连接");
            var data = this.getJsonData("check", null);
            this.ws.send(data);
            console.log("链接成功");
            toastr.info("连接服务器成功");
        },
        wsOnmessage: function (event) {
            var data = JSON.parse(event.data);
            if (data.dataType == "current") {//后台推送的 实时订单
                console.log(data.id);
                if (data.orderMode == 6) {
                    if (data.printTimes != 1) {
                        this.orderlist = [];
                        getPosOrder();
                    }
                } else {
                    this.orderlist.push(data);
                }
                this.printTask.push(data);
            } else if (data.dataType == "nopay") {
                if (data.orderState == 1) {
                    this.orderlist.push(data);
                } else if (data.orderState == 2) {
                    this.printTask.push(data);
                }
            } else if (data.dataType == "history") {//后台推送的 历史订单
                console.log(data.orders);
                this.historyOrdersDialog.historyorders = data.orders;
                this.historyOrdersDialog.show = true;
            } else if (data.dataType == "refresh") {//后台推送指令 刷新页面
                window.location.reload();
            }
        },
        wsOnclose: function () {
            console.log("----1");
            toastr.info("与服务器的连接断开,正在尝试重新连接");
            window.setTimeout(this.connWebsock, 1000);
            console.log("连接断开");
        },
        wsOnerror: function () {
            window.setTimeout(this.connWebsock, 1000);
            console.log("ws出错");
        },
        getJsonData: function (type, data) {
            var dataAPI = {};
            dataAPI.shopId = shopId;
            dataAPI.client = "pos";
            dataAPI.type = type;
            dataAPI.data = data;
            return JSON.stringify(dataAPI);
        }

    }
};


var historyOrderMix = {
    mixins: [posmix],
    data: function () {
        return {
            cancelBtn: false,
            printBtn: true,
            printKitchenBtn: true,
            orderMoney: true
        }
    },
};


var outFoodMix = {
    mixins: [posmix],
    data: function () {
        return {
            cancelBtn: false,
            outFood: true,
            printBtn: true,
            printKitchenBtn: true,
        }
    },
};

var printOrderMix = {
    mixins: [printMix],

};


