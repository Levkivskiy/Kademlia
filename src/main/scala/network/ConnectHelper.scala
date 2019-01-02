package network

import akka.actor.{Actor, ActorLogging}
import messeges.SocketEvents
import messeges.SocketEvents._
import util.GlobalConfig._

import scala.annotation.tailrec

class ConnectHelper {

  var isServerStart = false

  def startWaitConn(): Boolean = _WaitStartServer(attemptsToConnect)

  def serverWasStarting(): Unit = isServerStart = true

  @tailrec
  private def _WaitStartServer(attemptsLeft: Int): Boolean = {
    if (isServerStart)
      true
    else {
      attemptsLeft match {
        case 0 => false
        case left: Int =>
          Thread.sleep(timeBreakToConnectSeconds * 1000)
          _WaitStartServer(left - 1)
      }
    }
  }
}
