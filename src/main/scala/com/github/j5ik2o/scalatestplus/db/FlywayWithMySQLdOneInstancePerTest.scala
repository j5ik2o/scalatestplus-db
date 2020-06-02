package com.github.j5ik2o.scalatestplus.db

import org.flywaydb.core.internal.jdbc.DriverDataSource
import org.scalatest.{ FixtureTestSuite, Outcome, TestData, TestSuiteMixin }

case class MySQLdContextWithFlywayContexts(mySQLdContext: MySQLdContext, flywayContexts: Seq[FlywayContext])

trait FlywayWithMySQLdOneInstancePerTest extends TestSuiteMixin with FlywaySpecSupport with MySQLdSpecSupport {
  this: FixtureTestSuite =>

  type FixtureParam = MySQLdContextWithFlywayContexts

  def newMySQLd(testData: TestData): MySQLdContext = {
    startMySQLd(mySQLdConfig = MySQLdConfig(port = Some(RandomSocket.temporaryServerPort())))
  }

  def classLoader: ClassLoader = getClass.getClassLoader

  protected def classLoader(jdbcUrl: String): ClassLoader = getClass.getClassLoader

  protected def driverClassName(jdbcUrl: String): String = MY_SQL_JDBC_DRIVER_NAME

  protected def flywayConfig(jdbcUrl: String): FlywayConfig

  protected def flywayMigrate(flywayContext: FlywayContext): Int =
    flywayContext.flyway.migrate()

  protected def flywayClean(flywayContext: FlywayContext): Unit =
    flywayContext.flyway.clean()

  override def withFixture(test: OneArgTest): Outcome = {
    var mySQLdContext: MySQLdContext       = null
    var flywayContexts: Seq[FlywayContext] = null
    try {
      mySQLdContext = newMySQLd(test)
      flywayContexts = mySQLdContext.jdbUrls.map { jdbcUrl =>
        val flywayContext =
          createFlywayContext(
            FlywayConfigWithDataSource(
              new DriverDataSource(
                classLoader(jdbcUrl),
                driverClassName(jdbcUrl),
                jdbcUrl,
                mySQLdContext.userName,
                mySQLdContext.password
              ),
              flywayConfig(jdbcUrl)
            )
          )
        flywayMigrate(flywayContext)
        flywayContext
      }
      withFixture(test.toNoArgTest(MySQLdContextWithFlywayContexts(mySQLdContext, flywayContexts)))
    } finally {
      if (flywayContexts != null) {
        flywayContexts.foreach(flywayClean)
      }
      if (mySQLdContext != null)
        stopMySQLd(mySQLdContext)
    }
  }

}
