package it.unibo.demo.scenarios

import it.unibo.demo.robot.Actuation

class RobotRotation extends BaseDemo:
  override def main(): Actuation =
    Actuation.Rotation(normalize(0.5, 0.5))

