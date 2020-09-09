package com.odianyun.search.whale.index.scala.util

import org.apache.spark.sql.Row

/**
  * Created by cuikai on 16/5/27.
  */
object RowFieldUtil {

  def getAsString(row: Row, field: String): String = {
    if(row.getAs(field) == null)
      null
    else
      row.getAs(field).toString
  }

  def getAsInt(row: Row, field: String): Int = {
    if(row.getAs(field) == null)
      -99
    else
      row.getAs(field).toString.toInt
  }

  def getAsLong(row: Row, field: String): Long = {
    if(row.getAs(field) == null)
      -99L
    else
      row.getAs(field).toString.toLong
  }

  def getAsDouble(row: Row, field: String): Double = {
    if(row.getAs(field) == null)
      0.0
    else
      row.getAs(field).toString.toDouble
  }
}
