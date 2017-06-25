sonatypeProfileName := "com.github.j5ik2o"

organization := "com.github.j5ik2o"

name := "scala-test-for-db-support"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.11.8", "2.12.1")

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

libraryDependencies ++= Seq(
  "org.scalatest"              %% "scalatest"         % "3.0.1" % Provided,
  "com.wix"                    % "wix-embedded-mysql" % "2.2.4",
  "org.flywaydb"               % "flyway-core"        % "4.2.0",
  "org.seasar.util"            % "s2util"             % "0.0.1",
  "com.typesafe.scala-logging" %% "scala-logging"     % "3.5.0",
  "ch.qos.logback"             % "logback-classic"    % "1.1.7"
)

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ =>
  false
}

pomExtra := (
  <url>https://github.com/sisioh/baseunits-scala</url>
    <licenses>
      <license>
        <name>MIT License</name>
        <url>https://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:j5ik2o/scala-test-for-db-support.git</url>
      <connection>scm:git:git@github.com:j5ik2o/scala-test-for-db-support.git</connection>
    </scm>
    <developers>
      <developer>
        <id>j5ik2o</id>
        <name>Junichi Kato</name>
        <url>http://j5ik2o.me</url>
      </developer>
    </developers>
)

credentials := {
  val ivyCredentials = (baseDirectory in LocalRootProject).value / ".credentials"
  Credentials(ivyCredentials) :: Nil
}
