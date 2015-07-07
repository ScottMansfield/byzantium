package io.byzantium.model

/**
 * @author Scott Mansfield
 */
object ThruHolePad {
  def apply(label: String, innerRadius: Double, outerRadius: Double) = {
    new ThruHolePad(label, innerRadius, outerRadius)
  }
}

class ThruHolePad(label: String, innerRadius: Double, outerRadius: Double) {

}
