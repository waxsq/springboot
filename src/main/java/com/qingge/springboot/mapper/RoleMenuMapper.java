package com.qingge.springboot.mapper;

import com.qingge.springboot.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Select("select menu_id from s_role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId")Integer roleId);

    @Delete("delete from s_role_menu where role_id = #{roleId}")
    int deleteByRoleId(Integer roleId);
}
