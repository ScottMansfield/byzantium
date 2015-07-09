package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object Pin {
  def apply(name: String) = {
    new Pin(name)
  }
}

class Pin(name: String)
  extends BoardItem {

}
