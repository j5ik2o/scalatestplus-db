package com.github.j5ik2o.scalatest.db

import org.scalatest.{ fixture, TestSuiteMixin }

trait WixMySQLMixedFixtures extends TestSuiteMixin with fixture.UnitFixture with WixMySQLSpecSupport {
  this: fixture.TestSuite =>

  abstract class WithMySQLdContext(
      startMySQLdF: => MySQLdContext = startMySQLd(this.mySQLdConfig, this.downloadConfig, this.schemaConfigs),
      stopMySQLdF: (MySQLdContext) => Unit = stopMySQLd
  ) extends fixture.NoArg {

    private var _context: MySQLdContext = _

    def context: MySQLdContext = _context

    override def apply(): FixtureParam = {
      def callSuper(): FixtureParam = super.apply()
      try {
        _context = startMySQLdF
        callSuper()
      } finally {
        if (_context != null)
          stopMySQLdF(_context)
      }
    }
  }

}
