package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

//    @Autowired
    @Resource
    private RoleDao roleDao;
    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Map<String, Object> findRolesByAdminId(Long id) {
        //获取用户所有角色
        List<Role> roleList = roleDao.findAll();
//        System.out.println("测试数据======"+ roleList);
        //获取用户拥有的角色id
        List<Long> roleIds = adminRoleDao.findRoleIdByAdminId(id);
        //存放已选择和未选择的角色
        List<Role> noAssginRoleList= new ArrayList<>();
        List<Role> AssginRoleList= new ArrayList<>();

        //遍历所有角色
        for (Role role : roleList) {
            if (roleIds.contains(role.getId())){
                AssginRoleList.add(role);
            }else{
                noAssginRoleList.add(role);
            }
        }
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("noAssginRoleList",noAssginRoleList);
        roleMap.put("AssginRoleList",AssginRoleList);
//        System.out.println("noAssginRoleList======" + noAssginRoleList);
//        System.out.println("AssginRoleList======" + AssginRoleList);
        return roleMap;
    }

    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        //先根据用户id,把已分配角色删除
        adminRoleDao.deleteAssignRoleIds(adminId);
        //遍历所有的角色id
        for (Long roleId : roleIds) {
            if (roleId != null){
            //将用户id和角色id插入到数据库当中
            adminRoleDao.addRoleIdAndAdminId(roleId,adminId);
            }


        }
    }

    @Override
    protected BaseDao<Role> getEntityDao() {
        return this.roleDao;
    }
}
