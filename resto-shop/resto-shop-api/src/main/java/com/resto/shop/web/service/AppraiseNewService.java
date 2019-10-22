package com.resto.shop.web.service;


import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.dto.AppraiseNewBrandDto;
import com.resto.shop.web.dto.AppraiseNewShopDetailDto;
import com.resto.shop.web.dto.AppraiseNewShopDto;
import com.resto.shop.web.exception.AppException;
import java.util.List;

public interface AppraiseNewService extends GenericService<AppraiseNew, Long> {

    String addAppraise(AppraiseNew  appraisenew) throws AppException;

    List<AppraiseNewBrandDto> selectAppraiseNewBrand(String beginDate, String endDate, String brandId);

    List<AppraiseNewShopDto> selectAppraiseNewShop(String beginDate, String endDate, String shopId);

    List<AppraiseNewShopDetailDto> selectAppraiseNewShopDetail(String beginDate, String endDate, String shopId);

}
