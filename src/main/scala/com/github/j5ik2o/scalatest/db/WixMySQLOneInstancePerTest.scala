package com.github.j5ik2o.scalatest.db

import org.scalatest.{ fixture, Outcome, TestData, TestSuiteMixin }

trait WixMySQLOneInstancePerTest extends TestSuiteMixin with WixMySQLSpecSupport { this: fixture.TestSuite =>

  type FixtureParam = MySQLdContext

  private var _context: MySQLdContext = _

  protected def context = synchronized {
    _context
  }

  def newMySQLd(testData: TestData): MySQLdContext = {
    startMySQLd(mySQLdConfig = MySQLdConfig(port = Some(RandomSocket.nextPort())))
  }

  override def withFixture(test: OneArgTest): Outcome = {
    try {
      synchronized {
        _context = newMySQLd(test)
      }
      withFixture(test.toNoArgTest(_context))
    } finally {
      if (_context != null)
        stopMySQLd(_context)
    }
  }

}
