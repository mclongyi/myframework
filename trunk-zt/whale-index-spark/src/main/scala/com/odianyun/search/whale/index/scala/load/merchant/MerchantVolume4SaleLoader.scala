package com.odianyun.search.whale.index.scala.load.merchant

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OMerchantVolume4Sale
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by cuikai on 16/5/11.
  */
class MerchantVolume4SaleLoader extends Loader{

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {

    val stockDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_prod_sales_record", getUpdateIds(processContext))

    //merchant_prod_id,cumulative_sales_volume
    stockDF.map(p => (RowFieldUtil.getAsLong(p, "merchant_prod_id"),
      OMerchantVolume4Sale(RowFieldUtil.getAsLong(p, "cumulative_sales_volume"))))
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }

}