package com.longyi.mybatis.test;

import com.longyi.mybatis.test.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/18 14:50
 */
public class HelloMybatis {

  public static void main(String[] args) {
    String resource="config/Configuration.xml";
     Reader reader;
     try{
       reader= Resources.getResourceAsReader(resource);
       SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
       SqlSession sqlSession = sqlMapper.openSession();
       User user = (User)sqlSession.selectOne("com.longyi.mybatis.test.mapper.UserMapper.getUser", Long.valueOf(1));
      System.out.println(user.toString());
     }catch (Exception e){
      System.out.println(e);
     }
  }

}    
   