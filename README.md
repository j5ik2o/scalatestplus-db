# scalatestplus-db

This library is a ScalaTest extension library that supports embedded MySQL and Flyway.

## Installation

Add the following to your sbt build (Scala 2.11.x, 2.12.x):

### Release Version

```scala
resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "com.github.j5ik2o" %% "scalatestplus-db" % "1.0.0"
```

### Snapshot Version

```scala
resolvers += "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.github.j5ik2o" %% "scalatestplus-db" % "1.0.0-SNAPSHOT"
```

## Usage

### When starting MySQLd for each test class

```scala
import com.wix.mysql.EmbeddedMysql
import org.scalatest.{ FreeSpec, MustMatchers }

class MySQLdOnInstancePerFreeSpec extends FreeSpec with MustMatchers with MySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "MySQLdOnInstancePerFreeSpec" - {
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
import org.scalatest.{ fixture, MustMatchers }

class MySQLdOnInstancePerTestOfFreeSpec extends fixture.FreeSpec with MustMatchers with MySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  var mysqld: EmbeddedMysql = _

  "MySQLdOnInstancePerTestOfFreeSpec" - {
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
import org.scalatest.{ FreeSpec, MustMatchers }

class FlywayWithMySQLdOnInstancePerFreeSpec
    extends FreeSpec
    with MustMatchers
    with FlywayWithMySQLdOneInstancePerSuite {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(Seq("db"))

  var mysqld: EmbeddedMysql = _

  "FlywayWithMySQLdOnInstancePerFreeSpec" - {
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
import org.scalatest.{ fixture, MustMatchers }

class FlywayWithMySQLdOnInstancePerTestOfFreeSpec
    extends fixture.FreeSpec
    with MustMatchers
    with FlywayWithMySQLdOneInstancePerTest {

  override protected val schemaConfigs: Seq[SchemaConfig] = Seq(SchemaConfig("test"))

  override protected def flywayConfig(jdbcUrl: String): FlywayConfig = FlywayConfig(Seq("db"))

  var mysqld: EmbeddedMysql = _

  "FlywayWithMySQLdOnInstancePerTestOfFreeSpec" - {
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

