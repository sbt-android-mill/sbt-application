/**
 * sbt-application - application builder with ProGuard and JavaFX support
 *
 * Copyright (c) 2012 Alexey Aksenov ezh@ezh.msk.ru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sbt.application.proguard

import java.io.File
import java.util.Properties

import proguard.{ Configuration => ProGuardConfiguration }
import proguard.ConfigurationParser
import proguard.ProGuard

import sbt._
import sbt.IO._
import sbt.Keys._
import sbt.application.ApplicationKeys._

object Proguard {
  /** ProGuard plugin settings */
  val settings: Seq[Project.Setting[_]] = Seq(
    proguard <<= proguardTask,
    proguardArtifact <<= proguardArtifactTask,
    proguardEnabled := true,
    proguardInJars <<= proguardInJarsTask,
    proguardLibraryJars <<= proguardLibraryJarsTask,
    proguardOptimizations := Seq.empty,
    proguardOption <<= proguardOptionTask,
    proguardSuffix := "-proguard")

  /** ProGuard task */
  def proguardTask: Project.Initialize[Task[Option[File]]] =
    (proguardEnabled, proguardArtifact, sbt.Keys.`package` in Compile, proguardOptimizations, proguardInJars, proguardLibraryJars, proguardOption, streams) map {
      (proguardEnabled, proguardArtifact, originalArtifact, proguardOptimizations, proguardInJars, proguardLibraryJars, proguardOption, streams) =>
        if (proguardEnabled) {
          val optimizationOptions = if (proguardOptimizations.isEmpty) Seq("-dontoptimize") else proguardOptimizations
          val sep = File.pathSeparator
          val skipResources = List("!META-INF/MANIFEST.MF", "library.properties")
          val inJarsArg = "-injars " + (("\"" + originalArtifact.absolutePath + "\"") +:
            proguardInJars.map("\"" + _ + "\"" + skipResources.mkString("(", ",!**/", ")"))).mkString(sep)
          val outJarsArg = "-outjars " + "\"" + proguardArtifact.absolutePath + "\""
          val libraryJarsArg = proguardLibraryJars.map("\"" + _ + "\"") match {
            case Nil => ""
            case libraryJars => "-libraryjars " + libraryJars.mkString(sep)
          }
          val args = Seq(inJarsArg, outJarsArg, libraryJarsArg) ++ optimizationOptions ++ proguardOption
          streams.log.debug("executing proguard: " + (for (i <- 0 until args.size) yield { "arg" + (i + 1) + ": " + args(i) }).mkString("\n"))
          val config = new ProGuardConfiguration
          new ConfigurationParser(args.toArray[String], new Properties).parse(config)
          streams.log.debug("executing proguard: " + args.mkString("\n"))
          new ProGuard(config).execute
          Some(proguardArtifact)
        } else {
          streams.log.debug("skip proguard")
          None
        }
    }
  def proguardInJarsTask = (proguardLibraryJars, dependencyClasspath in Compile) map {
    (proguardLibraryJars, dependencyClasspath) =>
      dependencyClasspath.map(_.data) --- proguardLibraryJars get
  }
  def proguardLibraryJarsTask = (javafxRT) map ((javafxRT) => javafxRT.map(_.data))
  def proguardArtifactTask = (proguardSuffix, sbt.Keys.`package` in Compile) map {
    (proguardSuffix, originalArtifact) =>
      val name = originalArtifact.getName.split("""\.""")
      new File(originalArtifact.getParent, name.dropRight(1).mkString(".") +
        Seq(proguardSuffix, name.last).mkString("."))
  }
  def proguardOptionTask = (applicationPackage) map {
    case Some(applicationPackage) =>
      Seq(
        "-dontwarn",
        "-dontobfuscate",
        "-dontnote scala.Enumeration",
        "-dontnote org.xml.sax.EntityResolver",
        "-keep public class * implements junit.framework.Test { public void test*(); }",
        "-keep public class " + applicationPackage + ".** { public protected *; }",
        """
     -keepclassmembers class * implements java.io.Serializable {
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
      }
    """)
    case None =>
      sys.error("please, define 'application-package'")
      Seq()
  }
}
