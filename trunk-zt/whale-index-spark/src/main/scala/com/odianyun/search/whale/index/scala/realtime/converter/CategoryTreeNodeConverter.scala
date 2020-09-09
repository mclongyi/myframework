package com.odianyun.search.whale.index.scala.realtime.converter

import com.odianyun.search.whale.index.scala.common.{IndexInfo, ProcessContext, UpdatedIds}
import com.odianyun.search.whale.index.scala.load.DataFactory
import com.odianyun.search.whale.index.scala.realtime.IDConvertManager
import com.odianyun.search.whale.index.scala.util.RowFieldUtil

import scala.collection.mutable.ListBuffer

/**
  * Created by cuikai on 16/7/6.
  */
object CategoryTreeNodeConverter extends IDConverter {

  def convert(indexInfo: IndexInfo, context: ProcessContext, catNodeIds: List[Long]): UpdatedIds = {
    val productCatNodeDF = DataFactory.getDataFrameByTable(indexInfo, context.dBConfigService,
      "product", catNodeIds, "category_tree_node_id")

    val productIds = ListBuffer[Long]()

    for(merchantProduct <- productCatNodeDF.collect()) {
      productIds += RowFieldUtil.getAsLong(merchantProduct, "id")
    }

    if (productIds.size > 0)
      ProductIdConverter.convert(indexInfo, context, productIds.toList.distinct)
    else
      IDConvertManager.emptyUpdatedIds
  }
}
