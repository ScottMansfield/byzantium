package io.byzantium.model

/**
 * Marker class for things that exist on the board
 *
 * @author Scott Mansfield
 */
trait BoardItem extends Product with Serializable {
  def label: String
}

trait Position {
  def position: Point
}

trait Radius {
  def innerRadius: Double
  def outerRadius: Double
}

trait Rotatable {
  def rotation: Double
}

case class Point(x: Double, y: Double)

case class Component(label: String, position: Point, rotation: Double, desc: ComponentDesc)
  extends BoardItem
  with Position
  with Rotatable

case class NegativeSpace(label: String)
  extends BoardItem

case class ComponentDesc(label: String, dimensions: Dimensions, pins: Map[String, Pin])

case class Pin(label: String, position: Point, innerRadius: Double, outerRadius: Double, rotation: Double, shape: String)
  extends BoardItem
  with Position
  with Radius
  with Rotatable

case class Pad(label: String, position: Point)
  extends BoardItem
  with Position

case class ThruHolePad(label: String, position: Point, innerRadius: Double, outerRadius: Double)
  extends BoardItem
  with Position
  with Radius

case class Trace(label: String, vertices: Seq[Point])
  extends BoardItem

case class Via(label: String, position: Point, innerRadius: Double, outerRadius: Double)
  extends BoardItem
  with Position
  with Radius
