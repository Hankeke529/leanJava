package com.atguigu.dao;

import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleDao extends BaseDao<AdminRole> {


    List<Long> findRoleIdByAdminId(Long id);

    void deleteAssignRoleIds(Long adminId);

    void addRoleIdAndAdminId(@Param("roleId") Long roleId, @Param("adminId") Long adminId);
}
