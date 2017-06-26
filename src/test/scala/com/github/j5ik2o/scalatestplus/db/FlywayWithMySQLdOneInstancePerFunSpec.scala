package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ FunSpec, MustMatchers }

class FlywayWithMySQLdOneInstancePerFunSpec
    extends FunSpec
    with MustMatchers
    with FlywayWithMySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(Seq("db"))

  var mysqld: EmbeddedMysql = _

  describe("FlywayWithMySQLdOnInstancePerFunSpec") {
    it("should start & stop mysqld1") {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = mySQLdContext.embeddedMysql
      flywayContexts mustNot be(null)
      flywayContexts.forall(_.flyway != null) mustBe true
    }
    it("should start & stop mysqld2") {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mySQLdContext.embeddedMysql mustBe mysqld
      flywayContexts mustNot be(null)
      flywayContexts.forall(_.flyway != null) mustBe true
    }
  }

}
