package com.odianyun.search.whale.index.scala.util

import scala.collection.mutable.Map

/**
  * Created by cuikai on 16/5/6.
  */
object BeanUtil {

  def Bean2Map(cc: AnyRef): Map[String, Any] =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {
      (a, f) => f.setAccessible(true)
        f.get(cc) match {
          case p: Product if p.productArity > 0 => a ++ Bean2Map(p)
          case x => a + (f.getName -> f.get(cc))
        }
    }

}
