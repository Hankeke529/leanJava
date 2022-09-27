package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {

    //关注房源

    void follow(Long id, Long houseId);

    //查看是否关注房源
    Boolean isFollowed(Long id, Long id1);

    //分页查询关注房源
    PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long id);

    void cancelFollow(Long id);
}
