package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

   @Autowired
   private HouseDao houseDao;
   @Autowired
   private DictDao dictDao;

    @Override
    protected BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public void publish(long houseId, Integer status) {
        House house = new House();
        house.setId(houseId);
        house.setStatus(status);

        houseDao.update(house);
    }

    @Override
    public PageInfo<HouseVo> findPageList(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);

        //调用houseDao中带条件查询的方法及分页

        Page<HouseVo> page = houseDao.findPageList(houseQueryVo);

        for (HouseVo houseVo : page) {
            //获取房屋类型id
            String houseTypeName = dictDao.getNameId(houseVo.getHouseTypeId());
            //获取楼层
            String houseFloorName = dictDao.getNameId(houseVo.getFloorId());
            String houseDirectionName = dictDao.getNameId(houseVo.getDirectionId());
            houseVo.setHouseTypeName(houseTypeName);
            houseVo.setFloorName(houseFloorName);
            houseVo.setDirectionName(houseDirectionName);
        }



        return new PageInfo<>(page,5);
    }

    @Override
    public House getById(Serializable houseId) {
        House house = houseDao.getById(houseId);
        //获取户型
        String houseTypeName = dictDao.getNameId(house.getHouseTypeId());
        //获取楼层
        String houseFloorName = dictDao.getNameId(house.getFloorId());
        //获取朝向
        String directionName = dictDao.getNameId(house.getDirectionId());
        //获取建筑结构
        String buildStructureName= dictDao.getNameId(house.getBuildStructureId());
        //装修情况
        String decorationName = dictDao.getNameId(house.getDecorationId());
        //获取用途
        String houseUseName = dictDao.getNameId(house.getHouseUseId());
        //设置
        house.setHouseTypeName(houseTypeName);
        house.setFloorName(houseFloorName);
        house.setDirectionName(directionName);
        house.setBuildStructureName(buildStructureName);
        house.setDecorationName(decorationName);
        house.setHouseUseName(houseUseName);
        return house;

    }
}
