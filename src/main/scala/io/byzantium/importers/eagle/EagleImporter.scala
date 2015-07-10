package io.byzantium.importers.eagle

import io.byzantium.model._

import scala.xml.{Elem, Node, XML}
import scalax.collection.edge._
import scalax.collection.mutable.Graph

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

      val pins = (node \ "pad" map { (node: Node) =>
        val label = node \@ "name"
        val x = (node \@ "x").toDouble
        val y = (node \@ "y").toDouble
        val innerRadius = (node \@ "drill").toDouble
        val outerRadius = (node \@ "diameter").toDouble
        val rotation = rotToDouble(node \@ "rot")
        val shape = node \@ "shape"

        label -> Pin(label, x, y, innerRadius, outerRadius, rotation, shape)
      }).toMap

      pkgName -> ComponentDesc(pkgName, Dimensions(), pins)
    }).toMap

    //packages foreach { println }

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
      val desc = packages.getOrElse(pkgref, ComponentDesc("", Dimensions(), Map[String, Pin]()))

      label -> Component(label, x, y, rotation, desc)
    }).toMap

    //elements foreach { println }

    val elemsAndPins = (elements flatMap { case (label, elem: Component) =>
      elem.desc.pins map { case (plabel, pin: Pin) =>
        (elem.label, pin.label) -> (elem, pin)
      }
    }).toMap

    // Create our graph
    // +~+=(elem1, elem2, elems*)(label) adds a new LHyperEdge
    // +=(edge) will add an edge
    val retval = Graph[BoardItem, LHyperEdge]()

    // Signals connect the different components together logically
    // The contactref elements signify which pads of which element are connected
    // They become the labelled hyper edges, soon to be replaced by traces
    //
    // signals
    //   signal(@name) <- edge label (becomes trace label)
    //     contactref(@pad, @element) <- actual connections
    doc \\ "signals" \ "signal" foreach { (node: Node) =>

      val label = node \@ "name"
      val contactrefs = node \ "contactref"

      // TODO: What if there's only one contactref?
      // probably just ignore the element

      val pins = contactrefs map { (ref) => elemsAndPins((ref \@ "element", ref \@ "pad")) }

      retval += LHyperEdge(pins)(label).asInstanceOf[LHyperEdge[BoardItem]]
    }

    retval
  }
}
