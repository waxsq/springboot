package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-04-13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
  @TableName("w_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 文章标题
     */
      private String name;

      /**
     * 内容
     */
      private String content;

    private String  user;

    private String publishtime;


}
