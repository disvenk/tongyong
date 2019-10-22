package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAppraisePraise;
import com.resto.api.shop.dto.AppraisePraiseDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.AppraisePraise;
import com.resto.service.shop.service.AppraisePraiseService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppraisePraiseController implements ShopApiAppraisePraise {

    @Autowired
    private AppraisePraiseService appraisePraiseService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<AppraisePraiseDto> appraisePraiseList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                      @ApiParam(value = "点赞id", required = true) @PathVariable("id") String id) {
        return mapper.mapAsList(appraisePraiseService.appraisePraiseList(id), AppraisePraiseDto.class);
    }

    @Override
    public AppraisePraiseDto selectByAppraiseIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                          @ApiParam(value = "点赞id", required = true) @RequestParam("appraiseId") String appraiseId,
                                                          @ApiParam(value = "顾客id", required = true) @RequestParam("customerId")String customerId) {
        return mapper.map(appraisePraiseService.selectByAppraiseIdCustomerId(appraiseId, customerId), AppraisePraiseDto.class);
    }

    @Override
    public void updateCancelPraise(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   AppraisePraiseDto appraisePraiseDto) {
        appraisePraiseService.updateCancelPraise(mapper.map(appraisePraiseDto, AppraisePraise.class));
    }

    @Override
    public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "顾客id", required = true) @RequestParam("customerId") String customerId) {
        return appraisePraiseService.selectByCustomerCount(customerId);
    }

}
