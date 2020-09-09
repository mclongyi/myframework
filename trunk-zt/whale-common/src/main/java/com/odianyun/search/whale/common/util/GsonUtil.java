package com.odianyun.search.whale.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	
	static Gson gson;
	
	static {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.disableHtmlEscaping();
		gson=gsonBuilder.create();
	}
	
	public static Gson getGson(){
		return gson;
	}
	
	public static void main(String[] args){

		System.out.println(gson.toJson(gson));
	}

}
