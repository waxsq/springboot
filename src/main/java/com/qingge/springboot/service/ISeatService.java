package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.Seat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
public interface ISeatService extends IService<Seat> {

    Page<Seat> getAllSeatForPageByCondition(Page<Seat> seatPage, String date, String time, String cname);

    List<String> getDateList();

    List<String> getTimeList();

    List<String> getClassroomList();

    Integer getClassroomCIdByCName(String classroom);

    int removeSeatByYesterday(String s);

    int modifySeatUId(String s);

    int UpdateSeat(Seat seat);

    Boolean selectHavingSeat(Integer cId);

    Result choiceSeat(Seat seat);

    Result createDepartSeats(Classroom classroom);
}
