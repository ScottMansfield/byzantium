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
  def x: Double
  def y: Double
}

trait Radius {
  def innerRadius: Double
  def outerRadius: Double
}

trait Rotatable {
  def rotation: Double
}

case class Component(label: String, x: Double, y: Double, rotation: Double, desc: ComponentDesc)
  extends BoardItem
  with Position
  with Rotatable

case class NegativeSpace(label: String)
  extends BoardItem

case class ComponentDesc(label: String, dimensions: Dimensions, pins: Map[String, Pin])

case class Pin(label: String, x: Double, y: Double, innerRadius: Double, outerRadius: Double, rotation: Double, shape: String)
  extends BoardItem
  with Position
  with Radius
  with Rotatable

case class Pad(label: String, x: Double, y: Double)
  extends BoardItem
  with Position

case class ThruHolePad(label: String, x: Double, y: Double, innerRadius: Double, outerRadius: Double)
  extends BoardItem
  with Position
  with Radius

case class Trace(label: String)
  extends BoardItem

case class Via(label: String, x: Double, y: Double, innerRadius: Double, outerRadius: Double)
  extends BoardItem
  with Position
  with Radius