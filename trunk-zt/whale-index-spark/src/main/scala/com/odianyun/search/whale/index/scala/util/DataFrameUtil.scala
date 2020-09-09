package com.odianyun.search.whale.index.scala.util

import org.apache.spark.sql.{DataFrame, SQLContext}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._


/**
  * Created by cuikai on 16/5/10.
  */
object DataFrameUtil {

  /**
    * replace "null" in the row of DataFrame with 0(numeric field) or ""(String field)
    *
    * @param dataFrame
    * @return
    */
  def fillNull(dataFrame: DataFrame): DataFrame = {
    val filledNumeric = dataFrame.na.fill(-99, dataFrame.schema.fieldNames)
    val filledString = filledNumeric.na.fill("", dataFrame.schema.fieldNames)
    filledString
  }

  def camelToUnderscores(name: String) =
    "[A-Z]".r.replaceAllIn(name, "_" + _.group(0).toLowerCase())

  def getCaseMethods[T: TypeTag]: List[String] = typeOf[T].members.sorted.collect {
    case m: MethodSymbol if m.isCaseAccessor => m
  }.toList.map(_.toString)

  def getMethods[T: TypeTag] = typeOf[T].members.sorted.collect {
    case m: MethodSymbol if m.isCaseAccessor => m
  }.toList.map(_.name)

  def caseClassToSQLCols[T: TypeTag]: List[String] =
    getCaseMethods[T].map(_.split(" ")(1)).map(camelToUnderscores)

//  def schemaRDDToRDD[T: TypeTag : ClassTag](sqlContext: SQLContext, dataFrame: DataFrame,
//                                            fac: expressions.Row => T) = {
//    val tmpName = "tmpTableName" // Maybe should use a random string
//    dataFrame.registerTempTable(tmpName)
//    sqlContext.sql("SELECT " + caseClassToSQLCols[T].mkString(", ") + " FROM " + tmpName)
//      .map(fac)
//  }

}
