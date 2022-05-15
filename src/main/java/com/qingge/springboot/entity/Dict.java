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
  @TableName("s_dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 图标名称
     */
      private String name;

      /**
     * 图标链接
     */
      private String value;


}
