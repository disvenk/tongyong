/**
 * 得到最新订单集合
 * @param lastTime
 * @param callBack
 */
function getPushOrderList(lastTime,callBack){
	$.post("order/list_push",{lastTime:lastTime},function(result){
		callBack&&callBack(result.data);
	});
}

/**
 * 得到准备订单集合
 * @param lastTime
 * @param callBack
 */
function getReadyOrderList(lastTime,callBack){
	$.post("order/list_ready",{lastTime:lastTime},function(result){
		callBack&&callBack(result.data);
	})
}

/**
 * 得到叫号订单集合
 * @param lastTime
 * @param callBack
 */
function getCallOrderList(lastTime,callBack){
	$.post("order/list_call",{lastTime:lastTime},function(result){
		callBack&&callBack(result.data);
	})
}

function getOrderInfo(oid,cbk){
	$.post("order/getOrderInfo",{orderId:oid},function(result){
		cbk&&cbk(result.data);
	});
}


function saveChange(oid,count,orderItemId,type,cbk){
	$.post("order/saveChange",{orderId:oid,count:count,orderItemId:orderItemId,type:type},function(result){
		cbk&&cbk(result);
	});
}


function getChildItem(oid,cbk){
	$.post("order/getChildItem",{orderId:oid},function(result){
		cbk&&cbk(result.data);
	});
}



function getOutFoodInfo(oid,cbk){
	$.post("order/getOutFoodInfo",{orderId:oid},function(result){
		cbk&&cbk(result.data);
	});
}

function listHistoryOrder(cbk){
	$.post("order/getHistroyOrderList",function(result){
		cbk&&cbk(result.data);
	});
}

function getBrandUser(pwd,cbk){
	$.post("config/getBrandUser",{password:pwd},function(result){
		cbk&&cbk(result);
	});
}

function updateTurnTable(tableNumber,order_id,cbk){
	$.post("order/updateTurnTable",{tableNumber:tableNumber,order_id:order_id},function(result){
		cbk&&cbk(result);
	});
}

function refundOrder(order,cbk){
	$.ajax({
		url : "order/refundOrder",
		contentType : "application/json",
		type : "post",
		data : JSON.stringify(order),
		success : function(result) {
			cbk && cbk(result);
		},
		error:function(a,b,c,d){

		}
	});

}
function updateReceiptOrderNumber(serialNumber,cbk){
	$.post("order/updateReceiptOrderNumber",{serialNumber:serialNumber},function(result){
		cbk&&cbk(result);
	});
}

function checkOrder(order,cbk){
	$.ajax({
		url : "order/checkOrder",
		contentType : "application/json",
		type : "post",
		data : JSON.stringify(order),
		success : function(result) {
			cbk && cbk(result);
		},
		error:function(a,b,c,d){

		}
	});
}


function refund(pwd,cbk){
	$.post("config/getBrandUser",{password:pwd},function(result){
		cbk&&cbk(result);
	});
}


function getOutFoodList(cbk){
	$.post("order/getOutFoodList",function(result){
		cbk&&cbk(result.data);
	});
}

function listErrorOrder(cbk){
	$.post("order/getErrorOrderList",function(result){
		cbk&&cbk(result.data);
	});
}

function listPlatFormErrorOrder(cbk){
	$.post("order/getPlatFormErrorOrder",function(result){
		cbk&&cbk(result.data);
	});
}

function printList(cbk){
    $.post("config/printList",function(result){
        cbk&&cbk(result.data);
    });
}

function listOrderNoPay(cbk){
	$.post("order/listOrderNoPay",function(result){
		cbk&&cbk(result.data);
	});
}



function cancelOrderPos(oid,cbk){
	$.post("order/cancelOrderPos",{orderId:oid},function(result){
		cbk&&cbk(result);
	});
}

function confirmOrderPos(oid,cbk){
    $.post("order/confirmOrderPos",{orderId:oid},function(result){
        cbk&&cbk(result);
    });
}

