package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.scalatest.{ Args, Status, TestSuite, TestSuiteMixin }

trait FlywayOneInstancePerSuite extends FlywayOneInstancePerSuiteBase with WixMySQLOneInstancePerSuite {
  this: TestSuite =>
}

trait FlywayOneInstancePerSuiteBase extends TestSuiteMixin with FlywaySpecSupport { this: TestSuite =>

  private var _context: Seq[Flyway] = _

  protected def flywayContext: Seq[Flyway] = _context

  protected def flywayConfigs: Seq[FlywayConfig]

  abstract override def run(testName: Option[String], args: Args): Status = {
    try {
      _context = flywayConfigs.map { config =>
        val flyway = createFlyway(config)
        flyway.migrate()
        flyway
      }
      val status = super.run(testName, args)
      status.whenCompleted { _ =>
        _context.foreach(_.clean())
      }
      status
    } catch {
      case ex: Throwable =>
        if (_context != null)
          _context.foreach(_.clean())
        throw ex
    }
  }
}
