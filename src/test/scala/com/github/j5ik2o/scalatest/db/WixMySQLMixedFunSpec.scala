package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class WixMySQLMixedFunSpec extends fixture.FunSpec with MustMatchers with WixMySQLMixedFixtures {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  describe("WixMySQLMixedFunSpec") {
    it("should start & stop mysql1") {
      new WithMySQLdContext() {
        println(s"context = $context")
        context mustNot be(null)
        context.schemaConfigs.head.name mustBe "test"
        mysqld = context.embeddedMysql
      }
    }
    it("should start & stop mysql1") {
      new WithMySQLdContext() {
        println(s"context = $context")
        context mustNot be(null)
        context.schemaConfigs.head.name mustBe "test"
        context.embeddedMysql mustNot be(mysqld)
      }
    }
  }
}
