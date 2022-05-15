package com.qingge.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Record;
import com.qingge.springboot.service.IRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/record")
public class RecordController {


    @Resource
    private IRecordService recordService;

    /**
     * 记录分页
     */

    @GetMapping("/page")
    public Result getPage(@RequestParam Integer pageNum,
                          @RequestParam Integer pageSize,
                          @RequestParam(defaultValue = "") String date,
                          @RequestParam(defaultValue = "") String time,
                          @RequestParam(defaultValue = "") String cname,
                          @RequestParam String uId)
    {
        return recordService.getPageForCondition(new Page<Record>(pageNum,pageSize),date,time,cname,uId);
    }

    @GetMapping("/history/{uid}")
    public Result getHistory(@PathVariable("uid") String uId)
    {
//        System.out.println("---------------------------uId"+uId);
        return recordService.getPageForCondition(new Page<Record>(0,10),"","","",uId);
    }

}

