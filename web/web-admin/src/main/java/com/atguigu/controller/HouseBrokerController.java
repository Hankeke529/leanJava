package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {
    @Reference
    private  AdminService  adminService;

    @Reference
    private HouseBrokerService houseBrokerService;
    @RequestMapping("/create")
    public String goAdd(@RequestParam("houseId") Long houseId, Map map){
        map.put("houseId", houseId);
        List<Admin> adminList = adminService.findAll();
        map.put("adminList",adminList);


        return "houseBroker/create";
    }

    @RequestMapping("/save")
    public String save(HouseBroker houseBroker){
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());

        houseBrokerService.insert(houseBroker);

        return "common/successPage";

    }

    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id")Long id,Map map){
        HouseBroker byId = houseBrokerService.getById(id);
        map.put("houseBroker",byId);
        List<Admin> adminList = adminService.findAll();
        map.put("adminList",adminList);
        return "houseBroker/edit";
    }

    @RequestMapping("/update")
    public String update(HouseBroker houseBroker){
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());

        houseBrokerService.update(houseBroker);
        return "common/successPage";
    }

    @RequestMapping("/delete/{id}/{brokerId}")
    public String delete(@PathVariable("id") Long id , @PathVariable("brokerId") Long brokerId){
        houseBrokerService.delete(brokerId);

        return "redirect:/house/"+id;
    }
}
