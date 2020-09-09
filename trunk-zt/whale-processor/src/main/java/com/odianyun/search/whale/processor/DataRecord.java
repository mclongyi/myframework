package com.odianyun.search.whale.processor;

public class DataRecord<V> {
	     
	private V v;

	public DataRecord(V v) {
		super();
		this.v = v;
	}

	public V getV() {
		return v;
	}

	public void setV(V v) {
		this.v = v;
	}

	@Override
	public String toString() {
		return "DataRecord [v=" + v + "]";
	}

}
