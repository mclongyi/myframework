package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, ProcessContext}
import com.odianyun.search.whale.index.scala.model.{OAttributeValue, OBusinessProduct}
import org.apache.commons.lang.StringUtils
import org.apache.spark.broadcast.Broadcast

import scala.collection.Map

/**
  * Created by zengfenghua on 16/5/19.
  */
class ProductAttributeProcessor extends Processor {

  override def process(processContext: ProcessContext): Unit ={
    val attributeValueCache = processContext.broadcastMap.value.getOrElse(BroadcastVals.attributeValue,
      Map.empty[Long, OAttributeValue]).asInstanceOf[Map[Long, OAttributeValue]]
    processContext.data = processContext.data.map(a=>(a._1,calcAttribute(a._2, attributeValueCache)))
  }

  private def calcAttribute(businessProduct: OBusinessProduct,attributeValueCache:Map[Long, OAttributeValue]): OBusinessProduct ={
     val merchantProductSearch=businessProduct.merchantProductSearch
     val attrValueIdSearch=new StringBuffer()
     val attrValueSearch=new StringBuffer()
     for(productAttribute<-businessProduct.productAttributes){
          val attributeValue=attributeValueCache.getOrElse(productAttribute.att_value_id, null)
          if(attributeValue!=null && StringUtils.isNotBlank(attributeValue.value)){
             attrValueIdSearch.append(attributeValue.valueId).append(" ")
             attrValueSearch.append(attributeValue.value).append(" ")
          }
     }
     merchantProductSearch.attrValueId_search=attrValueIdSearch.toString
     merchantProductSearch.attrValue_search=attrValueSearch.toString
     businessProduct

  }

}
