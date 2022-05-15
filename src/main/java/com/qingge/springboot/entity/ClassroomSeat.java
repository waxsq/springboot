package com.qingge.springboot.entity;

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
 * @since 2022-04-10
 */
@Getter
@Setter
  @TableName("w_classroom_seat")
public class ClassroomSeat implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 教室id
     */
        private Integer cId;

      private Integer sId;


}
