package com.ly.springdatajap.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mclongyi
 * @date:2019-6-1
 */
@Entity
@Table(name="user_info")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;


    @Column(name = "age")
    private Integer age;

    @Column(name = "create_date")
    private String createDate;


    /**
     * 一对多关联
     * orphanRemoveal=true 表示删除的时候删除相关联的实体对象
     */
    @OneToMany( cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "user_role_id",referencedColumnName = "id")
    private List<RoleInfo> roleInfoList=new ArrayList<>();

    public void  setRoleInfoList(List<RoleInfo> roleInfoList){
        this.roleInfoList.clear();
        this.roleInfoList.addAll(roleInfoList);
    }



}
