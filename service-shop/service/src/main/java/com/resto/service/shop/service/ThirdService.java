package com.resto.service.shop.service;

import com.resto.api.brand.dto.BrandSettingDto;
import com.resto.api.brand.dto.PlatformDto;
import com.resto.service.shop.constant.PlatformName;
import com.resto.service.shop.constant.PushType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ThirdService {

    public Boolean orderAccept(Map map, BrandSettingDto brandSetting) {
        String pushType = map.get("pushType").toString();
        String brandId = map.get("brandId").toString();
//        List<PlatformDto> platformList = platformService.selectByBrandId(brandId);
        List<PlatformDto> platformList = new ArrayList<>();
        Boolean result = false;
        if (CollectionUtils.isEmpty(platformList)) {
            return false;
        }

        try {
            switch (pushType) {
                case PushType.HUNGER:
                    //饿了吗推送接口
                    Boolean check = false;
                    for (PlatformDto platform : platformList) {
                        if (platform.getName().equals(PlatformName.E_LE_ME)) {
                            check = true;
                            break;
                        }
                    }
                    if (check) {
//                        result = hungerPush(map, brandSetting, brandId);
                    }
                    break;
                case PushType.HUNGER_VERSION_2:
                    check = false;
                    for (PlatformDto platform : platformList) {
                        if (platform.getName().equals(PlatformName.E_LE_ME)) {
                            check = true;
                            break;
                        }
                    }
                    if (check) {
//                        result = hungerPushVersion2(map, brandId);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }

}
