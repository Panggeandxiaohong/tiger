package online.pangge.exam.service;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.QueryObject;

import java.util.List;

public interface IAdminService {
    public int deleteByPrimaryKey(Long id);

    public int deleteByIds(List<Long> id);

    public int insert(Admin record);

    public Admin selectByPrimaryKey(Long id);

    public List<Admin> selectAll();

    public int updateByPrimaryKey(Admin record);

    public Admin login(String username,String password);

    PageResult page(QueryObject qo);
}
