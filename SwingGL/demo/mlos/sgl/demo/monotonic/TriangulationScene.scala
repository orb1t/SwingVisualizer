package mlos.sgl.demo.monotonic

import mlos.sgl.Scene
import scala.actors.threadpool.Executors
import mlos.sgl.ui.InputAdapter
import java.awt.event.KeyEvent
import scala.collection.JavaConversions._
import mlos.sgl.canvas.CanvasPolygon
import mlos.sgl.core.Polygon
import mlos.sgl.App
import mlos.sgl.AlgorithmScene

class TriangulationScene(s: String) extends AlgorithmScene(s) { scene =>

  val handler = new InputAdapter {
    override def keyPressed(e: KeyEvent) {
      async {
        e.getKeyCode match {
          case KeyEvent.VK_F5 =>
            println("sdfsdf")
            extractPolys() foreach { p =>
              val classifier = new ClassifyVertices(p.vs)
              classifier.setListener(new ClassificationVisualizer(scene))
              val types = classifier.classify()
              new Splitter(p, types, new SplitVisualizer(scene)).run()
            }

          case KeyEvent.VK_F6 =>
            extractPolys() foreach { p =>
              val view = new TriangulationVisualizer(scene)
              new Triangulate(p, view).run()
            }
        }
      }
    }
  }
  handlerStack.push(handler)

  def extractPolys() = extract(classOf[CanvasPolygon]) map { p =>
    p.setOpaque(true)
    new Polygon(p.getPoints())
  }
}

object TriangulationScene extends scala.App {
  App.create(new TriangulationScene("triangulation"))
}