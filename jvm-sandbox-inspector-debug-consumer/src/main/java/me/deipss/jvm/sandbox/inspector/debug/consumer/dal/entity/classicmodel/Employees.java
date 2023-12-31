package me.deipss.jvm.sandbox.inspector.debug.consumer.dal.entity.classicmodel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "employees")
public class Employees {
    @TableId(value = "employeeNumber", type = IdType.INPUT)
    private Integer employeenumber;

    @TableField(value = "lastName")
    private String lastname;

    @TableField(value = "firstName")
    private String firstname;

    @TableField(value = "extension")
    private String extension;

    @TableField(value = "email")
    private String email;

    @TableField(value = "officeCode")
    private String officecode;

    @TableField(value = "reportsTo")
    private Integer reportsto;

    @TableField(value = "jobTitle")
    private String jobtitle;
}