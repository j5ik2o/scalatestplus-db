package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.scalatest.{ fixture, TestSuiteMixin }

trait FlywayMixedFixtures
    extends TestSuiteMixin
    with fixture.UnitFixture
    with WixMySQLSpecSupport
    with FlywaySpecSupport { this: fixture.TestSuite =>

  abstract class WithMySQLdContext(
      startMySQLdF: => MySQLdContext = startMySQLd(this.mySQLdConfig, this.downloadConfig, this.schemaConfigs),
      stopMySQLdF: (MySQLdContext) => Unit = stopMySQLd
  ) extends fixture.NoArg {

    private var _mySQLdContext: MySQLdContext = _

    def mySQLdContext: MySQLdContext = _mySQLdContext

    private var _flywayContext: Flyway = _

    def flywayContext: Flyway = _flywayContext

    def jdbcDriverClassName: String = "org.mysql.jdbc.Driver"

    def locations: Seq[String]

    override def apply(): FixtureParam = {
      def callSuper(): FixtureParam = super.apply()
      try {
        _mySQLdContext = startMySQLdF
        _flywayContext = new Flyway
        _flywayContext.migrate()
        callSuper()
      } finally {
        if (_flywayContext != null)
          _flywayContext.clean()
        if (_mySQLdContext != null)
          stopMySQLdF(_mySQLdContext)
      }
    }
  }

}
