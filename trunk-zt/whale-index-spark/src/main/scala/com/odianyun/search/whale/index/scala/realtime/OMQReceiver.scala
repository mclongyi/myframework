package com.odianyun.search.whale.index.scala.realtime

import com.odianyun.mq.common.consumer.ConsumerType
import com.odianyun.mq.common.message.{Destination, Message}
import com.odianyun.mq.consumer.{ConsumerConfig, MessageListener}
import com.odianyun.mq.consumer.impl.ConsumerFactoryImpl
import com.odianyun.search.whale.index.api.common.{IndexConstants, UpdateMessage, UpdateType}
import org.apache.commons.collections.CollectionUtils
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver
import org.elasticsearch.common.Strings

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map


/**
  * Created by cuikai on 16/6/24.
  */
class OMQReceiver()
  extends Receiver[UpdateMessage](StorageLevel.MEMORY_AND_DISK_2) {

  def onStart() {
    receive()
  }

  private def receive() {
    System.setProperty("global.config.path", Strings.cleanPath("./index/data/env"))
    println(Strings.cleanPath("./index/data/env"))

    val consumerFactory = ConsumerFactoryImpl.getInstance()
    val config = new ConsumerConfig()
    config.setConsumerType(ConsumerType.CLIENT_ACKNOWLEDGE)
    config.setThreadPoolSize(10)

    val consumer = consumerFactory.createLocalConsumer(Destination.topic(IndexConstants.OMQ_NAMESPACE,
      IndexConstants.CACHE_TOPIC), "consumer", config)
    consumer.setListener(new MessageListener() {
      override def onMessage(message: Message) {
        val updateMessage = message.transferContentToBean(classOf[UpdateMessage])
        if(CollectionUtils.isNotEmpty(updateMessage.getIds)) {
          store(updateMessage)
//          println("received: " + updateMessage.getIds)
        }
      }
    })
    consumer.start()
  }


  override def onStop() {
  }
}

object OMQReceiver {
  val log = Logger.getLogger(OMQReceiver.getClass.getName)
  def main(args: Array[String]) {
    System.setProperty("global.config.path", "/Users/cuikai/JavaDev/data/trunk_env/")
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("CustomReceiver")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    val omqStream = ssc.receiverStream(new OMQReceiver())

    omqStream.foreachRDD(rdd => {
      println("rdd size:" + rdd.count())
      val updateIdsMap = Map[UpdateType, ListBuffer[Long]]()
      for (item <- rdd.collect()) {
        if(!updateIdsMap.contains(item.getUpdateType))
          updateIdsMap.put(item.getUpdateType, ListBuffer[Long]())

        val updatedIds = updateIdsMap.getOrElse(item.getUpdateType, ListBuffer[Long]())
        for(id <- item.getIds) {
          if(!updatedIds.contains(id))
            updatedIds += id.asInstanceOf[Long]
        }
      }

      log.error(updateIdsMap.getOrElse(UpdateType.merchant_product_id, ListBuffer[Long]()).toList)
      println(updateIdsMap.getOrElse(UpdateType.merchant_product_id, ListBuffer[Long]()).toList)
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
