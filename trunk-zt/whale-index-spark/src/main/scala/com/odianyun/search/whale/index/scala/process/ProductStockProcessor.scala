package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.ProcessContext
import com.odianyun.search.whale.index.scala.model.OBusinessProduct

/**
  * Created by fishcus on 16/8/3.
  */
class ProductStockProcessor extends Processor{

  def calcProductStock(processContext: ProcessContext, product: OBusinessProduct) = {
    var stock = 0
    for(tempStock <- product.stocks){
      stock += tempStock.stock
    }
    product.merchantProductSearch.stock =  if (stock >0) 1 else 0

    product.merchantProductSearch.compositeSort =  product.merchantProductSearch.stock | product.merchantProductSearch.hasPic

    product
  }

  override def process(processContext: ProcessContext): Unit = {
    processContext.data = processContext.data.map(p => (p._1, calcProductStock(processContext, p._2)))
  }


}
