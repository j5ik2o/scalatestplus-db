package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.FixtureAnyFreeSpec
import org.scalatest.matchers.must.Matchers

class FlywayWithMySQLdOneInstancePerTestOfFreeSpec
    extends FixtureAnyFreeSpec
    with Matchers
    with FlywayWithMySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(Seq("db"))

  var mysqld: EmbeddedMysql = _

  "FlywayWithMySQLdOnInstancePerTestOfFreeSpec" - {
    "should start & stop mysqld1" in { context =>
      println(s"context = $context")
      context.mySQLdContext mustNot be(null)
      context.mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = context.mySQLdContext.embeddedMysql
    }
    "should start & stop mysqld2" in { context =>
      println(s"context = $context")
      context.mySQLdContext mustNot be(null)
      context.mySQLdContext.schemaConfigs.head.name mustBe "test"
      context.mySQLdContext.embeddedMysql mustNot be(mysqld)
    }
  }

}
