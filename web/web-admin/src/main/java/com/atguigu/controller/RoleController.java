package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private final static String SUCCESS_PAGE="common/successPage";

    @Reference
    private PermissionService permissionService;
    @Reference
    private RoleService roleService;

//    @RequestMapping
//    public String index(Map map){
//        List<Role> rolelist = roleService.findAll();
//        map.put("list",rolelist);
//        return "role/index";
//    }

//    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(Map map, HttpServletRequest request) {
        Map<String, Object> filters = getFilters(request);
        map.put("filters",filters);
        PageInfo<Role> pageInfo=roleService.findPage(filters);
        map.put("page",pageInfo);
        return "role/index";

    }

//    @PreAuthorize("hasAnyAuthority('role.create')")
    @RequestMapping("/create")
    public String goAddrole(){
        return "role/create";
    }


//    @PreAuthorize("hasAnyAuthority('role.create')")
    @RequestMapping("/save")
    public String save(Role role){
         roleService.insert(role);
//         return "redirect:/role";
        return SUCCESS_PAGE;
    }


//    @PreAuthorize("hasAnyAuthority('role.delete')")
    @RequestMapping("/delete/{roleId}")
    public String delete(@PathVariable("roleId") long roleId){
        roleService.delete(roleId);
        return "redirect:/role";
    }


//    @RequestMapping("/edit{roleId}")
//    public String edit(@PathVariable("roleId") long roleId,Map map){
//        Role role = roleService.getById(roleId);
//        map.put("relo",role);
//        return "role/edit";
//    }

//    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String edit(ModelMap model, @PathVariable Long id) {
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return "role/edit";
    }

//    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping(value="/update")
    public String update(Role role) {
        roleService.update(role);
        return SUCCESS_PAGE;
    }

    //去权限管理页面
    @PreAuthorize("hasAnyAuthority('role.assign')")
    @RequestMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable("roleId") Long roleId,Map map){
        //将角色id放入request中
        map.put("roleId",roleId);

        //调用permission中的方法获取权限的方法
        List<Map<String,Object>> zNodes = permissionService.findPermissionIdByRoleId(roleId);

        //将结果放入request中
        map.put("zNodes",zNodes);

        return "role/assignShow";
    }

//    @PreAuthorize("hasAnyAuthority('role.assign')")
    @RequestMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,@RequestParam("permissionIds")Long[] permissionIds){
        permissionService.assignPermission(roleId,permissionIds);
        return "common/successPage";
    }


}
