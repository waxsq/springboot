package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingge.springboot.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
public interface IClassroomService extends IService<Classroom> {

    Page<Classroom> selectAllClassroomByCondition(Page<Classroom> classroomPage, Integer cId, Integer size, String cLocation);

    int deleteClassroomById(Integer id);

    int deleteClassroomBatchByIds(List<Integer> ids);

    Classroom queryClassroomByCId(Integer cId);

    //查询教室信息（分页，条件）
    Page<Classroom> getClassroomInformationByCondition(Page<Classroom> classroomPage, String cName, String size, String cLocation, String name);

    Boolean getClassroomScoreByCId(Integer cId, String uId);

    int updateClassroomByEntity(Classroom classroom);
}
