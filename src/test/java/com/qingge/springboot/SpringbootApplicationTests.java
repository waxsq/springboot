package com.qingge.springboot;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.service.IUserService;
import com.qingge.springboot.utils.DesUtils;
import com.qingge.springboot.utils.SeatUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class SpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private IUserService userService;
    @Test
    public void export()
    {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        List<User> userList = userService.selectUserByCondition(ids, null, null);
        for (User user : userList)
        {
            System.out.println(user);
        }
    }



    @Test
    public void getDate()
    {
        Date day = SeatUtil.getNextNextDay(new Date(), -1);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(day);
        System.out.println(date);
    }


    @Test
    public void getTime()
    {
//        String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
//        System.out.println(nowTime);
//        String times[] = {"08点-12点","14点-18点","18点-22点"};   //三个时间段
        Integer hour = DateUtil.thisHour(true);
        if (8 <= hour && hour <= 12)
        {
            System.out.println("08点-12点");
        } else if ( 14 <= hour && hour < 18)
    {
        System.out.println("14点-18点");
    } else
    {
        System.out.println("18点-22点");
    }

    }

    @Test
    public void getTimeString()
    {
        String time = "08点-12点";
        String s = time.substring(1, 2);
        String t = time.substring(4,6);
        System.out.println(t);
        System.out.println(s);
    }

    @Test
    public void getDateString()
    {
        String date= "2022-04-15-08点-12点-8-1-1";
        String date1 = "2022-04-16-14点-18点-10-1-3";
        System.out.println(date.substring(19,20));
        System.out.println(date1.substring(19,21));
        System.out.println(date.length());
        System.out.println(date1.length());
        Date day = SeatUtil.getNextNextDay(new Date(), -1);
        System.out.println(day.toString());
    }

    @Test
    public void test10()
    {
        String time = null;
        Integer nowHour = DateUtil.thisHour(true);
//            String times[] = {"08点-12点","14点-18点","18点-22点"};   //三个时间段
        if (8 <= nowHour && nowHour <= 12)
        {
            time = "08点-12点";
        } else if ( 14 <= nowHour && nowHour <=18)
        {
            time = "14点-18点";
        } else
        {
            time = "18点-22点";
        }
        System.out.println(DateUtil.today()+"-"+time);
    }


    @Test
    public void test5()
    {
        String s = DesUtils.getInfo();
        String encrypt = DesUtils.encrypt("admin", s);
        System.out.println(s);
        System.out.println(encrypt);
        String decode = DesUtils.decode(encrypt, s);
        System.out.println(decode);
    }

    @Test
    public void test11()
    {
         String date= "2022-04-15-08点-12点-8-1-1";
        System.out.println(date.substring(19,20));
    }
}
