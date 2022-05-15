package com.qingge.springboot.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.Record;
import com.qingge.springboot.entity.Seat;
import com.qingge.springboot.service.IClassroomService;
import com.qingge.springboot.service.IFileService;
import com.qingge.springboot.service.IRecordService;
import com.qingge.springboot.service.ISeatService;
import com.qingge.springboot.utils.SeatUtil;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/seat")
public class SeatController {

    @Resource
    private ISeatService seatService;

    @Resource
    private IClassroomService classroomService;

    @Resource
    private IRecordService recordService;

    /**
     * 部分生成座位
     */
    @PostMapping
    public Result createSeatByCId(@RequestBody Classroom classroom)
    {
        return seatService.createDepartSeats(classroom);
    }


    /**
     * 显示各教室的空闲量
     */
    @GetMapping("/page")
    public Result getAllSeatForPageByCondition(@RequestParam Integer pageNum,
                                               @RequestParam Integer pageSize,
                                               @RequestParam("") String date,
                                               @RequestParam("") String time,
                                               @RequestParam("") String cname)
    {
         Page<Seat> page = seatService.getAllSeatForPageByCondition(new Page<Seat>(pageNum,pageSize),date,time,cname);
         return Result.success(page);
    }


//获取可选日期
    @GetMapping("/getDate")
    public Result getDate()
    {
        List<String> dateList = seatService.getDateList();
        return Result.success(dateList);
    }

//    获取可选时间
    @GetMapping("/getTime")
    public Result getTime()
    {
        List<String> timeList = seatService.getTimeList();
        return Result.success(timeList);
    }

    @GetMapping("/getClassroom")
    //获取可选教室
    public Result getClassroom()
    {
        List<String> timeList = seatService.getClassroomList();
        return Result.success(timeList);
    }

    //获取当前教室所有座位信息
    @GetMapping("/getAll")
    public Result getAll(@RequestParam String date,
                         @RequestParam String time,
                         @RequestParam String classroom)
    {
//        System.out.println("___________________________________________"+date+time+classroom);
        Integer cIds = seatService.getClassroomCIdByCName(classroom);
//
        Classroom classroomServiceById = classroomService.getById(cIds);

        //拼接关键字
        String keyword = date + '-' + time + "-" +cIds;
        System.out.println("--------------------keyword"+keyword);
        //根据关键字查询
        QueryWrapper<Seat> wrapper = new QueryWrapper<>();
        wrapper.like("keyword",keyword);
        List<Seat> seatList = seatService.list(wrapper);

        classroomServiceById.setSeatList(seatList);
        return Result.success(classroomServiceById);
    }

    //选择座位
    @PostMapping("/save")
    public Result save(@RequestBody Seat seat)
    {
//        System.out.println("座位--------"+seat);
        return seatService.choiceSeat(seat);
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestBody Seat seat)
    {
        //先取消记录表
        int i = recordService.cancelRecord(seat);
        if (i == 0)
        {
            return Result.error(Constants.CODE_400,"当前没有预约任何座位");
        }
        seat.setSStatus("1");
        seat.setUId("0");
        //更新座位信息
        int b = seatService.UpdateSeat(seat);
        if (b == 0)
        {
            return Result.error(Constants.CODE_400,"取消预约失败");
        }
        return Result.success();
    }
}

