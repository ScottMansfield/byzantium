package io.byzantium

import io.byzantium.importers.eagle.EagleImporter

/**
 * @author Scott Mansfield
 */
object Main {
  def main(args: Array[String]) {
    val brdGraph = EagleImporter("src/test/resources/ESP8266_BREAKOUT.brd").read()
  }
}
