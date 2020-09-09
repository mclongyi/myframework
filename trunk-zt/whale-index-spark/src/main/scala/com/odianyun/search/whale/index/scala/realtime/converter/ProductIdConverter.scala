package com.odianyun.search.whale.index.scala.realtime.converter

import com.odianyun.search.whale.index.scala.common.{IndexInfo, ProcessContext, UpdatedIds}
import com.odianyun.search.whale.index.scala.load.DataFactory
import com.odianyun.search.whale.index.scala.realtime.IDConvertManager
import com.odianyun.search.whale.index.scala.util.RowFieldUtil

import scala.collection.mutable.ListBuffer

/**
  * Created by cuikai on 16/7/6.
  */
object ProductIdConverter extends IDConverter {

  override def convert(indexInfo: IndexInfo, context: ProcessContext, ids: List[Long]): UpdatedIds = {
    if(ids.size > 0) {
      val productDF = DataFactory.getDataFrameByTable(indexInfo, context.dBConfigService, "merchant_product",
        ids, "product_id")

      val merchantProductIds = ListBuffer[Long]()
      val productIds = ListBuffer[Long]()
      val merchantIds = ListBuffer[Long]()

      for (merchantProduct <- productDF.collect()) {
        merchantProductIds += RowFieldUtil.getAsLong(merchantProduct, "id")
        productIds += RowFieldUtil.getAsLong(merchantProduct, "product_id")
        merchantIds += RowFieldUtil.getAsLong(merchantProduct, "merchant_id")
      }

      new UpdatedIds(merchantProductIds.toList.distinct, productIds.toList.distinct,
        merchantIds.toList.distinct, List[String]())
    } else{
      IDConvertManager.emptyUpdatedIds
    }
  }
}
