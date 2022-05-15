package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
public interface IUserService extends IService<User> {

    public User toLogin(User user);

    String selectByUId(Integer id);

    Map<String,String> selectUserByUIdForPwd(Integer id);

    int updatePw(Integer id, String newPassword);

    Page<User> findPage(Page<User> page, String username, String email,Integer phone);

    List<User> selectUserByCondition(List<Integer> ids, String username, String email);

    Page<User> getAllStudentScoreForPage(Page<User> page, String uNumber, String username);

    Integer updateScoreByUId(Integer uid, Integer rpScore);


    int reduceUserScoreByUId(Integer uId, Integer rpScore);

    Result deleteScoreByEntity(Integer rpId, Integer rpScore, Integer uid);
    String getUNUmberForNull();

    boolean hasExiting(String username);
}
