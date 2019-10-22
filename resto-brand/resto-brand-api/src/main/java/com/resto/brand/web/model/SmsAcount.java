package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

public class SmsAcount implements Serializable {
    private String id;
    private String brandId;
    private String smsSign;//短信标识
    private Integer remainderNum;//品牌剩余短信条数
    private Integer UsedNum;//品牌已使用短信条数
	private BigDecimal smsUnitPrice;//短信单价
	
	private BigDecimal totalAmcount; //发票总金额
	private BigDecimal usedAmcount;//已开开发的总金额
	private BigDecimal remainerAmcount;//剩余发票可开金额
	
	//新增字段 设置品牌 短信提醒的设置
	private String smsRemind;

	private String smsNoticeTelephone;

	public String getSmsNoticeTelephone() {
		return smsNoticeTelephone;
	}

	public void setSmsNoticeTelephone(String smsNoticeTelephone) {
		this.smsNoticeTelephone = smsNoticeTelephone;
	}

	public String getSmsRemind() {
		return smsRemind;
	}
	public void setSmsRemind(String smsRemind) {
        this.smsRemind = stringtoSort(smsRemind);
	}
	//关联查询
	private String brandName;//品牌名称
	
	public BigDecimal getTotalAmcount() {
		return totalAmcount;
	}
	public void setTotalAmcount(BigDecimal totalAmcount) {
		this.totalAmcount = totalAmcount;
	}
	public BigDecimal getUsedAmcount() {
		return usedAmcount;
	}
	public void setUsedAmcount(BigDecimal usedAmcount) {
		this.usedAmcount = usedAmcount;
	}
	public BigDecimal getRemainerAmcount() {
		return remainerAmcount;
	}
	public void setRemainerAmcount(BigDecimal remainerAmcount) {
		this.remainerAmcount = remainerAmcount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getSmsSign() {
		return smsSign;
	}
	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}
	public Integer getRemainderNum() {
		return remainderNum;
	}
	public void setRemainderNum(Integer remainderNum) {
		this.remainderNum = remainderNum;
	}
	public Integer getUsedNum() {
		return UsedNum;
	}
	public void setUsedNum(Integer usedNum) {
		UsedNum = usedNum;
	}
	public BigDecimal getSmsUnitPrice() {
		return smsUnitPrice;
	}
	public void setSmsUnitPrice(BigDecimal smsUnitPrice) {
		this.smsUnitPrice = smsUnitPrice;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


    private  static  String stringtoSort(String s){
        //1将字符串切割成字符串数组
        String [] str_nums = stringToArray(s);
        //2 将字符串数组转成int数组
        int[] arr = toIntArray(str_nums);
        //3.排序(按照从大到小)
         sortArrays(arr);
        //4排序完的数组转成字符串数组
        String s1 = arrayToString(arr);
        //字符串反转(用来从大到小)
        return  s1;
    }



    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<arr.length;i++){
            if(i!=arr.length-1){
                sb.append(arr[i]+",");
            }else {
                sb.append(arr[i]);
            }
        }
        return  sb.toString();
    }

    private static void sortArrays(int[] arr) {
       // Arrays.sort(arr);
        for(int i=0;i<arr.length-1;i++){
            for(int j=i+1;j<arr.length;j++){
                if(arr[i]<arr[j]){
                    int temp=arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
    }

    private static int[] toIntArray(String[] str_nums) {
        int[] arr = new int[str_nums.length];
        for(int i = 0;i<str_nums.length ; i++){
            arr[i] = Integer.parseInt(str_nums[i]);
        }
        return  arr;
    }

    private static String[] stringToArray(String s) {
        return  s.replaceAll("，",",").split(",");
       // return s.split(",");
    }

}