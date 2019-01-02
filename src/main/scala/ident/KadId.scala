package ident

import scala.annotation.tailrec
import scala.util.Random
import util.GlobalConfig._

@SerialVersionUID(100L)
case class KadId(decimal: BigInt) extends Serializable {

  def distance(id: KadId) = new KadId(decimal ^ id.decimal)

  def longestPrefixLength(id: KadId): Int = {

    val dist = distance(id)

    @tailrec
    def _longestPrefixLength(currentBit: Int): Int = currentBit match {
      case 0 => dist.size
      case _ if dist.isBitSet(currentBit) => dist.size - currentBit
      case _ => _longestPrefixLength(currentBit - 1)
    }

    _longestPrefixLength(dist.size)
  }

  private def isBitSet(bit: Int) = decimal.testBit(bit - 1)

  val size = KBucketsContainerSize
}

object KadId {

  def apply() =  new KadId(GenKadID.ID())

  def apply(data: String): KadId = new KadId(GenKadID.ID(data))
}