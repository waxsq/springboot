package com.qingge.springboot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.ClassroomSeat;
import com.qingge.springboot.entity.Record;
import com.qingge.springboot.entity.Seat;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.mapper.ClassroomMapper;
import com.qingge.springboot.mapper.RecordMapper;
import com.qingge.springboot.mapper.SeatMapper;
import com.qingge.springboot.mapper.UserMapper;
import com.qingge.springboot.service.IClassroomSeatService;
import com.qingge.springboot.service.IClassroomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements IClassroomService {

    @Resource
    private IClassroomSeatService classroomSeatService;

    @Resource
    private ClassroomMapper classroomMapper;

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SeatMapper seatMapper;

    @Override
    public Page<Classroom> selectAllClassroomByCondition(Page<Classroom> classroomPage, Integer cId, Integer size, String cLocation) {
        return classroomMapper.selectClassroomByConition(classroomPage,cId,size,cLocation);
    }

    @Override
    @Transactional
    public int deleteClassroomById(Integer id) {
        //先判断是否有人预约了教室,不能删除
            List<Seat> seatList = classroomSeatService.selectClassroomSeatInformationByCId(id);
            //判断座位中是否有人预约
            List<Seat> seatInteger = seatList.stream().filter(seat -> "0".equals(seat.getSStatus())).collect(Collectors.toList());
            //有人预约
            if (seatInteger.size() != 0)
            {
                return 0;
            }
            //先清空座位
            QueryWrapper<Seat> wrapper = new QueryWrapper<>();
            wrapper.eq("c_id",id);
            seatMapper.delete(wrapper);
            classroomMapper.deleteById(id);
            return 1;

    }

    @Transactional
    @Override
    public int deleteClassroomBatchByIds(List<Integer> ids) {
        int i = classroomMapper.deleteBatchIds(ids);
        return i;
    }

    @Override
    public Classroom queryClassroomByCId(Integer cId) {

        return classroomMapper.queryClassroomByCId(cId);
    }



    @Override
    public Page<Classroom> getClassroomInformationByCondition(Page<Classroom> classroomPage, String cName, String size, String cLocation, String name) {
        return classroomMapper.getClassroomInformationByCondition(classroomPage,cName,size,cLocation,name);

    }



    @Override
    public Boolean getClassroomScoreByCId(Integer cId, String uId) {
        Integer score = classroomMapper.getClassroomScore(cId);
        Integer userScore = userMapper.getUserScore(uId);
        if (userScore >= score)
        {
            return true;
        }
        return false;
    }



    //修改,先判断当前教室是否存在预约，否则修改失败
    //然后删除教室
    //然后重新创建教室，和座位

    @Transactional
    @Override
    public int updateClassroomByEntity(Classroom classroom) {
        //判断当前阅览室是否有预约
        List<Classroom> classroomList = classroomMapper.selectList(new QueryWrapper<Classroom>()
                .eq("c_id", classroom.getCId())
                .eq("s_status", "0"));
        if (!CollectionUtil.isEmpty(classroomList))
        {
            return 0;
        }
        //清空已空教室座位和教室
        int i = deleteClassroomById(classroom.getCId());
        if (i == 0)
        {
            return i;
        }
        //创建新教室
        classroom.setCId(null);
        classroom.setCreateTime(DateUtil.now());
        classroomMapper.insert(classroom);
        return 1;
    }
}
