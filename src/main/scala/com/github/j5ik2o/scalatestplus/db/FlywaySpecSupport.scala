package com.github.j5ik2o.scalatestplus.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.callback.FlywayCallback
import org.flywaydb.core.internal.util.jdbc.DriverDataSource
import scala.collection.JavaConverters._

case class PlaceholderConfig(placeholderReplacement: Boolean = false,
                             placeholders: Map[String, String] = Map.empty,
                             placeholderPrefix: Option[String] = None,
                             placeholderSuffix: Option[String] = None)

case class FlywayConfig(locations: Seq[String],
                        callbacks: Seq[FlywayCallback] = Seq.empty,
                        placeholderConfig: Option[PlaceholderConfig] = None)

case class FlywayConfigWithDataSource(driverDataSource: DriverDataSource, config: FlywayConfig)

case class FlywayContext(flyway: Flyway, config: FlywayConfigWithDataSource)

trait FlywaySpecSupport {

  protected def createFlywayContext(flywayConfigWithDataSource: FlywayConfigWithDataSource): FlywayContext = {
    val flyway = new Flyway
    flyway.setDataSource(flywayConfigWithDataSource.driverDataSource)
    flyway.setLocations(flywayConfigWithDataSource.config.locations: _*)
    flyway.setCallbacks(flywayConfigWithDataSource.config.callbacks: _*)
    flywayConfigWithDataSource.config.placeholderConfig.foreach { pc =>
      flyway.setPlaceholderReplacement(pc.placeholderReplacement)
      flyway.setPlaceholders(pc.placeholders.asJava)
      pc.placeholderPrefix.foreach { pp =>
        flyway.setPlaceholderPrefix(pp)
      }
      pc.placeholderSuffix.foreach { ps =>
        flyway.setPlaceholderSuffix(ps)
      }
    }
    FlywayContext(flyway, flywayConfigWithDataSource)
  }

}
