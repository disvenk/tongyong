package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntityResto;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_appraise_file")
public class AppraiseFile extends BaseEntityResto implements Serializable {

    private static final long serialVersionUID = -8561276482260697283L;

    private String appraiseId;

    private Date createTime;

    private String fileUrl;

    private String shopDetailId;

    private Integer sort;

    private String photoSquare;

    private String fileName;

    private Integer state;

}
