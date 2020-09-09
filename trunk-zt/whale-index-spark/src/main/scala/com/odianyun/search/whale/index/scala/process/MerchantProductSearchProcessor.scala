package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.model.OBusinessProduct

/**
  * Created by cuikai on 16/5/19.
  */
class MerchantProductSearchProcessor extends Processor{

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")

  override def process(processContext: ProcessContext) = {
    processContext.data = processContext.data.map(p => (p._1, convert2MerchantProductSearch(p._2,processContext)))
  }

  def convert2MerchantProductSearch(businessProduct: OBusinessProduct,processContext: ProcessContext): OBusinessProduct = {
    val merchantProductSearch = businessProduct.merchantProductSearch

    merchantProductSearch.productId = businessProduct.productId
    merchantProductSearch.merchantId = businessProduct.merchantId

    if(businessProduct.merchantProduct != null) {
      merchantProductSearch.is_deleted = businessProduct.merchantProduct.is_deleted
      merchantProductSearch.create_time = businessProduct.merchantProduct.create_time
      merchantProductSearch.code = businessProduct.merchantProduct.code
      merchantProductSearch.isNew = businessProduct.merchantProduct.isNew
      merchantProductSearch.companyId = businessProduct.merchantProduct.companyId.toInt
      merchantProductSearch.productName = businessProduct.merchantProduct.chinese_name
      merchantProductSearch.mpType = businessProduct.merchantProduct.product_type
      merchantProductSearch.tax = businessProduct.merchantProduct.tax
      merchantProductSearch.merchantSeriesId = businessProduct.merchantProduct.merchantSeriesId.toInt
      merchantProductSearch.sale_type = businessProduct.merchantProduct.sale_type
      merchantProductSearch.managementState = businessProduct.merchantProduct.managementState
    }

    if(businessProduct.product != null) {
      merchantProductSearch.ean_no = businessProduct.product.ean_no
      merchantProductSearch.productCode = businessProduct.product.code
      merchantProductSearch.calculation_unit = businessProduct.product.calculation_unit
      merchantProductSearch.standard = businessProduct.product.standard
    }

    if(businessProduct.price != null)
      merchantProductSearch.price = businessProduct.price.price

    if(businessProduct.stock != null)
      merchantProductSearch.stock = businessProduct.stock.stock

    if(businessProduct.merchant != null){
      merchantProductSearch.merchantType = businessProduct.merchant.merchantType
      merchantProductSearch.merchantName_search = businessProduct.merchant.shopName
    }

    if(businessProduct.volume4Sale != null)
      merchantProductSearch.volume4sale = businessProduct.volume4Sale.volume4Sale

    //评论
    if(businessProduct.rate != null){
      merchantProductSearch.rate = businessProduct.rate.rate
      merchantProductSearch.ratingCount = businessProduct.rate.ratingCount
      merchantProductSearch.positiveRate = businessProduct.rate.positiveRate
    }

    if(processContext.indexInfo.getIndexVersion != null){
      merchantProductSearch.updateTime = processContext.indexInfo.getIndexVersion
    }else{
      merchantProductSearch.updateTime = format.format(new java.util.Date())
    }


    businessProduct
  /*  merchantProductSearch.coverProvinceId
    merchantProductSearch.shopId*/


    //     tag_words:String = null

//     categoryId_search:String =null
//     categoryId:Long = NumConstants.ZERO
//     navCategoryId_search:String =null
//     categoryName_search:String =null

//     attrValueId_search:String=null
//      attrValue_search:String=null

//      merchantName_search:String =null
//      merchantCategoryId_search:String =null
//     merchant_categoryId:String  = null
  }
}
