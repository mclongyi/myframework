package com.odianyun.search.whale.index.api.common;

public class IndexException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1602572355950884628L;
	
	public IndexException(){
		super();
	}
	
	public IndexException(String message){
		super(message);
	}
	public IndexException(Exception e){
		super(e);
	}
	public IndexException(String message, Throwable cause){
		super(message, cause);
	}

}
