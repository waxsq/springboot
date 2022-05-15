package com.qingge.springboot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Menu;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.mapper.MenuMapper;
import com.qingge.springboot.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;
    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        List<Menu> parentNodes = null;
        //条件查询
        if (StringUtils.isNotBlank(name))
        {
            wrapper.like("m_name",name);
            List<Menu> list = list(wrapper);
            return list;
        }
        // 查询所有数据
        List<Menu> list = list(wrapper);
//        System.out.println("查出所有"+list);
        // 找出pid为null的一级菜单
        parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
//        System.out.println("-----------------------------"+parentNodes);
        // 找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            // 筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getMId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteById(Integer id) {

        //先查询此id是否为一级id
        List<Menu> list = menuMapper.selectList(null);
        for (Menu menu : list)
        {
            if (menu.getPid() == id)
            {
                try {
                    menuMapper.updatePIdForNullById(menu.getMId());
                } catch (Exception e)
                {
                    throw new ServiceException(Constants.CODE_400, "删除出错"+e.getMessage());
                }
            }

        }
        menuMapper.deleteById(id);
        return Result.success();
    }
}
