package com.odianyun.search.whale.data.saas.dao;


import java.util.List;

import com.odianyun.search.whale.data.saas.model.Company;

public interface CompanyDao {
	
	public List<Company> queryAllCompany();
	
	public List<Company> queryAllCompanyByGroup(String groupName);

}
