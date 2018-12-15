package node

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import akka.io.Udp
import akka.pattern.ask
import akka.util.Timeout
import ident.KadId
import messeges.ServerEvents.{Bootstrap, SendMsg}
import network.{Connect, StartCheck, StartConnection}

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
    connection ! StartConnection
//    //todo move start to newConn def
//    def _startChecker(attemptsLeft: Int, timeBreak: Int): Boolean = {
//      implicit val timeout: Timeout = 10.second
//      val res = Await.result(ask(connection, StartCheck).mapTo[Boolean], 10000.second)
//      res match {
//        case true => true
//        case false => attemptsLeft match {
//          case 0 => false
//          case left =>
//            Thread.sleep(timeBreak * 60)
//            _startChecker(left - 1, timeBreak)
//        }
//      }
//      res
//    }
//    _startChecker(GlobalConfig.attemptsToConnect, GlobalConfig.timeBreakTiConnectSeconds) match {
//      case true => connection ! SendMsg(Bootstrap(nodeInform))
//      case _ =>
//    }
    Thread.sleep(1000)
    connection ! SendMsg(Bootstrap(nodeInform))
    //connection ! Udp.Unbind
  }

  def newConnection(nodeInf: NodeInform) = system.actorOf(Props(new Connect(nodeInf)))
  def getNodeInforn = nodeInform
}
