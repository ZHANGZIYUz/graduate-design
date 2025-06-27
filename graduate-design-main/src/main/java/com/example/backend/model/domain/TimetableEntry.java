package com.example.backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName timetable_entry
 */
@TableName(value = "timetable_entry")
@Data
public class TimetableEntry implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long sessionId;

    /**
     *
     */
    private Long roomId;

    /**
     *
     */
    private String weekDay;

    /**
     *
     */
    private String startTime;

    /**
     *
     */
    private String endTime;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Integer allocated;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public TimetableEntry() {

    }

    public TimetableEntry(Long sessionId, Long roomId, String weekDay, String startTime, String endTime) {
        this.sessionId = sessionId;
        this.roomId = roomId;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}