package com.odianyun.search.whale.suggest.resp.handler;

public interface ResponseHandler<ESResponse, Response> {

	public void handle(ESResponse esResponse, Response response) throws Exception;

}
