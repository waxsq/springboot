package com.qingge.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.ClassroomType;
import com.qingge.springboot.mapper.ClassroomTypeMapper;
import com.qingge.springboot.service.IClassroomTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-12
 */
@Service
public class ClassroomTypeServiceImpl extends ServiceImpl<ClassroomTypeMapper, ClassroomType> implements IClassroomTypeService {

    @Resource
    private ClassroomTypeMapper classroomTypeMapper;


    @Override
    public Page<ClassroomType> getPage(Page<ClassroomType> page, Integer pageNum, Integer pageSize, String name, Integer score) {
        return classroomTypeMapper.getPage(page,pageNum,pageSize,name,score);

    }
}
