package com.longyi.csjl.elasticsearch;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public boolean save(Student student){
        Random random=new Random();
        for(int i=0;i<20;i++){
            Student studentInfo=new Student();
            int data=random.nextInt(100);
            studentInfo.setAge(data);
            studentInfo.setDesc("测试数据"+getRandomChar());
            studentInfo.setId(IdUtil.simpleUUID().toLowerCase());
            studentInfo.setName(getRandomChar());
            studentRepository.save(studentInfo);
        }
        return true;
    }

    private static String getRandomChar() {
        String str = "";
        int hightPos; //
        int lowPos;
        Random random = new Random();
        for(int i=0;i<4;i++){
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(hightPos)).byteValue();
            b[1] = (Integer.valueOf(lowPos)).byteValue();
            try {
                str += new String(b, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("错误");
            }
        }

        return str;
    }


    public void delAll(){
        studentRepository.deleteAll();
    }

    /**
     * ES多条件查询
     * @param student
     * @return
     */
    public PageInfo<Student> queryEsInfoByCondition(Student student){
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(student.getId())){
            boolQueryBuilder.must(QueryBuilders.termQuery("id",student.getId()));
        }
        if(!StringUtils.isEmpty(student.getName())){

            boolQueryBuilder.must(QueryBuilders.termQuery("name.keyword",student.getName()));
        }
        if(!StringUtils.isEmpty(student.getDesc())){
            //模糊匹配模式
            //全匹配模式
            //多字段匹配模式 MultiMatchQueryBuilder QueryBuilders.multiMatchQuery(student.getName(),"name","desc");
            boolQueryBuilder.must(QueryBuilders.matchQuery("desc",student.getDesc()));
        }
        if(null!=student.getAge()){
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
            rangeQuery.from(student.getAge()-10);
            rangeQuery.to(student.getAge());
            boolQueryBuilder.must(rangeQuery);
        }
        PageRequest page = PageRequest.of(0, 10);
        NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();
        builder.withFilter(boolQueryBuilder);
        builder.withPageable(page);
        builder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
        Page<Student> search = studentRepository.search(builder.build());
        PageInfo<Student> pageInfo=new PageInfo<>(search.toList());
        pageInfo.setTotal(search.getTotalPages());
        return pageInfo;
    }


}
