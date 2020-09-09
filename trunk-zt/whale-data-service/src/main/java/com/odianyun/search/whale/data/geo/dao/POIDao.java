package com.odianyun.search.whale.data.geo.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.geo.DeliveryArea;
import com.odianyun.search.whale.data.model.geo.MerchantFlag;
import com.odianyun.search.whale.data.model.geo.POI;

public interface POIDao {
     public List<POI> queryAllPOIs(int companyId) throws Exception;
     
     public List<POI> getPOIsWithPage(int pageNo,int pageSize,int companyId) throws Exception;
     
     public List<POI> getPOIsByIds(List<Long> shopIds,int companyId) throws Exception;
     
     public List<DeliveryArea> queryDeliveryAreasWithPage(int pageNo,int pageSize,int companyId) throws Exception;

     public List<MerchantFlag> queryAllFlags(int companyId) throws Exception;

     public List<DeliveryArea> queryDeliveryAreasByIds(List<Long> merchantIds,int companyId) throws Exception;

     public List<MerchantFlag> queryFlagsByIds(List<Long> merchantIds,int companyId) throws Exception;
}
