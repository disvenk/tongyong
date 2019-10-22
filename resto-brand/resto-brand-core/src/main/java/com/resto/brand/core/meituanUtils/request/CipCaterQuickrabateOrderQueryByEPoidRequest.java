package com.resto.brand.core.meituanUtils.request;

import com.resto.brand.core.meituanUtils.domain.RequestDomain;
import com.resto.brand.core.meituanUtils.domain.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuibaosen on 16/8/17.
 */
public class CipCaterQuickrabateOrderQueryByEPoidRequest extends CipCaterStringPairRequest {
    private String date;
    private Integer page;
    private Integer pageSize;

    public CipCaterQuickrabateOrderQueryByEPoidRequest() {
        this.url = RequestDomain.preUrl.getValue() + "/shanhui/order/queryListByEpoiId";
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<String, String>() {{
            put("date", date);
            put("page", page.toString());
            put("pageSize", pageSize.toString());
        }};
    }

    @Override
    public boolean paramsAbsent() {
        return date == null || date.trim().isEmpty() || page == null || pageSize == null;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
