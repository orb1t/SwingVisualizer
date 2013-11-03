package mlos.sgl.demo.delounay

import Edge.Ea
import Edge.Eb
import Edge.Ec
import Edge.Edge
import Vertex.Va
import Vertex.Vb
import Vertex.Vc
import Vertex.Vertex
import mlos.sgl.core.Geometry
import mlos.sgl.core.Vec2d


case class Triangle(val a: Vec2d, val b: Vec2d, val c: Vec2d,
  var na: Triangle, var nb: Triangle, var nc: Triangle) {

  def apply(v: Vertex) = v match {
    case Va => a
    case Vb => b
    case Vc => c
  }

  def n(e: Edge) = e match {
    case Ea => na
    case Eb => nb
    case Ec => nc
  }

  def update(e: Edge, t: Triangle) = e match {
    case Ea => na = t
    case Eb => nb = t
    case Ec => nc = t
  }

  def apply(e: Edge) = e match {
    case Ea => (a, b)
    case Eb => (b, c)
    case Ec => (c, a)
  }

  def normal(e: Edge): Vec2d = {
    val (p, q) = this(e)
    Geometry.normal(p, q)
  }
  
  override def hashCode = 
    31 * (a.hashCode + 31 * (b.hashCode + 31 * c.hashCode))

  def contains(v: Vec2d) = Geometry.inTriangle(v, a, b, c)

  def center = Geometry.center(a, b, c)

  def replaceNeighbour(t: Triangle, old: Triangle) {
    if (old eq na)
      na = t
    else if (old eq nb)
      nb = t
    else if (old eq nc)
      nc = t
    else throw new RuntimeException("Kurwa noo")
  }

  def opposite(a: Vec2d, b: Vec2d) =
    if (a == this.a && b == this.b)
      c
    else if (a == this.b && b == this.c)
      a
    else if (a == this.c && b == this.a)
      b

  def points = (a, b, c)

}