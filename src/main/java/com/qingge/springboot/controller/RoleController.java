package com.qingge.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Role;
import com.qingge.springboot.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
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
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @GetMapping
    public Result UserFindAllRoles()
    {
        return Result.success(roleService.list());
    }

    @GetMapping("/page")
    public Result getAllRolesForPage(@RequestParam String name,
                                     @RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize)
    {
//        System.out.println("角色分页");
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("r_id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId)
    {
        return Result.success(roleService.getRoleMenu(roleId));
    }


    @PostMapping
    public Result saveRole(@RequestBody Role role)
    {
//        System.out.println("保存永华管");
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }


    @DeleteMapping("{id}")
    public Result deleteRoleById(@PathVariable("id") Integer id)
    {
        return roleService.deleteRoleById(id);
    }



}

