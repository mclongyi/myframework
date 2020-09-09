package com.odianyun.search.whale.index.scala.realtime.converter

import com.odianyun.search.whale.index.scala.common.{IndexInfo, ProcessContext, UpdatedIds}
import com.odianyun.search.whale.index.scala.realtime.IDConvertManager

/**
  * Created by cuikai on 16/7/12.
  */
object DefaultConverter {

  def convert(indexInfo: IndexInfo, context: ProcessContext, ids: List[Long]): UpdatedIds = {
    IDConvertManager.emptyUpdatedIds
  }
}
