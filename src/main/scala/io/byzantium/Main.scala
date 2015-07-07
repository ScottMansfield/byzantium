package io.byzantium

import io.byzantium.importers.eagle.EagleImporter

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.edge.LHyperEdge

// Will want to use LHyperEdge to connect different PCB bits
// not sure about the negative spaces

/**
 * @author Scott Mansfield
 */
object Main {
  def main(args: Array[String]) {
    EagleImporter("src/test/resources/ESP8266_BREAKOUT.brd").read()
  }
}
