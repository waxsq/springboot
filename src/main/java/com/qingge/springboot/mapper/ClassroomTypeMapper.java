package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.ClassroomType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-12
 */
public interface ClassroomTypeMapper extends BaseMapper<ClassroomType> {

    //查询分页
    Page<ClassroomType> getPage(Page<ClassroomType> page, Integer pageNum, Integer pageSize, String name, Integer score);
}
