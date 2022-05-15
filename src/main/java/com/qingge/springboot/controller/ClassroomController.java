package com.qingge.springboot.controller;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Classroom;
import com.qingge.springboot.service.IClassroomSeatService;
import com.qingge.springboot.service.IClassroomService;
import com.qingge.springboot.service.ISeatService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-10
 */
@RestController
@RequestMapping("/classroom")
public class ClassroomController {



    @Resource
    private IClassroomSeatService classroomSeatService;


    @Resource
    private IClassroomService classroomService;


    @Resource
    private ISeatService seatService;


    /**
     * 在这里要负责生成座位
     */


    /**
     * 先写查询,分页查询，并且可以根据条件
     */
    @GetMapping("/page")
    public Result getClassroomForPageByCondition(@RequestParam(name = "pageNum") Integer pageNum,
                                                 @RequestParam(name = "pageSize") Integer pageSize,
                                                 @RequestParam(defaultValue = "") String cId,
                                                 @RequestParam(defaultValue = "") String size,
                                                 @RequestParam(defaultValue = "") String cLocation)
    {
        Page<Classroom> page = classroomSeatService.getClassroomForPageByCondition(new Page<Classroom>(pageNum,pageSize),cId,size,cLocation);
        return Result.success(page);
    }


    @GetMapping("/information/page")
    public Result getClassroomInformationForPageByCondition(@RequestParam(name = "pageNum") Integer pageNum,
                                                            @RequestParam(name = "pageSize") Integer pageSize,
                                                            @RequestParam("") String cName,
                                                            @RequestParam("") String cSize,
                                                            @RequestParam("") String cLocation,
                                                            @RequestParam("") String name)
    {
        Page<Classroom> page = classroomSeatService.getClassroomInformationForPageByCondition(new Page<Classroom>(pageNum,pageSize),cName,cLocation,cSize,name);
//        System.out.println("--------------------------------"+page.getRecords().size());
        for (int i = 0; i < page.getRecords().size(); i++) {
            System.out.println(page.getRecords().get(i));
        }
        return Result.success(page);
    }


    /**
     * 修改教室
     * @param classroom
     * @return
     */
    @PostMapping("/information/save")
    public Result saveOrUpdateClassInformation(@RequestBody Classroom classroom)
    {
        try {
            if (classroom.getCId() == null)
            {
                classroom.setSize(classroom.getCol() * classroom.getRow());
                classroom.setCreateTime(DateTime.now().toString());
                classroomService.save(classroom);
            }
            else{
                //修改阅览室
                int i = classroomService.updateClassroomByEntity(classroom);
                if (i == 0)
                {
                    return Result.error(Constants.CODE_400,"当前阅览室存在预约,不能修改");
                }
            }
        } catch (Exception e) {
            return Result.error(Constants.CODE_400,"当前阅览室存在预约,不能修改");
        }
        return Result.success();
    }

    @DeleteMapping("/information/{id}")
    public Result deleteClssroomByCId(@PathVariable("id") Integer cId)
    {
        int i = classroomService.deleteClassroomById(cId);
        if (i != 0 )
        {
            return Result.success();
        }
        return Result.error(Constants.CODE_400,"当前阅览室存在预约信息");

    }


}

