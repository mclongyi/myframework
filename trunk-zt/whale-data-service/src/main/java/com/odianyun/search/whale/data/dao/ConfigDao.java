package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.Config;

public interface ConfigDao {

	List<Config> queryAllConfigData(Long companyId);

}
