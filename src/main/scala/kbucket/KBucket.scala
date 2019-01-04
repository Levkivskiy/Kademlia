package kbucket

import akka.actor.ActorRef
import network.Connect
import node.NodeInform.ordering

import scala.collection.mutable

class KBucket(capacity: Int) {

  var nodesConnection = mutable.TreeSet[Connect]().empty

  def add(conn: Connect) = if (isFull) false else nodesConnection.add(conn)

  def size: Int = nodesConnection.size

  def contains(connect: Connect) = nodesConnection.contains(connect)

  def isFull = size >= capacity

  def remove(connect: Connect) = nodesConnection.remove(connect)

  def toArray: Array[Connect] = nodesConnection.toArray

  def update(connect: Connect) = {
    nodesConnection = nodesConnection.filterNot(_ == connect)
    nodesConnection.add(connect)
  }

  def isEmpty = nodesConnection.isEmpty
}
