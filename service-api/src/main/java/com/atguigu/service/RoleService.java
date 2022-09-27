package com.atguigu.service;

import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {
    List<Role> findAll();


    //根据用户Id查询用户角色
    Map<String , Object> findRolesByAdminId(Long id);

    void assignRole(Long adminId, Long[] roleIds);
}
