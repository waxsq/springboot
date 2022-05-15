package com.qingge.springboot.controller;


import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Dict;
import com.qingge.springboot.entity.Menu;
import com.qingge.springboot.mapper.DictMapper;
import com.qingge.springboot.service.IDictService;
import com.qingge.springboot.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private IDictService dictService;


    //更新或者新增
    @PostMapping
    public Result saveOrUpdate(@RequestBody Menu menu)
    {
//        System.out.println("-----------------------------------------------------");
//        System.out.println(menu.getPath().substring(1,menu.getPath().length()-1));
//        System.out.println(menu.getPagePage().toLowerCase());
        if (!menu.getPath().substring(1,menu.getPath().length()).equals(menu.getPagePage().toLowerCase()))
        {

            return Result.error(Constants.CODE_400,"菜单路径和页面名称不匹配");
        }
        menuService.saveOrUpdate(menu);
        return Result.success();
    }

    @GetMapping
    public Result findAllMenu(@RequestParam(defaultValue = "")  String name )
    {
        return Result.success(menuService.findMenus(name));
    }

    @GetMapping("/icons")
    public Result getAllIcon()
    {
        //获取图标
        List<Dict> dictList = dictService.getAllIcon();
//        for (Dict dict : dictList)
//        {
//            System.out.println("打印菜单信息"+dict);
//        }
        return Result.success(dictList);
    }


    @DeleteMapping("/{id}")
    public Result deleteByMenuId(@PathVariable("id") Integer id)
    {
        return menuService.deleteById(id);
    }

    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getMId));
    }


}

