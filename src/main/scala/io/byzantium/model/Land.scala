package io.byzantium.model

/**
 * @author Scott Mansfield
 */
object Land {
  def apply(label: String) = {
    new Land(label)
  }
}

class Land(label: String) {
  override def toString: String = {
    s"Land($label)"
  }
}