function callNumberRequest(oid,cbk){
	$.post("order/callNumber",{orderId:oid},function(result){
		cbk&&cbk(result);
	});
}
function printOrderAll(oid,cbk){
	$.post("order/printOrderAll",{orderId:oid},function(result){
		cbk&&cbk(result.data);
	});
}

function receiveMoney(oid,cbk){
    $.post("order/receiveMoney",{orderId:oid},function(result){
        cbk&&cbk(result.data);
    });
}

function printPlatformOrderAll(oid,type,cbk){
	$.post("order/printPlatformOrderAll",{orderId:oid,type:type},function(result){
		cbk&&cbk(result.data);
	});
}

function printOrderAllAndSuccess(oid,cbk){
	$.post("order/printOrderAndAutoSuccess",{orderId:oid},function(result){
		cbk&&cbk(result);
	})
}

function printTotal(object,cbk){
	$.post("order/printTotal",object,function(result){
		cbk&&cbk(result);
	})
}

function sendCleanShopMessage(cbk){
    $.post("order/sendCleanShopMessage",function(result){
        cbk&&cbk(result);
    })
}

function printReceipt(oid,printerId,cbk){
	$.post("order/printReceipt",{orderId:oid,printerId:printerId},function(result){
		cbk&&cbk(result.data);
	});
}

function printHungerReceipt(oid,printerId,cbk){
	$.post("order/printHungerReceipt",{orderId:oid,printerId:printerId},function(result){
		cbk&&cbk(result.data);
	});
}

function printKitchenReceipt(oid,cbk){
	$.post("order/printKitchenReceipt",{orderId:oid},function(result){
		cbk&&cbk(result);
	})

}

function printHungerKitchenReceipt(oid,cbk){
	$.post("order/printHungerKitchenReceipt",{orderId:oid},function(result){
		cbk&&cbk(result);
	})

}


function printSuccess(oid,cbk){
	$.post("order/printSuccess",{orderId:oid},function(result){
		cbk&&cbk(result);
	});
}

/**
 * R+外卖打印更新production_status为4
 * @param oid
 * @param cbk
 */
function printUpdate(oid,cbk){
	$.post("order/printUpdate",{orderId:oid},function(result){
		cbk&&cbk(result);
	});
}

function getOrderListById(id,cbk){
	$.post("order/selectOrderStatesById",{id:id},function(result){
		cbk&&cbk(result.data);
	})
}

/**
 * 根据验证码查询已付款的订单集合
 * @param vercode
 * @param cbk
 */
function getOrderListByVercode(vercode,cbk){
	$.post("order/selectOrderByVercode",{vercode:vercode},function(result){
		cbk&&cbk(result.data);
	})
}

/**
 * 根据桌号查询已付款的订单集合
 * @param tableNumber
 * @param cbk
 */
function getOrderListByTableNumber(tableNumber,cbk){
	$.post("order/selectOrderByTableNumber",{tableNumber:tableNumber},function(result){
		cbk&&cbk(result.data);
	})
}


/**
 * 修改就餐模式
 * @param modeId
 * @param oid
 * @param cbk
 */
function swtichDistrubitionMode(oid,modeId,cbk){
	$.post("order/swtichMode",{orderId:oid,modeId:modeId},function(result){
		cbk&&cbk(result);
	});
}

/**
 * 设置就餐餐桌号
 * @param oid
 * @param tableNumber
 * @param cbk
 */
function bindingTableNumber(oid,tableNumber,cbk){
	$.post("order/bindingTableNumber",{orderId:oid,tableNumber:tableNumber},function(result){
		cbk&&cbk(result);
	});
}

function clearMapRequest(){
	$.post("order/clearCallNumber");
}

function listOrderByStatus(cbk){
	$.post("order/listOrderByStatus",{},function(result){
		cbk&&cbk(result.data);
	});
}

function changeMode(oid,cbk){
    $.post("order/changeMode",{oid:oid},function(result){
        cbk&&cbk(result.data);
    });
}


