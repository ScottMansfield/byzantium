package io.byzantium.importers.eagle

import io.byzantium.model.Land

import scala.xml.{Node, XML, Elem}
import scalax.collection.immutable.Graph

/**
 * @author Scott Mansfield
 */
object EagleImporter {
  def apply(filename: String) = {
    val doc = XML.loadFile(filename)
    new EagleImporter(doc)
  }
}

class EagleImporter(doc: Elem) {
  def read(): Unit = {
    // grab the packages to create the Component instances
    // They have:
    //   The dimensions (<wire>)
    //   The pads (<pad>)
    val packages = doc \\ "packages" \ "package"

    packages foreach { (node: Node) =>
      println()
      println(node \ "@name")

      node \\ "pad" foreach { (node: Node) =>
        println(Land((node \ "@name").text))
      }
    }

    // Signals connect the different components together logically
    val signals = doc \\ "signals" \ "signal"

    signals foreach { (node: Node) =>
      println()
      println(node \ "@name")

      node \\ "contactref" foreach {
        println
      }
    }
  }
}
