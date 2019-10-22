package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiExperience;
import com.resto.api.shop.dto.ExperienceDto;
import com.resto.api.shop.dto.RecommendCategoryDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.Experience;
import com.resto.service.shop.service.ExperienceService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xielc on 2018/1/4.
 */
@RestController
public class ExperienceController implements ShopApiExperience {

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<ExperienceDto> selectListByShopId(@ApiParam(value = "品牌id", required = true)@RequestParam("brandId") String brandId,
                                                  @ApiParam(value = "店铺shopId", required = true) @RequestParam("shopId")String shopId){
        return mapper.mapAsList(experienceService.selectListByShopId(shopId),ExperienceDto.class);
    }

    @Override
    public ExperienceDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId, @ApiParam(value = "体验id", required = true) @RequestParam("id") Integer id) {
        return mapper.map(experienceService.selectById(id),ExperienceDto.class);
    }
}
