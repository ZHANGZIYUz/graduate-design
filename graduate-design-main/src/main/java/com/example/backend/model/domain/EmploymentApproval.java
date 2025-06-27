package com.example.backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName employment_approval
 */
@TableName(value = "employment_approval")
@Data
public class EmploymentApproval implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long taId;

    /**
     *
     */
    private Long moduleId;

    /**
     *
     */
    private Object rating;

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

    public EmploymentApproval() {

    }

    public EmploymentApproval(Long taId, Long moduleId, Object rating) {
        this.taId = taId;
        this.moduleId = moduleId;
        this.rating = rating;
    }
}