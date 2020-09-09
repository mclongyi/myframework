package com.odianyun.search.whale.index.geo;


import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.data.model.geo.O2OStore;
import com.odianyun.search.whale.index.geo.model.GeoStore;
import com.odianyun.search.whale.index.geo.model.Shape;

public class GSonTest {

	public static void main(String[] args) {
//		Gson gson = new Gson(); 
		O2OStore store = new O2OStore();
		store.setCompany_id(2l);
		store.setMerchantId(165l);
		store.setShopId(42l);
		store.setLocation("-77.03653, 38.897676");
		String polygon="[ [[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]] ]";
		StringBuffer deliveryAreaStr = new StringBuffer();
		deliveryAreaStr.append("[").append("\r\n");
		deliveryAreaStr.append(polygon);
		deliveryAreaStr.append("\r\n").append("]");
		System.out.println(deliveryAreaStr.toString());
		System.out.println("\n");
		store.setPolygon(deliveryAreaStr.toString());
		GeoStore geos = new GeoStore();
		geos.setCompanyId(store.getCompany_id());
		geos.setMerchantId(store.getMerchantId());
		geos.setShopId(store.getShopId());
		geos.setLocation(store.getLocation());
		geos.setPolygon(new Shape("multipolygon",store.getPolygon()));

		String s1 = GsonUtil.getGson().toJson(geos); 
		s1 = s1.replace("\"[", "[").replace("]\"", "]");
        System.out.println(s1);
	}
}
