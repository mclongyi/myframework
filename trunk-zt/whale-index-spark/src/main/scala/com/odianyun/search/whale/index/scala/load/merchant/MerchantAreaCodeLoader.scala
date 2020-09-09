package com.odianyun.search.whale.index.scala.load.merchant

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OArea, OMerchantBelongArea}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

import scala.collection.Map

/**
  * Created by cuikai on 16/5/13.
  */
class MerchantAreaCodeLoader extends Loader{

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val merchantBelongAreaDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_belong_area", getUpdateIds(processContext))

    //merchant_id,nation_id,province_id,city_id,region_id,area_code
    merchantBelongAreaDF.map(p => (RowFieldUtil.getAsLong(p, "merchant_id"),
      OMerchantBelongArea(RowFieldUtil.getAsLong(p, "merchant_id"),
        RowFieldUtil.getAsLong(p, "nation_id"), RowFieldUtil.getAsLong(p, "province_id"),
        RowFieldUtil.getAsLong(p, "city_id"), RowFieldUtil.getAsLong(p, "region_id"),
        RowFieldUtil.getAsLong(p, "area_code"))))
  }

  override def loadAsBroadcast(processContext: ProcessContext): Map[String, Map[Long, Product]] = {

    val areaDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService, "area")

    //id,code,name,level,parentCode,postCode,company_id
    val areaMap = areaDF.map(p => (RowFieldUtil.getAsLong(p, "code"),
      OArea(RowFieldUtil.getAsLong(p, "id"),
        RowFieldUtil.getAsInt(p, "code"), RowFieldUtil.getAsString(p, "name"),
        RowFieldUtil.getAsInt(p, "level"), RowFieldUtil.getAsInt(p, "parentCode"),
        RowFieldUtil.getAsString(p, "postCode"), RowFieldUtil.getAsInt(p, "company_id")))).collectAsMap()

    Map(BroadcastVals.area -> areaMap)

  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantIds
  }
}
