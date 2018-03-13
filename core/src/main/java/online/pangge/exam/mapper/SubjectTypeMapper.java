package online.pangge.exam.mapper;

import online.pangge.exam.domain.SubjectType;
import online.pangge.exam.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubjectTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SubjectType record);

    SubjectType selectByPrimaryKey(Long id);

    List<SubjectType> selectAll();

    int updateByPrimaryKey(SubjectType record);

    int deleteByIds(@Param("list") List<Long> id);

    long queryCount(QueryObject qo);

    List<SubjectType> queryList(QueryObject qo);
}