package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseController {
    @Reference
    private HouseService houseService;

    @Reference
    private UserFollowService userFollowService;

    @Reference
    private CommunityService communityService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findPageList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize")Integer pageSize
                                , @RequestBody HouseQueryVo houseQueryVo){

        PageInfo<HouseVo> pageInfo=houseService.findPageList(pageNum,pageSize, houseQueryVo);

        return Result.ok(pageInfo);
    }

    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id , HttpSession session){
        //查询房源信息
        House byId = houseService.getById(id);
        //查询小区信息
        Community community = communityService.getById(byId.getCommunityId());

        //查询房源图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImageByHouseIdAndType(id, 1);
        //查询经济人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokerByHouseId(id);
        Map map = new HashMap<>();

        map.put("house",byId);
        map.put("community",community);
        map.put("houseImage1List",houseImage1List);
        map.put("houseBrokerList",houseBrokerList);

//        map.put("isFollow",false);
        //设置关注信息
        Boolean isFollowed = false;

        //从session中获取userInfo信息
        UserInfo user = (UserInfo) session.getAttribute("user");


        if (user != null){
            //证明已经登录
            isFollowed = userFollowService.isFollowed(user.getId(),id);
        }

        map.put("isFollow",isFollowed);
        return Result.ok(map);
    }
}
