package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Classroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
public interface ClassroomMapper extends BaseMapper<Classroom> {

    Page<Classroom> selectClassroomByConition(Page<Classroom> classroomPage, Integer cId, Integer size, String cLocation);

    Classroom queryClassroomByCId( @Param("cId") Integer cId);

    Page<Classroom> getClassroomInformationByCondition(Page<Classroom> classroomPage, String cName, String size, String cLocation, String name);

    Page<Classroom> getClassroomForPageByCondition(Page<Classroom> classroomPage, String cId, String size, String cLocation);

    @Select("select score from w_classroom c left join w_classroom_type t on c.t_id = t.t_id where c_id = #{cId}")
    Integer getClassroomScore(Integer cId);

    @Select("select c_id from w_classroom where c_name =  #{cname}" )
    Integer selectCIdByCName(String cname);

    @Select("select c_name from w_classroom where c_id = #{substring}")
    String selectCNameByCId(String substring);
}
