name := """mlh_hack"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.mockito"%"mockito-core"%"1.9.5",
  "org.json"%"org.json"%"chargebee-1.0",
  "com.google.code.gson" % "gson" % "2.2.4",
  "org.neo4j" % "neo4j-jdbc" % "2.0.0-M06",
  "org.neo4j" % "neo4j" % "2.1.5" exclude("org.eo4j", "neo4j-jmx"),
  "org.springframework" % "spring-context" % "3.2.5.RELEASE"
)

resolvers ++= Seq(
  "Neo4j releases" at "http://m2.neo4j.org/content/repositories/releases",
  "Neo4j snapshots" at "http://m2.neo4j.org/content/repositories/snapshots",
  // for neo4j plugin
  "Neo4j Maven Repo" at "http://m2.neo4j.org/releases",
  Resolver.sonatypeRepo("snapshots")
)
