sbtPlugin := true

name := "sbt-application"

organization := "sbt.application"

version := "0.1-SNAPSHOT"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-Xfatal-warnings")

libraryDependencies ++= Seq(
  "org.apache.ant" % "ant" % "1.8.4",
  "net.sf.proguard" % "proguard-base" % "4.8"
)
