package com.odianyun.search.whale.index.scala.process
import com.odianyun.search.whale.index.scala.common.{NumConstants, ProcessContext}
import com.odianyun.search.whale.index.scala.model.OBusinessProduct
import org.apache.commons.lang3.StringUtils

/**
  * Created by cuikai on 16/5/30.
  */
class ProductPicUrlProcessor extends Processor {
  override def process(processContext: ProcessContext) = {
    processContext.data = processContext.data.map(p => (p._1, calcProductPic(processContext, p._2)))
  }

  def calcProductPic(processContext: ProcessContext, product: OBusinessProduct): OBusinessProduct = {
    var minSortValue = NumConstants.INT_MAX_VALUE
    var picUrl: String = null
    for (tempPic <- product.pics) {
      if(picUrl == null) {
        minSortValue = tempPic.sortValue
        picUrl = tempPic.url
      } else {
        if(tempPic.url != null && (tempPic.sortValue > 0 && tempPic.sortValue < minSortValue)) {
          minSortValue = tempPic.sortValue
          picUrl = tempPic.url
        }
      }
    }

    product.merchantProductSearch.picUrl = picUrl

    if(StringUtils.isBlank(picUrl)){
      product.merchantProductSearch.hasPic = 0
    }

    product
  }
}
