/**
 * Hello world example of JavaFX for Scala
 *
 * Copyright (c) 2012 Alexey Aksenov ezh@ezh.msk.ru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.digimead.helloworld

import java.io.File
import java.net.URL
import javafx.stage.Stage
import javafx.application.Application
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.scene.control.Button
import javafx.scene.paint.Color
import javafx.event.EventHandler
import javafx.event.ActionEvent
import javafx.scene.Scene

class UI extends Application {
  def start(stage: Stage): Unit = {
    stage.setTitle("Example of Scala - JavaFX application")
    val root = new VBox
    val stuff = root.getChildren
    val rect = new Rectangle(0, 0, 400, 100)
    val button = new Button("Press to change to: " + Color.RED.invert)

    stuff.addAll(button, rect)
    rect.setFill(Color.RED)
    button.setOnAction(new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        val oldc: Color = rect.getFill.asInstanceOf[Color]
        rect.setFill(oldc.invert)
        button.setText("Press to change to: " + oldc)
      }
    })
    button.setPrefSize(400, 100)
    stage.setScene(new Scene(root, 400, 200))
    stage.show
  }
}

object Main extends App {
  Application.launch(classOf[UI], args: _*)
}
