package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class FlywayWithMySQLdOnInstancePerTestOfFunSpec
    extends fixture.FunSpec
    with MustMatchers
    with FlywayWithMySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(Seq("db"))

  var mysqld: EmbeddedMysql = _

  describe("FlywayWithMySQLdOnInstancePerTestOfFunSpec") {
    it("should start & stop mysqld1") { context =>
      println(s"context = $context")
      context.mySQLdContext mustNot be(null)
      context.mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = context.mySQLdContext.embeddedMysql

    }
    it("should start & stop mysqld2") { context =>
      println(s"context = $context")
      context.mySQLdContext mustNot be(null)
      context.mySQLdContext.schemaConfigs.head.name mustBe "test"
      context.mySQLdContext.embeddedMysql mustNot be(mysqld)
    }
  }

}
