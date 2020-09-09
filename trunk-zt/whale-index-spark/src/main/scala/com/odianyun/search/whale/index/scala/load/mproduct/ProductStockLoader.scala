package com.odianyun.search.whale.index.scala.load.mproduct

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OMerchantProductStock
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by cuikai on 16/5/10.
  */
class ProductStockLoader extends Loader{
  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {

    val stockDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_product_warehouse_stock", getUpdateIds(processContext))

    //merchant_product_id,product_id,merchant_id,real_stock_num,real_frozen_stock_num
    stockDF.map(p => (RowFieldUtil.getAsLong(p, "merchant_product_id"),
      OMerchantProductStock(calcStock(RowFieldUtil.getAsLong(p, "real_stock_num"), RowFieldUtil.getAsLong(p, "real_frozen_stock_num")))))
//      .reduceByKey((stock1,stock2)=>OMerchantProductStock(if ((stock1.stock+stock2.stock)>0) 1 else 0))
  }

  def calcStock(realStock: Long, realFrozenStock: Long) =
    if(realStock - realFrozenStock > 0) 1 else 0

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }
}