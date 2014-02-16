/**
 * sbt-application - application builder with ProGuard and JavaFX support
 *
 * Copyright (c) 2014 Alexey Aksenov ezh@ezh.msk.ru
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

package sbt

package object application {
  /** Entry point for plugin in user's project */
  lazy val Application = Plugin.defaultSettings
  /** Entry point for plugin in user's project */
  lazy val ApplicationWithPackager = Plugin.defaultSettingsWithPackager

  // export declarations for end user
  lazy val ApplicationKey = Keys
  lazy val ApplicationConf = Keys.ApplicationConf
}
