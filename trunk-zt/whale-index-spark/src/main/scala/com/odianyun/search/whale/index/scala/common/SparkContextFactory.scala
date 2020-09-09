package com.odianyun.search.whale.index.scala.common

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by juzhongzheng on 2016/5/10.
  */
object SparkContextFactory {
  val conf = new SparkConf()
//  conf.setAppName("index")
  conf.setMaster("local[4]").setAppName("test")
  val sparkContext = new SparkContext(conf)
  val sqlContext = new SQLContext(sparkContext)
}
