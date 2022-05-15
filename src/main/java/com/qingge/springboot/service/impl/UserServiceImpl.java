package com.qingge.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Menu;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.mapper.RoleMapper;
import com.qingge.springboot.mapper.RoleMenuMapper;
import com.qingge.springboot.mapper.UserMapper;
import com.qingge.springboot.service.IMenuService;
import com.qingge.springboot.service.IRewardPunishmentService;
import com.qingge.springboot.service.IUserService;
import com.qingge.springboot.utils.DesUtils;
import com.qingge.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    private IRewardPunishmentService rewardPunishmentService;

    public User toLogin(User user){
        //密钥
        try {
            Map<String,String> secretInfo = userMapper.getInfo(user.getUsername());
            String decode = DesUtils.decode(secretInfo.get("password"), secretInfo.get("secret_key"));
            if ( decode.equals(user.getPassword()))
            {
                user.setPassword(null);
                User one = null;
                one = userMapper.queryUser(user);
                //如果数据库存在用户，则生成token
                String token = TokenUtils.genToken(one.getUId().toString(), one.getPassword());
                one.setToken(token);
                //获取用户身份
                String role = one.getRole();
                List<Menu> roleMenus = getRoleMenus(role);
                one.setRoleMeus(roleMenus);
                return one;
            }
            else{
                throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
            }
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    //用户名
    @Override
    public String selectByUId(Integer id) {
       return userMapper.selectUserByUId(id);
    }

    @Override
    public Map<String,String> selectUserByUIdForPwd(Integer id) {
        return userMapper.selectUserByUIdForPwd(id);
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();

        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getMId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getMId()));
        }
        return roleMenus;
    }

    @Override
    public int updatePw(Integer id, String newPassword) {
        return userMapper.updatePw(id,newPassword);
    }


    @Override
    public Page<User> findPage(Page<User> page, String username, String email,Integer phone) {
//        System.out.println("findPage在实现类"+userMapper.findPage(page,username,email,phone).getRecords().get(0));
        return userMapper.findPage(page,username,email,phone);
    }

    @Override
    public List<User> selectUserByCondition(List<Integer> ids, String username, String email) {
        return userMapper.selectUserByConditon(ids,username,email);
    }


    //这里应该是多表查询
    @Override
    public Page<User> getAllStudentScoreForPage(Page<User> page, String uNumber, String username) {

        Page<User> userPage = userMapper.getStudentScoreForPageByCondition(page,uNumber,username);

        return userPage;
    }

    @Override
    public Integer updateScoreByUId(Integer uid, Integer rpScore) {
        return userMapper.updateScoreBuUId(uid,rpScore);
    }

    @Override
    public int reduceUserScoreByUId(Integer uId, Integer rpScore) {
       return userMapper.reduceUserScoreByUId(uId,rpScore);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteScoreByEntity(Integer rpId, Integer rpScore, Integer uid) {
        try {
            boolean b = rewardPunishmentService.removeById(rpId);
            if (b)
            {
                updateScoreByUId(uid, rpScore);
                return Result.success();
            }
        } catch (Exception e) {
            return Result.error(Constants.CODE_400,"删除失败");
        }
        return Result.error(Constants.CODE_400,"删除失败");
    }


    @Override
    public String getUNUmberForNull() {
        return userMapper.getUNUmberForNull();
    }

    @Override
    public boolean hasExiting(String username) {
        if (userMapper.hasExiting(username) > 0)
        {
            return false;
        }
        return true;
    }

}
