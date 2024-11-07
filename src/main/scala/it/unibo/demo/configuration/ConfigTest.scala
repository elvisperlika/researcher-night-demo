package it.unibo.demo.configuration

import it.unibo.demo.robot.WaveRobot
import upickle.default._

import scala.collection.mutable.ListBuffer

object ConfigTest {
  def main(args: Array[String]): Unit = {
    val jsonString = os.read(os.pwd / "src"/"main"/"scala"/"it"/"unibo"/"demo"/"configuration"/"waveRobot.json")
    val data = ujson.read(jsonString)
    // create an empty list named robots
    println(data)
    val waveRobot: WaveRobot = read[WaveRobot]("""{"ip": "192.168.8.10","id": 6}""")
    val waveRobots = read[Seq[WaveRobot]](data)
    waveRobots.foreach(println)
    val list = read[Seq[WaveRobot]](data)
    println(list)
    /*
    var robots = ListBuffer[WaveRobot]()
    data.arr.map(i => {
      println(i("ip").str + " " + i("value").num.toInt)
      robots += WaveRobot(i("ip").str, i("value").num.toInt)
    }) */
  }
}
