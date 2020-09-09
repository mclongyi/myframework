package com.odianyun.search.whale.data.saas.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.saas.dao.CompanyDao;
import com.odianyun.search.whale.data.saas.model.Company;

public class CompanyDaoImpl extends SqlMapClientDaoSupport implements CompanyDao{

	@Override
	public List<Company> queryAllCompany() {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("queryAllCompany");
	}

	@Override
	public List<Company> queryAllCompanyByGroup(String groupName) {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("queryAllCompanyByGroup",groupName);
	}

}
