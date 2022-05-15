package com.qingge.springboot.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.Record;
import com.qingge.springboot.entity.Seat;
import com.qingge.springboot.exception.GlobalExceptionHandler;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.service.IClassroomService;
import com.qingge.springboot.service.IRecordService;
import com.qingge.springboot.service.ISeatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: waxsq
 * @date:
 */
//标记为组件，方便扫描
@Component
@RequestMapping("/seatList")
public class SeatUtil {

    @Resource
    private IClassroomService classroomService;


    @Resource
    private ISeatService seatService;


    @Resource
    private IRecordService recordService;


    /**
     * 同时开启两个定时器
     */

//每天晚上11点生成后天的所有座位信息
    @Scheduled(cron = "0 0 23 * * ?")  //每天晚上23点运行一次  生成所有新的座位
//    @Scheduled(cron = "0 49 21 * * ?")  //每天晚上22点运行一次  生成所有新的座位
    public void generateNextDay(){
        addNewSeat(1);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addNewSeat(int offset){
        try {
            String times[] = {"08点-12点","14点-18点","18点-22点"};   //三个时间段
            Date today = new Date();  //今天日期Date类型
            Date dayAfterTomorrow = getNextNextDay(today,offset); //后面的参数表示与今天的间隔，如1表示明天，2表示后天
            Date dayAfter = getNextNextDay(today,offset+1);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(dayAfterTomorrow);  //后天日期yyy-MM-dd string类型
            String dateAfter = new SimpleDateFormat("yyyy-MM-dd").format(dayAfter);

            //取消预约

            //删除以往过期座位记录表
            Date yesterday = getNextNextDay(today, 0);
            String yesterDay = new SimpleDateFormat("yyyy-MM-dd").format(yesterday);  //日期yyyy-MM-dd string类型
            seatService.removeSeatByYesterday(yesterDay);

            //查询所有教室
            List<Classroom> classroomList = classroomService.list();
//            List<Room> roomList = roomService.findRoom(new Room());
            for(int i=0;i<times.length;i++){               // 三个时间段
                String time = times[i];
                for(int j=0;j<classroomList.size();j++){         //所有的房间
                    Classroom room = classroomList.get(j);
                    int roomid = room.getCId();
                    int row = room.getRow();
                    int col = room.getCol();
                    for(int k=1;k<=row;k++){                 //行
                        for(int l=1;l<=col;l++){             //列
                            Seat seat = new Seat();
                            Seat seat1 = new Seat();
                            seat.setYRow(l);
                            seat.setDate(date);
                            seat.setCId(roomid);
                            seat.setXRow(k);
                            seat.setTime(time);
                            seat.setKeyword(date + "-" + time + "-" + roomid + "-" + k + "-" + l);

                            seat1.setYRow(l);
                            seat1.setDate(dateAfter);
                            seat1.setCId(roomid);
                            seat1.setXRow(k);
                            seat1.setTime(time);
                            seat1.setKeyword(dateAfter + "-" + time + "-" + roomid + "-" + k + "-" + l);
                            try {
                                seatService.save(seat);
                                seatService.save(seat1);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new ServiceException(Constants.CODE_400,"请勿重复生成座位");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping("newSeat")
    @ResponseBody
    public Result today(){
        try{
            Thread thread = new Thread(new Task());
            thread.start();
        }catch (Exception e)
        {
            return Result.error(Constants.CODE_400,"当前教室座位存在，不需要生成");
        }

        return Result.success();
    }

    // 获取后天日期
    public static Date getNextNextDay(Date date,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime();
        return date;
    }



    // 每天的08，14，18即三个时间段的开始时间 运行一次，将之前的占座取消
    @Scheduled(cron = "0 0 8,14,18 * * ?")
//    @Scheduled(cron = "0 05 8,16,18 * * ?")
    public void updateChoice(){
        try {
            //获取当时的日期和时间段
            String nowDate = DateUtil.today();

            String time = null;
            Integer nowHour = DateUtil.thisHour(true);
//            String times[] = {"08点-12点","14点-18点","18点-22点"};   //三个时间段
            if (8 <= nowHour && nowHour <= 13)
            {
                time = "08点-12点";
            } else if ( 14 <= nowHour && nowHour <=18)
            {
                time = "14点-18点";
            } else
            {
                time = "18点-22点";
            }
            //取消今天之前的预约记录
            recordService.modifyRecord(nowDate + '-' + time);
            //取消上一时间段的座位拥有者
            seatService.modifySeatUId(nowDate + '-' + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Task implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            addNewSeat(0);
        }

    }


}
