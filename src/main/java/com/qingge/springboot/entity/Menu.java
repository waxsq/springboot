package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
  @TableName("s_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "m_id", type = IdType.AUTO)
      private Integer mId;

      /**
     * 菜单名称
     */
      private String mName;

      /**
     * 路由位置
     */
      private String path;

      /**
     * 图标
     */
      private String icon;

      /**
     * 一级菜单对象的id
     */
      private Integer pid;

      /**
     * 页面名称
     */
      private String pagePage;

      @TableField(exist = false)
      private List<Menu> children;




}
