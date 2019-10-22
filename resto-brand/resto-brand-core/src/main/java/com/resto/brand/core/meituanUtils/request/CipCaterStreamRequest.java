package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.utils.SignUtils;

import java.io.File;
import java.util.Map;

/**
 * 流形式的请求
 * <p>
 * Created by cuibaosen on 16/8/18.
 */
public abstract class CipCaterStreamRequest extends CipCaterRequest {
    /**
     * 获取要上传的文件参数
     */
    public abstract Map<String, File> getFiles();

    /**
     * 根据公共参数和每个request独有的参数计算sign值
     * <p>
     * formdata形式post请求,只对get参数进行校验
     */
    @Override
    public String getSign() {
        return SignUtils.createSign(requestSysParams.getSecret(), this.getSharedParams());
    }
}
