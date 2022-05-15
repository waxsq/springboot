package com.qingge.springboot.mapper;

import com.qingge.springboot.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
public interface MenuMapper extends BaseMapper<Menu> {


    @Update("update s_menu set pid = null where m_id = #{mId}")
    int updatePIdForNullById(Integer mId);
}
