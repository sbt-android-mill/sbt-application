import sbt._
object PluginDef extends Build {
  override def projects = Seq(root)
  lazy val root = Project("plugins", file(".")) dependsOn()
//  lazy val ssa = uri("git://github.com/sbt-android-mill/sbt-source-align.git#0.2")
}
