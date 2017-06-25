package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.util.jdbc.DriverDataSource

trait FlywaySpecSupport {

  val classLoader: ClassLoader = getClass.getClassLoader
  val driverClass: String
  val url: String
  val user: String
  val password: String

  val locations: Seq[String]

  lazy val driverDataSource: DriverDataSource = new DriverDataSource(classLoader, driverClass, url, user, password)

  protected def createFlyway(driverDataSource: DriverDataSource = this.driverDataSource): Flyway = {
    val flyway = new Flyway
    flyway.setDataSource(driverDataSource)
    flyway.setLocations(locations: _*)
    flyway
  }

}
