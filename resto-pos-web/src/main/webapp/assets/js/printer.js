//包含打印接口的调用

function isPrintSupport() {
    return !(typeof window.callbackObj == "undefined");
}

function printPlus(task, success, error) {
    var errorList =[];
    var k = 0;
    var time2 = setInterval(function () {

        var t2 = task[k];
        var check2;
        if (t2.TICKET_TYPE == "RestaurantLabel" || t2.TICKET_TYPE == "DeliveryLabel") {
        } else {
            check2 = callbackObj.printTicket(JSON.stringify(t2));
            if (!check2) {
                errorList.push(t2);
            }
        }
        k++;
        if (k == task.length) {
            clearInterval(time2);
        }

    }, 500);

        var i = 0;
        var time = setInterval(function () {
            var check1;
            var t = task[i];
            if (t.TICKET_TYPE == "RestaurantLabel" || t.TICKET_TYPE == "DeliveryLabel") {
                check1 = callbackObj.printLabel(JSON.stringify(t));
                if (!check1) {
                    errorList.push(t);
                }
            }
            i++;
            if (i == task.length) {
                clearInterval(time);
            }

        }, 4000);


        success && success(errorList);

}





function printPlusNew(task, success, error,kind) {
    // if (isPrintSupport()) {
    // var tableNumber = task.TABLE_NO;
    // var orderId = task.TASK_ORDER_ID;
    // var taskId = task.TASK_ID;
    // var type;
    // if (task.TICKET_MODE == "RestaurantReceipt") { //总单没打
    //     type = 0;
    // } else if (task.TICKET_MODE == "KitchenTicket" || task.TICKET_MODE == "RestaurantLabel") { //厨打没打
    //     type = 1;
    // }else{
    //     type = 2;
    // }
    // if(kind == "delivery"){
    //     type = 2;
    // }
    // var that = this;
    console.log(JSON.stringify(task));
    PrintService.send(JSON.stringify(task), function (result) {}
    )
    ;
    success && success();
// }else{
//     error("不支持打印插件");
// }

}




