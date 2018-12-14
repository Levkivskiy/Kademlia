package node

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.ActorSystem
import ident.KadId

import scala.concurrent.ExecutionContextExecutor

class KademliaServer(kadId: KadId, hostName: String, port: Int) {

  val remoteAdr = new InetSocketAddress(hostName, port)

  implicit val system: ActorSystem = ActorSystem("Kademlia-system")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val listener =
}
