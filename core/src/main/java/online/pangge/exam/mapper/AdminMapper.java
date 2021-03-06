package online.pangge.exam.mapper;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Admin record);

    Admin selectByPrimaryKey(Long id);

    List<Admin> selectAll();

    int updateByPrimaryKey(Admin record);

    int deleteByIds(@Param("list") List<Long> id);

    long queryCount(QueryObject qo);

    List<Admin> queryList(QueryObject qo);

    /**
     *登陆使用方法
     * @param username
     * @param password
     * @return
     */
    Admin selectAdminByUsernameAndPassword(@Param("username")String username, @Param("password")String password);
}