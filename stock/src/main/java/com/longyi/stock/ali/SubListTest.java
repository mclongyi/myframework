package com.longyi.stock.ali;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/31 11:05
 */
public class SubListTest {

    public static void main(String[] args) {
        List<String> sourceList = new ArrayList() {{
            add("H");
            add("O");
            add("L");
            add("L");
            add("L");
            add("S");
        }};

        List subList = sourceList.subList(2, 5);
        List<String> childrenList = sourceList.stream().skip(2).limit(5).collect(Collectors.toList());
        System.out.println("sourceList:" + sourceList.toString());
        System.out.println("subList:" + subList.toString());
        System.out.println("children" + childrenList.toString());
        // 改变子对象结构
        System.out.println("开始修改子结构对象");
        subList.add("L");

        System.out.println("修改后：sourceList:" + sourceList.toString());
        System.out.println("修改后：subList:" + subList.toString());

        System.out.println("开始修改父结构对象");

        sourceList.add("W");
        System.out.println("修改后：sourceList:" + sourceList.toString());
        //System.out.println("修改后：subList:"+subList.toString());
        System.out.println("修改后:children" + childrenList.toString());
    }
}
   