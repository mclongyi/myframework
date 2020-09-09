package com.odianyun.search.whale.index.scala.common

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._


import scala.collection.Map

/**
  * Created by cuikai on 16/7/13.
  */
object AppArgParser {
  def parser(arg: String): IndexInfo={
    implicit val formats = DefaultFormats
    val appArgs = parse(arg, false).extract[Map[String, String]]
    val indexInfo = new IndexInfo()
    indexInfo.companyId = appArgs.getOrElse("companyId", "-1").toInt
    indexInfo.indexName = appArgs.getOrElse("indexName", null)
    indexInfo.indexType = appArgs.getOrElse("indexType", null)
    indexInfo.esClusterName = appArgs.getOrElse("esClusterName", null)
    indexInfo.esNodes = appArgs.getOrElse("esNodes", null)
    indexInfo.setIndexVersion(appArgs.getOrElse("indexVersion", null))
    indexInfo.setOmqNameSpace(appArgs.getOrElse("omqNameSpace", null))

    indexInfo
  }
}
