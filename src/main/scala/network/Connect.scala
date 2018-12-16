package network

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString
import messeges.ServerEvents._
import node.NodeInform
import util.Serialize

case object Conn

class Connect(nodeInform: NodeInform) extends Actor with ActorLogging {
  import context.system


  def receive = {
    case Conn => IO(UdpConnected) ! UdpConnected.Connect(self, nodeInform.inetSocketAddress)
    case UdpConnected.Connected => {
      log.info(s"Conect operation to ${nodeInform.inetSocketAddress}")
      context.become(ready(sender()))
    }
  }
  def ready(connection: ActorRef):Receive = {
    case UdpConnected.Received(data) =>
      log.info(s"Socket ${nodeInform.inetSocketAddress} receive data " +
        s"${Serialize.deserialize(data.toArray)}")
    case SendMsg(msg) ⇒
      connection ! UdpConnected.Send(ByteString(Serialize.serialize(msg)))
    case UdpConnected.Disconnect =>
      connection ! UdpConnected.Disconnect
    case UdpConnected.Disconnected => context.stop(self)
  }
}
