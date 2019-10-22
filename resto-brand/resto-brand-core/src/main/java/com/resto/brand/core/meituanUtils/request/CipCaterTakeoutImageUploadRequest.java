package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/18.
 */
public class CipCaterTakeoutImageUploadRequest extends CipCaterStreamRequest {
    private String imageName;
    private String ePoiId;
    private File file;

    public CipCaterTakeoutImageUploadRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/waimai/image/upload";
        this.requestMethod = RequestMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("imageName", imageName);
            put("ePoiId", ePoiId);
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return imageName == null || imageName.trim().isEmpty() || ePoiId == null || ePoiId.trim().isEmpty() || file == null;
    }

    @Override
    public Map<String, File> getFiles() {
        return new HashMap<String, File>() {{
            put("file", file);
        }};
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
