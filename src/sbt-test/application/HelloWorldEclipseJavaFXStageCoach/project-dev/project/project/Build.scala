import sbt._
object PluginDef extends Build {
  override def projects = Seq(root)
  lazy val root = Project("plugins", file(".")) dependsOn(app)
  lazy val app = uri("git@github.com:sbt-android-mill/sbt-application.git")
}
