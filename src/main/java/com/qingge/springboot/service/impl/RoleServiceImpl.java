package com.qingge.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Menu;
import com.qingge.springboot.entity.Role;
import com.qingge.springboot.entity.RoleMenu;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.mapper.MenuMapper;
import com.qingge.springboot.mapper.RoleMapper;
import com.qingge.springboot.mapper.RoleMenuMapper;
import com.qingge.springboot.service.IMenuService;
import com.qingge.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private IUserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        //先删除role和menu的关系
        try{
            roleMenuMapper.deleteByRoleId(roleId);

            //重新绑定
            List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
            for (Integer menuId : menuIds) {
                Menu menu = menuService.getById(menuId);
                //专门找二级id
                if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) { // 二级菜单 并且传过来的menuId数组里面没有它的父级id
                    // 那么我们就得补上这个父级id
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menu.getPid());
                    roleMenuMapper.insert(roleMenu);
                    //添加了一级的id，在上面循环的时候就不会，再给同一个一级id的二级菜单循环添加一级id
                    menuIdsCopy.add(menu.getPid());
                }
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }

        }catch ( Exception e)
        {
            throw new ServiceException(Constants.CODE_401,"操作出错");
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {

//        System.out.println("打印出当前角色的菜单树："+roleMenuMapper.selectByRoleId(roleId).toString());
        return roleMenuMapper.selectByRoleId(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteRoleById(Integer id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        try{
            Role role = roleMapper.selectById(id);
            //判断当前角色是否有用户绑定
            QueryWrapper<User> roleFlag = wrapper.like("role", role.getFlag());

            if (!CollectionUtil.isEmpty(userService.list(roleFlag)))
            {
                return Result.error(Constants.CODE_400,"当前角色已绑定用户");
            }
            //解绑菜单
            if (roleMenuMapper.selectByRoleId(id) != null)
            {
                roleMenuMapper.deleteByRoleId(id);
            }

            roleMapper.deleteById(id);
        } catch (Exception e)
        {
            throw new ServiceException(Constants.CODE_400,"操作错误");
        }
        return Result.success();
    }
}
