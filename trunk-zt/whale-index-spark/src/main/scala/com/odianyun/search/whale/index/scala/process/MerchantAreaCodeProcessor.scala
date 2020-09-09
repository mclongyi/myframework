package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.model.{OArea, OBusinessProduct}

import scala.collection.mutable.ListBuffer
import scala.collection.Map

/**
  * Created by cuikai on 16/5/19.
  */
class MerchantAreaCodeProcessor extends Processor {

  override def process(processContext: ProcessContext) = {
    val areaMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.area, Map.empty[Long, OArea])
      .asInstanceOf[Map[Long, OArea]]

    processContext.data = processContext.data.map(p => (p._1, calcMerchantAreaCode(processContext, areaMap, p._2)))
  }

  def calcMerchantAreaCode(context: ProcessContext, areaMap: Map[Long, OArea],
                           businessProduct: OBusinessProduct): OBusinessProduct = {
    val belongArea = businessProduct.merchantBelongArea
    if(belongArea != null)
      businessProduct.merchantProductSearch.areaCode = getFullPathArea(context,
        areaMap, belongArea.areaCode).mkString(" ")

    businessProduct
  }

  def getFullPathArea(context: ProcessContext, areaMap: Map[Long, OArea], areaCode: Long): ListBuffer[Long] = {
    val areaCodeList = ListBuffer(areaCode)

    var area = areaMap.getOrElse(areaCode, null)
    while(area != null && area.parentCode != 0) {
      areaCodeList.append(area.parentCode)
      area = areaMap.getOrElse(area.parentCode, null)
    }

    areaCodeList
  }
}
