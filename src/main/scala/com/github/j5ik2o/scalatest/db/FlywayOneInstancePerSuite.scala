package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.scalatest.{ Args, Status, TestSuite, TestSuiteMixin }

trait FlywayOneInstancePerSuite extends TestSuiteMixin with FlywaySpecSupport { this: TestSuite =>

  private var _context: Flyway = _

  protected def context = _context

  abstract override def run(testName: Option[String], args: Args): Status = {
    _context = new Flyway
    try {
      val status = super.run(testName, args)
      status.whenCompleted { _ =>
        _context.clean()
      }
      status
    } catch {
      case ex: Throwable =>
        _context.clean()
        throw ex
    }
  }
}
