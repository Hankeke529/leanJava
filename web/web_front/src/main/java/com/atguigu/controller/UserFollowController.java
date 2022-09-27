package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpSession session){

        //获取用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        userFollowService.follow(userInfo.getId(),houseId);
        return Result.ok();

    }

    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result MyFollowed(@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize
                            ,HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        PageInfo<UserFollowVo> pageInfo = userFollowService.findPageList(pageNum,pageSize,userInfo.getId());
        return Result.ok(pageInfo);

    }

    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
        userFollowService.cancelFollow(id);
        return Result.ok();
    }
}
