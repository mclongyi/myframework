package com.odianyun.search.whale.index.scala.load.dbconfig

import java.sql.{Connection, DriverManager}

import com.odianyun.search.whale.index.scala.model.ESClusterInfo

import scala.collection.mutable

/**
  * Created by cuikai on 16/7/29.
  */
class ESClusterConfig extends DBConfig{
  var clusterInfoMap = mutable.Map.empty[Int, ESClusterInfo]

  override def reloadCache(): Unit = {
    var connection: Connection = null
    try {
      connection = DriverManager.getConnection(jdbcUrl, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(
        "select id,cluster_name,cluster_nodes,admin_url from saas_escluster_config")
      while (resultSet.next()) {
        clusterInfoMap = clusterInfoMap + (resultSet.getInt("id")
          -> new ESClusterInfo(resultSet.getString("cluster_name"), resultSet.getString("cluster_nodes"),
          resultSet.getString("admin_url")))
      }
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      if (connection != null)
        connection.close()
    }
  }

  def getClusterInfo(companyId: Int): ESClusterInfo = {
    clusterInfoMap.getOrElse(companyId, null)
  }
}
