package io.byzantium.importers.eagle

import io.byzantium.model._

import scala.collection.mutable
import scala.xml.{Elem, Node, XML}
import scalax.collection.GraphPredef.Param
import scalax.collection.edge._
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
    val packages = getPackages
    val elemsAndPins = getElemsAndPins(packages)

    // Create our graph builder
    implicit val config = Graph.defaultConfig
    val retval = Graph.newBuilder[BoardItem, LHyperEdge]

    buildSignals(elemsAndPins, retval)

    retval.result()
  }

  /**
   * Builds the graph by connecting different components together with graph edges
   *
   * Signals connect the different components together logically
   * The contactref elements signify which pads of which element are connected
   * They become the labelled hyper edges, soon to be replaced by traces
   *
   * signals
   *   signal(@name) <- edge label (becomes trace label)
   *     contactref(@pad, @element) <- actual connections
   *     
   * @param elemsAndPins Map of element and pin names for connecting
   * @param graphBuilder The graph builder that's added to
   */
  def buildSignals(elemsAndPins: Map[(String, String), (Component, Pin)], graphBuilder: mutable.Builder[Param[BoardItem, LHyperEdge], Graph[BoardItem, LHyperEdge]]) {

     
    doc \\ "signals" \ "signal" foreach { (node: Node) =>

      val label = node \@ "name"
      val contactrefs = node \ "contactref"

      // TODO: What if there's only one contactref?
      // probably just ignore the element

      val pins = contactrefs map { (ref) => elemsAndPins((ref \@ "element", ref \@ "pad"))}

      graphBuilder += LHyperEdge(pins)(label).asInstanceOf[LHyperEdge[BoardItem]]
    }
  }

  /**
   * Elements are instances of the packages above
   *
   *  elements
   *    element(@name, @package, @x, @y, @rot)
   *     ^ label, component? reference, position, rotation of package
   *
   * @param packages A map from component (package) ID to description
   * @return A map of (element label, pin label) -> (element, pin)
   */
  def getElemsAndPins(packages: Map[String, ComponentDesc]): Map[(String, String), (Component, Pin)] = {
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

      label -> Component(label, Point(x, y), rotation, desc)
    }).toMap

    (elements flatMap { case (label, elem: Component) =>
      elem.desc.pins map { case (plabel, pin: Pin) =>
        (elem.label, pin.label) ->(elem, pin)
      }
    }).toMap
  }

  /**
   * grab the packages to create the Component instances
   *
   * package(@name) <- label
   *   pad(@name, @x, @y, @drill, @diameter, @shape, @rot)
   *    ^ label, position, hole size?, outer contact diameter?, shape?, rotation of shape
   *
   * @return A Map from package identifier to [[ComponentDesc]]
   */
  def getPackages: Map[String, ComponentDesc] = {
    (doc \\ "packages" \ "package" map { node =>
      val pkgName = node \@ "name"

      val pins = (node \ "pad" map { (node: Node) =>
        val label = node \@ "name"
        val x = (node \@ "x").toDouble
        val y = (node \@ "y").toDouble
        val innerRadius = (node \@ "drill").toDouble
        val outerRadius = (node \@ "diameter").toDouble
        val rotation = rotToDouble(node \@ "rot")
        val shape = node \@ "shape"

        label -> Pin(label, Point(x, y), innerRadius, outerRadius, rotation, shape)
      }).toMap

      pkgName -> ComponentDesc(pkgName, Dimensions(), pins)
    }).toMap
  }
}
