package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiTableCode;
import com.resto.api.shop.dto.TableCodeDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.TableCodeService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xielc on 2018/1/5.
 */
@RestController
public class TableCodeController implements ShopApiTableCode {
    @Autowired
    private TableCodeService tableCodeService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public TableCodeDto selectById(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                   @ApiParam(value = "桌位id", required = true) @RequestParam("id")String id){
        return mapper.map(tableCodeService.selectById(id),TableCodeDto.class);
    }

}
