package com.github.j5ik2o.scalatest.db

import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ fixture, MustMatchers }

class FlywayOnInstancePerTestOfFreeSpec extends fixture.FreeSpec with MustMatchers with FlywayOneInstancePerTest {

  override val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override def driverClassName(jdbcUrl: String): String = "org.mysql.jdbc.Driver"

  override def locations(jdbcUrl: String): Seq[String] = Seq("db")

  var mysqld: EmbeddedMysql = _

  "WixMySQLOnInstancePerFreeSpec" - {
    "should start & stop mysqld1" in { context =>
      println(s"context = $context")
      context._1 mustNot be(null)
      context._1.schemaConfigs.head.name mustBe "test"
      mysqld = context._1.embeddedMysql
    }
    "should start & stop mysqld2" in { context =>
      println(s"context = $context")
      context._1 mustNot be(null)
      context._1.schemaConfigs.head.name mustBe "test"
      context._1.embeddedMysql mustNot be(mysqld)
    }
  }

}
