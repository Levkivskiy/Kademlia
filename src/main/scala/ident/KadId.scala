package ident

import scala.util.Random

@SerialVersionUID(100L)
case class KadId(decimal: BigInt) extends Serializable {

}

object KadId {

  def apply() =  new KadId(GenKadID.ID())

  def apply(data: String): KadId = new KadId(GenKadID.ID(data))
}