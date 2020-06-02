package com.github.j5ik2o.scalatestplus.db

import org.flywaydb.core.internal.jdbc.DriverDataSource
import org.scalatest.{ Args, Status, TestSuite, TestSuiteMixin }

trait FlywayWithMySQLdOneInstancePerSuite extends FlywayOneInstancePerSuiteBase with MySQLdOneInstancePerSuite {
  this: TestSuite =>
}

trait FlywayOneInstancePerSuiteBase extends TestSuiteMixin with FlywaySpecSupport {
  this: TestSuite with MySQLdOneInstancePerSuite =>

  private var _contexts: Seq[FlywayContext] = _

  protected def flywayContexts: Seq[FlywayContext] = _contexts

  protected def flywayConfig(jdbcUrl: String): FlywayConfig

  protected def flywayConfigWithDataSources: Seq[FlywayConfigWithDataSource] =
    mySQLdContext.jdbUrls.map { jdbcUrl =>
      FlywayConfigWithDataSource(
        new DriverDataSource(
          getClass.getClassLoader,
          MY_SQL_JDBC_DRIVER_NAME,
          jdbcUrl,
          mySQLdContext.userName,
          mySQLdContext.password
        ),
        flywayConfig(jdbcUrl)
      )
    }

  protected def flywayMigrate(flywayContext: FlywayContext): Int =
    flywayContext.flyway.migrate()

  protected def flywayClean(flywayContext: FlywayContext): Unit =
    flywayContext.flyway.clean()

  abstract override def run(testName: Option[String], args: Args): Status = {
    try {
      _contexts = flywayConfigWithDataSources.map { flywayConfig =>
        val flywayContext = createFlywayContext(flywayConfig)
        flywayMigrate(flywayContext)
        flywayContext
      }
      val status = super.run(testName, args)
      status.whenCompleted { _ =>
        if (_contexts != null)
          _contexts.foreach(flywayClean)
      }
      status
    } catch {
      case ex: Throwable =>
        if (_contexts != null)
          _contexts.foreach(flywayClean)
        throw ex
    }
  }

}
