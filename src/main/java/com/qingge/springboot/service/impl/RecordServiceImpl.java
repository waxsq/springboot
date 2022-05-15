package com.qingge.springboot.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.Record;
import com.qingge.springboot.entity.Seat;
import com.qingge.springboot.mapper.ClassroomMapper;
import com.qingge.springboot.mapper.RecordMapper;
import com.qingge.springboot.mapper.UserMapper;
import com.qingge.springboot.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.xmlbeans.impl.inst2xsd.RussianDollStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {


    @Resource
    private RecordMapper recordMapper;

    @Resource
    private ClassroomMapper classroomMapper;

    @Resource
    private UserMapper userMapper;

    //取消记录
    @Override
    public int modifyRecord(String time) {
        return recordMapper.modifyRecord(time);
    }

    //判断是否重复记录

    @Override
    public Integer getExsitRecord(Record record) {
        Integer uId = record.getUId();
        String sDate = record.getSeatkeyword().substring(0, 18);
        Integer i = recordMapper.getExsitRecord(uId, sDate);
        return i;

    }

    @Override
    public int cancelRecord(Seat seat) {
        String keyword = seat.getKeyword();
        String uId = seat.getUId();
        return recordMapper.cancelRecord(keyword,uId);
    }

    @Override
    public Result getPageForCondition(Page<Record> recordPage, String date, String time, String cname,String uId) {
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("seatkeyword");
        String userRoleByUId = userMapper.getUserRoleByUId(uId);
//        System.out.println("--------------------uid--------------------"+uId);
        if (userRoleByUId.equals("ROLE_USER"))
        {
            wrapper.eq("u_id",uId);
        }

        if (!"".equals(date))
        {
            wrapper.like("seatkeyword",date);
        }
        if (!"".equals(time))
        {
            wrapper.like("seatkeyword",time);
        }
        if (!StringUtils.isBlank(cname))
        {
            Integer cId = classroomMapper.selectCIdByCName(cname);
            wrapper.like("seatkeyword",cId);
        }

        String classroomName = null;
        Page<Record> selectPage = recordMapper.selectPage(recordPage, wrapper);
        List<Record> records = selectPage.getRecords();

//        System.out.println("--------------------------"+JSONUtil.toJsonStr(records));
        for (Record record : records)
        {
            record.setUsername(userMapper.selectUserByUId(record.getUId()));
            if (record.getSeatkeyword().length() == 25)
            {
                record.setCname(classroomMapper.selectCNameByCId(record.getSeatkeyword().substring(19,21)));
                record.setSeat(record.getSeatkeyword().substring(22,25));
            }
            else{
                record.setCname(classroomMapper.selectCNameByCId(record.getSeatkeyword().substring(19,20)));
                record.setSeat(record.getSeatkeyword().substring(21,24));
            }

            record.setTime(record.getSeatkeyword().substring(0,18));
            if (record.getStatus().equals("1"))
            {
                record.setStatus("无效");
            }else record.setStatus("有效");

        }
//        System.out.println("--------------------------"+selectPage.toString());
//        System.out.println(JSONUtil.toJsonStr(selectPage));
        return Result.success(selectPage);
    }
}
