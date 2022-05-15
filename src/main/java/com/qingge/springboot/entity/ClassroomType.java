package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-12
 */
@Getter
@Setter
@ToString
  @TableName("w_classroom_type")
public class ClassroomType implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "t_id", type = IdType.AUTO)
      private Integer tId;

    private String name;

    private Integer score;




}
