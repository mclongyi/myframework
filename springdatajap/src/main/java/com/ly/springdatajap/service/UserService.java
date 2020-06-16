package com.ly.springdatajap.service;

import cn.hutool.core.date.DateTime;
import com.ly.springdatajap.entity.UserInfo;
import com.ly.springdatajap.resposity.UserResposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserResposity userResposity;

    @Transactional
    public boolean addUser(UserInfo userInfo) {
        if (Objects.nonNull(userInfo)) {
            userResposity.save(userInfo);
        }
        return true;
    }


    public boolean delete(Integer id) {
        if (Objects.nonNull(id)) {
            userResposity.deleteById(id);
        }
        return true;
    }


    public List<UserInfo> findList() {
        return userResposity.findAll();
    }

    public Optional<UserInfo> findById(Integer id) {
        return userResposity.findById(id);
    }

    /**
     * 根据多条件查询
     *
     * @param pageable
     * @param searchKey
     * @param beginDate
     * @param endDate
     * @param isShow
     * @return
     */
    public Page<UserInfo> findByConsition(Pageable pageable, String searchKey, String beginDate, String endDate, boolean isShow) {
        Specification query = buildSpecification(searchKey, beginDate, endDate, isShow);
        Page<UserInfo> all = userResposity.findAll(query, pageable);
        return all;
    }

    private Specification<UserInfo> buildSpecification(String searchKey, String beginDate, String endDate, boolean isShow) {
        Specification<UserInfo> query = (Specification<UserInfo>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> roleInfoList = null;
            if (Objects.nonNull(searchKey)) {
                roleInfoList = root.join("roleInfoList");
                Predicate or = criteriaBuilder.or(criteriaBuilder.like(roleInfoList.get("roleName"), "%" + searchKey + "%"),
                        criteriaBuilder.like(root.get("name"), "%" + searchKey + "%"));
                predicates.add(or);
                if (Objects.isNull(isShow)) {
                    predicates.add(criteriaBuilder.isTrue(roleInfoList.get("isShow")));
                }
            }
            if (Objects.nonNull(beginDate) && Objects.nonNull(endDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.between(root.get("createDate"), beginDate, endDate)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return query;

    }

}
