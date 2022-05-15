package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@Getter
@Setter
  @TableName("s_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 用户身份的主键
     */
        @TableId(value = "r_id", type = IdType.AUTO)
      private Integer rId;

      /**
     * 角色名称
     */
      private String name;

      /**
     * 角色描述
     */
      private String description;

      /**
     * 身份标识
     */
      private String flag;


}
