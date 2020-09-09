package com.odianyun.search.whale.api.recommend;

import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.ClientRequestInfo;

import java.util.List;

/**
 * Created by zengfenghua on 16/12/11.
 */
public class RecommendRequest implements java.io.Serializable {

    //用户Id
    private Long userId;
    //推荐场景
    private RecommendScene recommendScene;
    //当前访问的商品
    private List<Long> currentVisitMpIds;

    //区分是否店铺内
    private List<Long> merchantIds;

    //销售区域
    private List<Long> saleAreaCodes;

    //是否过滤分销商品
    private Boolean isDistributionMp=null;
    private Point point;

    private int start=0;

    private int count=10;

    private ClientRequestInfo clientRequestInfo;

    public RecommendRequest(){

    }

    public RecommendRequest(RecommendScene recommendScene){
        this.recommendScene=recommendScene;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RecommendScene getRecommendScene() {
        return recommendScene;
    }

    public void setRecommendScene(RecommendScene recommendScene) {
        this.recommendScene = recommendScene;
    }

    public List<Long> getCurrentVisitMpIds() {
        return currentVisitMpIds;
    }

    public void setCurrentVisitMpIds(List<Long> currentVisitMpIds) {
        this.currentVisitMpIds = currentVisitMpIds;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ClientRequestInfo getClientRequestInfo() {
        return clientRequestInfo;
    }

    public void setClientRequestInfo(ClientRequestInfo clientRequestInfo) {
        this.clientRequestInfo = clientRequestInfo;
    }

    public List<Long> getSaleAreaCodes() {
        return saleAreaCodes;
    }

    public void setSaleAreaCodes(List<Long> saleAreaCodes) {
        this.saleAreaCodes = saleAreaCodes;
    }

    public List<Long> getMerchantIds() {
        return merchantIds;
    }

    public void setMerchantIds(List<Long> merchantIds) {
        this.merchantIds = merchantIds;
    }

    public Boolean getDistributionMp() {
        return isDistributionMp;
    }

    public void setDistributionMp(Boolean distributionMp) {
        isDistributionMp = distributionMp;
    }
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
