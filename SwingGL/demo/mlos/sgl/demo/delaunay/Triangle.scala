package mlos.sgl.demo.delaunay

import mlos.sgl.core.Geometry
import mlos.sgl.core.Vec2d

case class Triangle(val a: Vec2d, val b: Vec2d, val c: Vec2d) {

  var na: Triangle = null
  var nb: Triangle = null
  var nc: Triangle = null

  var children: List[Triangle] = List.empty

  def apply(v: Vertex) = v match {
    case Va => a
    case Vb => b
    case Vc => c
  }

  def apply(e: Edge) = e match {
    case Eab => na
    case Ebc => nb
    case Eca => nc
  }

  def update(e: Edge, t: Triangle) = e match {
    case Eab => na = t
    case Ebc => nb = t
    case Eca => nc = t
  }

  def segment(e: Edge) = e match {
    case Eab => (a, b)
    case Ebc => (b, c)
    case Eca => (c, a)
  }

  def normal(e: Edge): Vec2d = {
    val (p, q) = segment(e)
    Geometry.normal(p, q)
  }

  override def hashCode =
    31 * (a.hashCode + 31 * (b.hashCode + 31 * c.hashCode))

  def contains(v: Vec2d) = Geometry.insideTriangleClosure(v, a, b, c)
  
  def inside(v: Vec2d) = Geometry.insideTriangle(v, a, b, c)

  def containingEdge(v: Vec2d) = {
    if (Geometry.colinear(v, a, b))
      Eab
    else if (Geometry.colinear(v, b, c))
      Ebc
    else if (Geometry.colinear(v, c, a))
      Eca
    else null
  }

  def center = Geometry.center(a, b, c)

  def commonEdge(t: Triangle): Edge = {
    if (t == na) Eab
    else if (t == nb) Ebc
    else if (t == nc) Eca
    else null
  }

  def edge(p: Vec2d, q: Vec2d) = (p, q) match {
    case (`a`, `b`) => Eab
    case (`b`, `c`) => Ebc
    case (`c`, `a`) => Eca
  }

  def connect(e: Edge, t: Triangle) {
    this(e) = t
    if (t != null) {
      val (p, q) = segment(e)
      val otherEdge = t.edge(q, p)
      t(otherEdge) = this
    }
  }

  def connect(na: Triangle, nb: Triangle, nc: Triangle) {
    List(na, nb, nc) zip Edge.all foreach { p => connect(p._2, p._1) }
  }
  
  def doubleArea = Geometry.orient2d(a, b, c)
  
  override def toString = s"($a, $b, $c)"

  def points = (a, b, c)
  def pointSeq = Seq(a, b, c)
}
