package com.resto.api.customer.entity;

import com.resto.conf.db.BaseEntity;
import com.resto.conf.db.BaseEntityResto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_customer_group")
public class CustomerGroup extends BaseEntity implements Serializable {

    @ApiModelProperty("组号")
    @Column(name = "group_id")
    private String groupId;

    @ApiModelProperty("用户id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("用户昵称")
    @Column(name = "customer_name")
    private String customerName;

    @ApiModelProperty("微信头像地址")
    @Column(name = "head_photo")
    private String headPhoto;

    @ApiModelProperty("是否是组长，0不是 1是")
    @Column(name = "is_leader")
    private Boolean isLeader;

    @ApiModelProperty("桌号")
    @Column(name = "table_number")
    private String tableNumber;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @Column(name = "brand_id")
    private String brandId;

    private static final long serialVersionUID = 1L;
}