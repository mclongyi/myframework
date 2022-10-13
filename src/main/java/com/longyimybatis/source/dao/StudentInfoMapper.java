package com.longyimybatis.source.dao;

import com.longyimybatis.source.dto.StudentInfo;

/**
 * @author leiyi
 * @since 2022/10/13 1:38 PM
 */
public interface StudentInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentInfo record);

    int insertSelective(StudentInfo record);

    StudentInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudentInfo record);

    int updateByPrimaryKey(StudentInfo record);
}