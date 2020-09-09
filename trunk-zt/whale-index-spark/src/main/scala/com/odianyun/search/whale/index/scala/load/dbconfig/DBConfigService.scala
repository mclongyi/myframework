package com.odianyun.search.whale.index.scala.load.dbconfig

import com.odianyun.search.whale.index.scala.model.{CommonIndexInfo, ConnectionInfo, DumpSql, ESClusterInfo}

/**
  * Created by cuikai on 16/7/29.
  */
class DBConfigService extends Serializable{

  val companyConfig = new CompanyConfig
  val connectionConfig = new ConnectionConfig
  val dumpSqlConfig = new DumpSqlConfig
  val eSClusterConfig = new ESClusterConfig
  val indexConfig = new IndexConfig

  def reloadAllDBCache(): Unit = {
    companyConfig.reloadCache()
    connectionConfig.reloadCache()
    dumpSqlConfig.reloadCache()
    eSClusterConfig.reloadCache()
    indexConfig.reloadCache()
  }

  def getJDBCInfo(companyId: Int, dbType: String): ConnectionInfo = {
    val connectionInfo = connectionConfig.getJDBCInfo(companyId, dbType)
    if (connectionInfo != null)
      connectionInfo
    else
      connectionConfig.getJDBCInfo(companyConfig.getVirtualCompanyId(companyId), dbType)
  }

  def getDumpSql(companyId: Int, sqlName: String): DumpSql = {
    val dumpSql = dumpSqlConfig.getDumpSql(companyId, sqlName)
    if(dumpSql != null)
      dumpSql
    else
      dumpSqlConfig.getDumpSql(companyConfig.getVirtualCompanyId(companyId), sqlName)
  }

  def getCommonConfig(companyId: Int): CommonIndexInfo = {
    indexConfig.getIndexCommonConfig(companyId)
  }

  def getESCluserInfo(companyId: Int): ESClusterInfo = {
    val commonConfig = indexConfig.getIndexCommonConfig(companyId)
    if(commonConfig != null)
      eSClusterConfig.getClusterInfo(commonConfig.esClusterId)
    else
      null
  }
}

object DBConfigService {
  def main(args: Array[String]) {
    val dBConfigService = new DBConfigService
    dBConfigService.reloadAllDBCache()
    println(dBConfigService.getDumpSql(1, "brand"))
    println(dBConfigService.getDumpSql(-1, "brand"))
    println(dBConfigService.getJDBCInfo(1, "merchant"))
    println(dBConfigService.getJDBCInfo(-1, "merchant"))
    println(dBConfigService.getESCluserInfo(1))
  }
}
