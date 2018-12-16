package node

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import ident.KadId
import messeges.ServerEvents.{Bootstrap, SendMsg}
import network.{Conn, Connect}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}
import util.GlobalConfig

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
    val connection = newConnection(nodeInf)
    connection ! Conn
    Thread.sleep(1000)
    connection ! SendMsg(Bootstrap(nodeInform))
  }

  def newConnection(nodeInf: NodeInform) = system.actorOf(Props(new Connect(nodeInf)))
  def getNodeInforn = nodeInform
}
