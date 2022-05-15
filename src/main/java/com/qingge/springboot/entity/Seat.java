package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
  @TableName("w_seat")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 座位的横坐标
     */
      private Integer xRow;

      /**
     * 座位的纵坐标
     */
      private Integer yRow;

      /**
     * 教室id
     */
      private Integer cId;


      /**
     * 时间段
     */
      private String time;

      /**
     * 日期
     */
      private String date;

      /**
     * 有日期-时间段-教室id-座位的行与列
     */
      @TableId(value = "keyword")
      private String keyword;

      //标识空座
      private String sStatus;

      @TableField(exist = false)
      private Integer freeSeat;

      @TableField(exist = false)
      //教室名称
      private String cName;

      private String uId;

      //乐观锁
      @Version
      @TableField(fill = FieldFill.INSERT_UPDATE)
      private int version;


}
