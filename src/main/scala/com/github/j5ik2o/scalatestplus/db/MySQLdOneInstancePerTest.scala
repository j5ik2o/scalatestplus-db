package com.github.j5ik2o.scalatestplus.db

import org.scalatest.{ fixture, Outcome, TestData, TestSuiteMixin }

trait MySQLdOneInstancePerTest extends TestSuiteMixin with MySQLdSpecSupport { this: fixture.TestSuite =>

  type FixtureParam = MySQLdContext

  def newMySQLd(testData: TestData): MySQLdContext = {
    startMySQLd(mySQLdConfig = MySQLdConfig(port = Some(RandomSocket.temporaryServerPort())))
  }

  override def withFixture(test: OneArgTest): Outcome = {
    var _context: MySQLdContext = null
    try {
      _context = newMySQLd(test)
      withFixture(test.toNoArgTest(_context))
    } finally {
      if (_context != null)
        stopMySQLd(_context)
    }
  }

}
