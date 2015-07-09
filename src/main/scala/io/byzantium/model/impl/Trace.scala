package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object Trace {
  def apply(label: String) = {
    new Trace(label)
  }
}

class Trace(label: String)
  extends BoardItem {

}
