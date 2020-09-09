package com.odianyun.search.whale.index.scala.util

import org.apache.spark.rdd.RDD
import org.elasticsearch.spark.rdd.EsSpark

/**
  * Created by fishcus on 16/5/12.
  */
object EsSparkUtil {

  def sendToEs(rdd: RDD[_], indexName: String, indexType: String, options: Map[String, String]): Unit = {
    EsSpark.saveToEs(rdd, indexName + "/" + indexType, options)
  }
}
