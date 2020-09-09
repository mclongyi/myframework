package com.odianyun.search.whale.index.scala.load

import com.odianyun.search.whale.index.scala.common.{ProcessContext, SparkContextFactory}
import com.odianyun.search.whale.index.scala.load.merchant._
import com.odianyun.search.whale.index.scala.load.mproduct._
import com.odianyun.search.whale.index.scala.load.product._
import com.odianyun.search.whale.index.scala.model._
import org.apache.spark.rdd.RDD

import scala.collection.Map

/**
  * Created by cuikai on 16/5/17.
  */
class DataLoad extends Serializable{

  val merchantProductId = "id"
  val merchantId = "merchantId"
  val productId =  "productId"

  //merchatProduct related loaders
  val merchantProductLoaders = List(
    new MerchantProductLoader(),
    new ProductPicUrlLoader(),
    new ProductPriceLoader(),
    new ProductStockLoader(),
    new MerchantVolume4SaleLoader(),
    new MerchantProductAttributeLoader(),
    new MerchantProdMerchantCateTreeNodeLoader(),
    new MerchantSaleOffsetLoader(),
    new MerchantRateLoader()
  )

  //merchant related loaders
  val merchantLoaders = List(
    new ProductMerchantLoader(),
    new MerchantAreaCodeLoader(),
    new MerchantProductSeriesLoader())

  //product related loaders
  val productLoaders = List(
    new ProductLoader,
    new ProductBrandLoader(),
    new ProductCategoryLoader(),
    new ProductAttributeLoader())

  val joinMap = Map(merchantId -> merchantLoaders, productId -> productLoaders)

  def load(processContext: ProcessContext): RDD[(Long, OBusinessProduct)] = {
    prepareBroadCastMap(processContext)

    val mProductUnionRDD = loadAsSingleRDD(merchantProductLoaders, processContext)
    //filter操作为了过滤一些OMerchantProduct信息不完整的脏数据
    var joinedRDDs: RDD[(Long, List[Product])] = mProductUnionRDD
      .filter(p => getFieldValue(p._2, merchantProductId) != Long.MinValue)

    for ((key, loaders) <- joinMap) {
      val unionRDD = loadAsSingleRDD(loaders, processContext)
      val convertKeyRDD = joinedRDDs.map(p => (getFieldValue(p._2, key), p._2))
      joinedRDDs = convertKeyRDD.leftOuterJoin(unionRDD).map(p => (p._1, p._2._1 ++ p._2._2.getOrElse(List())))
    }

    val mpKeyResultRDD = joinedRDDs.map(p => (getFieldValue(p._2, merchantProductId), convert2BusinessProduct(p._2)))

    processContext.data = mpKeyResultRDD
    mpKeyResultRDD
  }


  def convert2BusinessProduct(objs: List[Product]): OBusinessProduct = {
    val businessProduct = new OBusinessProduct(getFieldValue(objs, merchantProductId),
      getFieldValue(objs, productId), getFieldValue(objs, merchantId))
    objs.foreach(obj => obj match {
      case obj: OMerchantProduct => businessProduct.merchantProduct = obj
      case obj: OMerchantProductPic => businessProduct.pics = businessProduct.pics :+ obj
      case obj: OMerchantProductPrice => businessProduct.price = obj
      case obj: OMerchantProductStock => businessProduct.stocks = businessProduct.stocks :+ obj
      case obj: OMerchantProdMerchantCateTreeNode => businessProduct.merchantProdMerchantCateTreeNodes=
             businessProduct.merchantProdMerchantCateTreeNodes:+obj
      case obj: OMerchant => businessProduct.merchant = obj
      case obj: OMerchantVolume4Sale => businessProduct.volume4Sale = obj
      case obj: OMerchantBelongArea => businessProduct.merchantBelongArea = obj
      case obj: OProductAttribute =>  businessProduct.productAttributes=businessProduct.productAttributes:+obj
      case obj: OMerchantProductAttribute => businessProduct.merchantProductAttributeIds += obj.att_value_id
      case obj: OProduct => businessProduct.product=obj
      case obj: OMerchantRate => businessProduct.rate = obj
      case obj: OMerchantSaleOffset => businessProduct.saleOffset = obj
      case _ =>
    })

    businessProduct
  }

  def prepareBroadCastMap(processContext: ProcessContext) = {
    var broadcastMap = Map.empty[String, Map[Long, Any]]
    val loaders = merchantProductLoaders ++ merchantLoaders ++ productLoaders
    loaders.foreach(p => broadcastMap = broadcastMap ++ p.loadAsBroadcast(processContext))
    processContext.broadcastMap = SparkContextFactory.sparkContext.broadcast(broadcastMap)
  }

  def loadAsSingleRDD(loaders: List[Loader], context: ProcessContext): RDD[(Long, List[Product])] = {
    var unionRDD: RDD[(Long, Product)] = null
    loaders.foreach(p => {
      if (unionRDD == null) unionRDD = p.loadAsRDD(context)
      else unionRDD = unionRDD.union(p.loadAsRDD(context))
    })

    unionRDD.groupByKey().map(p => (p._1, p._2.toList))
  }

  def getFieldValue(objs: List[Product], field: String): Long = {
    var merchantProduct: OMerchantProduct = null
    objs.foreach(obj => if (obj.isInstanceOf[OMerchantProduct]) merchantProduct = obj.asInstanceOf[OMerchantProduct])

    if (merchantProduct != null)
      merchantProduct.getClass.getMethods.find(_.getName == field).get.invoke(merchantProduct).asInstanceOf[Long]
    else
      Long.MinValue
  }

  def containBasicInfo(objs: List[Product]): Boolean = {
    var containBasicInfo = false
    objs.foreach(obj => if (obj.isInstanceOf[OMerchantProduct]) containBasicInfo = true)
    containBasicInfo
  }
}