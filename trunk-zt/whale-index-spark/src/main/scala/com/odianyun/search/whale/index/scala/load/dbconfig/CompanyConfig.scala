package com.odianyun.search.whale.index.scala.load.dbconfig

import java.sql.{Connection, DriverManager, Statement}

import com.odianyun.search.whale.index.scala.common.NumConstants

import scala.collection.mutable


class CompanyConfig extends DBConfig {
  //companyId -> virtual companyId
  var virtualCompanyIdMap = mutable.Map.empty[Int, Int]

  override def reloadCache(): Unit = {
    var connection: Connection = null
    try {
      connection = DriverManager.getConnection(jdbcUrl, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(
        "SELECT company_id, virtual_company_id FROM company")
      while (resultSet.next()) {
        if(resultSet.getInt("virtual_company_id") != 0) {
          virtualCompanyIdMap = virtualCompanyIdMap + (resultSet.getInt("company_id")
            -> resultSet.getInt("virtual_company_id"))
        }
      }
    } catch {
      case e: Throwable => e.printStackTrace
    } finally {
      if (connection != null)
        connection.close()
    }
  }

  def getVirtualCompanyId(companyId: Int): Int = {
    if (virtualCompanyIdMap.contains(companyId))
      virtualCompanyIdMap.get(companyId).get
    else
      NumConstants.DEFAULT_COMPANY_ID
  }
}
