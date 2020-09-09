package com.odianyun.search.whale.suggest.req.builder;

public interface RequestBuilder<ESRequest, Request> {

	public void build(ESRequest esRequest, Request request);

}
