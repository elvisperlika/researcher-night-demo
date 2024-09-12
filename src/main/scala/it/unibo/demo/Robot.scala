package it.unibo.demo

import java.net.URLEncoder

trait Robot:
  def id: Int
  def spin(): Unit
  def forward(): Unit

class WaveRobot(ip: String, val id: Int) extends Robot:
  def spin(): Unit = requests.get(url = s"http://$ip/js?json=${Command(0.2, -0.2).toJson}")
  def forward(): Unit = requests.get(url = s"http://$ip/js?json=${Command(0.2, 0.2).toJson}")
  def backward(): Unit = requests.get(url = s"http://$ip/js?json=${Command(-0.2, -0.2).toJson}")
  private class Command(left: Double, right: Double):
    def toJson: String = URLEncoder.encode(s"""{"T":1, "L":$left, "R":$right}""")