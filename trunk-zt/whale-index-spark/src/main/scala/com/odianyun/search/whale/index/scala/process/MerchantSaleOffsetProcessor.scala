package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.model.OBusinessProduct

/**
  * Created by juzhongzheng on 2016-08-24.
  */
class MerchantSaleOffsetProcessor extends Processor{

  def calcMerchantSaleOffset(processContext: ProcessContext,product:OBusinessProduct) = {
    var sale = 0L;
    if(product.saleOffset != null && product.saleOffset.saleOffset != null){
      sale = product.saleOffset.saleOffset;
    }
    if(product.volume4Sale != null && product.volume4Sale.volume4Sale != null){
      sale = sale + product.volume4Sale.volume4Sale
    }
    product.merchantProductSearch.volume4sale = sale
    product
  }

  override def process(processContext: ProcessContext): Unit = {
    processContext.data = processContext.data.map(p => (p._1, calcMerchantSaleOffset(processContext, p._2)))
  }
}
