package com.odianyun.search.whale.index.scala.load.merchant

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OMerchant
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by cuikai on 16/5/16.
  */
class ProductMerchantLoader extends Loader{

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {

    val merchantDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_shop", getUpdateIds(processContext))

    //merchant_id, merchant_type, shop_name
    merchantDF.map(p => (RowFieldUtil.getAsLong(p, "merchant_id"),
      OMerchant(RowFieldUtil.getAsLong(p, "merchant_id"),
        if(RowFieldUtil.getAsString(p, "merchant_type") == null) 0 else RowFieldUtil.getAsInt(p, "merchant_type"),
        true,RowFieldUtil.getAsString(p, "shop_name"))))
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantIds
  }
}
