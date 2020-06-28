package com.longyi.mybatis.test.annotation;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/24 18:44
 */
public class TestAnnotation {

  public static void main(String[] args) throws NoSuchMethodException {
      LongyiAnnotation longyiAnnotation=new LongyiAnnotation();
      Method sayBest = longyiAnnotation.getClass().getMethod("sayBest");
      Star annotation = AnnotationUtils.findAnnotation(sayBest, Star.class);
      System.out.println(annotation.value());
  }
}    
   