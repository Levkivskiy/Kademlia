import java.net.{InetAddress, InetSocketAddress}

import ident.KadId
import node.KademliaServer

object MainKademlia extends App {

  val node = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 12345)

  println(KadId().decimal.toByteArray)
  println(KadId().decimal.bitLength)

}
