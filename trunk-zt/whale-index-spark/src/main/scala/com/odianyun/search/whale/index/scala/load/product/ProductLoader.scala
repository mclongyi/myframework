package com.odianyun.search.whale.index.scala.load.product

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OProduct
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD

/**
  * Created by zengfenghua on 16/5/17.
  */
class ProductLoader extends Loader {

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val productDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "product", getUpdateIds(processContext))

    //id,brand_id,category_tree_node_id,ean_no,code,calculation_unit,standard
    productDF.map(p => (RowFieldUtil.getAsLong(p, "id"),
      OProduct(RowFieldUtil.getAsLong(p, "id"),
        RowFieldUtil.getAsLong(p, "brand_id"), RowFieldUtil.getAsLong(p, "category_tree_node_id"),
        RowFieldUtil.getAsString(p, "ean_no"), RowFieldUtil.getAsString(p, "code"),
        RowFieldUtil.getAsString(p, "calculation_unit"), RowFieldUtil.getAsString(p, "standard"))))
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.productIds
  }
}
