package com.odianyun.search.whale.index.scala.load.mproduct

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OMerchantProductAttribute, OMerchantProduct}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by fishcus on 16/8/3.
  */
class MerchantProductAttributeLoader extends Loader{
  //处理后生成一个(Long, Product)结构的RDD
  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val mpAttributeDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_product_attributes", getUpdateIds(processContext))

    mpAttributeDF.map(p => {
      (RowFieldUtil.getAsLong(p, "merchant_product_id"),
        OMerchantProductAttribute(RowFieldUtil.getAsLong(p, "merchant_product_id"),
        RowFieldUtil.getAsLong(p, "att_value_id") ))})
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }
}
