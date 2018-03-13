package online.pangge.exam.mapper;


import online.pangge.exam.domain.Permission;
import online.pangge.exam.query.PermissionQueryObject;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Long id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    Long queryForPageCount(PermissionQueryObject qo);

    List<Permission> queryForPage(PermissionQueryObject qo);

    /**
     * 根据管理员id查询拥有权限
     * @param adminId   管理员id
     * @return  该管理拥有的权限表达式的集合
     */
    List<String> queryByAdminId(Long adminId);

}