import scala.util.Random


//val md = java.security.MessageDigest.getInstance("SHA-1").digest(new Random().toString.getBytes("UTF-8"))
//val md1 = java.security.MessageDigest.getInstance("SHA-1").digest("Hoger".getBytes("UTF-8"))
//
//val v1 = List(1, 3, 6)
//val v2 = List(1, 3, 6, 10)
//md.zip(md1).map {
//  case (a: Byte, b: Byte) => a ^ b
//}
//
//def hex2bytes(hex: String): Array[Byte] = {
//  if(hex.contains(" ")){
//    hex.split(" ").map(Integer.parseInt(_, 16).toByte)
//  } else if(hex.contains("-")){
//    hex.split("-").map(Integer.parseInt(_, 16).toByte)
//  } else {
//    hex.sliding(2,2).toArray.map(Integer.parseInt(_, 16).toByte)
//  }
//}
//
//val md2 = java.security.MessageDigest.getInstance("SHA-1").digest("Hoger".getBytes("UTF-8"))
//  .map("%02x".format(_)).mkString


import ident.KadId
KadId(1)
123123