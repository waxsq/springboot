package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
//分布查询结果返回json
@JsonIgnoreProperties(value = { "handler" })
@Getter
@Setter
@ToString
  @TableName("w_classroom")
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 教室唯一id
     */
        @TableId(value = "c_id", type = IdType.AUTO)
      private Integer cId;

      /**
     * 创建时间
     */
      private String createTime;

      /**
     * 教室容量
     */
      private Integer size;

      /**
     * 教室具体地址
     */
      private String cLocation;

    private Integer row;

    private Integer col;


    //教室名称
    private String cName;


    private Integer tId;


    //教室类型名称
    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private Integer score;

    @TableField(exist = false)
    private List<Seat> seatList;

}
