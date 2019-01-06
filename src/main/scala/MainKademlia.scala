import java.net.{InetAddress, InetSocketAddress}

import ident.KadId
import node.KademliaServer

object MainKademlia extends App {

  val node = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 12345)
  val node1 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 22343)

  node.bootstrap(node1.getNodeInforn)

}
