package io.github.r_40021.nightcoreplayer

import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.input.DragEvent
import javafx.scene.media.{Media, MediaPlayer}

import java.io.File

class MovieFileDragDroppedEventHandler(movies: ObservableList[Movie]) extends EventHandler[DragEvent] {
  override def handle(t: DragEvent): Unit = {
    val db = t.getDragboard
    if (db.hasFiles) {
      db.getFiles.toArray(Array[File]()).toSeq.foreach { f =>
        val filePath = f.getAbsolutePath
        val fileName = f.getName
        val media = new Media(f.toURI.toString)
        val player = new MediaPlayer(media)
        player.setOnReady(new Runnable {
          def run(): Unit = {
            val time = formatTime(media.getDuration)
            val movie = Movie(System.currentTimeMillis(), fileName, time, filePath, media)
            while (movies.contains(movie)) {
              movie.setId(movie.getId + 1L)
            }
            movies.add(movie)
            player.dispose()
          }
        })
      }
    }
    t.consume()
  }
}
