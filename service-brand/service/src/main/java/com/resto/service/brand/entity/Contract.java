package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Contract implements Serializable{

    private static final long serialVersionUID = -3591333838855003506L;

    private Long id;

    //合同编号
    private String constractNum;

    //品牌名称
    private String brandName;

    //店铺数量
    private Integer shopNum;

    //签约时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    //签约金额
    private BigDecimal signMoney;

    //甲方公司
    private String aCompanyName;

    //甲方签约人
    private String aSigntory;

    //甲方签约人
    private String aTelephone;

    //甲方 邮箱
    @Email
    private String aEmail;

    //乙方公司名称
    private String bCompanyName;

    //乙方签约人
    private String bSigntory;

    //乙方手机号
    private String bTelephone;

    //乙方邮箱
    @Email
    @NotEmpty
    private String bEmail;

    //计费方式 1时间 2效果
    private Integer chargeMode;

    //已收款
    private BigDecimal receiveMoney;

    //未收款
    private BigDecimal unreceiveMoney;

    //每年收
    private BigDecimal yearMoney;

    //上线时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    //到期时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationTime;

    //有效期
    private Integer validity;

    //短信条数
    private Integer smsNum;

    //状态
    private Integer status;

    //合同附件
    private String picUrl;

    //账户余额
    private BigDecimal accountBalance;

    //已使用余额
    private BigDecimal usedBalance;

    //是否开启新用户注册
    private Byte openNewCustomerRegister;

    //每个新用户扣费
    private BigDecimal newCustomerValue;

    //是否开启短信付费
    private Byte openSendSms;

    //每条短信价格
    private BigDecimal sendSmsValue;

    //所有订单 0不开启 1.订单总额 2实付金额
    private Byte openAllOrder;

    //所有订单占比
    private Double allOrderValue;

	//回头用户订单 0不开启 1.回头用户订单总额 2实付金额
    private Byte openBackCustomerOrder;

	//回头用户订单占比
    private BigDecimal backCustomerOrderValue;

    private Byte openOutFoodOrder;

    private BigDecimal outFoodOrderValue;

    private Byte openThirdFoodOrder;

    private Double thirdFoodOrderValue;

    //创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    private Date updateTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")

    //更新人
    private String updateUser;

    //店铺名称 多个店铺用","隔开
    private String shopNames;

    //一个合同可以有多个收入
    List<Income> incomeList ;

    //剩余可开发票
    private BigDecimal invoiceMoney;


    //已开发票
    private BigDecimal hasInvoiceMoney;

}