package io.github.r_40021.nightcoreplayer

import io.github.r_40021.nightcoreplayer.SizeConstants._
import io.github.r_40021.nightcoreplayer.ToolbarButtonCreator.createButton
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.{Label, TableView}
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.stage.Stage
import javafx.util.Duration

import java.lang

object ToolbarCreator {
  def create(mediaView: MediaView, tableView: TableView[Movie], timeLabel: Label, scene: Scene, primaryStage: Stage): HBox = {
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")
    toolBar.visibleProperty().bind(primaryStage.fullScreenProperty().not())
    toolBar.managedProperty().bind(primaryStage.fullScreenProperty().not())

    // first button
    val firstButton = createButton("first.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playPre(tableView, mediaView, timeLabel)
        }
      }
    })

    // back button
    val backButton = createButton("back.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000))
          )
        }
      }
    })

    // play button
    val playButton = createButton("play.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        val selectionModel = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionModel.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    })

    // pause button
    val pauseButton = createButton("pause.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) mediaView.getMediaPlayer.pause()
      }
    })

    // forward button
    val forwardButton = createButton("forward.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          val player = mediaView.getMediaPlayer
          val totalDuration = player.getTotalDuration
          val currentTime = player.getCurrentTime
          if (totalDuration.greaterThan(currentTime.add(new Duration(10000)))) {
            MoviePlayer.playForward(mediaView, 10000)
          } else {
            MoviePlayer.playForward(mediaView,
              Math.ceil(totalDuration.toMillis - currentTime.toMillis - 100).toInt)
          }
        }
      }
    })

    // last button
    val lastButton = createButton("last.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playNext(tableView, mediaView, timeLabel)
        }
      }
    })

    // fullscreen button
    val fullscreenButton = createButton("fullscreen.png", new EventHandler[ActionEvent]() {
      override def handle(t: ActionEvent): Unit = {
        primaryStage.setFullScreen(true)
        mediaView.fitHeightProperty().unbind()
        mediaView.fitHeightProperty().bind(scene.heightProperty())
        mediaView.fitWidthProperty().unbind()
        mediaView.fitWidthProperty().bind(scene.widthProperty())
      }
    })
    primaryStage.fullScreenProperty().addListener(new ChangeListener[java.lang.Boolean] {
      override def changed(observable: ObservableValue[_ <: java.lang.Boolean], oldValue: java.lang.Boolean, newValue: java.lang.Boolean): Unit =
        if (!newValue) {
          mediaView.fitHeightProperty().unbind()
          mediaView.fitWidthProperty().unbind()
          mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
          mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
        }
    })
    toolBar.getChildren.addAll(firstButton, backButton, playButton, pauseButton, forwardButton, lastButton, fullscreenButton, timeLabel)
    toolBar
  }
}
