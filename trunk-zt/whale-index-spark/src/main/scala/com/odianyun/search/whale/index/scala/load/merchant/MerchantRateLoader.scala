package com.odianyun.search.whale.index.scala.load.merchant

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OMerchantRate, OMerchantVolume4Sale}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by juzhongzheng on 2016-08-24.
  */
class MerchantRateLoader extends Loader{
  //处理后生成一个(Long, Product)结构的RDD
  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val rateDF = DataFactory.getDataFrameByTable(processContext.indexInfo,processContext.dBConfigService,
    "queryRate",getUpdateIds(processContext))
   //mp_id,rate rate,positive_rate positive_rate,rating_user_count rating_count
    rateDF.map(p => (RowFieldUtil.getAsLong(p, "mp_id"),
      OMerchantRate(RowFieldUtil.getAsDouble(p, "rate"),RowFieldUtil.getAsInt(p, "positive_rate"),RowFieldUtil.getAsInt(p, "rating_count"))))
  }

  //获取实时索引更新对应的ID列表
  override def getUpdateIds(processContext: ProcessContext): List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }
}
