package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, NumConstants, ProcessContext}
import com.odianyun.search.whale.index.scala.model.{OAttributeValue, OBusinessProduct}

import scala.collection.{Map, mutable}
import scala.collection.mutable.ListBuffer

/**
  * Created by cuikai on 16/5/20.
  */
class MerchantProductSeriesProcessor extends Processor{

  override def process(processContext: ProcessContext) = {
    val merchantSeriesMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.merchantSeries,
      Map.empty[Long, mutable.LinkedList[OAttributeValue]]).asInstanceOf[Map[Long, mutable.LinkedList[OAttributeValue]]]
    val mainSeriesMerchantProductsMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.mainSeriesMerchantProducts,
      Map.empty[Long,Long]).asInstanceOf[Map[Long, Long]]
    processContext.data = processContext.data.map(p => (p._1, calcSeries(p._2, merchantSeriesMap,mainSeriesMerchantProductsMap)))
  }

  def calcSeries(businessProduct: OBusinessProduct,
                 merchantSeriesMap: Map[Long, mutable.LinkedList[OAttributeValue]],mainSeriesMerchantProductsMap:Map[Long,Long]): OBusinessProduct = {
    val attributeValues = merchantSeriesMap.getOrElse(businessProduct.merchantProduct.merchantSeriesId, null)
    if(mainSeriesMerchantProductsMap.contains(businessProduct.id)){
      businessProduct.merchantProductSearch.isMainSeries = NumConstants.ONE
    }else{
      businessProduct.merchantProductSearch.isMainSeries = NumConstants.ZERO

    }
    if(attributeValues != null) {

      //TODO
      val buffer = ListBuffer.empty[Long]
      for (attributeValue <- attributeValues) {
        val attributeValueId = attributeValue.valueId
        if(businessProduct.merchantProductAttributeIds.contains(attributeValueId)){
          buffer.append(attributeValueId)
        }
      }
      businessProduct.merchantProductSearch.seriesAttrValueIdSearch = buffer.mkString(" ")
    }

    businessProduct
  }
}
