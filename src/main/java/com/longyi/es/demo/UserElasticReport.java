package com.longyi.es.demo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Administrator
 */
public interface UserElasticReport extends ElasticsearchRepository<Student, String> {

}
