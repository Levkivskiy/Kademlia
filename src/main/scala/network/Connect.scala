package network

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString
import events.ServerEvents._
import node.NodeInform
import util.Serialize
import events.SocketEvents._

case class Connect(nodeInform: NodeInform, connectActor: ActorRef)

object Connect {
  implicit val ordering: Ordering[Connect] = Ordering.by {
    conn: Connect =>
      conn.nodeInform
  }
}

