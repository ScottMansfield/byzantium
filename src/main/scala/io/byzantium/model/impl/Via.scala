package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object Via {
  def apply(label: String) = {
    new Via(label)
  }
}

class Via(label: String)
  extends BoardItem {

}
