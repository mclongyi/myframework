package com.odianyun.search.whale.data.service;

public interface ConfigService {
	
	String get(String configName,int companyId);
	
	String get(String configName,String def,int companyId);
	
	Double getDouble(String configName,int companyId);
	
	Double getDouble(String configName,double def,int companyId);
	
	Long getLong(String configName,int companyId);
	
	Long getLong(String configName,long def,int companyId);
	
	Integer getInt(String configName,int companyId);
	
	Integer getInt(String configName,int def,int companyId);

	Boolean getBool(String string,int companyId);

	Boolean getBool(String string, boolean def,int companyId);


}
