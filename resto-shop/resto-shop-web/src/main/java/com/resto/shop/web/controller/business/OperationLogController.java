package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.StoreOperationsLog;
import com.resto.shop.web.model.PaymentReview;
import com.resto.shop.web.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/10/22/0022 15:04
 * @Description:
 */
@Controller
@RequestMapping("operationLog")
public class OperationLogController extends GenericController {

    @Autowired
    OperationLogService operationLogService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<StoreOperationsLog> listData(Integer logType, String time) {
        return operationLogService.selectListByShopIdAndTime(getCurrentShopId(),logType,time);
    }

    /**
     * 查询详情
     * @param logId
     * @param time
     * @return
     */
    @RequestMapping("/checkDetails")
    @ResponseBody
    public List<PaymentReview> checkDetails(String logId, String time) {
        return operationLogService.selectPaymentReviewByLogId(getCurrentShopId(),logId,time);
    }


    @RequestMapping("/printReport")
    @ResponseBody
    public Result printReport(String time, HttpServletRequest request) {
        return null;
    }

}
