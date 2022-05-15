package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingge.springboot.entity.Seat;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
public interface IRecordService extends IService<Record> {

    int modifyRecord(String time);

    Integer getExsitRecord(Record record);

    int cancelRecord(Seat seat);

    Result getPageForCondition(Page<Record> recordPage, String date, String time, String cname,String uId);
}
