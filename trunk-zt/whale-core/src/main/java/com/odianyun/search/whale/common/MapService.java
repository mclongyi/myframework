package com.odianyun.search.whale.common;

import com.google.gson.Gson;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.common.util.HttpClientUtil;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by fishcus on 16/12/28.
 */
public class MapService {

    static final String OK = "ok";

    static final String SUCCESS = "1";

    public static Point getPoint(String address,String map_url) throws Exception {

        GeoInfo geoInfo = getGeoInfo(address,map_url);
        String location = geoInfo.getLocation();
        String[] locationStrArray = location.split(",");
        Double longitude = Double.valueOf(locationStrArray[0]);
        Double latitude = Double.valueOf(locationStrArray[1]);

        return new Point(longitude, latitude);

    }

    public static GeoInfo getGeoInfo(String address,String map_url) throws Exception {
        map_url = map_url+"&address="+ URLEncoder.encode(address);
        JSONObject jsonObject= HttpClientUtil.doGet2JSONObject(map_url);
        JSONArray jsonArray=jsonObject.getJSONArray("geocodes");
        JSONObject obj = jsonArray.getJSONObject(0);
        GeoInfo geoInfo = GsonUtil.getGson().fromJson(obj.toString(),GeoInfo.class);
        Object districtObj = obj.get("district");
        if(districtObj instanceof JSONArray){
            JSONArray array  = (JSONArray)districtObj;
            if(array.length()>0){
                geoInfo.setDistricts((String)array.get(0));
            }
        }else {
            geoInfo.setDistricts((String)districtObj);
        }

        return geoInfo;
    }

    public static GeoPathInfo geoGeoPathInfo(Point origin,Point destination,String map_url) throws Exception {
        StringBuilder mapUrl = new StringBuilder(map_url);
        mapUrl.append("&origin=");
        mapUrl.append(origin.getLongitude());
        mapUrl.append(",");
        mapUrl.append(origin.getLatitude());
        mapUrl.append("&destination=");
        mapUrl.append(destination.getLongitude());
        mapUrl.append(",");
        mapUrl.append(destination.getLatitude());
        JSONObject jsonObject= HttpClientUtil.doGet2JSONObject(mapUrl.toString());
        GeoPathInfo geoPathInfo = new GeoPathInfo();
        geoPathInfo.setOrigin(origin);
        geoPathInfo.setDestination(destination);
        String status = (String)jsonObject.get("status");
        String info = (String)jsonObject.get("info");

        if(SUCCESS.equals(status) && OK.equals(info)){
            JSONObject obj = jsonObject.getJSONObject("route").getJSONArray("paths").getJSONObject(0);
            String distance = (String) obj.get("distance");
            if(StringUtils.isNotBlank(distance)){
                geoPathInfo.setDistance(Long.valueOf(distance));

            }
        }

        return geoPathInfo;
    }

    public static void main(String[] args) throws Exception {
        String address = "上海浦东张江高科";
        String mapUrl = IndexConstants.MAP_URL;
        long start = System.currentTimeMillis();
        GeoInfo info = getGeoInfo(address,mapUrl);
        System.out.println(System.currentTimeMillis()-start);
        System.out.println(info);

        Point origin = new Point(121.602944,31.199061);
        Point destination = getPoint(address,mapUrl);
        String map_working_url = IndexConstants.MAP_WORKING_URL;
        GeoPathInfo geoPathInfo = geoGeoPathInfo(origin,destination,map_working_url);

        System.out.println(geoPathInfo);


        for(int i=0;i<1000;i++){
            start = System.currentTimeMillis();
            info = getGeoInfo(address,mapUrl);
            long cost = System.currentTimeMillis() - start;
            if(cost > 100){
                System.out.println(cost);
            }
        }

        System.out.println("end");
    }



}
