package com.github.j5ik2o.scalatestplus.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ FunSpec, MustMatchers }

class MySQLdOnInstancePerFunSpec extends FunSpec with MustMatchers with MySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  describe("MySQLdOnInstancePerFunSpec") {
    it("should start & stop mysqld1") {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = mySQLdContext.embeddedMysql
    }
    it("should start & stop mysqld2") {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mySQLdContext.embeddedMysql mustBe mysqld
    }
  }

}
