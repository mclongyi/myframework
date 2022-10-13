/*
 * Copyright 2022 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package com.longyimybatis.unittest;

import com.longyimybatis.source.dao.StudentInfoMapper;
import com.longyimybatis.source.dto.StudentInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author leiyi
 * @since 2022/10/13 1:45 PM
 */
public class MybatisTest {

    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        StudentInfoMapper mapper = sqlSession.getMapper(StudentInfoMapper.class);
        StudentInfo studentInfo=new StudentInfo();
        studentInfo.setAddress("上海闵行");
        studentInfo.setAge(20);
        studentInfo.setName("测试1232");
        mapper.insert(studentInfo);
    }
}
