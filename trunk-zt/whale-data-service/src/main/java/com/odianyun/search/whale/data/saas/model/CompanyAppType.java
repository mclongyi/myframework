package com.odianyun.search.whale.data.saas.model;

public enum CompanyAppType {
	
	B2C("b2c"),
	O2O("o2o"),
	SUGGEST("suggest");
	private String value;
	
	private CompanyAppType(String value) {
        this.value = value;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
