package com.github.j5ik2o.scalatestplus.db

import java.io.File
import java.net.URL
import java.util.TimeZone
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.LazyLogging
import com.wix.mysql.config.{ Charset, DownloadConfig => DC, MysqldConfig => MC, SchemaConfig => SC }
import com.wix.mysql.distribution.Version
import com.wix.mysql.distribution.Version._
import com.wix.mysql.{ EmbeddedMysql, SqlScriptSource }
import org.seasar.util.io.ResourceUtil

import scala.collection.JavaConverters._
import scala.concurrent.duration.{ Duration, _ }

case class MySQLdContext(embeddedMysql: EmbeddedMysql,
                         mySQLdConfig: MySQLdConfig,
                         downloadConfig: DownloadConfig,
                         schemaConfigs: Seq[SchemaConfig]) {
  def jdbUrls: Seq[String] =
    schemaConfigs.map { sc =>
      s"jdbc:mysql://localhost:${mySQLdConfig.port.getOrElse(3310)}/${sc.name}?useSSL=false"
    }

  def userName: String = mySQLdConfig.userWithPassword.map(_.userName).getOrElse("auser")

  def password: String = mySQLdConfig.userWithPassword.map(_.password).getOrElse("sa")

  override def toString: String = {
    s"MySQLdContext($embeddedMysql, jdbcUrls = $jdbUrls, userName = $userName, mySQLdConfig = $mySQLdConfig, downloadConfig = $downloadConfig, schemaConfigs = $schemaConfigs)"
  }
}

case class UserWithPassword(userName: String, password: String)

case class MySQLdConfig(version: Version = v5_7_latest,
                        port: Option[Int] = None,
                        timeout: Option[Duration] = Some(30 seconds),
                        charset: Charset = Charset.defaults(),
                        userWithPassword: Option[UserWithPassword] = None,
                        timeZone: Option[TimeZone] = None,
                        serverVariables: Map[String, Any] = Map.empty,
                        tempDir: Option[File] = None)

case class DownloadConfig(baseUrl: URL, cacheDir: File)

case class SchemaConfig(name: String,
                        charset: Charset = Charset.defaults(),
                        commands: Seq[String] = Seq.empty,
                        scripts: Seq[SqlScriptSource] = Seq.empty)

trait MySQLdSpecSupport extends LazyLogging {

  final val MY_SQL_JDBC_DRIVER_NAME = "org.mysql.jdbc.Driver"

  protected def mySQLdConfig: MySQLdConfig = MySQLdConfig(port = Some(RandomSocket.temporaryServerPort()))

  protected def downloadConfig: DownloadConfig = DownloadConfig(
    baseUrl = new URL("https://dev.mysql.com/get/Downloads/"),
    cacheDir = new File(ResourceUtil.getBuildDir(getClass), "/../../")
  )

  protected def schemaConfigs: Seq[SchemaConfig]

  private implicit class ToMySQLdConfig(mySQLdConfig: MySQLdConfig) {

    def asWix: MC = {
      val result0 = MC
        .aMysqldConfig(mySQLdConfig.version)
        .withCharset(mySQLdConfig.charset)
      val result1 = mySQLdConfig.port.fold(result0)(result0.withPort)
      val result2 = mySQLdConfig.timeout.fold(result1)(v => result1.withTimeout(v.length, v.unit))
      val result3 = mySQLdConfig.userWithPassword.fold(result2)(v => result2.withUser(v.userName, v.password))
      val result4 = mySQLdConfig.timeZone.fold(result3)(result3.withTimeZone)
      val result5 = mySQLdConfig.serverVariables.foldLeft(result4) {
        case (l, (k, v: Boolean)) => l.withServerVariable(k, v: Boolean)
        case (l, (k, v: Int))     => l.withServerVariable(k, v: Int)
        case (l, (k, v: String))  => l.withServerVariable(k, v: String)
      }
      val result6 = mySQLdConfig.tempDir.fold(result5)(f => result5.withTempDir(f.toString))
      result6.build()
    }

  }

  private implicit class ToDownloadConfig(downloadConfig: DownloadConfig) {
    def asWix: DC = {
      val result0 = DC.aDownloadConfig()
      val result1 = result0.withBaseUrl(downloadConfig.baseUrl.toString)
      val result2 = result1.withCacheDir(downloadConfig.cacheDir.toString)
      result2.build()
    }
  }

  private implicit class ToSchemaConfigs(schemaConfigs: Seq[SchemaConfig]) {

    def asWix: Seq[SC] = schemaConfigs.map(toSchemaConfig)

    private def toSchemaConfig(schemaConfig: SchemaConfig): SC = {
      val result0 = SC.aSchemaConfig(schemaConfig.name).withCharset(schemaConfig.charset)
      val result1 =
        if (schemaConfig.commands.isEmpty)
          result0
        else
          result0.withCommands(schemaConfig.commands.asJava)
      val result2 =
        if (schemaConfig.scripts.isEmpty)
          result1
        else
          result1.withScripts(schemaConfig.scripts.asJava)
      result2.build()
    }

  }

  protected def startMySQLd(mySQLdConfig: MySQLdConfig = this.mySQLdConfig,
                            downloadConfig: DownloadConfig = this.downloadConfig,
                            schemaConfigs: Seq[SchemaConfig] = this.schemaConfigs): MySQLdContext = {
    val mysql = EmbeddedMysql.anEmbeddedMysql(mySQLdConfig.asWix, downloadConfig.asWix)
    val mysql2 =
      if (schemaConfigs.nonEmpty) schemaConfigs.asWix.foldLeft(mysql)(_ addSchema _)
      else mysql
    val instance = mysql2.start()
    val result = MySQLdContext(
      instance,
      mySQLdConfig.copy(
        port = Some(instance.getConfig.getPort),
        timeout = Some(instance.getConfig.getTimeout(TimeUnit.MILLISECONDS) milliseconds),
        charset = instance.getConfig.getCharset,
        userWithPassword = Some(UserWithPassword(instance.getConfig.getUsername, instance.getConfig.getPassword)),
        timeZone = Some(instance.getConfig.getTimeZone),
        tempDir = Some(new File(instance.getConfig.getTempDir))
      ),
      downloadConfig,
      schemaConfigs
    )
    logger.info(s"mysqld has started: context = $result")
    result
  }

  protected def stopMySQLd(mySQLdContext: MySQLdContext): Unit = {
    mySQLdContext.embeddedMysql.stop()
    logger.info(s"mysqld has stopped: context = $mySQLdContext")
  }

}
