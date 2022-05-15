package com.qingge.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.service.IUserService;
import com.qingge.springboot.utils.DesUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    IUserService userService;



    @PostMapping("/register")
    public Result register(@RequestBody  User user)
    {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        if (!CollectionUtil.isEmpty(userService.list(wrapper)))
        {
            return Result.error(Constants.CODE_400,"当前用户名存在");
        }
        user.setScore(100);
        user.setRole("ROLE_USER");
        user.setCreateTime(DateUtil.now());
        user.setSecretKey(DesUtils.getInfo());
        String password = user.getPassword();
        user.setPassword(DesUtils.encrypt(password,user.getSecretKey()));
        user.setUNumber(String.valueOf(Long.parseLong(userService.getUNUmberForNull())+1));
        try {
            userService.save(user);
        } catch (Exception e) {
            return Result.error(Constants.CODE_400,"注册失败");
        }
        return Result.success();
    }


    @PostMapping("/login")
    public Result toLogin(@RequestBody User user)
    {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        User login = userService.toLogin(user);
//        System.out.println("信息："+login);
        //将密码为空
        login.setPassword("");
        return Result.success(login);
    }

    @GetMapping("/username/{username}")
    public Result getUsername(@PathVariable("username") String username)
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));

    }


    @PutMapping("/password/{userId}/{password}/{newPassword}")
    public Result updatePassword(@PathVariable("userId") Integer id,
                                    @PathVariable("password") String password,
                                    @PathVariable("newPassword") String newPassword)
    {
        try{
            Map<String,String> pw = userService.selectUserByUIdForPwd(id);
            String pwName = DesUtils.decode(pw.get("password"),pw.get("secret_key"));
//            System.out.println("--------------"+pwName);
            System.out.println(password);

            if (pwName.equals(password))
            {
                newPassword = DesUtils.encrypt(newPassword,pw.get("secret_key"));
                    if(userService.updatePw(id,newPassword) >0 )
                    {
                        return Result.success();
                    }
            }
        } catch (Exception e)
        {
            return Result.error(Constants.CODE_400,"操作出错"+e.getMessage());
        }

        return Result.error(Constants.CODE_400,"原密码出错");
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") Integer phone) {
        //这里的分页已经封装好了
//        System.out.println("打印出来username"+userService.findPage(new Page<>(pageNum, pageSize), username, email,phone).getRecords().get(0));
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), username,email,phone));
    }

    @PostMapping
    public Result saveOrUpdateInformation(@RequestBody User user)
    {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (user.getUId() == null && user.getPassword() == null)
        {
            user.setCreateTime(DateUtil.now());
            user.setSecretKey(DesUtils.getInfo());
            user.setPassword(DesUtils.encrypt("123456",user.getSecretKey()));
            user.setScore(100);
            if (userService.getOne(wrapper.eq("u_number", user.getUNumber())) != null)
            {
                return Result.error(Constants.CODE_400,"当前"+user.getUNumber()+"存在");
            }
        }
        return Result.success(userService.saveOrUpdate(user));
    }


    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id)
    {
        return Result.success(userService.removeById(id));
    }

    //当对象，数组，集合都用@RequestBody
    @PostMapping("/del/batch")
    public Result deleteList(@RequestBody List<Integer> ids)
    {
        System.out.println(ids instanceof List);
        return Result.success(userService.removeBatchByIds(ids));
    }




    //导入事件
    //导入excle表
    @PostMapping("/import")
    public Result inputFile(MultipartFile file) throws Exception
    {
        String msg = "";
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //获取学号
        String lastUNumber = userService.getUNUmberForNull() ;
        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        int i = 1;
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(RandomUtil.randomNumbers(11));
            user.setEmail(row.get(1).toString());
            user.setPhone(row.get(2).toString());
            if (userService.getOne(new QueryWrapper<User>().eq("u_number", row.get(0).toString())) != null)
            {
                msg += msg+"、"+ row.get(0).toString();
                continue;
            }
            user.setUNumber(row.get(0).toString());
            user.setRole("ROLE_USER");
            user.setSecretKey(DesUtils.getInfo());
            user.setPassword(DesUtils.encrypt("123456",user.getSecretKey()));
            user.setScore(100);
            user.setCreateTime(DateUtil.now());
            users.add(user);
        }
        userService.saveBatch(users);
        if (StringUtils.isBlank(msg))
        {
            return Result.success();
        }
        return Result.success(msg+"存在");
    }


    @PostMapping("/refreshUserInformation")
    public Result refreshUserInformation(@RequestBody User user)
    {
        if (!userService.hasExiting(user.getUsername()))
        {
            return Result.error(Constants.CODE_400,"已存在相同用户名");
        }
        boolean saveOrUpdate = userService.saveOrUpdate(user);
        if (!saveOrUpdate)
        {
            return Result.error(Constants.CODE_400,"保存错误");
        }
        return Result.success();
    }


    /**
     * 导出excel
     * 可以根据选中的数组和搜索栏的关键字导出
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception
    {
        //根据条件查询所有
         List<User> userList= userService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("createTime", "账号创建时间");
        writer.addHeaderAlias("role", "账号身份");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(userList, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }






}

