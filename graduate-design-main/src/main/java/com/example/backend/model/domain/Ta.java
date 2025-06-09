package com.example.backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName ta
 */
@TableName(value = "ta")
@Data
public class Ta implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Integer maxHoursPerWeek;

    /**
     *
     */
    private Integer maxHoursPerYear;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Ta() {

    }

    public Ta(String name, int maxHoursPerWeek, int maxHoursPerYear) {
        this.name = name;
        this.maxHoursPerWeek = maxHoursPerWeek;
        this.maxHoursPerYear = maxHoursPerYear;
    }
}