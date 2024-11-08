package it.unibo.demo

import it.unibo.core.UpdateLoop
import it.unibo.core.aggregate.AggregateIncarnation.{AggregateProgram, ID}
import it.unibo.mock.AggregateServiceExample.stage
import it.unibo.core.aggregate.AggregateOrchestrator
import it.unibo.demo.camera.CameraProvider
import it.unibo.demo.robot.{Actuation, RobotUpdate, ThymioRobot, WaveRobot}
import it.unibo.demo.scenarios.*
import it.unibo.mock.{MagnifierPolicy, SimpleRender}
import scalafx.application.JFXApp3
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import view.fx.{NeighborhoodPanel, NodeStyle, WorldPanel}
import it.unibo.utils.Position.{*, given}
import org.bytedeco.javacpp.Loader
import org.bytedeco.opencv.opencv_java
import upickle.default.*

import java.awt.Robot
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

object DemoProgramEntrypoint extends JFXApp3 {
  try Loader.load(classOf[opencv_java])
  catch case e: Exception => e.printStackTrace()
  private val agentsNeighborhoodRadius = 200
  private val nodeGuiSize = 4
  private val aggregateProgram: AggregateProgram = CircleFormation(0.4, 1, 0.1)
  //FollowTheLeaderRotating(0)
  // FollowTheLeaderRotating(6)
  // AllRobotsAlignedProgram()
  // LineFormation(0.4, 5, 0.05)
  // CircleFormation(0.4, 3, 0.1)

  private val jsonString =
    os.read(os.pwd / "src" / "main" / "scala" / "it" / "unibo" / "demo" / "configuration" / "waveRobot.json")
  private val waveData = ujson.read(jsonString)
  private val robots = read[Seq[WaveRobot]](waveData).toList
  private val waveList = robots.map(_.id)

  private val thymioRobotJson =
    os.read(os.pwd / "src" / "main" / "scala" / "it" / "unibo" / "demo" / "configuration" / "thymioRobot.json")
  private val thymioData = ujson.read(thymioRobotJson)
  private val thymioRobots = read[Seq[ThymioRobot]](thymioData).toList
  private val thymioList = thymioRobots.map(_.id)

  private val list = thymioRobots ++ robots
  println(list)

  private val provider = CameraProvider(
    list.map(_.id),
    10,
    0
  )

  /*
  private val robots = List(
    WaveRobot("192.168.8.10", 6),
    WaveRobot("192.168.8.11", 1),
    WaveRobot("192.168.8.12", 2),
    WaveRobot("192.168.8.13", 3),
    WaveRobot("192.168.8.14", 5)
  ) */

  private val update = RobotUpdate(list, 0.25)

  override def start(): Unit =
    val aggregateOrchestrator =
      AggregateOrchestrator[Position, Info, Actuation](list.map(_.id).toSet, aggregateProgram)
    val magnifier = MagnifierPolicy.translateAndScale((200, 300), 400)
    val basePane = Pane()
    val guiPane = Pane()
    val neighborhoodPane = Pane()
    basePane.children.addAll(neighborhoodPane, guiPane)
    val worldPane = WorldPanel(guiPane, NodeStyle(nodeGuiSize, nodeGuiSize * 2, Color.Blue))
    val neighbouringPane = NeighborhoodPanel(guiPane, neighborhoodPane)
    val render = SimpleRender(worldPane, neighbouringPane, magnifier)

    UpdateLoop.loop(0)(
      provider,
      aggregateOrchestrator,
      update,
      render
    )

    stage = new JFXApp3.PrimaryStage:
      title = "Aggregate Service Example"
      scene = new scalafx.scene.Scene:
        content = basePane
      width = 800
      height = 800

  private def randomAgents(howMany: Int, maxPosition: Int): Map[ID, (Double, Double)] =
    val random = new scala.util.Random
    (1 to howMany).map { i =>
      i -> (random.nextDouble() * maxPosition, random.nextDouble() * maxPosition)
    }.toMap
}
