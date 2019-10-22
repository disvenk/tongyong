package com.resto.wechat.web.validate;


import com.resto.api.customer.entity.CustomerAddress;

/**
 * Created by xielc on 2017/6/12.
 */
public class CustomerAddressValidate {

    public static String customerAddressValidate(CustomerAddress customerAddress){
        String errorText = "";
        String name=customerAddress.getMobileNo();
        Integer sex=customerAddress.getSex();
        String mobileNo=customerAddress.getMobileNo();
        String address=customerAddress.getAddress();
        String addressReality=customerAddress.getAddress();
        //String customerId=customerAddress.getCustomerId();

        if(name==null || name.equals(""))
            errorText+="不能为空！\n";
        else if (name.length()<=4)
            errorText+="联系人长度为1~4！\n";
        if(sex==null || sex.equals(""))
            errorText+="性别不能为空！\n";
        if(mobileNo==null || mobileNo.equals(""))
            errorText+="手机号不能为空！\n";
        else if(!Validate.checkMobileFormat(mobileNo))
            errorText+="手机号格式不正确！\n";
        if(address==null || address.equals(""))
            errorText+="地址不能为空！\n";
        if(addressReality==null || addressReality.equals(""))
            errorText+="配送地址不能为空！\n";
        /*if(customerId==null || customerId.equals(""))
            errorText+="用户id不能为空！\n";*/
		/*if(mobile==null || mobile.equals(""))
			errorText+="手机号不能为空！\n";
		else if(!Validate.checkMobileFormat(mobile))
			errorText+="手机号格式不正确！\n";
		if(email==null || email.equals(""))
			errorText+="邮箱不能为空！\n";
		if(!email.equals("")){
			if(!Validate.checkEmailFormat(email)) errorText+="邮箱格式不正确！\n";
		}*/
        return errorText;
    }
}
