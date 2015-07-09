package io.byzantium.model.impl

import io.byzantium.model.BoardItem

/**
 * @author Scott Mansfield
 */
object Pad {
  def apply(label: String) = {
    new Pad(label)
  }
}

class Pad(label: String)
  extends BoardItem {
  override def toString: String = {
    s"Land($label)"
  }
}
