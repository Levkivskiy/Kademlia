package network

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString
import messeges.ServerEvents._
import node.NodeInform
import util.Serialize
import messeges.SocketEvents._

class ConnectActor(nodeInform: NodeInform, connListener: ConnectHelper) extends Actor with ActorLogging {

  import context.system

  def receive = {
    case StartConnect => IO(UdpConnected) ! UdpConnected.Connect(self, nodeInform.inetSocketAddress)
    case UdpConnected.Connected => {
      log.info(s"Connect operation to ${nodeInform.inetSocketAddress}")
      //connListener ! ConnectedSuccess
      connListener.serverWasStarting()
      context.become(ready(sender()))
    }
  }

  def ready(connection: ActorRef): Receive = {
    case GetNodeInform => sender() ! nodeInform
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
