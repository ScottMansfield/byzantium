package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object ThruHolePad {
  def apply(label: String, innerRadius: Double, outerRadius: Double) = {
    new ThruHolePad(label, innerRadius, outerRadius)
  }
}

class ThruHolePad(label: String, innerRadius: Double, outerRadius: Double)
  extends BoardItem {

}
