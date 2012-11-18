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

package sbt.application.javafx

import java.io.File
import java.lang.Boolean
import java.lang.String
import java.net.URLClassLoader

import org.apache.tools.ant.Project

class CSSToBinTask(val project: Project, val classLoader: URLClassLoader) extends AntTask {
  lazy val mainClass = classLoader.loadClass("com.sun.javafx.tools.ant.DeployFXTask")
  lazy val setVerboseMethod = mainClass.getMethod("setVerbose", classOf[java.lang.Boolean])
  lazy val setOutdirMethod = mainClass.getMethod("setOutdir", classOf[java.lang.String])
  //lazy val createFilesetMethod = mainClass.getMethod("createFileset", classOf[String], classOf[String], classOf[java.util.List])
  lazy val instance = Class.forName("com.sun.javafx.tools.ant.DeployFXTask", true, classLoader).newInstance()

  def setVerbose(verbose: java.lang.Boolean): Unit = setVerboseMethod.invoke(instance, verbose)
  def setOutdirMethod(outDir: File): Unit = setOutdirMethod.invoke(instance, outDir)
  //def createFileset(cp: String): Unit = setClasspathMethod.invoke(instance, cp)
}