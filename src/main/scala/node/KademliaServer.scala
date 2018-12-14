package node

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.{ActorSystem, Props}
import ident.KadId
import messeges.ServerEvents.{Bootstrap, SendMsg}
import network.Connect

import scala.concurrent.ExecutionContextExecutor

@SerialVersionUID(10L)
case class NodeInform(inetSocketAddress: InetSocketAddress, kadId: KadId)
  extends Serializable

class KademliaServer(kadId: KadId, hostName: String, port: Int) {

  val remoteAdr: InetSocketAddress = new InetSocketAddress(hostName, port)

  implicit val system: ActorSystem = ActorSystem("Kademlia-system")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val nodeInform = NodeInform(remoteAdr, kadId)

  val node = system.actorOf(Props(new Node(nodeInform)))

  def bootstrap(nodeInf: NodeInform) = {
    val connection = system.actorOf(Props(new Connect(nodeInform)))
    connection ! SendMsg(Bootstrap(nodeInform))
  }

  def getNodeInforn = nodeInform
}
