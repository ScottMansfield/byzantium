package io.byzantium.importers.eagle

import io.byzantium.model._

import scala.xml.{Elem, Node, XML}
import scalax.collection.edge.LHyperEdge
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
  // <!ENTITY % Rotation "CDATA"> <!-- rotation of an object; allowed range: [MSR]0..359.9 -->
  // I don't know what MSR means, I assume R is right, but M and S are mysteries
  def rotToDouble(rot: String): Double = {
    rot.tail.toDouble
  }

  def read(): Graph[BoardItem, LHyperEdge] = {
    // grab the packages to create the Component instances
    // package(@name) <- label
    //   pad(@name, @x, @y, @drill, @diameter, @shape, @rot)
    //    ^ label, position, hole size?, outer contact diameter?, shape?, rotation of shape
    val packages = (doc \\ "packages" \ "package" map { node =>
      val pkgName = node \@ "name"

      val pads = node \ "pad" map { (node: Node) =>
        val label = node \@ "name"
        val x = (node \@ "x").toDouble
        val y = (node \@ "y").toDouble
        val innerRadius = (node \@ "drill").toDouble
        val outerRadius = (node \@ "diameter").toDouble
        val rotation = rotToDouble(node \@ "rot")
        val shape = node \@ "shape"

        Pad(label, x, y, innerRadius, outerRadius, rotation, shape)
      }

      pkgName -> ComponentDesc(pkgName, Dimensions(), pads)
    }).toMap

    packages foreach { println }

    // Elements are instances of the packages above
    // elements
    //   element(@name, @package, @x, @y, @rot)
    //    ^ label, component? reference, position, rotation of package
    val elements = (doc \\ "elements" \ "element" map { (node: Node) =>
      val label = node \@ "name"
      val x = (node \@ "x").toDouble
      val y = (node \@ "y").toDouble
      val rotation = rotToDouble(node \@ "rot")
      val pkgref = node \@ "package"
      val desc = packages.getOrElse(pkgref, ComponentDesc("", Dimensions(), Seq()))

      label -> Component(label, x, y, rotation, desc)
    }).toMap

    elements foreach { println }

    // Signals connect the different components together logically
    // The contactref elements signify which pads of which element are connected
    // They become the labelled hyper edges, soon to be replaced by traces
    //
    // signals
    //   signal(@name) <- edge label (becomes trace label)
    //     contactref(@pad, @element) <- actual connections
    val signals = doc \\ "signals" \ "signal"

    signals foreach { (node: Node) =>
      println()
      println(node \@ "name")

      node \\ "contactref" foreach {
        println
      }
    }

    val edge = LHyperEdge(Trace("trace label"), Pin("pad label"), Pin("pad 2 label"))("edge 1")

    Graph(edge)
  }
}
