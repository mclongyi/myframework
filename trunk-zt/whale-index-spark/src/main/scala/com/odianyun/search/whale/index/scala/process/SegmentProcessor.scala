package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.model.OBusinessProduct
import com.odianyun.search.whale.index.scala.util.SegmentUtil
import org.apache.commons.lang3.StringUtils


/**
  * Created by fishcus on 16/5/17.
  */
class SegmentProcessor extends Processor{

  val supplement = "(\\w+)\\((\\w+)\\)(\\w+)".r

  override def process(processContext: ProcessContext): Unit = {
    processContext.data=processContext.data.map(bp => (bp._1,calcSegment(bp._2)))
  }

  private def calcSegment(businessProduct: OBusinessProduct): OBusinessProduct = {

    var tag_words = StringBuilder.newBuilder
    val chineseName = businessProduct.merchantProduct.chinese_name
    val englishName = businessProduct.merchantProduct.english_name
    val subtitle = businessProduct.merchantProduct.subtitle

    if(StringUtils.isNotBlank(chineseName)){
      tag_words ++= " "+chineseName
    }
    if(StringUtils.isNotBlank(englishName)){
      tag_words ++= " "+englishName
    }
    if(StringUtils.isNotBlank(subtitle)){
      tag_words ++= " "+subtitle
    }
    val words = tag_words.toString()
    if(StringUtils.isNotBlank(words)){
      tag_words = StringBuilder.newBuilder
      tag_words ++= doSegment(words)
      tag_words ++= doCompensation(words)
    }

    val code = businessProduct.merchantProduct.code

    if(StringUtils.isNotBlank(code)){
      tag_words ++= " " + code.toLowerCase()
    }

    if(businessProduct.product != null) {
      val productCode = businessProduct.product.code
      val ean = businessProduct.product.ean_no
      if (StringUtils.isNotBlank(productCode)) {
        tag_words ++= " " + productCode.toLowerCase()
      }
      if (StringUtils.isNotBlank(ean)) {
        tag_words ++= " " + ean.toLowerCase()
      }
    }

    if(businessProduct.merchant != null) {
      val shopName = businessProduct.merchant.shopName
      if (StringUtils.isNotBlank(shopName)) {
        businessProduct.getMerchantProductSearch.merchantName_search = doSegment(shopName)
      }
    }

    val attributeValues = businessProduct.merchantProductSearch.attrValue_search

    val categoryNames = businessProduct.merchantProductSearch.categoryName_search

    val brandNames = businessProduct.merchantProductSearch.brandName_search

    businessProduct.merchantProductSearch.tag_words = tag_words.toString()

    if(StringUtils.isNotBlank(attributeValues)){
      businessProduct.merchantProductSearch.attrValue_search = doSegment(attributeValues)
    }

    if(StringUtils.isNotBlank(categoryNames)){
      businessProduct.merchantProductSearch.categoryName_search = doSegment(categoryNames)
    }

    if(StringUtils.isNotBlank(brandNames)){
      businessProduct.merchantProductSearch.brandName_search = doSegment(brandNames)
    }

    businessProduct
  }

   private def doSegment(text : String) : String = {
     val segList = SegmentUtil.segment(text)
     var sb = StringBuilder.newBuilder
     segList.foreach(sb ++= _ + " ")
     sb.toString()
  }

  private def doCompensation(text : String) : String = {
    val matchList = supplement.findAllIn(text.toLowerCase).toList
    var sb = StringBuilder.newBuilder
    matchList.foreach {
      case supplement(pre, line, post) => sb ++= (" " + pre + line + post + " " + pre + post)
      case _ =>
    }
    sb.toString()
  }

}

//object SegmentProcessor{
//  def main(args: Array[String]): Unit = {
//    val regex="(\\w+)\\((\\w+)\\)(\\w+)".r
//    val line = "你好Cpc(s)32s你好cs(d)Sss哈哈哈"
//
//    println(regex.findAllIn(line).toList)
//    println(new SegmentProcessor().doCompensation(line))
//
//  }
//}
