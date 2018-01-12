package online.pangge.exam.service;

import online.pangge.exam.domain.Admin;

import java.util.List;

public interface IAdminService {
    public int deleteByPrimaryKey(Long id);

    public int insert(Admin record);

    public Admin selectByPrimaryKey(Long id);

    public List<Admin> selectAll();

    public int updateByPrimaryKey(Admin record);

    public Admin login(String username,String password);
}
