package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.{NumConstants, ProcessContext}
import com.odianyun.search.whale.index.scala.util.{BeanUtil, EsSparkUtil}

/**
  * Created by cuikai on 16/5/17.
  */
class Send2ESProcessor extends Processor {
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

    EsSparkUtil.sendToEs(resultMap, processContext.indexInfo.indexName + "_" + processContext.indexInfo.indexVersion,
      processContext.indexInfo.indexType, conf)
  }
}
