package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class FlywayWithMySQLdOneInstancePerFreeSpec
    extends AnyFreeSpec
    with Matchers
    with FlywayWithMySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(Seq("db"))

  var mysqld: EmbeddedMysql = _

  "FlywayWithMySQLdOnInstancePerFreeSpec" - {
    "should start & stop mysqld1" in {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = mySQLdContext.embeddedMysql
    }
    "should start & stop mysqld2" in {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mySQLdContext.embeddedMysql mustBe mysqld
    }
  }

}
