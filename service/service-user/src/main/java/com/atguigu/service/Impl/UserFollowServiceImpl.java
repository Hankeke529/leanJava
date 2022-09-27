package com.atguigu.service.Impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.dao.UserInfoDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.service.impl.BaseServiceImpl;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Reference
    private DictService dictService;
    @Override
    protected BaseDao<UserFollow> getEntityDao() {
        return null;
    }



    @Override
    public void follow(Long id, Long houseId) {
        UserFollow userFollow = new UserFollow();

        userFollow.setUserId(id);

        userFollow.setHouseId(houseId);

        userFollowDao.insert(userFollow);
    }

    @Override
    public Boolean isFollowed(Long userId, Long houseId) {


        Integer count = userFollowDao.getCountByUserIdAndHouseId(userId,houseId);
        if (count > 0){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long userId) {

        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        Page<UserFollowVo> page = userFollowDao.findPageList(userId);
        for (UserFollowVo userFollowVo : page) {

            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            String houseFloorName = dictService.getNameById(userFollowVo.getFloorId());
            String directName = dictService.getNameById(userFollowVo.getDirectionId());
            //赋值
            userFollowVo.setHouseTypeName(houseTypeName);
            userFollowVo.setFloorName(houseFloorName);
            userFollowVo.setDirectionName(directName);

        }
        return new PageInfo<>(page ,3);
    }

    @Override
    public void cancelFollow(Long id) {
        userFollowDao.delete(id);
    }
}
