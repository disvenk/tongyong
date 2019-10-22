package com.resto.shop.web.controller.business;

import com.resto.shop.web.powerbi.authorization.PIRestClient;
import com.resto.shop.web.powerbi.model.PIReportView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * title
 * company resto+
 * author jimmy 2018/8/24 下午2:05
 * version 1.0
 */
@RestController
@RequestMapping("pi")
public class PIController {
    @Autowired
    private PIRestClient piRestClient;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public PIReportView viewReport(String reportName){
        return piRestClient.getReportViewMap().get(reportName);
    }
}
