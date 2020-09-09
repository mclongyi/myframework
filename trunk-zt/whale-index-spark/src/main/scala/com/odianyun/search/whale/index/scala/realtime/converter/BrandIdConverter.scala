package com.odianyun.search.whale.index.scala.realtime.converter

import com.odianyun.search.whale.index.scala.common.{IndexInfo, ProcessContext, UpdatedIds}
import com.odianyun.search.whale.index.scala.load.DataFactory
import com.odianyun.search.whale.index.scala.realtime.IDConvertManager
import com.odianyun.search.whale.index.scala.util.RowFieldUtil

import scala.collection.mutable.ListBuffer

/**
  * Created by cuikai on 16/7/6.
  */
object BrandIdConverter extends IDConverter {

  override def convert(indexInfo: IndexInfo, context: ProcessContext, brandIds: List[Long]): UpdatedIds = {
    val productBrandDF = DataFactory.getDataFrameByTable(indexInfo, context.dBConfigService,
      "product", brandIds, "brand_id")

    val productIds = ListBuffer[Long]()

    for(merchantProduct <- productBrandDF.collect()) {
      productIds += RowFieldUtil.getAsLong(merchantProduct, "id")
    }

    if(productIds.size > 0)
      ProductIdConverter.convert(indexInfo, context, productIds.toList.distinct)
    else
      IDConvertManager.emptyUpdatedIds
  }
}
