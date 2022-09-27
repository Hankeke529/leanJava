package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseService;
import com.atguigu.service.HouseUserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController {
    private HouseUserService houseUserService;

    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId , Map map){

        map.put("houseId",houseId);
        return "houseUser/create";
    }

    //添加
    @RequestMapping("/save")
    public String save(HouseUser houseUser){
        houseUserService.insert(houseUser);
        return "common/successPage";
    }

    //修改
    @RequestMapping("/edit/{id}")
    public String goEdit(@PathVariable("id") Long id,Map map){
        HouseUser byId = houseUserService.getById(id);

        map.put("houseUser",byId);

        return "houseUser/edit";
    }

    //更新
    @RequestMapping("/update")
    public String update(HouseUser houseUser){
        houseUserService.update(houseUser);
        return "common/successPage";
    }



    //删除
    @RequestMapping("/delete/{houseId}/{houseUserId}")
    public String delete(@PathVariable("id") Long id,@PathVariable("houseUserId") Long houseUserId){

    houseUserService.delete(houseUserId);

    return "redirect:/house/"+id;
}



}

