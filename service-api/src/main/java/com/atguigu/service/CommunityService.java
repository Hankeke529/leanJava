package com.atguigu.service;

import com.atguigu.entity.Community;
import com.atguigu.service.BaseService;

import java.util.List;

public interface CommunityService extends BaseService<Community>{


    List<Community> findAll();
}
