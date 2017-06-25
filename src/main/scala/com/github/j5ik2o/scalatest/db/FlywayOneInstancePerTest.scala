package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.scalatest.{ fixture, Outcome, TestSuiteMixin }

trait FlywayOneInstancePerTest extends TestSuiteMixin with FlywaySpecSupport { this: fixture.TestSuite =>
  type FixtureParam = Flyway

  override def withFixture(test: OneArgTest): Outcome = {
    var flyway: Flyway = null
    try {
      flyway = createFlyway()
      flyway.migrate()
      withFixture(test.toNoArgTest(flyway))
    } finally {
      flyway.clean()
    }
  }
}
