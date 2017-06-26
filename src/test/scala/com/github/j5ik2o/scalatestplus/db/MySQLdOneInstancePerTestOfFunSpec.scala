package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class MySQLdOneInstancePerTestOfFunSpec extends fixture.FunSpec with MustMatchers with MySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  describe("MySQLdOnInstancePerTestOfFunSpec") {
    it("should start & stop mysqld1") { context =>
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      mysqld = context.embeddedMysql
    }
    it("should start & stop mysqld2") { context =>
      println(s"bbbb = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      context.embeddedMysql mustNot be(mysqld)
    }
  }
}
