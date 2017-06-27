package com.github.j5ik2o.scalatestplus.db

import org.scalatest.{ Args, Status, TestSuite, TestSuiteMixin }

trait MySQLdOneInstancePerSuite extends TestSuiteMixin with MySQLdSpecSupport { this: TestSuite =>

  private var _context: MySQLdContext = _

  protected def mySQLdContext: MySQLdContext = _context

  protected def mySQLdPort: Option[Int] = Some(RandomSocket.temporaryServerPort())

  abstract override def run(testName: Option[String], args: Args): Status = {
    try {
      _context = startMySQLd(mySQLdConfig = MySQLdConfig(port = mySQLdPort))
      val status = super.run(testName, args)
      status.whenCompleted { _ =>
        if (_context != null)
          stopMySQLd(_context)
      }
      status
    } catch {
      case ex: Throwable =>
        if (_context != null)
          stopMySQLd(_context)
        throw ex
    }
  }

}
