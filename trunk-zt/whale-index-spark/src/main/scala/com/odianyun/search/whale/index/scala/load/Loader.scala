package com.odianyun.search.whale.index.scala.load

import com.odianyun.search.whale.index.scala.common.{ProcessContext, SparkContextFactory}
import org.apache.spark.rdd.RDD

import scala.collection.Map

/**
  * Created by cuikai on 16/5/10.
  */
trait Loader extends Serializable{

  //处理后生成一个(Long, Product)结构的RDD
  def loadAsRDD(processContext: ProcessContext) : RDD[(Long, Product)] = SparkContextFactory.sparkContext.emptyRDD

  //处理后生成一个Map结果当做广播变量
  def loadAsBroadcast(processContext: ProcessContext) : Map[String, Map[Long, Any]] = Map.empty[String, Map[Long, Any]]

  //获取实时索引更新对应的ID列表
  def getUpdateIds(processContext: ProcessContext) : List[Long] = List.empty[Long]
}
