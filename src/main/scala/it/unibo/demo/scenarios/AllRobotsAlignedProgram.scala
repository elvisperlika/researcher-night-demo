package it.unibo.demo.scenarios

import it.unibo.demo.robot.Actuation

class AllRobotsAlignedProgram extends BaseDemo:
  override def main(): Actuation = Actuation.Rotation(normalize((1.0, 0.0)))
