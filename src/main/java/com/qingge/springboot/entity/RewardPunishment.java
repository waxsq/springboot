package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
  @TableName("w_reward_punishment")
public class RewardPunishment implements Serializable {

    private static final long serialVersionUID = 1L;


  /**
   * 自增主键
   */
    @TableId(value = "rp_id", type = IdType.AUTO)
    private Integer rpId;


  /**
     * 发布时间
     */
      private String datatime;

      /**
     * 奖惩情况
     */
      private String situation;

      /**
     * 对谁进行操作
     */
      private Integer uId;

      private Integer rpScore;


}
