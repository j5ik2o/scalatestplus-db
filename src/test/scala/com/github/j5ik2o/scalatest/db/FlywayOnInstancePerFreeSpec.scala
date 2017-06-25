package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.flywaydb.core.internal.util.jdbc.DriverDataSource
import org.scalatest.{ FreeSpec, MustMatchers }

class FlywayOnInstancePerFreeSpec extends FreeSpec with MustMatchers with FlywayOneInstancePerSuite {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfigs: Seq[FlywayConfig] = wixMySQLContext.jdbUrls.map { jdbcUrl =>
    FlywayConfig(
      new DriverDataSource(
        getClass.getClassLoader,
        "org.mysql.jdbc.Driver",
        jdbcUrl,
        wixMySQLContext.userName,
        wixMySQLContext.password
      ),
      Seq("db")
    )
  }

  var mysqld: EmbeddedMysql = _

  "WixMySQLOnInstancePerFreeSpec" - {
    "should start & stop mysqld1" in {
      println(s"context = $wixMySQLContext")
      wixMySQLContext mustNot be(null)
      wixMySQLContext.schemaConfigs.head.name mustBe "test"
      mysqld = wixMySQLContext.embeddedMysql
    }
    "should start & stop mysqld2" in {
      println(s"context = $wixMySQLContext")
      wixMySQLContext mustNot be(null)
      wixMySQLContext.schemaConfigs.head.name mustBe "test"
      wixMySQLContext.embeddedMysql mustBe mysqld
    }
  }

}
