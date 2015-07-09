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

case class Component(label: String, dimensions: Dimensions)
  extends BoardItem

case class NegativeSpace(label: String)
  extends BoardItem

case class Pad(label: String)
  extends BoardItem

case class Pin(label: String)
  extends BoardItem

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
