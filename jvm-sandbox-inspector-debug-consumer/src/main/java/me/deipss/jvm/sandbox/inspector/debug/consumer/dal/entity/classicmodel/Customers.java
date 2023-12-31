package me.deipss.jvm.sandbox.inspector.debug.consumer.dal.entity.classicmodel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "customers")
public class Customers {
    @TableId(value = "customerNumber", type = IdType.INPUT)
    private Integer customernumber;

    @TableField(value = "customerName")
    private String customername;

    @TableField(value = "contactLastName")
    private String contactlastname;

    @TableField(value = "contactFirstName")
    private String contactfirstname;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "addressLine1")
    private String addressline1;

    @TableField(value = "addressLine2")
    private String addressline2;

    @TableField(value = "city")
    private String city;

    @TableField(value = "`state`")
    private String state;

    @TableField(value = "postalCode")
    private String postalcode;

    @TableField(value = "country")
    private String country;

    @TableField(value = "salesRepEmployeeNumber")
    private Integer salesrepemployeenumber;

    @TableField(value = "creditLimit")
    private BigDecimal creditlimit;
}