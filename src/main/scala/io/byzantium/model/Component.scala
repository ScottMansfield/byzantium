package io.byzantium.model

/**
 * @author Scott Mansfield
 */
object Component {
  def apply(label: String, dimensions: Dimensions) = {
    new Component(label, dimensions)
  }
}

class Component(label: String, dimensions: Dimensions) {

}
