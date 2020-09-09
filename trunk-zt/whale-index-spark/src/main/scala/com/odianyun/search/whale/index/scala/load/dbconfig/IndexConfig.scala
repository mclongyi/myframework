package com.odianyun.search.whale.index.scala.load.dbconfig

import java.sql.{Connection, DriverManager}

import com.odianyun.search.whale.index.scala.model.CommonIndexInfo

import scala.collection.mutable

/**
  * Created by cuikai on 16/7/29.
  */
class IndexConfig extends DBConfig {
  var indexConfigMap = mutable.Map.empty[Int, CommonIndexInfo]

  override def reloadCache(): Unit = {
    var connection: Connection = null
    try {
      connection = DriverManager.getConnection(jdbcUrl, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(
        "select company_id,index_name,es_cluster_id from saas_common_config where is_deleted=0 and app_type=1")
      while (resultSet.next()) {
        indexConfigMap = indexConfigMap + (resultSet.getInt("company_id")
          -> new CommonIndexInfo(resultSet.getInt("company_id"), resultSet.getString("index_name"),
          resultSet.getInt("es_cluster_id")))
      }
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      if (connection != null)
        connection.close()
    }
  }

  def getIndexCommonConfig(clusterId: Int): CommonIndexInfo = {
    indexConfigMap.getOrElse(clusterId, null)
  }
}
