package online.pangge.exam.service.impl;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.mapper.AdminMapper;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.QueryObject;
import online.pangge.exam.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<Long> id) {
        return adminMapper.deleteByIds(id);
    }

    @Override
    public int insert(Admin record) {
        return adminMapper.insert(record);
    }

    @Override
    public Admin selectByPrimaryKey(Long id) {
        return selectByPrimaryKey(id);
    }

    @Override
    public List<Admin> selectAll() {
        return adminMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Admin record) {
        return adminMapper.updateByPrimaryKey(record);
    }

    @Override
    public Admin login(String username, String password) {
        return adminMapper.selectAdminByUsernameAndPassword(username,password);
    }
    @Override
    public PageResult<Admin> page(QueryObject qo) {
        Long count = adminMapper.queryCount(qo);
        if (count <= 0) {
            return new PageResult(0, Collections.emptyList());
        }
        return new PageResult(count.intValue(), adminMapper.queryList(qo));
    }
}