function checkMealFee(cbk){
	$.post("config/checkMealFee",{},function(result){
		cbk&&cbk(result);
	});
}

function receiveMoney(oid,cbk){
    $.post("order/receiveMoney",{orderId:oid},function(result){
        cbk&&cbk(result.data);
    });
}

function receiveMoneyNew(oid,cbk){
    $.post("order/receiveMoneyNew",{orderId:oid},function(result){
        cbk&&cbk(result.data);
    });
}



function changeMode(oid,cbk){
    $.post("order/changeMode",{oid:oid},function(result){
        cbk&&cbk(result.data);
    });
}

function getPrinterByType(tp,cbk){
	$.post("order/getPrinters",{printerType:tp},function(result){
		cbk&&cbk(result.data);
	});
}

/**
 * 得到 POS 中新列表订单中，未处理的订单
 * 订单状态为：2；生产状态：1（订单已支付，但未打印）
 * 订单状态为：2；生产状态：2（订单已支付，已打印，但在POS端未作任何操作--->ps:操作后状态都会改变）
 * @param cbk
 */
function getPosOrder(cbk){
	$.post("order/getPosOrder",function(result){
		cbk&&cbk(result.data);
	})
}

/**
 * POS端确认订单支付【只有 后付款 模式可调用】
 * @param oid
 * @param cbk
 */
function confirmOrder(oid,cbk){
	$.post("order/confirmOrder",{oid:oid},function(result){
		cbk&&cbk(result);
	})
}

/**
 * 结店操作
 * 将所有未消费的订单，进行退单处理
 */
function cleanShopOrder(offLineOrder,cbk){
	// $.post("order/cleanShopOrder",{offLineOrder:offLineOrder},function(result){
	// 	cbk&&cbk(result);
	// })
    $.ajax({
        url : "order/cleanShopOrder",
        contentType : "application/json",
        type : "post",
        data : JSON.stringify(offLineOrder),
        success : function(result) {
            cbk && cbk(result);
        },
    });
}


/**
 * 查询品牌设置参数
 * @param cbk
 */
function getBrandSetting(cbk){
	$.post("config/getBrandSetting",function(result){
		cbk&&cbk(result.data);
	})
};
function getServerIp(cbk){
    $.post("config/getServerIp",function(result){
        cbk&&cbk(result.data);
    })
};


function getShopInfo(cbk){
	$.post("config/getShopInfo",function(result){
		cbk&&cbk(result.data);
	})
};

function getShopTvConfig(cbk){
	$.post("config/getShopTvConfig",function(result){
		cbk&&cbk(result.data);
	})
};

/**
 * 根据分类查询分类下的所有菜品
 */
function getArticleListByFamily(articleFamilyId,currentPage,showCount,cbk){
	$.post("article/getArticleListByFamily",{articleFamilyId:articleFamilyId,currentPage:currentPage,showCount:showCount},function(result){
		cbk&&cbk(result.data);
	})
};

function getFamily(cbk){
	$.post("article/getFamily",function(result){
		cbk&&cbk(result.data);
	})
};

function getFamilyBySort(currentPage, showCount, cbk){
	$.post("article/getFamilySort",{currentPage:currentPage,showCount:showCount},function(result){
		cbk&&cbk(result.data);
	})
};

function saveOrderForm(orderForm, sbk,error) {
	$.ajax({
		url : "order/saveOrder",
		contentType : "application/json",
		type : "post",
		async: false,
		data : JSON.stringify(orderForm),
		success : function(result) {
			sbk && sbk(result);
		},
		error:function(a,b,c,d){
			error&&error(a,b,c,d);
		}
	});
};

function getArticleList(sbk){
	$.ajax({
		url : "article/getArticleListByDiscount",
		type : "post",
		success : function(result) {
			sbk && sbk(result.data);
		}
	});
}



/**
 *  存储打印日志
 * @param cbk
 */
function saveLog(result,model,cbk){
    $.post("config/saveLog",{result:result,model:JSON.stringify(model)},function(result){
        cbk&&cbk(result.data);
    })
};


