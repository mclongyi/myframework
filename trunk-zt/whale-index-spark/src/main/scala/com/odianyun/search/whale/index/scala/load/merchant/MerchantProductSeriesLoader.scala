package com.odianyun.search.whale.index.scala.load.merchant

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OAttributeValue, OCategoryRelation}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil

import scala.collection.{Map, mutable}

/**
  * Created by cuikai on 16/5/18.
  */
class MerchantProductSeriesLoader extends Loader {

  override def loadAsBroadcast(processContext: ProcessContext): Map[String, Map[Long, Any]] = {
    Map(BroadcastVals.categoryTreeNode -> calcMerchantSeries(processContext),
      BroadcastVals.mainSeriesMerchantProducts -> calcMainSeriesMerchantProducts(processContext))
  }

  def calcMerchantSeries(processContext: ProcessContext): Map[Long, Any] = {
    val merchantProductSeriesDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "seriesAttrValueList")


    //merchantSeriesId,valueId, value,nameId,name_name,type,parent_id,sort_value
    val merchantSeriesArray = merchantProductSeriesDF.map(p => (RowFieldUtil.getAsLong(p, "merchantSeriesId"),
      OAttributeValue(RowFieldUtil.getAsLong(p, "merchantSeriesId"),
        RowFieldUtil.getAsLong(p, "valueId"), RowFieldUtil.getAsString(p, "value"),
        RowFieldUtil.getAsLong(p, "nameId"), RowFieldUtil.getAsString(p, "name_name"),
        RowFieldUtil.getAsInt(p, "type"), RowFieldUtil.getAsLong(p, "parent_id"),
        RowFieldUtil.getAsInt(p, "sort_value")))).collect()

    val merchantSeriesMap = new mutable.HashMap[Long, mutable.LinkedList[OAttributeValue]]
    for(merchantSeries <- merchantSeriesArray) {
      var attributeValues = merchantSeriesMap.getOrElse(merchantSeries._1, mutable.LinkedList.empty[OAttributeValue])
      attributeValues = attributeValues :+ merchantSeries._2
      merchantSeriesMap.put(merchantSeries._1, attributeValues)
    }
    merchantSeriesMap
//    Map(BroadcastVals.merchantSeries -> merchantSeriesMap)
  }
  def calcMainSeriesMerchantProducts(processContext: ProcessContext): Map[Long, Any] = {
    val mainSeriesMerchantProductsDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_series")


    //merchantSeriesId,valueId, value,nameId,name_name,type,parent_id,sort_value
    val mainSeriesMerchantProductsArray = mainSeriesMerchantProductsDF.map(p => (RowFieldUtil.getAsLong(p, "main_merchant_product_id"),
      RowFieldUtil.getAsLong(p, "id"))).collect()

    val mainSeriesMerchantProductsMap = new mutable.HashMap[Long, Long]
    for(mainSeriesMerchantProduct <- mainSeriesMerchantProductsArray) {
      mainSeriesMerchantProductsMap.put(mainSeriesMerchantProduct._1,mainSeriesMerchantProduct._2)
    }
    mainSeriesMerchantProductsMap
  }


  }
