package com.qingge.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.RewardPunishment;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.service.IRewardPunishmentService;
import com.qingge.springboot.service.IUserService;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/rewardpunishment")
public class RewardPunishmentController {

    @Resource
    private IRewardPunishmentService rewardPunishmentService;

    @Resource
    private IUserService userService;

//

    @GetMapping("/page")
    public Result getAllStudentScoreForPage(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(defaultValue = "") String uNumber,
                                            @RequestParam(defaultValue = "") String username)
    {
        Page<User> page = userService.getAllStudentScoreForPage(new Page<User>(pageNum, pageSize), uNumber, username);
        return Result.success(page);
    }

    @DeleteMapping("/{rpId}/{rpScore}/{uid}")
    public Result deleteRewardPunishment(@PathVariable("rpId") Integer rpId,
                                         @PathVariable("rpScore") Integer rpScore,
                                         @PathVariable("uid") Integer uid)
    {
        //修改用户积分
        return userService.deleteScoreByEntity(rpId,rpScore,uid);
    }

    //刷新表单
    @GetMapping("/{uid}")
    public Result refreshData(@PathVariable("uid") Integer uId)
    {
        QueryWrapper<RewardPunishment> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id",uId);
        List<RewardPunishment> list = rewardPunishmentService.list(wrapper);
        return Result.success(list);
    }

    //保存添加记录
    @PostMapping("/save")
    public Result save(@RequestBody RewardPunishment rewardPunishment)
    {
//        System.out.println("---------------------"+rewardPunishment.getDatatime());
        boolean b = rewardPunishmentService.save(rewardPunishment);
        if (!b)
        {
            return Result.error();
        }
        int i = userService.reduceUserScoreByUId(rewardPunishment.getUId(), rewardPunishment.getRpScore());
        if (i <= 0)
        {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 获取危机信息
     */
    @GetMapping("/getUserByUId/{uid}")
    public Result getUserByUId(@PathVariable("uid") Integer uId)
    {
        QueryWrapper<RewardPunishment> rewardPunishmentQueryWrapper = new QueryWrapper<>();
        rewardPunishmentQueryWrapper.eq("u_id",uId);
        List<RewardPunishment> list = rewardPunishmentService.list(rewardPunishmentQueryWrapper);
        return Result.success(list);
    }
}

