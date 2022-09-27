package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Override
    protected BaseDao getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionIdByRoleId(Long roleId) {

        //获取所有权限
        List<Permission> permissionList =  permissionDao.findAll();

        //创建返回的list
        List<Map<String, Object>> returnList = new ArrayList<>();
        //根据角色id查询已分配的权限
        List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleId(roleId);
        //遍历所有的权限
        for (Permission permission : permissionList) {
            //判断权限是否在当前的角色里
            Map<String,Object> map = new HashMap<>();
            map.put("id",permission.getId());
            map.put("pid",permission.getParentId());
            map.put("name",permission.getName());
            //判断当前的权限诗否是在permissionIds中h
            if (permissionIds.contains(permission.getId())){
                map.put("checked" , true);
            }
            //将map放入list中
            returnList.add(map);

        }
        return returnList;
    }

    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        //先删除原有权限
        rolePermissionDao.deletePermissionIdsByRoleId(roleId);
        //遍历所有的权限
        for (Long permissionId : permissionIds) {
            if (permissionId != null){
                //保存权限id和角色id
                rolePermissionDao.addRoleIdAndPermissionId(roleId,permissionId);
            }
        }
    }

    @Override
    public List<Permission> getMenuPermissionsByAdminId(Long userId) {
        List<Permission> permissionList = null;
        if (userId == 1){
            //说明是系统管理员,然后获取所有权限
            permissionList = permissionDao.findAll();
        }else {
            //根据用户id查询对应的权限
            permissionList = permissionDao.getMenuPermissionByAdminId(userId);
        }

        //通过工具类将list转换为树形结构
        List<Permission> treeList = PermissionHelper.bulid(permissionList);


        return treeList;
    }

    @Override
    public List<String> getPermissionCodeByAdminId(Long id) {

        List<String> permissionCodes = null;
        if (id == 1){

            permissionCodes = permissionDao.getAllPermissionCodes();
        }else {
            permissionCodes = permissionDao.getPermissionCodeByAdminId(id);
        }
        return permissionCodes;
    }


}
