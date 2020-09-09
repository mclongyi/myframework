package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.ProcessContext

/**
  * Created by cuikai on 16/5/17.
  */
trait Processor extends Serializable{
  def process(processContext: ProcessContext)
}
