package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.FixtureAnyFreeSpec
import org.scalatest.matchers.must.Matchers

class MySQLdMixedFreeSpec extends FixtureAnyFreeSpec with Matchers with MySQLdMixedFixtures {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "MySQLdMixedFreeSpec" - {
    "should start & stop mysql1" in new WithMySQLdContext() {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = mySQLdContext.embeddedMysql
    }
    "should start & stop mysql2" in new WithMySQLdContext() {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mySQLdContext.embeddedMysql mustNot be(mysqld)
    }
    "any test without MySQLd" in { _ =>
      true mustBe true
    }
  }
}
