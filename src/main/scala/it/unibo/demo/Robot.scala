package it.unibo.demo

import java.net.URLEncoder

trait Robot:
  def id: Int
  def spinRight(): Unit
  def spinLeft(): Unit
  def forward(): Unit
  def backward(): Unit
  def nop(): Unit

class WaveRobot(ip: String, val id: Int) extends Robot:
  def spinRight(): Unit = requests.get(url = s"http://$ip/js?json=${Command(0.2, -0.2).toJson}")
  def spinLeft(): Unit = requests.get(url = s"http://$ip/js?json=${Command(-0.2, 0.2).toJson}")
  def forward(): Unit = requests.get(url = s"http://$ip/js?json=${Command(0.10, 0.10).toJson}")
  def backward(): Unit = requests.get(url = s"http://$ip/js?json=${Command(-0.10, -0.10).toJson}")
  override def nop(): Unit = requests.get(url = s"http://$ip/js?json=${Command(-0, -0).toJson}")
  private class Command(left: Double, right: Double):
    def toJson: String = URLEncoder.encode(s"""{"T":1, "L":$left, "R":$right}""")
