package com.odianyun.search.whale.index.scala.load.mproduct

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.OMerchantProdMerchantCateTreeNode
import com.odianyun.search.whale.index.scala.util.RowFieldUtil
import org.apache.spark.rdd.RDD


/**
  * Created by zengfenghua on 16/5/23.
  */
class MerchantProdMerchantCateTreeNodeLoader extends Loader{

  override def loadAsRDD(processContext: ProcessContext): RDD[(Long, Product)] = {
    val dataDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_prod_merchant_cate_tree_node", getUpdateIds(processContext))


    //merchant_product_id, merchant_cate_tree_node_id
    dataDF.map(p => (RowFieldUtil.getAsLong(p, "merchant_product_id"),
      OMerchantProdMerchantCateTreeNode(RowFieldUtil.getAsLong(p, "merchant_cate_tree_node_id"))))
  }

  override def getUpdateIds(processContext: ProcessContext) : List[Long] = {
    if(processContext.updateIds == null) null else processContext.updateIds.merchantProductIds
  }
}
