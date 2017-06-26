# scalatestplus-db

This library is a ScalaTest extension library that supports embedded MySQL and Flyway.

## Installation

Add the following to your sbt build (Scala 2.11.x, 2.12.x):

### Release Version

```scala
resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "com.github.j5ik2o" %% "scalatestplus-db" % "1.0.0"
```

### Snapshot Version

```scala
resolvers += "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.github.j5ik2o" %% "scalatestplus-db" % "1.0.0-SNAPSHOT"
```

## Usage

### When starting MySQLd for each test class

```scala

```

### When starting MySQLd for each test case


```scala

```

### When starting MySQLd with Flyway for each test class

```scala

```

### When starting MySQLd with Flyway for each test case


```scala

```

