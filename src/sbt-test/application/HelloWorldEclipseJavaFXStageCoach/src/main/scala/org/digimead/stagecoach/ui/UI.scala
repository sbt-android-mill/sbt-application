/**
 * StageCoach example from JavaFX Pro 2
 * by James Weaver, Weiqi Gao, Stephen Chin, Dean Iverson, Johan Vos
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

package org.digimead.stagecoach.ui

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.VPos
import javafx.scene.GroupBuilder
import javafx.scene.SceneBuilder
import javafx.scene.control.ButtonBuilder
import javafx.scene.control.CheckBoxBuilder
import javafx.scene.control.Label
import javafx.scene.control.TextFieldBuilder
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBoxBuilder
import javafx.scene.layout.VBoxBuilder
import javafx.scene.paint.Color
import javafx.scene.shape.RectangleBuilder
import javafx.scene.text.TextBuilder
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.WindowEvent
import javafx.util.Builder
import javafx.scene.text.Text
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.Scene
import javafx.scene.Group
import javafx.scene.shape.Rectangle
import javafx.scene.layout.VBox
import javafx.scene.layout.HBox
import javafx.scene.control.Button

/**
 * Main class of StageCoach
 * There are only 3 mutable variables.
 */
class UI extends Application {
  var stageStyle = StageStyle.DECORATED
  var dragAnchorX: Double = 0
  var dragAnchorY: Double = 0
  val title = new SimpleStringProperty()
  lazy val textStageX = <>[TextBuilder[_], Text](TextBuilder.create()) { b =>
    b.textOrigin(VPos.TOP)
    b.build()
  }
  lazy val textStageY = <>[TextBuilder[_], Text](TextBuilder.create()) { b =>
    b.textOrigin(VPos.TOP)
    b.build()
  }
  lazy val textStageW = <>[TextBuilder[_], Text](TextBuilder.create()) { b =>
    b.textOrigin(VPos.TOP)
    b.build()
  }
  lazy val textStageH = <>[TextBuilder[_], Text](TextBuilder.create()) { b =>
    b.textOrigin(VPos.TOP)
    b.build()
  }
  lazy val textStageF = <>[TextBuilder[_], Text](TextBuilder.create()) { b =>
    b.textOrigin(VPos.TOP)
    b.build()
  }
  lazy val checkBoxResizable = <>[CheckBoxBuilder[_], CheckBox](CheckBoxBuilder.create()) { b =>
    b.text("resizable")
    b.disable(stageStyle == StageStyle.TRANSPARENT ||
      stageStyle == StageStyle.UNDECORATED)
    b.build()
  }
  lazy val checkBoxFullScreen = <>[CheckBoxBuilder[_], CheckBox](CheckBoxBuilder.create()) { b =>
    b.text("fullScreen")
    b.build()
  }
  lazy val titleTextField = <>[TextFieldBuilder[_], TextField](TextFieldBuilder.create()) { b =>
    b.text("Stage Coach")
    b.prefColumnCount(15)
    b.build()
  }
  def start(stage: Stage): Unit = {
    val unnamedParams = getParameters().getUnnamed()
    if (unnamedParams.size() > 0) {
      val stageStyleParam = unnamedParams.get(0)
      if (stageStyleParam.equalsIgnoreCase("transparent")) {
        stageStyle = StageStyle.TRANSPARENT
      } else if (stageStyleParam.equalsIgnoreCase("undecorated")) {
        stageStyle = StageStyle.UNDECORATED
      } else if (stageStyleParam.equalsIgnoreCase("utility")) {
        stageStyle = StageStyle.UTILITY
      }
    }

    val scene = <>[SceneBuilder[_], Scene](SceneBuilder.create()) { b =>
      b.width(270)
      b.height(370)
      b.fill(Color.TRANSPARENT)
      b.root(<>[GroupBuilder[_], Group](GroupBuilder.create()) { b =>
        b.children(
          <>[RectangleBuilder[_], Rectangle](RectangleBuilder.create()) { b =>
            b.width(250)
            b.height(350)
            b.arcWidth(50)
            b.arcHeight(50)
            b.fill(Color.SKYBLUE)
            b.build()
          },
          <>[VBoxBuilder[_], VBox](VBoxBuilder.create) { b =>
            b.layoutX(30)
            b.layoutY(20)
            b.spacing(10)
            b.children(
              textStageX,
              textStageY,
              textStageW,
              textStageH,
              textStageF,
              checkBoxResizable,
              checkBoxFullScreen,
              <>[HBoxBuilder[_], HBox](HBoxBuilder.create()) { b =>
                b.spacing(10)
                b.children(new Label("title:"), titleTextField)
                b.build()
              },
              <>[ButtonBuilder[_], Button](ButtonBuilder.create()) { b =>
                b.text("toBack()")
                b.onAction(new EventHandler[ActionEvent]() {
                  override def handle(e: ActionEvent) = stage.toBack()
                })
                b.build()
              },
              <>[ButtonBuilder[_], Button](ButtonBuilder.create()) { b =>
                b.text("toFront()")
                b.onAction(new EventHandler[ActionEvent]() {
                  override def handle(e: ActionEvent) = stage.toFront()
                })
                b.build()
              },
              <>[ButtonBuilder[_], Button](ButtonBuilder.create()) { b =>
                b.text("close()")
                b.onAction(new EventHandler[ActionEvent]() {
                  override def handle(e: ActionEvent) = stage.close()
                })
                b.build()
              })
            b.build()
          })
        val rootGroup = b.build()
        // When mouse button is pressed, save the initial position of screen
        rootGroup.setOnMousePressed(new EventHandler[MouseEvent]() {
          def handle(me: MouseEvent) {
            dragAnchorX = me.getScreenX() - stage.getX()
            dragAnchorY = me.getScreenY() - stage.getY()
          }
        })
        // When screen is dragged, translate it accordingly
        rootGroup.setOnMouseDragged(new EventHandler[MouseEvent]() {
          def handle(me: MouseEvent) {
            stage.setX(me.getScreenX() - dragAnchorX)
            stage.setY(me.getScreenY() - dragAnchorY)
          }
        })
        rootGroup
      })
      b.build()
    }

    textStageX.textProperty().bind(new SimpleStringProperty("x: ")
      .concat(stage.xProperty().asString()))
    textStageY.textProperty().bind(new SimpleStringProperty("y: ")
      .concat(stage.yProperty().asString()))
    textStageW.textProperty().bind(new SimpleStringProperty("width: ")
      .concat(stage.widthProperty().asString()))
    textStageH.textProperty().bind(new SimpleStringProperty("height: ")
      .concat(stage.heightProperty().asString()))
    textStageF.textProperty().bind(new SimpleStringProperty("focused: ")
      .concat(stage.focusedProperty().asString()))
    stage.setResizable(true)
    checkBoxResizable.selectedProperty.bindBidirectional(stage.resizableProperty())
    checkBoxFullScreen.selectedProperty.addListener(new ChangeListener[Any]() {
      def changed(ov: ObservableValue[_], oldValue: Any, newValue: Any) =
        stage.setFullScreen(checkBoxFullScreen.selectedProperty.getValue())
    })
    title.bind(titleTextField.textProperty())
    stage.setScene(scene)
    stage.titleProperty().bind(title)
    stage.initStyle(stageStyle)
    stage.setOnCloseRequest(new EventHandler[WindowEvent]() {
      def handle(we: WindowEvent) = System.out.println("Stage is closing")
    })
    stage.show()
    val primScreenBounds = Screen.getPrimary.getVisualBounds()
    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2)
    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4)
  }
  /**
   * little wrapper that negate effect of Java <B extends T<B>>
   * and prevent to crush Scala compiler
   */
  def <>[T, S](b: Builder[_])(f: T => S)(implicit m: Manifest[T]) = f(b.asInstanceOf[T])
}

object UI extends App {
  Application.launch(classOf[UI], args: _*)
}
