sonatypeProfileName := "com.github.j5ik2o"

organization := "com.github.j5ik2o"

name := "scalatestplus-db"

scalaVersion := "2.12.11"

crossScalaVersions := Seq("2.11.12", "2.12.11", "2.13.2")

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-Xfatal-warnings",
  "-language:_",
  "-Ywarn-dead-code" // Warn when dead code is identified.
  ,
  "-Ywarn-numeric-widen" // Warn when numerics are widened.
) ++ {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2L, scalaMajor)) if scalaMajor == 13 => Seq(
      "-Xlint:adapted-args"  // Warn if an argument list is modified to match the receiver.
      ,
      "-Xlint:inaccessible" // Warn about inaccessible types in method signatures.
      ,
      "-Xlint:infer-any" // Warn when a type argument is inferred to be `Any`.
      ,
      "-Xlint:nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
      ,
      "-Xlint:nullary-unit" // Warn when nullary methods return Unit.
      ,
      "-Xlint:unused" // Enable -Wunused:imports,privates,locals,implicits.
    )
    case Some((2L, scalaMajor)) if scalaMajor == 12 => Seq(
      "-Ywarn-adapted-args" // Warn if an argument list is modified to match the receiver
      ,
      "-Ywarn-inaccessible" // Warn about inaccessible types in method signatures.
      ,
      "-Ywarn-infer-any" // Warn when a type argument is inferred to be `Any`.
      ,
      "-Ywarn-nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
      ,
      "-Ywarn-nullary-unit" // Warn when nullary methods return Unit.
      ,
      "-Ywarn-unused-import" // Warn when imports are unused.
    )
    case Some((2L, scalaMajor)) if scalaMajor <= 11 => Seq(
      "-Ywarn-adapted-args" // Warn if an argument list is modified to match the receiver
      ,
      "-Ywarn-inaccessible" // Warn about inaccessible types in method signatures.
      ,
      "-Ywarn-infer-any" // Warn when a type argument is inferred to be `Any`.
      ,
      "-Ywarn-nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
      ,
      "-Ywarn-nullary-unit" // Warn when nullary methods return Unit.
      ,
      "-Ywarn-unused-import" // Warn when imports are unused.
    )
  }
}

resolvers ++= Seq(
  "Seasar Repository" at "https://maven.seasar.org/maven2/"
)

libraryDependencies ++= Seq(
  "org.scalatest"              %% "scalatest"            % "3.1.2"  % Provided,
  "com.wix"                     % "wix-embedded-mysql"   % "4.6.1",
  "org.flywaydb"                % "flyway-core"          % "6.4.3",
  "org.seasar.util"             % "s2util"               % "0.0.1",
  "com.typesafe.scala-logging" %% "scala-logging"        % "3.9.2",
  "ch.qos.logback"              % "logback-classic"      % "1.2.3"  % Test,
  "mysql"                       % "mysql-connector-java" % "5.1.49" % Test
) ++ {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2L, scalaMajor)) if scalaMajor == 13 =>
      Seq.empty
    case Some((2L, scalaMajor)) if scalaMajor == 12 =>
      Seq(
        "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
      )
    case Some((2L, scalaMajor)) if scalaMajor == 11 =>
      Seq(
        "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
      )
  }
}

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
