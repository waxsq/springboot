package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Seat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
public interface SeatMapper extends BaseMapper<Seat> {

    Page<Seat> getAllSeatForPageByCondition(Page<Seat> seatPage, String date, String time, String cname);

    int getFreeSeatByCondition(String sDate, String stime, String sCName);

    @Select("select distinct  date from w_seat")
    List<String> getDateList();

    @Select("select distinct time from w_seat")
    List<String> getTimeList();
    @Select("select distinct c_name from w_classroom")
    List<String> getClassroomList();

    @Select("select distinct c_id from w_classroom where c_name = #{classroom}")
    Integer getClassroomCIdByCName(String classroom);


    int removeSeatByYesterday(String s);


    int modifySeatUId(String s);

    @Select("select distinct c_id from w_classroom where c_id = #{#cId}")
    int selectHavingSeat(Integer cId);
}
