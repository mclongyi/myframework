package com.odianyun.search.whale.index.scala.realtime.converter

import com.odianyun.search.whale.index.scala.common.{IndexInfo, ProcessContext, UpdatedIds}

/**
  * Created by cuikai on 16/7/5.
  */
trait IDConverter extends Serializable {

  def convert(indexInfo: IndexInfo, context: ProcessContext, ids: List[Long]): UpdatedIds
}
