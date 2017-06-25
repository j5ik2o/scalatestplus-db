package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ FunSpec, MustMatchers }

class WixMySQLOnInstancePerFunSpec extends FunSpec with MustMatchers with WixMySQLOneInstancePerSuite {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  describe("WixMySQLOnInstancePerFunSpec") {
    it("should start & stop mysqld1") {
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      mysqld = context.embeddedMysql
    }
    it("should start & stop mysqld2") {
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      context.embeddedMysql mustBe mysqld
    }
  }

}
