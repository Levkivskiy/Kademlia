package node

import java.net.InetSocketAddress

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.{Udp, UdpConnected}
import akka.japi.Option.Some
import akka.pattern.ask
import akka.util.Timeout
import ident.KadId
import events.ServerEvents.{Bootstrap, SendMsg}
import network.{Connect, ConnectActor, ConnectHelper}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}
import util.GlobalConfig._
import events.SocketEvents
import events.SocketEvents._
import kbucket.KBucketsContainers


class KademliaServer(kadId: KadId, hostName: String, port: Int) {

  val remoteAdr: InetSocketAddress = new InetSocketAddress(hostName, port)

  implicit val system: ActorSystem = ActorSystem("Kademlia-system")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val nodeInform = NodeInform(remoteAdr, kadId)

  val node = system.actorOf(Props(new Node(nodeInform)))

  def bootstrap(nodeInf: NodeInform) = {
    newConnection(nodeInf).get match {
      case Connect(_, connection) => {
        connection ! SendMsg(Bootstrap(nodeInf))

        connection ! UdpConnected.Disconnect
      }
      case _ =>
    }
  }

  def newConnection(nodeInf: NodeInform): Option[Connect] = {
    val connListener = new ConnectHelper()
    val kBucketsContainers = new KBucketsContainers(kadId)
    val connection = system.actorOf(Props(new ConnectActor(nodeInf, connListener, kBucketsContainers)))

    connection ! StartConnect
    val waitTime = ((attemptsToConnect * timeBreakToConnectSeconds) + 5).second
    implicit val timeout: Timeout = waitTime

    if (connListener.startWaitConn()) {
      Some(Connect(nodeInf, connection))
    } else {
      None
    }
  }

  def getNodeInforn = nodeInform
}
