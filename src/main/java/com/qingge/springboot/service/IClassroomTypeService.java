package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.ClassroomType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-12
 */
public interface IClassroomTypeService extends IService<ClassroomType> {

    Page<ClassroomType> getPage(Page<ClassroomType> classroomTypePage, Integer pageNum, Integer pageSize, String name, Integer score);
}
