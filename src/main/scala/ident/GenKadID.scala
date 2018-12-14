package ident

import scala.util.Random

object GenKadID {
  private def geneRandVal: String = {
    //todo replace to normal random
    new Random().nextInt().toString + new Random().nextInt().toString
  }

  def shaFromString(data: String) = {
    java.security.MessageDigest.getInstance("SHA-1").digest(data.getBytes("UTF-8"))
  }

  def ID(): BigInt = toUnsigned(shaFromString(geneRandVal))

  def ID(data: String): BigInt = toUnsigned(shaFromString(data))

  private def toUnsigned(bytes: Array[Byte]): BigInt = {

    def _toUnsigned(index: Int = 0, decVal: BigInt = BigInt(0)): BigInt =
      if (index == bytes.length)
        decVal
      else
        _toUnsigned(index + 1, (decVal << 8) + (bytes(index) & 0xff))
    _toUnsigned()
  }
}
