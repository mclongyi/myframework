package com.odianyun.search.whale.api.common;

/**
 * 运行时异常
 * 
 * @author E
 *
 */
public class SearchException extends RuntimeException {

	private static final long serialVersionUID = 8879372824773187442L;
	
	public SearchException(){
		super();
	}
	
	public SearchException(String message){
		super(message);
	}
	public SearchException(Exception e){
		super(e);
	}
	public SearchException(String message, Throwable cause){
		super(message, cause);
	}

}
