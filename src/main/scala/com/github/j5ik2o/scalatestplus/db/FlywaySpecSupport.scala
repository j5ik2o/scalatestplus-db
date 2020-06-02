package com.github.j5ik2o.scalatestplus.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.callback.Callback
import org.flywaydb.core.internal.jdbc.DriverDataSource

import scala.jdk.CollectionConverters._

case class PlaceholderConfig(
    placeholderReplacement: Boolean = false,
    placeholders: Map[String, String] = Map.empty,
    placeholderPrefix: Option[String] = None,
    placeholderSuffix: Option[String] = None
)

case class FlywayConfig(
    locations: Seq[String],
    callbacks: Seq[Callback] = Seq.empty,
    placeholderConfig: Option[PlaceholderConfig] = None
)

case class FlywayConfigWithDataSource(driverDataSource: DriverDataSource, config: FlywayConfig)

case class FlywayContext(flyway: Flyway, config: FlywayConfigWithDataSource)

trait FlywaySpecSupport {

  protected def createFlywayContext(flywayConfigWithDataSource: FlywayConfigWithDataSource): FlywayContext = {
    val configure = Flyway.configure()
    configure.dataSource(flywayConfigWithDataSource.driverDataSource)
    configure.locations(flywayConfigWithDataSource.config.locations: _*)
    configure.callbacks(flywayConfigWithDataSource.config.callbacks: _*)
    flywayConfigWithDataSource.config.placeholderConfig.foreach { pc =>
      configure.placeholderReplacement(pc.placeholderReplacement)
      configure.placeholders(pc.placeholders.asJava)
      pc.placeholderPrefix.foreach { pp =>
        configure.placeholderPrefix(pp)
      }
      pc.placeholderSuffix.foreach { ps =>
        configure.placeholderSuffix(ps)
      }
    }
    FlywayContext(configure.load(), flywayConfigWithDataSource)
  }

}
