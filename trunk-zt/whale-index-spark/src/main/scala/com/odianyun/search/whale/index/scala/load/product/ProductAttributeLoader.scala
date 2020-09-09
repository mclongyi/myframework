package com.odianyun.search.whale.index.scala.load.product

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OAttributeValue, OProductAttribute}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

import scala.collection.Map

/**
  * Created by zengfenghua on 16/5/17.
  */
class ProductAttributeLoader extends Loader {

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val productAttributeDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "product_att_name_value", getUpdateIds(processContext))

    //product_id,att_name_id,att_value_id
    productAttributeDF.map(p => (RowFieldUtil.getAsLong(p, "product_id"),
      OProductAttribute(RowFieldUtil.getAsLong(p, "product_id"),
        RowFieldUtil.getAsLong(p, "att_name_id"), RowFieldUtil.getAsLong(p, "att_value_id"))))
  }

  override def loadAsBroadcast(processContext: ProcessContext): Map[String, Map[Long, Any]] = {
    val productAttributeDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "attribute_name_value")

    //merchantSeriesId, valueId,value,nameId,name_name,type,parent_id,sort_value
    val attrValueMap = productAttributeDF.map(p => (RowFieldUtil.getAsLong(p, "valueId"),
      OAttributeValue(p.getAs("merchantSeriesId").toString.toInt,
        RowFieldUtil.getAsLong(p, "valueId"), RowFieldUtil.getAsString(p, "value"),
        RowFieldUtil.getAsLong(p, "nameId"), RowFieldUtil.getAsString(p, "name_name"),
        RowFieldUtil.getAsInt(p, "type"), RowFieldUtil.getAsLong(p, "parent_id"),
        RowFieldUtil.getAsInt(p, "sort_value")))).collectAsMap()


    val dataMap = Map(BroadcastVals.attributeValue -> attrValueMap)

    val serialAttributeDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "seriesAttrValueList")

    val serialAttributeMap = serialAttributeDF.map(p => (RowFieldUtil.getAsLong(p, "merchantSeriesId"),
      OAttributeValue(RowFieldUtil.getAsLong(p, "merchantSeriesId"),
        RowFieldUtil.getAsLong(p, "valueId"), RowFieldUtil.getAsString(p, "value"),
        RowFieldUtil.getAsLong(p, "nameId"), RowFieldUtil.getAsString(p, "name_name"),
        RowFieldUtil.getAsInt(p, "type"), RowFieldUtil.getAsLong(p, "parent_id"),
        RowFieldUtil.getAsInt(p, "sort_value")))).collectAsMap()

    dataMap ++ Map(BroadcastVals.serialAttribute -> serialAttributeMap)
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.productIds
  }
}
