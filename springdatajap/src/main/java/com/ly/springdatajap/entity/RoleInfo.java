package com.ly.springdatajap.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/***
 * 角色信息
 */
@Entity
@Table(name = "role_info")
@Data
public class RoleInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "is_show")
    private boolean isShow;

}
