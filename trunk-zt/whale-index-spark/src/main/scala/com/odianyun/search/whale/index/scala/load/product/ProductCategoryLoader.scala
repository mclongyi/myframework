package com.odianyun.search.whale.index.scala.load.product

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.load.{DataFactory, Loader}
import com.odianyun.search.whale.index.scala.model.{OCategoryRelation, OCategoryTreeNode, OMerchantCategoryNode}
import com.odianyun.search.whale.index.scala.util.RowFieldUtil

import scala.collection.{Map, mutable}

/**
  * Created by cuikai on 16/5/12.
  */
class ProductCategoryLoader extends Loader {


  override def loadAsBroadcast(processContext: ProcessContext): Map[String, Map[Long, Any]] = {
    Map(BroadcastVals.categoryTreeNode -> calcCategoryNode(processContext),
      BroadcastVals.categoryRelation -> calcCategoryRelation(processContext),
      BroadcastVals.merchantCategoryNode -> calcMerchantCategoryNode(processContext))
  }

  def calcCategoryNode(processContext: ProcessContext): Map[Long, Any] = {
    val categoryNodeDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "category_tree_node")

    //id,parent_id, category_id, list_sort, name
    categoryNodeDF.map(p => (RowFieldUtil.getAsLong(p, "id"),
      OCategoryTreeNode(RowFieldUtil.getAsLong(p, "id"),
        RowFieldUtil.getAsLong(p, "parent_id"), RowFieldUtil.getAsLong(p, "category_id"),
        RowFieldUtil.getAsInt(p, "list_sort"), RowFieldUtil.getAsString(p, "name")))).collectAsMap()
  }

  def calcCategoryRelation(processContext: ProcessContext): Map[Long, Any] = {
    val categoryRelationDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "category_tree_node_relation")

    //id,left_tree_node_id,right_tree_node_id
    val map = categoryRelationDF.map(p => (RowFieldUtil.getAsLong(p, "id"),
      OCategoryRelation(RowFieldUtil.getAsLong(p, "id"),
        RowFieldUtil.getAsLong(p, "left_tree_node_id"),
        RowFieldUtil.getAsLong(p, "right_tree_node_id")))).collectAsMap()

    val resultMap = new mutable.HashMap[Long, mutable.LinkedList[OCategoryRelation]]
    for (m <- map) {
      var list = resultMap.getOrElse(m._2.rightTreeNodeId, mutable.LinkedList.empty[OCategoryRelation])
      list = list :+ m._2
      resultMap.put(m._2.rightTreeNodeId, list)
    }
    resultMap
  }

  def calcMerchantCategoryNode(processContext: ProcessContext): Map[Long, Any] = {
    val categoryRelationDF = DataFactory.getDataFrameByTable(processContext.indexInfo, processContext.dBConfigService,
      "merchant_cate_tree_node")

    //id,parent_id,merchant_cat_tree_id,category_id,name
    categoryRelationDF.map(p => (RowFieldUtil.getAsLong(p, "id"),
      OMerchantCategoryNode(RowFieldUtil.getAsLong(p, "id"),
        RowFieldUtil.getAsLong(p, "parent_id"), RowFieldUtil.getAsLong(p, "merchant_cat_tree_id"),
        RowFieldUtil.getAsInt(p, "category_id"), RowFieldUtil.getAsString(p, "name")))).collectAsMap()
  }
}