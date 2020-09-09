package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.model.{OBrand, OBusinessProduct}

import scala.collection.Map

/**
  * Created by cuikai on 16/5/20.
  */
class ProductBrandProcessor extends Processor {

  override def process(processContext: ProcessContext) = {
    val brandMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.brand, Map.empty[Long, OBrand])
      .asInstanceOf[Map[Long, OBrand]]

    processContext.data = processContext.data.map(p => (p._1, calcBrand(p._2, brandMap)))
  }

  def calcBrand(businessProduct: OBusinessProduct, brandMap: Map[Long, OBrand]): OBusinessProduct = {
    if(businessProduct.product != null) {
      val brandId = businessProduct.product.brand_id
      businessProduct.merchantProductSearch.brandId_search = brandId.toString
      val brand = brandMap.getOrElse(brandId, null)
      if (brand != null) {
        businessProduct.merchantProductSearch.brandName_search = brand.chinese_name + " " + brand.english_name + " " + brand.name
      }
    }

    businessProduct
  }
}
