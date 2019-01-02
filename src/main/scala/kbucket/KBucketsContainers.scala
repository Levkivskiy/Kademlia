package kbucket

import ident.KadId
import network.Connect
import util.GlobalConfig._

class KBucketsContainers(selfId: KadId) {

  val KBuckets: Array[KBucket] = Array.fill(KBucketsContainerSize)(new KBucket(KBucketMaxCapacity))

  def add(nodeConnect: Connect): Boolean = {
    val kBucket = getkBucket(nodeConnect)
    if (kBucket.isFull()) {
      //todo refresh operation
      false
    }
    else {
      kBucket.add(nodeConnect)
    }
  }

  def contains(nodeConnect: Connect): Boolean = getkBucket(nodeConnect).contains(nodeConnect)

  def remove(nodeConnect: Connect): Boolean = getkBucket(nodeConnect).remove(nodeConnect)

  private def getkBucketIndex(kadId: KadId) = selfId.longestPrefixLength(kadId)

  private def getkBucket(nodeConnect: Connect) = KBuckets(KBucketsContainerSize - getkBucketIndex(nodeConnect.nodeInform.kadId) - 1)

}
