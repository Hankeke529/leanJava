package com.atguigu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface PermissionService extends BaseService<Permission>{
    List<Map<String, Object>> findPermissionIdByRoleId(Long roleId);

    void assignPermission(Long roleId, Long[] permissionIds);

    List<Permission> getMenuPermissionsByAdminId(Long userId);

    List<String> getPermissionCodeByAdminId(Long id);
}
