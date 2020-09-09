package com.odianyun.search.whale.index.scala.load.product

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OBrand
import com.odianyun.search.whale.index.scala.util.RowFieldUtil

import scala.collection.Map

/**
  * Created by cuikai on 16/5/12.
  */
class ProductBrandLoader extends Loader {

  override def loadAsBroadcast(processContext: ProcessContext): Map[String, Map[Long, OBrand]] = {

    val brandDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService, "brand")

    //id,name,chinese_name,english_name
    val brandMap = brandDF.map(p => (RowFieldUtil.getAsLong(p, "id"),
      OBrand(RowFieldUtil.getAsString(p, "name"),
        RowFieldUtil.getAsString(p, "chinese_name"), RowFieldUtil.getAsString(p, "english_name")))).collectAsMap()

    Map(BroadcastVals.brand -> brandMap)
  }
}
