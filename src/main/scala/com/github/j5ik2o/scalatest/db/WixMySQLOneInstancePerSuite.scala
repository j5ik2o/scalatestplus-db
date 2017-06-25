package com.github.j5ik2o.scalatest.db

import org.scalatest.{ Args, Status, TestSuite, TestSuiteMixin }

trait WixMySQLOneInstancePerSuite extends TestSuiteMixin with WixMySQLSpecSupport { this: TestSuite =>

  private var _context: MySQLdContext = _

  protected def wixMySQLContext: MySQLdContext = _context

  abstract override def run(testName: Option[String], args: Args): Status = {
    _context = startMySQLd(mySQLdConfig = MySQLdConfig(port = Some(RandomSocket.nextPort())))
    try {
      val status = super.run(testName, args)
      status.whenCompleted { _ =>
        println("finish mysql")
        stopMySQLd(_context)
      }
      status
    } catch {
      case ex: Throwable =>
        stopMySQLd(_context)
        throw ex
    }
  }

}
