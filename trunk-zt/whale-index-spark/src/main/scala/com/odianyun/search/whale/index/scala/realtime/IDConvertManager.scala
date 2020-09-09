package com.odianyun.search.whale.index.scala.realtime

import com.odianyun.search.whale.index.api.common.UpdateType
import com.odianyun.search.whale.index.scala.common.{IndexInfo, ProcessContext, UpdatedIds}
import com.odianyun.search.whale.index.scala.realtime.converter._

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

/**
  * Created by cuikai on 16/7/12.
  */
object IDConvertManager {

  val emptyUpdatedIds = new UpdatedIds(List[Long](), List[Long](), List[Long](), List[String]())

  val converts: Map[UpdateType, IDConverter] = Map(UpdateType.brand_id -> BrandIdConverter,
    UpdateType.category_tree_node_id -> CategoryTreeNodeConverter,
    UpdateType.merchant_product_id -> MerchantProductIdConverter,
    UpdateType.product_id -> ProductIdConverter)

  def convert(indexInfo: IndexInfo, context: ProcessContext, updatedIdMap: Map[UpdateType, ListBuffer[Long]]): UpdatedIds = {
    println("sourceIdMap:" + updatedIdMap)
    val merchantProductIds = ListBuffer[Long]()
    val productIds = ListBuffer[Long]()
    val merchantIds = ListBuffer[Long]()
    val deletedMerchantProductIds = ListBuffer[String]()

    for(entry <- updatedIdMap) {
      val converter = converts.getOrElse(entry._1, DefaultConverter).asInstanceOf[IDConverter]
      val tempIds = converter.convert(indexInfo, context, entry._2.toList)

      merchantIds ++= tempIds.merchantIds
      productIds ++= tempIds.productIds
      merchantProductIds ++= tempIds.merchantProductIds
      deletedMerchantProductIds ++= tempIds.deletedMerchantProductIds
    }

    println("merchantProductIds:" + merchantProductIds.toList)
    println("productIds:" + productIds.toList)
    println("merchantIds:" + merchantIds.toList)
    println("deletedMerchantProductIds:" + deletedMerchantProductIds.toList)
    new UpdatedIds(merchantProductIds.toList, productIds.toList, merchantIds.toList, deletedMerchantProductIds.toList)
  }

}
