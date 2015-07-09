package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object NegativeSpace {
  def apply(label: String) = {
    new NegativeSpace(label)
  }
}

class NegativeSpace(label: String)
  extends BoardItem {

}
