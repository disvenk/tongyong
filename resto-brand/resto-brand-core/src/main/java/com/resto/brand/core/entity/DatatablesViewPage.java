package com.resto.brand.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * name:yjuany
 * 分页
 */
public class DatatablesViewPage<T> implements Serializable {
    private List<T> aaData; //aaData 与datatales 加载的“dataSrc"对应
    private int iTotalDisplayRecords;
    private int iTotalRecords;






    public DatatablesViewPage() {
    }

    public DatatablesViewPage(List<T> aaData, int iTotalDisplayRecords, int iTotalRecords) {
        this.aaData = aaData;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.iTotalRecords = iTotalRecords;
    }

    public List<T> getAaData() {
        return aaData;
    }

    public void setAaData(List<T> aaData) {
        this.aaData = aaData;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }
}