/**
 *  存储打印失败的订单
 * @param cbk
 */
function saveError(id){
    $.post("config/saveError",{id:id},function(result){

    })
};


/**
 *  监测打印失败的订单
 * @param cbk
 */
function checkError(cbk){
    $.post("config/checkError",{},function(result){
        cbk&&cbk(result.data);
    })
};

/**
 * 校验大众点评订单号
 * @param shanhuiOrderId
 * @param sbk
 */
function checkShanHuiOrder(orderId,shanhuiOrderId, sbk){
	$.post("meituan/checkShanHuiOrder",{orderId:orderId,shanhuiOrderId:shanhuiOrderId},function(result){
		sbk&&sbk(result);
	})
}

/**
 * 保存大众点评闪惠支付的订单result_data
 * @param orderId
 * @param shanhuiOrderId
 * @param sbk
 */
function saveShanhuiOrder(orderId, shanhuiOrderId, sbk){
	$.post("meituan/saveShanhuiOrder",{orderId:orderId,shanhuiOrderId:shanhuiOrderId},function(result){
		sbk&&sbk(result);
	})
}


//本地上报健康反馈给美团
function reportHeartbeat(cbk){
    $.post("meituan/reportHeartbeat",function(){
        cbk&&cbk();
    })
};


//本地上报健康反馈给美团
function checkPlatformSetting(cbk){
    $.post("config/checkPlatformSetting",function(result){
        cbk&&cbk(result);
    })
};

function getRecommends(articleId,cbk){
	$.ajax({
		url : "order/getRecommend",
		type : "post",
		async: false,
		data : {articleId: articleId},
		success : function(result) {
			cbk&&cbk(result);
		}
	});
}

function payOrder(object,cbk) {
    $.post("order/payOrder",object,function(result){
        cbk&&cbk(result);
    });
};

function posPayOrderGetCustomer(orderId,cbk) {
    $.post("order/posPayOrderGetCustomer",{"orderId" : orderId},function(result){
        cbk&&cbk(result.data);
    });
}

function checkErrorTask(taskId,type,id,cbk){
    $.post("order/checkErrorTask",{taskId:taskId,type:type,id:id},function(result){
        cbk&&cbk(result.data);
    });
}

function reminder(orderItemId,orderId,cbk) {
    $.post("order/reminder",{orderItemIds : orderItemId, orderId : orderId},function (result) {
        cbk&&cbk(result);
    });
}

function turntable(order_id,brandId,tableNumber,serialNumber,createTime,shopDetailId,oldtableNumber,cbk) {
	$.post("order/turntable",{id:order_id,brandId:brandId,tableNumber : tableNumber,serialNumber:serialNumber,createTime:createTime,shopDetailId:shopDetailId,oldtableNumber:oldtableNumber},function (result) {
		cbk&&cbk(result);
	});
}

function getRefundRemarkList(cbk){
	$.post("order/getRefundRemarkList",null,function(result){
		cbk&&cbk(result);
	});
}

function badAppraisePrintOrder(orderId, cbk, error) {
	$.post("order/badAppraisePrintOrder", {orderId : orderId}, function (result) {
		if (result.success) {
            cbk && cbk(result.data);
        }else {
			error && error(result.message);
		}
    });
}


function posDisCountAjax(orderId, discount, eraseMoney, noDiscountMoney, type, orderPosDiscountMoney, cbk){
	$.ajax({
		url : "order/posDisCount",
		type : "post",
		async: false,
		data : {orderId: orderId, discount: discount, eraseMoney: eraseMoney, noDiscountMoney: noDiscountMoney, type: type, orderPosDiscountMoney: orderPosDiscountMoney},
		success : function(result) {
			cbk&&cbk(result);
		}
	});
}

function checkCustomerSubscribe(customerId, cbk){
	$.post("customer/checkCustomerSubscribe", {customerId : customerId}, function (result) {
		cbk && cbk(result);
	});

}