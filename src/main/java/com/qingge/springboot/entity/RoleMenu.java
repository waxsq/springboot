package com.qingge.springboot.entity;

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
 * @since 2022-04-08
 */
@Getter
@Setter
@ToString
  @TableName("s_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 身份标识
     */
        private Integer roleId;

      /**
     * 菜单标识
     */
        private Integer menuId;


}
