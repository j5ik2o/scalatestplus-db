sonatypeProfileName := "com.github.j5ik2o"

organization := "com.github.j5ik2o"

name := "scalatestplus-db"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.11.11", "2.12.8")

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-Xfatal-warnings",
  "-language:_",
  "-Ywarn-adapted-args" // Warn if an argument list is modified to match the receiver
  ,
  "-Ywarn-dead-code" // Warn when dead code is identified.
  ,
  "-Ywarn-inaccessible" // Warn about inaccessible types in method signatures.
  ,
  "-Ywarn-infer-any" // Warn when a type argument is inferred to be `Any`.
  ,
  "-Ywarn-nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
  ,
  "-Ywarn-nullary-unit" // Warn when nullary methods return Unit.
  ,
  "-Ywarn-numeric-widen" // Warn when numerics are widened.
  ,
  "-Ywarn-unused-import" // Warn when imports are unused.
)

resolvers ++= Seq(
  "Seasar Repository" at "http://maven.seasar.org/maven2/"
)

libraryDependencies ++= Seq(
  "org.scalatest"              %% "scalatest"           % "3.0.1" % Provided,
  "com.wix"                    % "wix-embedded-mysql"   % "4.2.0",
  "org.flywaydb"               % "flyway-core"          % "4.2.0",
  "org.seasar.util"            % "s2util"               % "0.0.1",
  "com.typesafe.scala-logging" %% "scala-logging"       % "3.5.0",
  "ch.qos.logback"             % "logback-classic"      % "1.1.7" % Test,
  "mysql"                      % "mysql-connector-java" % "5.1.42" % Test
)

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ =>
  false
}

pomExtra := (
  <url>https://github.com/j5ik2o/scalatestplus-db</url>
    <licenses>
      <license>
        <name>MIT License</name>
        <url>https://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:j5ik2o/scalatestplus-db.git</url>
      <connection>scm:git:git@github.com:j5ik2o/scalatestplus-db.git</connection>
    </scm>
    <developers>
      <developer>
        <id>j5ik2o</id>
        <name>Junichi Kato</name>
        <url>http://blog.j5ik2o.me</url>
      </developer>
    </developers>
)

publishTo in ThisBuild := sonatypePublishTo.value

scalafmtOnCompile in Compile := true

credentials := {
  val ivyCredentials = (baseDirectory in LocalRootProject).value / ".credentials"
  Credentials(ivyCredentials) :: Nil
}

