package com.longyi.mybatis.test.mapper;

import com.longyi.mybatis.test.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/18 14:59
 */
public interface UserMapper {

    User getUser(@Param("id")Long id);

}
