package io.byzantium

import io.byzantium.importers.eagle.EagleImporter
import io.byzantium.model.{BoardItem, Trace}

import scalax.collection.edge.LHyperEdge
import scalax.collection.immutable.Graph

/**
 * @author Scott Mansfield
 */
object Main {
  def main(args: Array[String]) {
    val brdGraph = EagleImporter("src/test/resources/ESP8266_BREAKOUT.brd").read()

    step(brdGraph)

    //brdGraph.edges foreach { x => println(x); println() }

    //brdGraph.nodes foreach { println }
  }

  /**
   * Generates all the possible next steps from the current state
   * 
   * @param state The current state of the board as a graph
   * @return A Seq that contains all the next steps to try
   */
  def step(state: Graph[BoardItem, LHyperEdge]): Seq[Graph[BoardItem, LHyperEdge]] = {

    val traces = state.nodes map {
      _.value.asInstanceOf[(BoardItem, BoardItem)]._1
    } filter {
      _.isInstanceOf[Trace]
    }

    println("traces")

    traces foreach { println }

    Seq(Graph.from(traces, Seq.empty[LHyperEdge[BoardItem]]))
  }
}
