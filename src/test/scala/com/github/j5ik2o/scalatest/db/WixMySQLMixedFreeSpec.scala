package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class WixMySQLMixedFreeSpec extends fixture.FreeSpec with MustMatchers with WixMySQLMixedFixtures {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "WixMySQLMixedFreeSpec" - {
    "should start & stop mysql1" in new WithMySQLdContext() {
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      mysqld = context.embeddedMysql
    }
    "should start & stop mysql2" in new WithMySQLdContext() {
      println(s"context = $context")
      context mustNot be(null)
      context.schemaConfigs.head.name mustBe "test"
      context.embeddedMysql mustNot be(mysqld)
    }
  }
}
