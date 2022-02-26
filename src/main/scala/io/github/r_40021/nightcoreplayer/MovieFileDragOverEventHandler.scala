package io.github.r_40021.nightcoreplayer

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.{DragEvent, TransferMode}

class MovieFileDragOverEventHandler(scene: Scene) extends EventHandler[DragEvent]{
  override def handle(t: DragEvent): Unit = {
    if (t.getGestureSource != scene &&
    t.getDragboard.hasFiles) {
      t.acceptTransferModes(TransferMode.COPY_OR_MOVE: _*)
    }
  }
}
