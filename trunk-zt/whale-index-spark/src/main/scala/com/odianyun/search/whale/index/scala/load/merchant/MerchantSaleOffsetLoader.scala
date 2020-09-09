package com.odianyun.search.whale.index.scala.load.merchant

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OMerchantSaleOffset}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

import scala.collection

/**
  * Created by juzhongzheng on 2016-08-24.
  */
class MerchantSaleOffsetLoader extends Loader{
  //获取实时索引更新对应的ID列表
  override def getUpdateIds(processContext: ProcessContext): List[Long] = {
    if(processContext.updateIds == null)null else processContext.updateIds.merchantProductIds
  }

  //处理后生成一个(Long, Product)结构的RDD
  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val offsetDF = DataFactory.getDataFrameByTable(processContext.indexInfo,processContext.dBConfigService,
    "merchant_prod_sales_offset",getUpdateIds(processContext))

    //merchant_product_id,offset
    offsetDF.map(p => (RowFieldUtil.getAsLong(p,"merchant_product_id"),
      OMerchantSaleOffset(RowFieldUtil.getAsLong(p,"offset"))))
  }

}
