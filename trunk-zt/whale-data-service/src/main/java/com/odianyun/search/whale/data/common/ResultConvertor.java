package com.odianyun.search.whale.data.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.elasticsearch.common.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.MerchantProdMerchantCateTreeNode;
import com.odianyun.search.whale.data.model.MerchantProduct;
/**
 * hashMap转换成对象的工具类
 * @author ody
 *
 */
public class ResultConvertor {

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String RESULT_MAP = "resultMap";
	
	// 将hashmap结果转换成对象
	public static <T> T convertFromMap(Map<String, Object> data, Class<T> type) throws Exception{
		T result = type.newInstance();
		if(data == null || data.size() == 0){
			return result;
		}
        Field resultMapField = type.getField(RESULT_MAP);
        Map<String,String> resultMap = (HashMap<String, String>) resultMapField.get(null);
        if(null == resultMap){
        	return result;
        }
		Field[] fields=type.getDeclaredFields();
		for(Field field:fields){
			String propertyName = field.getName();
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			if(null == propertyName || isStatic){
				continue;
			}
			String setMethodName="set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
			Method setMethod=type.getMethod(setMethodName, field.getType());
			if(setMethod==null)
            	continue;        
            String fieldType = field.getType().getSimpleName(); 
            String mapKey = resultMap.get(propertyName);
            if(StringUtils.isBlank(mapKey)){
            	continue;
            }
            if(data.get(mapKey) == null){
            	mapKey =  mapKey.toLowerCase();
            	if(data.get(mapKey) == null){
                	continue;
                }
            }
            
            Object obj = data.get(mapKey);
            if("Integer".equals(fieldType) || "int".equals(fieldType)){
            	if("Long".equals(obj.getClass().getSimpleName())){
            		setMethod.invoke(result,((Long)obj).intValue());
            	}else if("String".equals(obj.getClass().getSimpleName())){
            		setMethod.invoke(result,Integer.valueOf((String)obj));
            	}else{
            		setMethod.invoke(result,(Integer)obj);
            	}
			}else if("Long".equals(fieldType) || "long".equals(fieldType)){
				if("Integer".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(result,((Integer)obj).longValue());
				}else if("String".equals(obj.getClass().getSimpleName())){
            		setMethod.invoke(result,Long.valueOf((String)obj));
            	}else{
		            setMethod.invoke(result,(Long)obj);
				}
			}else if("Double".equals(fieldType) || "double".equals(fieldType)){
				if("BigDecimal".equals(obj.getClass().getSimpleName())){
					setMethod.invoke(result,((BigDecimal)obj).doubleValue());
				}else{
		            setMethod.invoke(result,(Double)obj);
				}
			}else if("String".equals(fieldType)){
				if("Integer".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(result,((Integer)obj).toString());
				}else if("Long".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(result,((Long)obj).toString());
				}else if("Timestamp".equals(obj.getClass().getSimpleName())){
					setMethod.invoke(result,((java.sql.Timestamp)obj).toString());
				}else{
		            setMethod.invoke(result,(String)obj);
				}
			}
		}
		
		return result;
	} 
	
	public static <T> List<T> convertFromMap(List<Map> data, Class<T> type) throws Exception{
		List<T> list = new ArrayList<T>();
		if(data == null || data.size() == 0){
			return list;
		}
		for(Map map : data){
			T result = (T) convertFromMap(map,type);
			list.add(result);
		}
		
		return list;
	}
	
	public static <T> T convertFromMapWithBeanUtil(Map<String, Object> data, Class<T> type) throws Exception{
		T result = type.newInstance();
		if(data == null || data.size() == 0){
			return result;
		}
		org.apache.commons.beanutils.BeanUtils.populate(result, data);
		return result;
	}
	
	public static <T> List<T> convertFromMapWithBeanUtil(List<Map> data, Class<T> type) throws Exception{
		List<T> list = new ArrayList<T>();
		if(data == null || data.size() == 0){
			return list;
		}
		for(Map map : data){
			T result = type.newInstance();
			org.apache.commons.beanutils.BeanUtils.populate(result, map);
			list.add(result);
		}
		
		return list;
	}
	
	public static <T> T convertFromMapWithObjectMapper(Map<String, Object> data, Class<T> type) throws Exception{
		if(data == null || data.size() == 0){
			return type.newInstance();
		}
		T result = mapper.convertValue(data, type);
		return result;
	}
	
	public static <T> Map<String,String> convertToMapWithBeanUtil(T object) throws Exception{
		Map<String,String> map = new HashMap<>();
		if(null != object){
			map = org.apache.commons.beanutils.BeanUtils.describe(object);
		}
		return map;
	}
	
	public static <T> Map<String,Object> convertToMapWithObjectMapper(T object) throws Exception{
		Map<String,Object> map = new HashMap<>();
		if(null != object){
//			map =  mapper.convertValue(new BeanMap(object), Map.class);
			map =  mapper.convertValue(object, Map.class);

		}
		return map;
	}

	
	public static void main(String[] args) {
		Map<String,Object> data = new HashMap<>();
		data.put("id", 1l);
		data.put("name", "aabb");
		try {
//			Area aa = ResultConvertor.convertFromMap(data, Area.class);
			Field resultMapField = Area.class.getDeclaredField("resultMap");
			System.out.println(resultMapField.getName());
			System.out.println(resultMapField.get(null));

//			Area bb = ResultConvertor.convertFromMapWithBeanUtil(data, Area.class);
//			ObjectMapper mapper = new ObjectMapper();
//			Area cc = mapper.convertValue(data, Area.class);
//			System.out.println(aa);
//			System.out.println(bb);
//			System.out.println(cc);
			
//			System.out.println(convertToMapWithObjectMapper(cc));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}
