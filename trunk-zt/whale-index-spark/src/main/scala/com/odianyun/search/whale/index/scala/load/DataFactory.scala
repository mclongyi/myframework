package com.odianyun.search.whale.index.scala.load

import java.util.Properties

import com.odianyun.search.whale.index.scala.common.{IndexInfo, SparkContextFactory}
import com.odianyun.search.whale.index.scala.load.dbconfig.{ConnectionConfig, DBConfigService, DumpSqlConfig}
import com.odianyun.search.whale.index.scala.model.{ConnectionInfo, DumpSql}
import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.DataFrame
/**
  * Created by juzhongzheng on 2016/5/9.
  */
object DataFactory {

  def getDataFrameByTable(indexInfo: IndexInfo,  dBConfigService: DBConfigService, name: String) : DataFrame = {
    getDataFrameByTable(indexInfo, dBConfigService, name, null)
  }

  def getDataFrameByTable(indexInfo: IndexInfo,  dBConfigService: DBConfigService,
                          name: String, updateIds: List[Long]) : DataFrame = {
    getDataFrameByTable(indexInfo, dBConfigService, name, updateIds, null)
  }

  def getDataFrameByTable(indexInfo: IndexInfo, dBConfigService: DBConfigService, name: String,
                          updateIds: List[Long], updateIdKey: String) : DataFrame = {
    val dumpSql = dBConfigService.getDumpSql(indexInfo.companyId, name)
    if(dumpSql == null) {
      throw new NoSuchElementException
    }

    val jdbcInfo = dBConfigService.getJDBCInfo(indexInfo.companyId, dumpSql.dbSource)
    getData(dumpSql, jdbcInfo, updateIds, updateIdKey)
  }

  private def getData(dumpSql: DumpSql, jdbcInfo: ConnectionInfo,
                      updateIds: List[Long], updateIdKey: String) : DataFrame = {
    var table_sql = dumpSql.sqlContext
    val pids = table_sql.split(",").apply(0).split(" ")
    val id = pids.apply(pids.length - 1)

    var idFilterField = updateIdKey
    if(StringUtils.isBlank(idFilterField)) {
      idFilterField = dumpSql.idFilterField
    }
    if(updateIds != null && !updateIds.isEmpty && StringUtils.isNotBlank(idFilterField)) {
      table_sql = table_sql + " and " + idFilterField + " in (" + updateIds.mkString(",") + ")"
    }

    val jdbcProperties = new Properties()
    jdbcProperties.setProperty("user", jdbcInfo.username)
    jdbcProperties.setProperty("password", jdbcInfo.password)
    jdbcProperties.setProperty("driver", "com.mysql.jdbc.Driver")

    val dataFrame = SparkContextFactory.sqlContext.read.jdbc(jdbcInfo.jdbcUrl,
      "(" + table_sql + ") as " + dumpSql.sqlName,
      id, 0L, Long.MaxValue, 5, jdbcProperties).repartition(10)

    dataFrame
  }
}
