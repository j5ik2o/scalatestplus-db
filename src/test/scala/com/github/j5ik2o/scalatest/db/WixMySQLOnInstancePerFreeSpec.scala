package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ FreeSpec, MustMatchers }

class WixMySQLOnInstancePerFreeSpec extends FreeSpec with MustMatchers with WixMySQLOneInstancePerSuite {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "WixMySQLOnInstancePerFreeSpec" - {
    "should start & stop mysqld1" in {
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      mysqld = context.embeddedMysql
    }
    "should start & stop mysqld2" in {
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      context.embeddedMysql mustBe mysqld
    }
  }

}
