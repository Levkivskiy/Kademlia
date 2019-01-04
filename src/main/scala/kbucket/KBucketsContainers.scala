package kbucket

import ident.KadId
import network.Connect
import util.GlobalConfig._

class KBucketsContainers(selfId: KadId) {

  val KBuckets: Array[KBucket] = Array.fill(KBucketsContainerSize)(new KBucket(KBucketMaxCapacity))

  def add(nodeConnect: Connect): Boolean = {
    val kBucket = getkBucket(nodeConnect)
    if (kBucket.isFull) {
      //todo refresh operation
      false
    }
    else {
      kBucket.add(nodeConnect)
    }
  }

  def contains(nodeConnect: Connect): Boolean = getkBucket(nodeConnect).contains(nodeConnect)

  def remove(nodeConnect: Connect): Boolean = getkBucket(nodeConnect).remove(nodeConnect)

  def getkBucketIndex(kadId: KadId): Int = selfId.longestPrefixLength(kadId)

  def getkBucket(nodeConnect: Connect) = KBuckets(KBucketsContainerSize - getkBucketIndex(nodeConnect.nodeInform.kadId) - 1)

  def getKBucketArray = KBuckets

  def zipWithIndexNonEmpty = KBuckets.zipWithIndex.filterNot(_._1.isEmpty)

  def flatten = KBuckets.flatMap(_.toArray)

  def update(nodeConnect: Connect) = getkBucket(nodeConnect).update(nodeConnect)

}
