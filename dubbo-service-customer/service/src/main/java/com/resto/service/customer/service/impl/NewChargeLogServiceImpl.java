package com.resto.service.customer.service.impl;

import com.resto.api.customer.entity.ChargeLog;
import com.resto.api.customer.service.NewChargeLogService;
import com.resto.service.customer.service.impl.service.ChargeLogService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-30 17:36
 */

@RestController
public class NewChargeLogServiceImpl implements NewChargeLogService {

    @Resource
    ChargeLogService chargeLogService;

    @Override
    public ChargeLog dbSave(String brandId, ChargeLog chargeLog) {
        chargeLogService.dbSave(chargeLog);
        return chargeLog;
    }

    @Override
    public int dbInsertList(String brandId, List<ChargeLog> list) {
        return chargeLogService.dbInsertList(list);
    }

    @Override
    public int dbDelete(String brandId, ChargeLog chargeLog) {
        return chargeLogService.dbDelete(chargeLog);
    }

    @Override
    public int dbDeleteByPrimaryKey(String brandId, Object var) {
        return chargeLogService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(String brandId, String var) {
        return chargeLogService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(String brandId, ChargeLog chargeLog) {
        return chargeLogService.dbUpdate(chargeLog);
    }

    @Override
    public List<ChargeLog> dbSelect(String brandId, ChargeLog t) {
        return chargeLogService.dbSelect(t);
    }

    @Override
    public List<ChargeLog> dbSelectPage(String brandId, ChargeLog t, Integer pageNum, Integer pageSize) {
        return chargeLogService.dbSelectPage(t,pageNum ,pageSize );
    }

    @Override
    public ChargeLog dbSelectByPrimaryKey(String brandId, Object key) {
        return chargeLogService.dbSelectByPrimaryKey(key);
    }

    @Override
    public ChargeLog dbSelectOne(String brandId, ChargeLog record) {
        return chargeLogService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(String brandId, ChargeLog record) {
        return chargeLogService.dbSelectCount(record);
    }

    @Override
    public List<ChargeLog> dbSelectByIds(String brandId, String ids) {
        return chargeLogService.dbSelectByIds(ids);
    }
}
