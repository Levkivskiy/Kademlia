package App
import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.io.IO
import akka.io.Udp
import java.net.InetSocketAddress

import akka.util.ByteString
import akka.io.UdpConnected

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.nio.charset.StandardCharsets

import akka.event.Logging
import akka.serialization.SerializerWithStringManifest
import akka.pattern.ask
import akka.util.Timeout
import util.Serialize.deserialize

//@SerialVersionUID(10L)
//case class User(name: String) extends Serializable
////@SerialVersionUID(10L)
//case class Customer(name: String, age: Int) extends Serializable {
//  override def toString: String = s"Customer($name, $age)"
//}





//class Spec extends Actor {
//  override def receive: Receive = {
//    case any: Any => println(any)
//  }
//}
//object Main extends App {
//
//  val remoteAdr = new InetSocketAddress("127.0.0.1", 60144)
//  def listenerProps(next: ActorRef) = Props(new Listener(next, remoteAdr))
//  def connectedProps(remote: InetSocketAddress, next: ActorRef) = Props(new Connected(remote, next))
//
//  implicit val system: ActorSystem = ActorSystem("movie-system")
//  //implicit val materializer: ActorMaterializer = ActorMaterializer()
//  implicit val ec: ExecutionContextExecutor = system.dispatcher
//
//
//  val probe = system.actorOf(Props(new Spec()))
//  val listen = system.actorOf(listenerProps(probe))
//
//  import scala.concurrent.duration._
//
//  val conn =  system.actorOf(connectedProps(remoteAdr, probe))
//  implicit val timeout = Timeout(5 seconds)
//  val fut = conn.ask(conn, 2).mapTo[Int]
//
//}
