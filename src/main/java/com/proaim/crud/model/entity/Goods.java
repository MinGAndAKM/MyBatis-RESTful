package com.proaim.crud.model.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Goods {
    private Integer id;

    private String name;

    private BigDecimal price;

    /*@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;

    private String picfile;

    private String remark;

    private Integer status;
}
