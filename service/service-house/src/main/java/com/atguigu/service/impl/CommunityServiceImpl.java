package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.CommunityDao;
import com.atguigu.service.impl.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private DictDao dictDao;

    @Override
    protected BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

        PageHelper.startPage(pageNum, pageSize);
        Page<Community> page = communityDao.findPage(filters);
        List<Community> list = page.getResult();
        for(Community community : list) {
            String areaName = dictDao.getNameId(community.getAreaId());
            String plateName = dictDao.getNameId(community.getPlateId());
            community.setAreaName(areaName);
            community.setPlateName(plateName);
        }
        return new PageInfo<Community>(page, 10);

    }

    @Override
    public List<Community> findAll() {
        return communityDao.findAll();
    }

    @Override
    public Community getById(Serializable id) {
        //调用CommunityDao中根据id获取小区的方法
        Community community = communityDao.getById(id);
        if(community == null){
            return null;
        }
        //根据区域的id获取区域的名字
        String areaName = dictDao.getNameId(community.getAreaId());
        //根据板块的id获取板块的名字
        String plateName = dictDao.getNameId(community.getPlateId());
        //将区域的名字和板块的名字设置到Community对象中
        community.setAreaName(areaName);
        community.setPlateName(plateName);
        return community;
    }


}