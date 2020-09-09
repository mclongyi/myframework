package com.odianyun.search.whale.index.scala.util

import com.odianyun.search.whale.analysis.user.IndexPolicy
import scala.collection.JavaConverters._

/**
  * Created by fishcus on 16/5/16.
  */
object SegmentUtil {

  System.setProperty("global.config.path", "./index/data/env")
  var index = new IndexPolicy()
  var segment = index.get()

  def segment(text : String) : List[String] = {
    segment.segment(text).asScala.toList
  }

  def main(args: Array[String]) {
    println(segment("scp300s阿里巴巴"))
  }
}
