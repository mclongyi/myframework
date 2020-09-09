package com.odianyun.search.whale.index.scala.load.mproduct

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OMerchantProduct
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by cuikai on 16/5/4.
  */
class MerchantProductLoader extends Loader{

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val merchantProductDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_product", getUpdateIds(processContext))

    //id,product_id,merchant_id,chinese_name,english_name,subtitle,
    // is_deleted,create_time,code,company_id,merchant_series_id,tax_no,TYPE, sale_type,isNew
    merchantProductDF.map(p => {
      (RowFieldUtil.getAsLong(p, "id"), OMerchantProduct(RowFieldUtil.getAsLong(p, "id"),
        RowFieldUtil.getAsLong(p, "product_id"), RowFieldUtil.getAsLong(p, "merchant_id"),
        RowFieldUtil.getAsString(p, "chinese_name"), RowFieldUtil.getAsString(p, "english_name"),
        RowFieldUtil.getAsString(p, "subtitle"), RowFieldUtil.getAsInt(p, "is_deleted"),
        RowFieldUtil.getAsString(p, "create_time"),  RowFieldUtil.getAsString(p, "code"),
        RowFieldUtil.getAsLong(p, "company_id"),  RowFieldUtil.getAsLong(p, "merchant_series_id"),
        RowFieldUtil.getAsString(p, "tax_no"),  RowFieldUtil.getAsInt(p, "TYPE"),
        RowFieldUtil.getAsInt(p, "sale_type"),  RowFieldUtil.getAsInt(p, "isNew"),
        RowFieldUtil.getAsInt(p, "management_state")))})
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }
}
