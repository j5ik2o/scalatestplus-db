package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class MySQLdMixedFunSpec extends fixture.FunSpec with MustMatchers with MySQLdMixedFixtures {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  describe("MySQLdMixedFunSpec") {
    it("should start & stop mysql1") {
      new WithMySQLdContext() {
        println(s"context = $mySQLdContext")
        mySQLdContext mustNot be(null)
        mySQLdContext.schemaConfigs.head.name mustBe "test"
        mysqld = mySQLdContext.embeddedMysql
      }
    }
    it("should start & stop mysql2") {
      new WithMySQLdContext() {
        println(s"context = $mySQLdContext")
        mySQLdContext mustNot be(null)
        mySQLdContext.schemaConfigs.head.name mustBe "test"
        mySQLdContext.embeddedMysql mustNot be(mysqld)
      }
    }
  }
}
