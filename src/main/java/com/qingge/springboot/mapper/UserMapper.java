package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
public interface UserMapper extends BaseMapper<User> {


    User queryUser(@Param("user") User user);

    @Select("select username from s_user where u_id = #{id}")
    String selectUserByUId( @Param("id") Integer id);

//    @Select("select password from s_user where u_id = #{id}")
    Map<String,String> selectUserByUIdForPwd( @Param("id") Integer id);

    @Update("update s_user set password = #{newPassword} where u_id = #{id}")
    int  updatePw(@Param("id") Integer id,@Param("newPassword") String newPassword);


    Page<User> findPage(Page<User> page, @Param("username") String username, @Param("email") String email,@Param("phone") Integer phone);

    List<User> selectUserByConditon(@Param("ids") List<Integer> ids, @Param("username") String username, @Param("email") String email);


    Page<User> getStudentScoreForPageByCondition(Page<User> page,@Param("uNumber") String uNumber, @Param("username") String username);

    @Update("update s_user set score = score + #{rpScore} where u_id =#{uid}")
    Integer  updateScoreBuUId(@Param("uid") Integer uid, @Param("rpScore") Integer rpScore);

    @Update("update s_user set score = score - #{rpScore} where u_id = #{uid}")
    int  reduceUserScoreByUId(@Param("uid") Integer uid,@Param("rpScore") Integer rpScore);

    @Select("select score from s_user where u_id = #{uId}")
    Integer getUserScore(String uId);

    @Select("select username from s_user where u_id = #{uId}")
    String seleUsernameByUId(Integer uId);

    @Select("select role from s_user where u_id = #{uId}")
    String getUserRoleByUId(String uId);

    @Select("select max(u_number) from s_user where u_number is not null and role = 'ROLE_USER'")
    String getUNUmberForNull();

    Map<String,String> getInfo(String username);

    @Select("select count(*) from s_user where username = #{username}")
    int hasExiting(String username);
}
