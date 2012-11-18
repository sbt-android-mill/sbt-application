import com.jsuereth.sbtsite.SiteKeys

sbtPlugin := true

name := "sbt-application"

organization := "sbt.application"

version := "0.1-SNAPSHOT"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-Xfatal-warnings")

libraryDependencies ++= Seq(
  "org.apache.ant" % "ant" % "1.8.4",
  "net.sf.proguard" % "proguard-base" % "4.8"
)

ScriptedPlugin.scriptedSettings

sbt.dependency.manager.Plugin.activate

site.settings

scriptedLaunchOpts ++= {
  import scala.collection.JavaConverters._
  val args = Seq("-Xmx8196M","-Xms8196M")
  management.ManagementFactory.getRuntimeMXBean().getInputArguments().asScala.filter(a => args.contains(a) || a.startsWith("-XX")).toSeq
}

sourceDirectory <<= (baseDirectory) (_ / ".." / "src")

target <<= (baseDirectory) (_ / ".." / "target")

//ssaPath <<= (baseDirectory) map (_ / "align")
