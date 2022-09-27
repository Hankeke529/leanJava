package com.atguigu.dao;

import com.atguigu.entity.House;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

public interface HouseDao extends BaseDao<House> {


    Page<HouseVo> findPageList(@Param("vo") HouseQueryVo houseQueryVo);
}
