package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController{
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    public void serReq(Map map){
        //获取所有小区
        List<Community> communityList = communityService.findAll();
        //获取户型
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        //获取楼层
        List<Dict> floorList = dictService.findListByDictCode("floor");
        //获取建筑结构
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        //获取朝向
        List<Dict> directionList = dictService.findListByDictCode("direction");
        //装修情况
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        //房屋用途
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");

        map.put("communityList",communityList);
        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);
    }


    @RequestMapping
    public String index(Map map ,HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        map.put("filters",filters);
        PageInfo<House> page = houseService.findPage(filters);
        map.put("page",page);
        //设置属性
        serReq(map);

        return "house/index";
    }

    @RequestMapping("/create")
    public String goPage(Map map){
        //设置属性
        serReq(map);

        return "house/create";
    }

    @RequestMapping("/save")
    public String add(House house){
        houseService.insert(house);
        return "common/successPage";
    }

    @RequestMapping("/edit/{houseId}")
    public String edit(@PathVariable("houseId")Long houseId,Map map){
        House house = houseService.getById(houseId);
        map.put("house",house);
        //设置属性
        serReq(map);

        return "house/edit";
    }
    @RequestMapping("/update")
    public String update(House house){
        houseService.update(house);
        return "common/successPage";
    }

    @RequestMapping("/delete/{houseId}")
    public String delete(@PathVariable("houseId")Long houseId){
        houseService.delete(houseId);
        return "redirect:/house";
    }

    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id")Long id, @PathVariable("status")Integer status){
        houseService.publish(id,status);
        return "redirect:/house";
    }

    @RequestMapping("/{id}")
    public String show(@PathVariable("id")Long houseId, Map map){

        House house= houseService.getById(houseId);
        Community comId = communityService.getById(house.getCommunityId());
        map.put("house",house);
        map.put("community",comId);

        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokerByHouseId(houseId);
        List<HouseUser> houseUserList = houseUserService.getHouseUserByHouseId(houseId);

        List<HouseImage> houseImage1Liat = houseImageService.getHouseImageByHouseIdAndType(houseId, 1);
        List<HouseImage> houseImage2Liat = houseImageService.getHouseImageByHouseIdAndType(houseId, 2);





        map.put("houseImage1Liat",houseImage1Liat);
        map.put("houseImage2Liat",houseImage2Liat);

        map.put("houseBrokerList",houseBrokerList);

        map.put("houseUserList",houseUserList);
        return "house/show";
    }

}
