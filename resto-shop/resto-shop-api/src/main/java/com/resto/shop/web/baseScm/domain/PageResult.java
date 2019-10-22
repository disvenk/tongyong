package com.resto.shop.web.baseScm.domain;



import java.util.List;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public class PageResult<E> extends BaseParms {
    private List<E> result;

    public void setResult(List<E> result) {
        this.result = result;
    }

    public List<E> getResult() {
        return result;
    }

    public PageResult() {
    }
}
