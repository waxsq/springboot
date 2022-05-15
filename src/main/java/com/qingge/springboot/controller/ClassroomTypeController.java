package com.qingge.springboot.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.pinyin.engine.houbbpinyin.HoubbPinyinEngine;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.entity.ClassroomType;
import com.qingge.springboot.service.IClassroomSeatService;
import com.qingge.springboot.service.IClassroomService;
import com.qingge.springboot.service.IClassroomTypeService;
import org.omg.CORBA.INTERNAL;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/classroomtype")
public class ClassroomTypeController {

    @Resource
    private IClassroomTypeService classroomTypeService;

    @Resource
    private IClassroomService classroomService;

    @GetMapping("/page")
    public Result getClassroomtypePage(@RequestParam(name = "pageNum") Integer pageNum,
                                       @RequestParam(name = "pageSize") Integer pageSize,
                                       @RequestParam(defaultValue = "") String name,
                                       @RequestParam(defaultValue = "") Integer score)
    {
        Page<ClassroomType> page = classroomTypeService.getPage(new Page<ClassroomType>(pageNum, pageSize), pageNum, pageSize, name, score);
        return Result.success(page);
    }


    @PostMapping()
    public Result saveOrUpdateClassroomType(@RequestBody ClassroomType classroomType)
    {
        boolean b = classroomTypeService.saveOrUpdate(classroomType);
        if (!b)
        {
            return Result.error();
        }
        return Result.success();
    }


    @DeleteMapping("/{tid}")
    public Result deleteClassroomTypeBytId(@PathVariable("tid") Integer tId)
    {
        //判断该类型的阅览室是否存在
        QueryWrapper<Classroom> classroomQueryWrapper = new QueryWrapper<>();
        classroomQueryWrapper.eq("t_id",tId);
        List<Classroom> list = classroomService.list(classroomQueryWrapper);
        if (!CollectionUtil.isEmpty(list))
        {
            return Result.error(Constants.CODE_400,"当前存在该类型的阅览室");
        }
        boolean b = classroomTypeService.removeById(tId);
        if (!b)
        {
            return  Result.error();
        }
        return Result.success();
    }

    @GetMapping("/name")
    public Result getClassroomTypeAllName()
    {
        List<ClassroomType> list = classroomTypeService.list();
//        System.out.println("打印出阅览室类型"+list);
        return Result.success(list);
    }
}

