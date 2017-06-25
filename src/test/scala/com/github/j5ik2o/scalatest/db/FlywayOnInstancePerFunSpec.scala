package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.flywaydb.core.internal.util.jdbc.DriverDataSource
import org.scalatest.{ FunSpec, MustMatchers }

class FlywayOnInstancePerFunSpec extends FunSpec with MustMatchers with FlywayOneInstancePerSuite {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

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

  describe("WixMySQLOnInstancePerFreeSpec") {
    it("should start & stop mysqld1") {
      println(s"context = $wixMySQLContext")
      wixMySQLContext mustNot be(null)
      wixMySQLContext.schemaConfigs.head.name mustBe "test"
      mysqld = wixMySQLContext.embeddedMysql
    }
    it("should start & stop mysqld2") {
      println(s"context = $wixMySQLContext")
      wixMySQLContext mustNot be(null)
      wixMySQLContext.schemaConfigs.head.name mustBe "test"
      wixMySQLContext.embeddedMysql mustBe mysqld
    }
  }

}
