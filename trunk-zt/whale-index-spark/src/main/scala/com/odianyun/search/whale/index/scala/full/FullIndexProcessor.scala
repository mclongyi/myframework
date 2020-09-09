package com.odianyun.search.whale.index.scala.full

import com.odianyun.search.whale.index.scala.common.{AppArgParser, IndexInfo, ProcessContext, SparkContextFactory}
import com.odianyun.search.whale.index.scala.load._
import com.odianyun.search.whale.index.scala.load.dbconfig.DBConfigService
import com.odianyun.search.whale.index.scala.process._

import scala.collection.Map

/**
  * Created by cuikai on 16/5/10.
  */
object FullIndexProcessor {

  def main(args: Array[String]) {

    if (args.length < 1) {
      System.err.println("Usage: FullIndexProcessor <application argument>")
      System.exit(1)
    }

    val indexInfo = AppArgParser.parser(args(0))
    /*val indexInfo = new IndexInfo()
    indexInfo.setCompanyId(10)
    indexInfo.setIndexName("b2c_ds")
    indexInfo.setIndexType("mp")
    indexInfo.setEsClusterName("ds-test-es-cluster")
    indexInfo.setIndexVersion("1.1")
*/
    if (indexInfo.companyId == -1 || indexInfo.indexName == null
      || indexInfo.indexType == null) {
      System.err.println("Application arguments is wrong, companyId=" + indexInfo.companyId + ", indexName=" + indexInfo.indexName
        + ",indexVersion=" + indexInfo.indexVersion + ",indexType=" + indexInfo.indexType)
      System.exit(1)
    }

    val context = new ProcessContext(indexInfo,
      SparkContextFactory.sparkContext.emptyRDD,
      SparkContextFactory.sparkContext.broadcast(Map.empty[String, Map[Long, Any]]),
      new DBConfigService)
    context.dBConfigService.reloadAllDBCache()

    new DataLoad().load(context)

    val processors = List(
      new MerchantProductSearchProcessor(),
      new MerchantSaleOffsetProcessor(),
      new MerchantAreaCodeProcessor(),
      new ProductBrandProcessor(),
      new ProductPicUrlProcessor(),
      new ProductStockProcessor(),
      new ProductCategoryProcessor(),
      new ProductAttributeProcessor(),
      new MerchantProductSeriesProcessor(),
      new SegmentProcessor(),
      new Send2ESProcessor())

    processors.foreach(
      processor => processor.process(context)
    )
  }

}
