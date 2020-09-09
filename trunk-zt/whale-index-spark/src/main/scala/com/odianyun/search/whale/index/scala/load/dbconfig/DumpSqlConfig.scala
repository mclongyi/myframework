package com.odianyun.search.whale.index.scala.load.dbconfig

import java.sql.{Connection, DriverManager, Statement}

import com.odianyun.search.whale.index.scala.model.DumpSql
import com.typesafe.config.ConfigFactory

import scala.collection.mutable



class DumpSqlConfig extends DBConfig{

  var dumpSqlMap = mutable.Map.empty[(Int, String), DumpSql]

  override def reloadCache(): Unit = {
    var connection: Connection = null
    try {
      connection = DriverManager.getConnection(jdbcUrl, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(
        "SELECT company_id, sql_name, db_source, sql_content, id_filter_field FROM saas_dump_sql")
      while (resultSet.next()) {
        dumpSqlMap = dumpSqlMap + ((resultSet.getInt("company_id"), resultSet.getString("sql_name"))
          -> new DumpSql(resultSet.getString("sql_name"), resultSet.getString("db_source"),
          resultSet.getString("sql_content"), resultSet.getString("id_filter_field")))
      }
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      if (connection != null)
        connection.close()
    }
  }

  def getDumpSql(companyId: Int, sqlName: String): DumpSql = {
    dumpSqlMap.getOrElse((companyId, sqlName), null)
  }
}
