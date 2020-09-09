package com.odianyun.search.whale.index.scala.common

import com.odianyun.search.whale.index.scala.load.dbconfig._
import com.odianyun.search.whale.index.scala.model.OBusinessProduct
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

import scala.beans.BeanProperty
import scala.collection.Map

/**
  * Created by cuikai on 16/5/11.
  */
case class ProcessContext(val indexInfo: IndexInfo, var data:RDD[(Long, OBusinessProduct)],
                          var broadcastMap: Broadcast[Map[String, Map[Long, Any]]],
                          val dBConfigService: DBConfigService){
  @BeanProperty var updateIds: UpdatedIds = null
}

case class IndexInfo() {
  @BeanProperty var companyId: Int = -1
  @BeanProperty var indexName: String = null
  @BeanProperty var indexType: String = null
  @BeanProperty var esClusterName: String = null
  @BeanProperty var esNodes: String = null
  @BeanProperty var indexVersion: String = null
  @BeanProperty var omqNameSpace: String = null
}

case class UpdatedIds(val merchantProductIds: List[Long], val productIds: List[Long],
                      val merchantIds: List[Long], val deletedMerchantProductIds: List[String]) {
}