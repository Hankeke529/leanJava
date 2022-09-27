package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
    @Reference
    private AdminService adminService;

    @Reference
    private RoleService roleService;

    //注入密码加密器
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping
    public String findPage(Map map, HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        map.put("filters",filters);
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        map.put("page",pageInfo);
        return "admin/index";
    }

    @RequestMapping("/create")
    public String goAddPage(){
        return "admin/create";
    }

    @RequestMapping("/save")
    public String save(Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.insert(admin);
        return "common/successPage";
    }

    @RequestMapping("/delete/{adminId}")
    public String delete(@PathVariable("adminId") Long adminId){
        adminService.delete(adminId);
        return "redirect:/role";
    }

    @RequestMapping("/edit/{adminId}")
    public String goEditPage(@PathVariable("adminId") long adminId ,Map map){
        Admin admin = adminService.getById(adminId);

        map.put("admin",admin);
        return "admin/edit";

    }

    @RequestMapping("/update")
    public String update(Admin admin){
        adminService.update(admin);
        return "common/successPage";
    }

    //去分配权限的页面
    @RequestMapping("/assignShow/{adminId}")
    public String goAssignShowPag(@PathVariable("adminId") Long adminId , ModelMap modelMap){
        //调用roleservice中根据用户id查询用户角色的方法
        Map<String, Object> rolesByAdminId = roleService.findRolesByAdminId(adminId);
        modelMap.addAttribute("adminId",adminId);
        //将map放入request中
        modelMap.addAllAttributes(rolesByAdminId);

        return "admin/assignShow";
    }

    //分配角色
    @RequestMapping("/assignRole")
    public String assignRole(Long adminId,Long[] roleIds){
        roleService.assignRole(adminId,roleIds);
        return "common/successPage";
    }
}
