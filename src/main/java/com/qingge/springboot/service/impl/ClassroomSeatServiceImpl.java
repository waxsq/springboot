package com.qingge.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.ClassroomSeat;
import com.qingge.springboot.entity.Seat;
import com.qingge.springboot.mapper.ClassroomMapper;
import com.qingge.springboot.mapper.ClassroomSeatMapper;
import com.qingge.springboot.mapper.SeatMapper;
import com.qingge.springboot.service.IClassroomSeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
@Service
public class ClassroomSeatServiceImpl extends ServiceImpl<ClassroomSeatMapper, ClassroomSeat> implements IClassroomSeatService {

    @Resource
    private ClassroomMapper classroomMapper;

    @Resource
    private SeatMapper seatMapper;

    @Override
    public Page<Classroom> getClassroomForPageByCondition(Page<Classroom> classroomPage, String cId, String size, String cLocation) {
        return classroomMapper.getClassroomForPageByCondition(classroomPage,cId,size,cLocation);
    }

    @Override
    public Page<Classroom> getClassroomInformationForPageByCondition(Page<Classroom> classroomPage, String cName, String cSize, String cLocation, String name) {
        return classroomMapper.getClassroomInformationByCondition(classroomPage,cName,cSize,cLocation,name);
    }

    //查询当前删除的阅览室是否存在预约记录

    @Override
    public List<Seat> selectClassroomSeatInformationByCId(Integer id) {
        QueryWrapper<Seat> seatQueryWrapper = new QueryWrapper<>();
        seatQueryWrapper.eq("c_id",id);
        List<Seat> seats = seatMapper.selectList(seatQueryWrapper);
        return seats;
    }
}
