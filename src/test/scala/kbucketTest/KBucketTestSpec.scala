package kbucketTest

import java.net.InetAddress

import akka.actor.{ActorRef, ActorSystem}
import ident.KadId
import org.scalatest.FlatSpec
import kbucket.{KBucket, KBucketsContainers}
import network.Connect
import node.KademliaServer

class KBucketTestSpec extends FlatSpec {

  val node = new KademliaServer(KadId("asd13e12"), InetAddress.getLocalHost.getHostAddress, 61212)
  val node1 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 61322)
  val node2 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 60123)
  val node3 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 60975)
  val node4 = new KademliaServer(KadId(), InetAddress.getLocalHost.getHostAddress, 61145)

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

  val kBucketsStorage = new KBucketsContainers(node.nodeInform.kadId)

  "A set of kbuckets" should "Add" in {
    kBucketsStorage.add(connection1)
    kBucketsStorage.add(connection2)
    kBucketsStorage.add(connection3)

    assert(kBucketsStorage.contains(connection1))
    assert(kBucketsStorage.contains(connection2))
    assert(kBucketsStorage.contains(connection3))

    assert(!kBucketsStorage.contains(connection4))
  }
  it should "remove" in {
    assert(kBucketsStorage.remove(connection1))
    assert(kBucketsStorage.remove(connection2))
    assert(kBucketsStorage.remove(connection3))

    assert(!kBucketsStorage.remove(connection1))

  }
}
