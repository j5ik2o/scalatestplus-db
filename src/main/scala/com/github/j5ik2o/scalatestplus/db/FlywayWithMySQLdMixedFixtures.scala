package com.github.j5ik2o.scalatestplus.db

import org.flywaydb.core.internal.jdbc.DriverDataSource
import org.scalatest.{ fixture, TestSuiteMixin }

trait FlywayWithMySQLdMixedFixtures
    extends TestSuiteMixin
    with fixture.UnitFixture
    with MySQLdSpecSupport
    with FlywaySpecSupport { this: fixture.TestSuite =>

  abstract class WithFlywayContext(
      flywayConfigF: (String) => FlywayConfig,
      startMySQLdF: => MySQLdContext =
        startMySQLd(this.mySQLdConfig.copy(port = Some(RandomSocket.temporaryServerPort())),
                    this.downloadConfig,
                    this.schemaConfigs),
      stopMySQLdF: (MySQLdContext) => Unit = stopMySQLd
  ) extends fixture.NoArg {

    private var _mySQLdContext: MySQLdContext = _

    private var _flywayContexts: Seq[FlywayContext] = _

    protected def mySQLdContext: MySQLdContext = _mySQLdContext

    protected def flywayContexts: Seq[FlywayContext] = _flywayContexts

    protected def classLoader(jdbcUrl: String): ClassLoader = getClass.getClassLoader

    protected def driverClassName(jdbcUrl: String): String = MY_SQL_JDBC_DRIVER_NAME

    protected def flywayMigrate(flywayContext: FlywayContext): Int =
      flywayContext.flyway.migrate()

    protected def flywayClean(flywayContext: FlywayContext): Unit =
      flywayContext.flyway.clean()

    override def apply(): FixtureParam = {
      def callSuper(): FixtureParam = super.apply()
      try {
        _mySQLdContext = startMySQLdF
        _flywayContexts = _mySQLdContext.jdbUrls.map { jdbcUrl =>
          val flywayContext = createFlywayContext(
            FlywayConfigWithDataSource(new DriverDataSource(classLoader(jdbcUrl),
                                                            driverClassName(jdbcUrl),
                                                            jdbcUrl,
                                                            mySQLdContext.userName,
                                                            mySQLdContext.password),
                                       flywayConfigF(jdbcUrl))
          )
          flywayMigrate(flywayContext)
          flywayContext
        }
        callSuper()
      } finally {
        if (_flywayContexts != null)
          _flywayContexts.foreach(flywayClean)
        if (_mySQLdContext != null)
          stopMySQLdF(_mySQLdContext)
      }
    }

  }

}
