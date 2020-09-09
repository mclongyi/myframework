package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.ShopService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 17/3/9.
 */
public class GetChildMerchantByPointHandlerTest {



    public static void main(String[] args) throws Exception {
        System.setProperty("global.config.path","/data/hhplus_branch_env");
        ShopService shopService=SearchClient.getShopService("test");
        List<Long> parentIds=new ArrayList<Long>();
        parentIds.add(1022021800000001l);
        Point point=new Point(121.485806,31.235078);
        Map<Long,List<Merchant>> map= shopService.getChildMerchantByPoint(parentIds,point,11);
        System.out.println(map);
    }
}
