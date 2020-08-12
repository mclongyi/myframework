package com.longyi.csjl.retrofitscan;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest
@RunWith(SpringRunner.class)
class RestServiceTest {
  @Autowired
  private RestService restService;


  @Test
  void getWarehouseInfo() {
    Response result = restService.getWarehouseInfo("X001", 11);
    System.out.println(result.getData());
  }
}