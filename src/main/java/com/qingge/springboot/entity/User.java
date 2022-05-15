package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Insert;

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
  @TableName("s_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 学生的唯一标识
     */
        @TableId(value = "u_id", type = IdType.AUTO)
      private Integer uId;

      /**
     * 账号为11位，可以只能以数字或者字符开头
     */
      private String username;

      /**
     * 密码字段不能小于6位或者大于11位，可以为任意字符
     */
      private String password;

      /**
     * 用户创建时间
     */
      private String createTime;

      /**
     * 用户身份标识，主要有ROLE_ADMIN和ROLE_USER
     */
      private String role;

      @TableField(exist = false)
      private String Token;

      @TableField(exist = false)
      private List<Menu> roleMeus;

      private String email;

      private String phone;

      //用户积分
      private Integer score;

      //用户扣分事项
      @TableField(exist = false)
      private List<RewardPunishment> rewardPunishmentList;

      //学号
      private String uNumber;

      private String secretKey;


}
