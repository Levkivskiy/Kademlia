package node

import java.net.InetSocketAddress

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.{Udp, UdpConnected}
import akka.japi.Option.Some
import akka.pattern.ask
import akka.util.Timeout
import ident.KadId
import messeges.ServerEvents.{Bootstrap, SendMsg}
import network.{Connect, ConnectHelper}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}
import util.GlobalConfig._
import messeges.SocketEvents
import messeges.SocketEvents._

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
    newConnection(nodeInf).get match {
      case connection: ActorRef => {
        connection ! SendMsg(Bootstrap(nodeInf))

        connection ! UdpConnected.Disconnect
      }
      case _ =>
    }
  }

  def newConnection(nodeInf: NodeInform): Option[ActorRef] = {
    val connListener = new ConnectHelper()
    val connection = system.actorOf(Props(new Connect(nodeInf, connListener)))

    connection ! StartConnect
    val waitTime = ((attemptsToConnect * timeBreakToConnectSeconds) + 5).second
    implicit val timeout: Timeout = waitTime

    connListener.startWaitConn() match {
      case true => Some(connection)
      case false => None
    }
  }

  def getNodeInforn = nodeInform
}
