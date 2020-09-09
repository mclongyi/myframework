package com.odianyun.search.whale.processor;

import java.util.List;

public interface ProcessorsBuilder {
	
	public List<Processor> build() throws Exception;

}

