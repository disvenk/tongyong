package com.resto.service.shop.controller;

import com.resto.api.shop.define.api.ShopApiAppraiseComment;
import com.resto.api.shop.dto.AppraiseCommentDto;
import com.resto.core.OrikaBeanMapper;
import com.resto.service.shop.entity.AppraiseComment;
import com.resto.service.shop.service.AppraiseCommentService;
import com.resto.service.shop.service.AppraisePraiseService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppraiseCommentController implements ShopApiAppraiseComment {

    @Autowired
    private AppraisePraiseService appraisePraiseService;
    @Autowired
    private AppraiseCommentService appraiseCommentService;
    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public List<AppraiseCommentDto> appraiseCommentList(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                                        @ApiParam(value = "点赞id", required = true) @RequestParam("id") String id) {
        return mapper.mapAsList(appraisePraiseService.appraisePraiseList(id), AppraiseCommentDto.class);
    }

    @Override
    public AppraiseCommentDto insertComment(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                            AppraiseCommentDto appraiseCommentDto) {
        AppraiseComment appraiseComment = mapper.map(appraiseCommentDto, AppraiseComment.class);
        return mapper.map(appraiseCommentService.insertComment(appraiseComment), AppraiseCommentDto.class);
    }

    @Override
    public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam("brandId") String brandId,
                                     @ApiParam(value = "点赞id", required = true) @RequestParam("customerId") String customerId) {
        return appraiseCommentService.selectByCustomerCount(customerId);
    }

}
