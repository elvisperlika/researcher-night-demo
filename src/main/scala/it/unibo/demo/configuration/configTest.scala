package it.unibo.demo.configuration

import it.unibo.demo.robot.WaveRobot
import scala.collection.mutable.ListBuffer

object configTest {
  def main(args: Array[String]): Unit = {
    val jsonString = os.read(os.pwd / "src"/"main"/"scala"/"it"/"unibo"/"demo"/"configuration"/"waveRobot.json")
    val data = ujson.read(jsonString)
    // create an empty list named robots
    var robots = ListBuffer[WaveRobot]()
    data.arr.map(i => {
      println(i("ip").str + " " + i("value").num.toInt)
      robots += WaveRobot(i("ip").str, i("value").num.toInt)
    })
  }
}
