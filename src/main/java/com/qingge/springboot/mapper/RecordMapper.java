package com.qingge.springboot.mapper;

import com.qingge.springboot.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
public interface RecordMapper extends BaseMapper<Record> {

    //修改预约记录
    int modifyRecord(String time);



    //查询预约时间段是否已有其他教室的记录
    int getExsitRecord(Integer uId, String sDate);

    @Update("Update w_record set status = 1 where seatkeyword = #{keyword} and u_id = #{uId}")
    int cancelRecord(String keyword, String uId);

    @Select("select count(*) from w_classroom c left join w_seat s on c.c_id = s.c_id where s.s_status = '0' and c.c_id = #{cId}")
    int queryClassroomAndSeat(Integer cId);
}
