package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
  @TableName("w_record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uId;

    private String seatkeyword;

      /**
     * 预约id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 预约时间
     */
      private String time;

      /**
     * 当前记录是否有效:1标识不生效，0表示生效
     */
      private String status;

      @TableField(exist = false)
    private String username;

      @TableField(exist = false)
    private String cname;

      @TableField(exist = false)
    private String seat;


}
