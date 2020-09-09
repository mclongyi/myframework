package com.odianyun.search.whale.index.geo;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.data.model.geo.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xml.utils.StringBufferPool;
import org.elasticsearch.common.geo.GeoPoint;

import com.odianyun.search.whale.data.geo.service.POIService;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.index.geo.GeoProcessorApplication;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public class GeoProcessor implements Processor{

	static Logger logger = Logger.getLogger(GeoProcessor.class);
	public static final String Splitter_Point = "\\}\\{";
	MerchantService merchantService;
	POIService poiService;
	
	public GeoProcessor(){
		merchantService =(MerchantService)GeoProcessorApplication.getBean("merchantService");
		poiService =(POIService)GeoProcessorApplication.getBean("poiService");
	}
	@Override
	public String getName() {
		return GeoProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<DataRecord> newDataRecords = new ArrayList<DataRecord>();
		for(DataRecord<Merchant> dataRecord : dataRecords){
			Merchant merchant = dataRecord.getV();
			O2OStore store = convert(merchant);
			if(store != null ){
				newDataRecords.add(new DataRecord<O2OStore>(store));
			}
		}
		
		processorContext.setDataRecords(newDataRecords);
	}
	
	public O2OStore convert(Merchant merchant) throws Exception{
		if(merchant==null)
			return null;
		O2OStore store = new O2OStore();
		Long merchantId = merchant.getId();
		store.setMerchantId(merchantId);
		store.setCompany_id(merchant.getCompany_id());
		store.setMerchantFlag(merchant.getMerchant_flag());
		store.setParentId(merchant.getParentId());
		Shop shop = merchantService.getShop(merchantId,merchant.getCompany_id().intValue());
		if(shop!=null){
			store.setShopId(shop.getId());
			String tagWords = null;
			if(merchant.getName()!=null){
				tagWords = merchant.getName();
			}
			if(shop.getName()!=null){
				tagWords = tagWords +" "+ shop.getName();
			}
			if(shop.getAddress()!=null){
				tagWords = tagWords +" "+ shop.getAddress();
			}
			store.setBusinessState(shop.getBusinessState());
			store.setTag_words(tagWords);
			store.setHasInSiteService(shop.getHasInSiteService());
		}
		List<POI> poilist = poiService.getShopPOIById(merchantId,merchant.getCompany_id().intValue());
		if(poilist!=null&&poilist.size()>0){
			POI poi = poilist.get(0);
            if(poi.getLongitude()!=null&&poi.getLatitude()!=null){
            	store.setLocation("["+poi.getLongitude()+","+poi.getLatitude()+"]");
            }
			List<Long> areaCodes = new ArrayList<>();
			if (poi.getProvinceId() != null) {
				areaCodes.add(poi.getProvinceId());
			}
			if (poi.getCityId() != null) {
				areaCodes.add(poi.getCityId());
			}
			if (poi.getRegionId() != null) {
				areaCodes.add(poi.getRegionId());
			}
			store.setAreaCodes(areaCodeBuild(areaCodes));
		}
		List<DeliveryArea> deliveryAreaList = poiService.getShopDeliveryAreaById(merchantId,merchant.getCompany_id().intValue());
		if(deliveryAreaList!=null&&deliveryAreaList.size()>0){
			StringBuffer deliveryAreaStr = new StringBuffer();
			deliveryAreaStr.append("[ ");
			int len1 = deliveryAreaList.size();
			for(int i=0;i<len1;i++){
				DeliveryArea area = deliveryAreaList.get(i);
				String poiAddress =area.getPoi_addr();
				deliveryAreaStr.append(formatPolygon1(poiAddress));
				if(i<len1-1){
					deliveryAreaStr.append(",");
				}
			}
			deliveryAreaStr.append(" ]");
			store.setPolygon(deliveryAreaStr.toString());
		}
		List<MerchantFlag> flags = poiService.getMerchantFlags(merchantId,merchant.getCompany_id().intValue());
		if(flags!=null && flags.size()>0){
			StringBuffer flagCode = new StringBuffer();
			for(MerchantFlag flag : flags){
				String  merchantFlagCode = flag.getMerchantFlagCode();
				if(StringUtils.isNoneBlank(merchantFlagCode)) {
					flagCode.append(merchantFlagCode).append(" ");
				}
				String  merchantFlagName = flag.getMerchantFlagName();
				if(StringUtils.isNoneBlank(merchantFlagName)) {
					flagCode.append(merchantFlagName).append(" ");
					store.setTag_words(store.getTag_words()+" "+merchantFlagName);
				}

			}
			store.setCodeSearch(flagCode.toString());
		}
		//TODO
		List<BusinessTime> businessTimes = merchantService.getMerchantBusinessTimes(merchantId,merchant.getCompany_id().intValue());
		if(CollectionUtils.isNotEmpty(businessTimes)){
			store.setBusinessTimes(businessTimes);
		}
		//外卖新增merchantType
		if(null != merchant.getMerchant_type()){
			store.setMerchantType(merchant.getMerchant_type());
		}
		return store;
	}

	private String areaCodeBuild(List<Long> areaCodes) {
		StringBuffer retStr = new StringBuffer("");
		int i = 0;
		for(Long areaCode : areaCodes){
			i++;
			retStr.append(areaCode);
			if(i < areaCodes.size()){
				retStr.append(" ");
			}
		}
		return retStr.toString();
	}
	
	public static String formatPolygon(String poiStr){
		String ret ="[ [";
		poiStr = poiStr.replaceAll(Splitter_Point, "\\],\\[");
		poiStr = poiStr.replace("{","[").replace("}", "]");
		int index = poiStr.indexOf("]");
		String tmp = poiStr.substring(0, index+1);
		ret += poiStr+","+tmp;
		ret +="] ]";
		return ret;
	}

	public static String formatPolygon1(String poiStr){
		if(poiStr==null || poiStr.length()==0){
			return null;
		}
		StringBuffer ret = new StringBuffer("[ [");
		//String str = "["+poiStr+"]";
		String str = poiStr;
		JSONArray data = JSONArray.fromObject(str);
		if(data!=null && data.size()>0){
			for(int i = 0;i<data.size();i++){
				JSONObject obj = data.getJSONObject(i);
				ret.append("[").append(obj.get("lng")).append(",").append(obj.get("lat")).append("]");
				if(i<data.size()-1){
					ret.append(",");
				}else{
					ret.append(",");
					obj = data.getJSONObject(0);
					ret.append("[").append(obj.get("lng")).append(",").append(obj.get("lat")).append("]");
				}
			}
		}
		ret.append("] ]");
		return ret.toString();
	}
	
  public static void main(String[] args) {
	  /*StringBuffer deliveryAreaStr = new StringBuffer();
		String poiAddress ="{121.600543,31.199196}{121.601862,31.19915}{121.601181,31.19837}";
		String ret =formatPolygon(poiAddress);
		deliveryAreaStr.append(ret);
		System.out.println(deliveryAreaStr.toString());*/

	  StringBuffer deliveryAreaStr = new StringBuffer();
	  deliveryAreaStr.append("[ ");
	  for(int i=0;i<1;i++){
		  String poiAddress ="{121.600543,31.199196}{121.601862,31.19915}{121.601181,31.19837}";
		  deliveryAreaStr.append(formatPolygon(poiAddress));
	  }
	  deliveryAreaStr.append(" ]");
	  System.out.println(deliveryAreaStr.toString());




	String str = "{\"J\":31.200344,\"G\":121.600708,\"lng\":121.600708,\"lat\":31.200344},{\"J\":31.198637,\"G\":121.60105099999998,\"lng\":121.601051,\"lat\":31.198637},{\"J\":31.198289,\"G\":121.60331500000001,\"lng\":121.603315,\"lat\":31.198289},{\"J\":31.198605,\"G\":121.604447,\"lng\":121.604447,\"lat\":31.198605},{\"J\":31.199619,\"G\":121.60438299999998,\"lng\":121.604383,\"lat\":31.199619},{\"J\":31.200436,\"G\":121.60249399999998,\"lng\":121.602494,\"lat\":31.200436},{\"J\":31.201349,\"G\":121.602441,\"lng\":121.602441,\"lat\":31.201349},{\"J\":31.201322,\"G\":121.602081,\"lng\":121.602081,\"lat\":31.201322}";
	  System.out.println(formatPolygon1(str));
}
}
