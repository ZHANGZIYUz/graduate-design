package com.example.backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName room
 */
@TableName(value = "room")
@Data
public class Room implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String campus;

    /**
     *
     */
    private String location;

    /**
     *
     */
    private Integer capacity;

    /**
     *
     */
    private Integer available;

    /**
     *
     */
    private Date create_time;

    /**
     *
     */
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Room() {
    }

    public Room(String campus, String location, Integer capacity) {
        this.campus = campus;
        this.location = location;
        this.capacity = capacity;
    }
}