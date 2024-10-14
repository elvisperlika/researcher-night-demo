package it.unibo.demo.robot

import java.net.URLEncoder
import upickle.default.ReadWriter
trait Robot:
  def id: Int
  def spinRight(): Unit
  def spinLeft(): Unit
  def forward(): Unit
  def backward(): Unit
  def nop(): Unit
  def intensities(left: Double, right: Double): Unit

case class WaveRobot(ip: String, val id: Int) extends Robot derives ReadWriter:
  
  private var lastCommandWasNoOp = false
  def spinRight(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(0.20, -0.20).toJson}")
    lastCommandWasNoOp = false
  def spinLeft(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(-0.20, 0.20).toJson}")
    lastCommandWasNoOp = false
  def forward(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(-0.16, -0.16).toJson}")
    lastCommandWasNoOp = false
  def backward(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(0.16, 0.16).toJson}")
    lastCommandWasNoOp = false
  def intensities(left: Double, right: Double): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(left, right).toJson}")
    lastCommandWasNoOp = false
  override def nop(): Unit =
    if !lastCommandWasNoOp then requests.get(url = s"http://$ip/js?json=${Command(0, 0).toJson}")
    else ()
    lastCommandWasNoOp = true
  private class Command(left: Double, right: Double):
    def toJson: String = URLEncoder.encode(s"""{"T":1, "L":$left, "R":$right}""")

case class TymioRobot(val id: Int) extends Robot derives ReadWriter:
  override def spinRight(): Unit = ???

  override def spinLeft(): Unit = ???

  override def forward(): Unit =
    requests.get(url = s"localhost:52000/thymio?json=${Command(id, +20, +20).toJson}")

  override def backward(): Unit = ???

  override def nop(): Unit = ???

  override def intensities(left: Double, right: Double): Unit = ???

  private class Command(id: Int, left: Double, right: Double):
    def toJson: String = URLEncoder.encode(s"""{"id":$id, "l":$left, "r":$right}""")
  
  