package com.odianyun.search.whale.index.scala.realtime

import com.odianyun.search.whale.index.api.common.UpdateType
import com.odianyun.search.whale.index.scala.common._
import com.odianyun.search.whale.index.scala.load._
import com.odianyun.search.whale.index.scala.load.dbconfig.DBConfigService
import com.odianyun.search.whale.index.scala.process._
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Map}

/**
  * Created by cuikai on 16/5/10.
  */
object RealTimeProcessor {

  def main(args: Array[String]) {

    if (args.length < 1) {
      System.err.println("Usage: RealTimeProcessor <application argument>")
      System.exit(1)
    }

    val indexInfo = AppArgParser.parser(args(0))

    //val indexInfo = new IndexInfo(1, 1, "b2c_yh", "20160701", "mp", "ody-test-es-cluster","192.168.20.58")

    val ssc = new StreamingContext(SparkContextFactory.sparkContext, Seconds(60))

    val omqStream = ssc.receiverStream(new OMQReceiver())

    omqStream.foreachRDD(rdd => {
      val companyUpdateIdsMap = Map[Int, Map[UpdateType, ListBuffer[Long]]]()
      for (item <- rdd.collect()) {
        if(!companyUpdateIdsMap.contains(item.getCompanyId))
          companyUpdateIdsMap.put(item.getCompanyId, Map[UpdateType, ListBuffer[Long]]())
        val updateIdsMap = companyUpdateIdsMap.get(item.getCompanyId).get

        if (!updateIdsMap.contains(item.getUpdateType))
          updateIdsMap.put(item.getUpdateType, ListBuffer[Long]())

        val updatedIds = updateIdsMap.getOrElse(item.getUpdateType, ListBuffer[Long]())
        for (id <- item.getIds) {
          if (!updatedIds.contains(id))
            updatedIds += id.asInstanceOf[Long]
        }
      }

      if (companyUpdateIdsMap.size > 0) {
        //initial realtime context
        val context = new ProcessContext(indexInfo,
          SparkContextFactory.sparkContext.emptyRDD,
          SparkContextFactory.sparkContext.broadcast(Map.empty[String, mutable.Map[Long, Any]]),
          new DBConfigService)
        context.dBConfigService.reloadAllDBCache()

        for(updateIdsMap <- companyUpdateIdsMap) {
          indexInfo.companyId = updateIdsMap._1
          //id转换逻辑
          val updatedIds = IDConvertManager.convert(indexInfo, context, updateIdsMap._2)

          try {
            process(indexInfo, context, updatedIds)
          } catch {
            case e: Throwable => println(e.getMessage)
          }
        }
      }
    })


    ssc.start()
    ssc.awaitTermination()
  }

  def process(indexInfo: IndexInfo, context: ProcessContext, updatedIds: UpdatedIds): Unit = {
    context.updateIds = updatedIds

    //load data from database by spark sql
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
      new RealTimeSend2ESProcessor())

    processors.foreach(
      processor => processor.process(context)
    )

    println(context.updateIds)
  }

}
