package com.odianyun.search.whale.index.scala.load.mproduct

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OMerchantProductPic
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by cuikai on 16/5/10.
  */
class ProductPicUrlLoader extends Loader {
  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val picUrlDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchantProductUrls", getUpdateIds(processContext))

    //id,url,sort_value
    picUrlDF.map(p => {
      (RowFieldUtil.getAsLong(p, "id"), OMerchantProductPic(RowFieldUtil.getAsString(p, "url"),
        RowFieldUtil.getAsInt(p, "sort_value")))
    })
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }
}