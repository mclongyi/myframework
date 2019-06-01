package com.ly.springdatajap.resposity;

import com.ly.springdatajap.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserResposity extends JpaRepository<UserInfo,Integer>, JpaSpecificationExecutor<UserInfo> {



}
