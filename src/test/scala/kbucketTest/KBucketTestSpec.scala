package kbucketTest

import java.net.InetAddress

import akka.actor.ActorRef
import ident.KadId
import org.scalatest.FlatSpec
import kbucket.KBucket
import node.KademliaServer

class KBucketTestSpec extends FlatSpec {
  val node = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 0)
  val node1 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 0)
  val node2 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 0)
  val node3 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 0)
  val node4 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 0)

  val connection1 = node.newConnection(node1.nodeInform).get
  val connection2 = node.newConnection(node2.nodeInform).get
  val connection3 = node.newConnection(node3.nodeInform).get
  val connection4 = node.newConnection(node4.nodeInform).get

  val bucketSize = 3
  val kBucket = new KBucket(bucketSize)

  "A Bucket" should "Add" in {
    kBucket.add(connection1)
    kBucket.add(connection2)
    kBucket.add(connection3)

    if(bucketSize == 3) assert(!kBucket.add(connection4))

    assert(kBucket.contains(connection1))
    assert(kBucket.contains(connection2))
    assert(kBucket.contains(connection3))
    if(bucketSize == 3) assert(!kBucket.contains(connection4))
  }
  it should "Remove" in {
    kBucket.remove(connection1)
    kBucket.remove(connection2)
    kBucket.remove(connection3)
  }

}
