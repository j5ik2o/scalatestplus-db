package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class WixMySQLOnInstancePerTestOfFreeSpec extends fixture.FreeSpec with MustMatchers with WixMySQLOneInstancePerTest {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "WixMySQLOnInstancePerTestOfFreeSpec" - {
    "should start & stop mysqld1" in { context =>
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      mysqld = context.embeddedMysql
    }
    "should start & stop mysqld2" in { context =>
      println(s"bbbb = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      context.embeddedMysql mustNot be(mysqld)
    }
  }
}
