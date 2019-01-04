package node

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.{IO, Udp}
import akka.util.ByteString
import ident.KadId
import events.ServerEvents.Bootstrap
import util.Serialize


class Node(nodeInform: NodeInform) extends Actor with ActorLogging {

  val bootstrapResponseTest = "Good"

  import context.system
  IO(Udp) ! Udp.Bind(self, nodeInform.inetSocketAddress)

   def receive = {
     case Udp.Bound(local) => {
       log.info(s"Server was bound on adress $local")
       context.become(ready(sender()))
     }
  }
  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) => {
      val receiveRes = Serialize.deserialize(data.toArray)
          log.info(s"Receive data $receiveRes from socket $remote")
       receiveRes match {
        case Bootstrap(node) => Udp.Send(
          ByteString(bootstrapResponseTest), remote
        )
      }
    }
    case Udp.Unbind  ⇒ socket ! Udp.Unbind
    case Udp.Unbound ⇒ context.stop(self)
  }
}
