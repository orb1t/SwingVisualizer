package mlos.sgl.demo

import java.awt.Color

import mlos.sgl.canvas.CanvasPoint
import mlos.sgl.core.Vec2d

trait CanSignalPoint extends AbstractVisualizer {

  val focus: CanvasPoint = new CanvasPoint

  def showFocusPoint() = scene addObject focus
  def hideFocusPoint() = scene removeObject focus

  def signalPoint(p: Vec2d, c: Color) {
    focus setPoint p
    focus setColor c
    focus setBorderColor Color.black
    focus setBorderSize 1
    focus setSize 18
    focus setZ 0.2
    showFocusPoint()
    focus signalUpdate()
    delay(200)
    focus setSize 12
    focus signalUpdate()
  }

  
}