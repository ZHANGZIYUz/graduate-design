package com.example.backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName teaching_session
 */
@TableName(value ="teaching_session")
@Data
public class TeachingSession implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long moduleId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer hoursPaidPerOccurrence;

    /**
     * 
     */
    private Integer numTAsPerSession;

    /**
     * 
     */
    private String time_table_weeks;

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

    public TeachingSession() {

    }

    public TeachingSession(Long moduleId, String name, Integer hoursPaidPerOccurrence, Integer numTAsPerSession, String time_table_weeks) {
        this.moduleId = moduleId;
        this.name = name;
        this.hoursPaidPerOccurrence = hoursPaidPerOccurrence;
        this.numTAsPerSession = numTAsPerSession;
        this.time_table_weeks = time_table_weeks;
    }
}