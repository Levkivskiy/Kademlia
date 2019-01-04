package kbucketTest

import java.net.InetAddress

import ident.KadId
import node.KademliaServer

object GlobalTestValue {

  val node = new KademliaServer(KadId("asd13e12"), InetAddress.getLocalHost.getHostAddress, 61212)
  val node1 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 61322)
  val node2 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 60123)
  val node3 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 60975)
  val node4 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 61145)

  val connection1 = node.newConnection(node1.nodeInform).get
  val connection2 = node.newConnection(node2.nodeInform).get
  val connection3 = node.newConnection(node3.nodeInform).get
  val connection4 = node.newConnection(node4.nodeInform).get

}
