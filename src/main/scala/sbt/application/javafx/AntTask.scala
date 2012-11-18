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

import java.net.URLClassLoader
import org.apache.tools.ant.types.DataType
import org.apache.tools.ant.Project

trait AntTask {
  val project: Project
  val classLoader: URLClassLoader
  val mainClass: Class[_]
  protected lazy val executeMethod = mainClass.getMethod("execute")
  val instance: Any

  /** Execute ant task */
  def execute(): Unit = executeMethod.invoke(instance)
}

object AntTask {
  class Application(application: DataType) {
    protected lazy val setMainClassMethod = application.getClass.getMethod("setMainClass", classOf[String])
    protected lazy val setPreloaderClassMethod = application.getClass.getMethod("setPreloaderClass", classOf[String])
    /*
  public void setVersion(java.lang.String);
  public void setToolkit(java.lang.String);
  public void setFallbackClass(java.lang.String);
  public void setName(java.lang.String);
  public com.sun.javafx.tools.packager.Param createParam();
  public void setParams(java.util.Properties);
  public com.sun.javafx.tools.ant.Application$Argument createArgument();
  public com.sun.javafx.tools.packager.HtmlParam createHtmlParam();
  public void setId(java.lang.String);
  public void setRefid(org.apache.tools.ant.types.Reference);
  public void (java.lang.String);*/

    /** Set qualified name of the application class to be executed */
    def setMainClass(mainClass: String): Unit = setMainClassMethod.invoke(application, mainClass)
    /** Set qualified name of the preloader class to be executed */
    def setPreloaderClass(preloaderClass: String): Unit = setPreloaderClassMethod.invoke(application, preloaderClass)
  }
  class Platform(platform: DataType) {
    /*
    *   public com.sun.javafx.tools.ant.Platform();
  public void setJ2se(java.lang.String);
  public void setJavafx(java.lang.String);
  public com.sun.javafx.tools.ant.Platform$Property createProperty();
  public com.sun.javafx.tools.ant.Platform$Jvmarg createJvmarg();
  public com.sun.javafx.tools.ant.Platform get();
    */
  }
  class Resources(platform: DataType) {
    /*
  public com.sun.javafx.tools.ant.FileSet createFileSet();
     */
  }
}