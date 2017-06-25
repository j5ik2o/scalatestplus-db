package com.github.j5ik2o.scalatest.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.util.jdbc.DriverDataSource

case class FlywayConfig(driverDataSource: DriverDataSource, locations: Seq[String])

trait FlywaySpecSupport {

  def classLoader: ClassLoader = getClass.getClassLoader

//  protected def createFlyway(classLoader: ClassLoader = this.classLoader,
//                             driverClass: String,
//                             url: String,
//                             user: String,
//                             password: String,
//                             locations: Seq[String]): Flyway =
//    createFlyway(new DriverDataSource(classLoader, driverClass, url, user, password), locations)

  protected def createFlyway(flywayConfig: FlywayConfig): Flyway = {
    val flyway = new Flyway
    flyway.setDataSource(flywayConfig.driverDataSource)
    flyway.setLocations(flywayConfig.locations: _*)
    flyway
  }

}
