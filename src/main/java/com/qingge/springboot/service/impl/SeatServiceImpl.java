package com.qingge.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.Record;
import com.qingge.springboot.entity.Seat;
import com.qingge.springboot.mapper.SeatMapper;
import com.qingge.springboot.service.IClassroomService;
import com.qingge.springboot.service.IRecordService;
import com.qingge.springboot.service.ISeatService;
import com.qingge.springboot.utils.SeatUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements ISeatService {

    @Resource
    private SeatMapper seatMapper;

    @Resource
    private IClassroomService classroomService;

    @Resource
    private IRecordService recordService;

    @Override
    public Page<Seat> getAllSeatForPageByCondition(Page<Seat> seatPage, String date, String time, String cname) {
        Page<Seat> seat = seatMapper.getAllSeatForPageByCondition(seatPage, date, time, cname);
        Seat s = null;
        for (int i = 0; i < seat.getRecords().size() ; i++) {
            s = seat.getRecords().get(i);
            String sDate = s.getDate();
            String stime = s.getTime();
            String sCName = s.getCName();
            int freeseat = seatMapper.getFreeSeatByCondition(sDate, stime, sCName);
            seat.getRecords().get(i).setFreeSeat(freeseat);
        }
        return seat;
    }


    @Override
    public List<String> getDateList() {
        return seatMapper.getDateList();
    }

    @Override
    public List<String> getTimeList() {
        return seatMapper.getTimeList();
    }

    @Override
    public List<String> getClassroomList() {
        return seatMapper.getClassroomList();
    }

    @Override
    public Integer getClassroomCIdByCName(String classroom) {
        return seatMapper.getClassroomCIdByCName(classroom);
    }

    @Override
    public int removeSeatByYesterday(String s) {
        return seatMapper.removeSeatByYesterday(s);
    }

    @Override
    public int modifySeatUId(String s) {
        return seatMapper.modifySeatUId(s);
    }

    @Override
    public int UpdateSeat(Seat seat) {
        return seatMapper.modifySeatUId(seat.getKeyword());
    }

    @Override
    public Boolean selectHavingSeat(Integer cId) {
        int i = seatMapper.selectHavingSeat(cId);
        if (i <= 0)
        {
            return true;
        }
        return false;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result choiceSeat(Seat seat) {

        try {
            //先修改seat表的内容
            /**
             * 判断预约是否合理
             */
            String sDate = seat.getDate();
            String sTime = seat.getTime();
            QueryWrapper<Seat> wrapper = new QueryWrapper<>();
            wrapper.eq("keyword", seat.getKeyword());
            Seat one = getOne(wrapper);
            int parseInt = Integer.parseInt(one.getSStatus());
            if ( 1 != parseInt || !one.getUId().equals("0") )
            {
                return Result.error(Constants.CODE_400,"当前位置有人预约");
            }

            String today = DateUtil.today();
            if (today.compareTo(sDate) == 1)
            {
                return Result.error(Constants.CODE_400,"请选择合法日期");
            }

            int hour = DateUtil.thisHour(true);
            String time = seat.getTime();
//       String minTime = time.substring(0,2);
            String maxTime = time.substring(4,6);
            if (Integer.parseInt(maxTime) < hour && (today.compareTo(sDate) == 0))
            {
                return Result.error(Constants.CODE_400,"请选择合法时间段");
            }
            Boolean scoreByCId = null;


            try {
                //判断用户信誉积分
                scoreByCId = classroomService.getClassroomScoreByCId(seat.getCId(), seat.getUId());
            } catch (Exception e) {
                return Result.error(Constants.CODE_400,"非普通用户不能选座位");
            }
            if (!scoreByCId)
            {
                return Result.error(Constants.CODE_400,"当前用户信誉积分不足,请选择其他教室");
            }

            //添加预约记录
            Record record = new Record();
            record.setSeatkeyword(seat.getKeyword());
            record.setStatus("0");
            record.setUId(Integer.parseInt(seat.getUId()));
            record.setTime(DateUtil.now());

            //判断预约表中是否有当前跟同一时间段一样的预约
            Integer i = recordService.getExsitRecord(record);
            if (i != 0 )
            {
                return Result.error(Constants.CODE_400,"当前时间段已经预约了其他教室,请先取消预约记录");
            }
            //保存到记录表
            try {

                //这里应该使用乐观锁
                /**
                 * 步骤一：先查询
                 * 步骤二：使用updateById（Entity）或者update(Entity,wrapper)
                 */
                recordService.saveOrUpdate(record);

//                Seat select = seatMapper.selectById(seat.getKeyword());

                boolean b = saveOrUpdate(seat);
                if (!b)
                {
                    //预约或者更新失败
                    return Result.error(Constants.CODE_400,"预约失败");
                }
            } catch (Exception e) {
                return Result.error(Constants.CODE_400,"写回出错");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Result.error(Constants.CODE_400,e.getMessage());
        }

        return Result.success();
    }




    //生成部分座位

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createDepartSeats(Classroom classroom) {
        try {
            Integer cId = classroom.getCId();
            Boolean aBoolean = selectHavingSeat(cId);
            if (aBoolean)
            {
                return Result.error(Constants.CODE_400,"当前已经生成座位");
            }
            Integer classroomRow = classroom.getRow();
            Integer classroomCol = classroom.getCol();
            Integer roomid = classroom.getCId();
            String times[] = {"08点-12点","14点-18点","18点-22点"};   //三个时间段
            Date today = new Date();  //今天日期Date类型
            Date dayAfterTomorrow = SeatUtil.getNextNextDay(today,0);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(dayAfterTomorrow);  //后天日期yyy-MM-dd string类型
            for (int i = 0 ; i< times.length ; ++i)
            {
                String time = times[i];
                for(int k=1;k<=classroomRow;k++){                 //行
                    for(int l=1;l<=classroomCol;l++){             //列
                        Seat seat = new Seat();
                        seat.setYRow(l);
                        seat.setDate(date);
                        seat.setCId(roomid);
                        seat.setXRow(k);
                        seat.setTime(time);
                        seat.setKeyword(date + "-" + time + "-" + roomid + "-" + k + "-" + l);
                        save(seat);
                    }
                }
            }
        } catch (Exception e) {
            return Result.error(Constants.CODE_400,"当前已经生成座位");
        }
        return Result.success();
    }
}
