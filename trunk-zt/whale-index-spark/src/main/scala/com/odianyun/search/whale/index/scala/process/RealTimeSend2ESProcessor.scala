package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.es.api.{ESClient, ESService}
import com.odianyun.search.whale.es.request.ESDeleteRequest
import com.odianyun.search.whale.index.api.common.IndexConstants
import com.odianyun.search.whale.index.scala.common.{NumConstants, ProcessContext}
import com.odianyun.search.whale.index.scala.util.{BeanUtil, EsSparkUtil}

import scala.collection.JavaConversions._

/**
  * Created by cuikai on 16/5/17.
  */
class RealTimeSend2ESProcessor extends Processor {

  override def process(processContext: ProcessContext) = {
    val resultMap = processContext.data.map(p => {
      val map = BeanUtil.Bean2Map(p._2.merchantProductSearch)
      map += ("type" -> map.getOrElse("mpType", NumConstants.NEGATIVE_NINE_NINE))
      map -= "mpType"
      map
    })

    var conf = Map("pushdown" -> "true", "es.index.auto.create" -> "true",
      "es.mapping.id" -> "id", "es.write.operation" -> "index")

    val esClusterInfo = processContext.dBConfigService.getESCluserInfo(processContext.indexInfo.companyId)
    conf = conf + ("es.nodes" -> esClusterInfo.cluster_nodes)

    val commonConfig = processContext.dBConfigService.getCommonConfig(processContext.indexInfo.companyId)
    EsSparkUtil.sendToEs(resultMap, commonConfig.indexName + "_alias",
      IndexConstants.index_type, conf)

    //delete from es index
    deleteIds(processContext, processContext.indexInfo.indexName + "_alias")
  }

  def deleteIds(processContext: ProcessContext, indexAlias: String) = {
    if (processContext.updateIds != null
      && processContext.updateIds.deletedMerchantProductIds.size > 0) {
      try {
        val esDeleteRequest = new ESDeleteRequest(indexAlias,
          processContext.indexInfo.indexType, processContext.updateIds.deletedMerchantProductIds)

        val client = ESClient.getClient(processContext.indexInfo.esClusterName,
          processContext.indexInfo.esNodes)
        val bulkResponse = ESService.delete(client, esDeleteRequest)
        if (bulkResponse != null && bulkResponse.hasFailures()) {
          println("delete error : " + bulkResponse.buildFailureMessage())
        }
      } catch {
        case e: Throwable => println("delete index error," + e.getMessage)
      }
    }
  }
}
