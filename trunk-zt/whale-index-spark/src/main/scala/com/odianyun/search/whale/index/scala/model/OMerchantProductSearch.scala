package com.odianyun.search.whale.index.scala.model

import com.odianyun.search.whale.index.scala.common.NumConstants

import scala.beans.BeanProperty
/**
  * Created by zengfenghua on 16/5/17.
  */
case class OMerchantProductSearch(id:Long) {

  @BeanProperty var productId:Long = NumConstants.ZERO

  @BeanProperty var tag_words:String = null

  @BeanProperty var categoryId_search:String =null

  @BeanProperty var categoryId:Long = NumConstants.ZERO

  @BeanProperty var navCategoryId_search:String =null

  @BeanProperty var categoryName_search:String =null

  @BeanProperty var  brandId_search:String=null

  @BeanProperty var brandName_search:String=null

  @BeanProperty var attrValueId_search:String=null

  @BeanProperty var  attrValue_search:String=null

  @BeanProperty var  merchantId:Long = NumConstants.ZERO

  @BeanProperty var  coverProvinceId:String = null

  @BeanProperty var  is_deleted: Int = NumConstants.ZERO

  @BeanProperty var create_time:String =null

  @BeanProperty var  price:Double = NumConstants.ZERO

  @BeanProperty var  ean_no:String =null
  //商品code
  @BeanProperty var code:String = null

  @BeanProperty var productCode:String = null

  @BeanProperty var  merchantName_search:String =null

  @BeanProperty var  merchantCategoryId_search:String =null

  @BeanProperty var isNew:Int = NumConstants.ZERO

  @BeanProperty var stock:Int = NumConstants.ZERO

  @BeanProperty var  merchantType:Int = NumConstants.ZERO //10:自营，11：第三方

  @BeanProperty var shopId:Long = NumConstants.ZERO

  @BeanProperty var merchant_categoryId:String  = null

  @BeanProperty var companyId:Int = NumConstants.NEGATIVE_ONE

  @BeanProperty var picUrl:String = null

  @BeanProperty var productName:String =null
   //在es b2c_mapping对应type字段
  @BeanProperty var mpType:Int = NumConstants.NEGATIVE_NINE_NINE

  @BeanProperty var tax:String =null

  @BeanProperty var merchantSeriesId: Int = NumConstants.ZERO

  @BeanProperty var isMainSeries : Int =NumConstants.NEGATIVE_ONE

  @BeanProperty var isMain : Int =NumConstants.NEGATIVE_ONE

  @BeanProperty var seriesAttrValueIdSearch:String =null

  //商品销数售量
  @BeanProperty var volume4sale:Long = NumConstants.ZERO
  //真实销量
  @BeanProperty var realVolume4sale:Long = NumConstants.ZERO

  //计量单位:5kg/箱
  @BeanProperty  var calculation_unit:String =null

  @BeanProperty var  areaCode:String=null //商家所属的四级区域code

  @BeanProperty var sale_type: Int = NumConstants.ZERO

  //产品规格
  @BeanProperty var standard:String = null

  //产品规格
  @BeanProperty var updateTime:String = null

  //评价
  @BeanProperty var rate:Double = 0
  //好评数
  @BeanProperty var positiveRate:Int = 0
  //好评率
  @BeanProperty var ratingCount:Int = 0
  //是否上架
  @BeanProperty var managementState:Int = 0

  @BeanProperty var seasonWeight : Int = 0

  @BeanProperty var hasPic : Int = 2

  @BeanProperty var compositeSort : Int = 0

  @BeanProperty var promotionId_search : String = null

  @BeanProperty var promotionType_search : String = null
}

object test{

  def main(args: Array[String]) {
    var mp=OMerchantProductSearch(2L)
    println(mp)

  }

}

