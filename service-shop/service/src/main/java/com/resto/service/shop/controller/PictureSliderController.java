package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiPictureSlider;
import com.resto.api.shop.dto.PictureSliderDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.service.PictureSliderService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PictureSliderController implements ShopApiPictureSlider {

    @Autowired
    private PictureSliderService pictureSliderService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<PictureSliderDto> selectListByShopId(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                     @ApiParam(value = "门店id", required = true) @RequestParam("shopId") String shopId) {
        return mapper.mapAsList(pictureSliderService.selectListByShopId(shopId), PictureSliderDto.class);
    }

}
