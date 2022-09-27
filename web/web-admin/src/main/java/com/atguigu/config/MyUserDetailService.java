package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Component
public class MyUserDetailService implements UserDetailsService {


    @Reference
    private AdminService  adminService;

    @Reference
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询admin对象
        Admin admin = adminService.getAdminByUserName(username);


        if (admin == null){
            throw new UsernameNotFoundException("用户名不存在~");
        }
        //创捷一个集合存放每一个状态
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        //获取用户权限的状态码
        List<String> permissionCodes = permissionService.getPermissionCodeByAdminId(admin.getId());
        //遍历得到每一个状态码
        for (String permissionCode : permissionCodes) {
            if (!StringUtils.isEmpty(permissionCode)) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permissionCode);
                grantedAuthorityList.add(simpleGrantedAuthority);
            }
        }

        return new User(username,admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
    }
}
