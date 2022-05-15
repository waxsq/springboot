package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.ClassroomSeat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingge.springboot.entity.Seat;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
public interface IClassroomSeatService extends IService<ClassroomSeat> {

    Page<Classroom> getClassroomForPageByCondition(Page<Classroom> classroomPage, String cId, String size, String cLocation);

    Page<Classroom> getClassroomInformationForPageByCondition(Page<Classroom> classroomPage, String cName, String cSize,String cLocation, String name);


    List<Seat> selectClassroomSeatInformationByCId(Integer id);
}
