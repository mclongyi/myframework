package com.odianyun.search.whale.index.scala.load.dbconfig

import java.sql.{Connection, DriverManager}

import com.odianyun.search.whale.index.scala.model.ConnectionInfo

import scala.collection.mutable



class ConnectionConfig extends DBConfig{
  var connectionInfoMap = mutable.Map.empty[(Int, String), ConnectionInfo]

  override def reloadCache(): Unit = {
    var connection: Connection = null
    try {
      connection = DriverManager.getConnection(jdbcUrl, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(
        "SELECT company_id, db_type, username, password, jdbc_url FROM saas_datasource_config")
      while (resultSet.next()) {
        val companyId = resultSet.getInt("company_id")
        val dbType = resultSet.getString("db_type")
        val username = resultSet.getString("username")
        val password = resultSet.getString("password")
        val jdbcUrl = resultSet.getString("jdbc_url")

        connectionInfoMap = connectionInfoMap + ((companyId, dbType)
          -> new ConnectionInfo(companyId, dbType, username, password, jdbcUrl))
      }
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      if (connection != null)
        connection.close()
    }
  }

  def getJDBCInfo(companyId: Int, dbType: String): ConnectionInfo = {
    connectionInfoMap.getOrElse((companyId, dbType), null)
  }
}