package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.util.jdbc.DriverDataSource
import org.scalatest.{ fixture, Outcome, TestData, TestSuiteMixin }

trait FlywayOneInstancePerTest extends TestSuiteMixin with FlywaySpecSupport with WixMySQLSpecSupport {
  this: fixture.TestSuite =>

  type FixtureParam = (MySQLdContext, Seq[Flyway])

  def newMySQLd(testData: TestData): MySQLdContext = {
    startMySQLd(mySQLdConfig = MySQLdConfig(port = Some(RandomSocket.nextPort())))
  }

  def driverClassName(jdbcUrl: String): String

  def locations(jdbcUrl: String): Seq[String]

  override def withFixture(test: OneArgTest): Outcome = {
    var mySQLdContext: MySQLdContext = null
    var flyways: Seq[Flyway]         = null
    try {
      mySQLdContext = newMySQLd(test)
      flyways = mySQLdContext.jdbUrls.map { jdbcUrl =>
        val flyway =
          createFlyway(
            FlywayConfig(new DriverDataSource(classLoader,
                                              driverClassName(jdbcUrl),
                                              jdbcUrl,
                                              mySQLdContext.userName,
                                              mySQLdContext.password),
                         locations(jdbcUrl))
          )
        flyway.migrate()
        flyway
      }
      withFixture(test.toNoArgTest((mySQLdContext, flyways)))
    } finally {
      if (flyways != null) {
        flyways.foreach(_.clean())
      }
      if (mySQLdContext != null)
        stopMySQLd(mySQLdContext)
    }
  }

}
