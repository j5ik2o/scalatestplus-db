package com.github.j5ik2o.scalatestplus.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.callback.FlywayCallback
import org.flywaydb.core.internal.util.jdbc.DriverDataSource

case class FlywayConfig(locations: Seq[String], callbacks: Seq[FlywayCallback] = Seq.empty)

case class FlywayConfigWithDataSource(driverDataSource: DriverDataSource, config: FlywayConfig)

case class FlywayContext(flyway: Flyway, config: FlywayConfigWithDataSource)

trait FlywaySpecSupport {

  protected def createFlywayContext(flywayConfigWithDataSource: FlywayConfigWithDataSource): FlywayContext = {
    val flyway = new Flyway
    flyway.setDataSource(flywayConfigWithDataSource.driverDataSource)
    flyway.setLocations(flywayConfigWithDataSource.config.locations: _*)
    flyway.setCallbacks(flywayConfigWithDataSource.config.callbacks: _*)
    FlywayContext(flyway, flywayConfigWithDataSource)
  }

}
