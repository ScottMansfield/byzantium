package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object Component {
  def apply(label: String, dimensions: Dimensions) = {
    new Component(label, dimensions)
  }
}

class Component(label: String, dimensions: Dimensions)
  extends BoardItem {

}
