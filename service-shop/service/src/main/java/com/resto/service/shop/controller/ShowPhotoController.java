package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiShowPhoto;
import com.resto.api.shop.dto.ShowPhotoDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.ShowPhoto;
import com.resto.service.shop.service.ShowPhotoService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowPhotoController implements ShopApiShowPhoto {

    @Autowired
    private ShowPhotoService showPhotoService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public ShowPhotoDto selectById(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                   @ApiParam(value = "菜品id", required = true) @RequestParam("id") Integer id) {
        return mapper.map(showPhotoService.selectById(id), ShowPhotoDto.class);
    }

    @Override
    public int update(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                      ShowPhotoDto showPhotoDto) {
        return showPhotoService.update(mapper.map(showPhotoDto,ShowPhoto.class));
    }

}
