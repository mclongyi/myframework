package com.odianyun.search.whale.common.serializer;

public interface YSerializer {

	public <T> T fromBytes(Class<T> clz, byte[] bytes);

	public byte[] toBytes(Object obj);

}
