package com.odianyun.search.whale.index.scala.load.dbconfig

import com.typesafe.config.ConfigFactory

/**
  * Created by cuikai on 16/7/29.
  */
abstract class DBConfig extends Serializable{
  val driver = "com.mysql.jdbc.Driver"
  val conf = ConfigFactory.load("./index/data/env/search/common/dataSource_search_saas.properties")
  Class.forName(driver)
/*  val jdbcUrl = conf.getString("search.saas.jdbc.url")
  val username = conf.getString("search.saas.jdbc.username")
  val password = conf.getString("search.saas.jdbc.password")*/

  val jdbcUrl = "jdbc:mysql://192.168.20.243:3306/search?rewriteBatchedStatements=true&characterEncoding=utf8"
  val username = "root"
  val password = "ody,123"

  def reloadCache()
}
