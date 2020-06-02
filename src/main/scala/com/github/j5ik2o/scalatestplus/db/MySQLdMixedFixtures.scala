package com.github.j5ik2o.scalatestplus.db

import org.scalatest.{ fixture, FixtureTestSuite, TestSuiteMixin }

trait MySQLdMixedFixtures extends TestSuiteMixin with fixture.UnitFixture with MySQLdSpecSupport {
  this: FixtureTestSuite =>

  abstract class WithMySQLdContext(
      startMySQLdF: => MySQLdContext = startMySQLd(
        this.mySQLdConfig.copy(port = Some(RandomSocket.temporaryServerPort())),
        this.downloadConfig,
        this.schemaConfigs
      ),
      stopMySQLdF: MySQLdContext => Unit = stopMySQLd
  ) extends fixture.NoArg {

    private var _context: MySQLdContext = _

    protected def mySQLdContext: MySQLdContext = _context

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
