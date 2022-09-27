package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
////    去首页
//    @RequestMapping("/")
//    public String index(){
//        return "frame/index";
//    }


    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;
    //    去首页
    @RequestMapping("/")
    public String index(Map map){
//        Long userId =2l;
//        Admin admin = adminService.getById(userId);

        //动态获取user对象
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //根据用户名获取admin对象
        Admin admin = adminService.getAdminByUserName(user.getUsername());
        //通过用户id调用用户的权限
        List<Permission> permissionList = permissionService.getMenuPermissionsByAdminId(admin.getId());
        map.put("admin",admin);
        map.put("permissionList",permissionList);
        return "frame/index";
    }
    @RequestMapping("/main")
    public String main(){
        return "frame/main";
    }

    @RequestMapping("/login")
    public String goLogin(){
        return "frame/login.html";
    }

//    @RequestMapping("/logout")
//    public String logout(HttpSession httpSession){
//        httpSession.invalidate();
//        return "redirect:login";
//    }

    @RequestMapping("/auth")
    public String auth(){
        return "frame/auth";
    }
}
