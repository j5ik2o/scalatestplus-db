# scalatestplus-db

[![CircleCI](https://circleci.com/gh/j5ik2o/scalatestplus-db.svg?style=svg)](https://circleci.com/gh/j5ik2o/scalatestplus-db)

This library is a ScalaTest extension library that supports embedded MySQL and Flyway.

## Installation

Add the following to your sbt build (Scala 2.11.x, 2.12.x, 2.13.x):

### Release Version

```scala
resolvers ++= Seq(
  "Seasar Repository" at "https://maven.seasar.org/maven2/",
  Resolver.sonatypeRepo("releases")
)

libraryDependencies += "com.github.j5ik2o" %% "scalatestplus-db" % "<version>"
```

### Snapshot Version

```scala
resolvers ++= Seq(
  "Seasar Repository" at "https://maven.seasar.org/maven2/",
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies += "com.github.j5ik2o" %% "scalatestplus-db" % "<version>-SNAPSHOT"
```

## Usage

### When starting MySQLd for each test class

```scala
import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class MySQLdOneInstancePerFreeSpec extends AnyFreeSpec with Matchers with MySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig(name = "test"))

  var mysqld: EmbeddedMysql = _

  "MySQLdOneInstancePerFreeSpec" - {
    "should start & stop mysqld1" in {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = mySQLdContext.embeddedMysql
    }
    "should start & stop mysqld2" in {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mySQLdContext.embeddedMysql mustBe mysqld
    }
  }

}
```

### When starting MySQLd for each test case


```scala
import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.FixtureAnyFreeSpec
import org.scalatest.matchers.must.Matchers

class MySQLdOneInstancePerTestOfFreeSpec extends FixtureAnyFreeSpec with Matchers with MySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig(name = "test"))

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
```

### When starting MySQLd with Flyway for each test class

```scala
import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class FlywayWithMySQLdOneInstancePerFreeSpec
    extends AnyFreeSpec
    with Matchers
    with FlywayWithMySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig(name = "test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(locations = Seq("db"))

  var mysqld: EmbeddedMysql = _

  "FlywayWithMySQLdOneInstancePerFreeSpec" - {
    "should start & stop mysqld1" in {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = mySQLdContext.embeddedMysql
    }
    "should start & stop mysqld2" in {
      println(s"context = $mySQLdContext")
      mySQLdContext mustNot be(null)
      mySQLdContext.schemaConfigs.head.name mustBe "test"
      mySQLdContext.embeddedMysql mustBe mysqld
    }
  }

}
```

### When starting MySQLd with Flyway for each test case

```scala
import com.wix.mysql.EmbeddedMysql
import org.scalatest.freespec.FixtureAnyFreeSpec
import org.scalatest.matchers.must.Matchers

class FlywayWithMySQLdOneInstancePerTestOfFreeSpec
    extends FixtureAnyFreeSpec
    with Matchers
    with FlywayWithMySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig(name = "test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(locations = Seq("db"))

  var mysqld: EmbeddedMysql = _

  "FlywayWithMySQLdOneInstancePerTestOfFreeSpec" - {
    "should start & stop mysqld1" in { context =>
      println(s"context = $context")
      context.mySQLdContext mustNot be(null)
      context.mySQLdContext.schemaConfigs.head.name mustBe "test"
      mysqld = context.mySQLdContext.embeddedMysql
    }
    "should start & stop mysqld2" in { context =>
      println(s"context = $context")
      context.mySQLdContext mustNot be(null)
      context.mySQLdContext.schemaConfigs.head.name mustBe "test"
      context.mySQLdContext.embeddedMysql mustNot be(mysqld)
    }
  }

}
```

