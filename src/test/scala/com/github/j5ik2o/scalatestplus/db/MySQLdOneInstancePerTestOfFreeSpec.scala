package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.FixtureAnyFreeSpec
import org.scalatest.matchers.must.Matchers

class MySQLdOneInstancePerTestOfFreeSpec extends FixtureAnyFreeSpec with Matchers with MySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "MySQLdOneInstancePerTestOfFreeSpec" - {
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
