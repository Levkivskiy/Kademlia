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

import akka.serialization.SerializerWithStringManifest

import akka.pattern.ask
import akka.util.Timeout

@SerialVersionUID(10L)
case class User(name: String) extends Serializable
//@SerialVersionUID(10L)
case class Customer(name: String, age: Int) extends Serializable {
  override def toString: String = s"Customer($name, $age)"
}

object Serialize {
  def serialise(value: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close()
    stream.toByteArray
  }

  def deserialise(bytes: Array[Byte]): Any = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close()
    value
  }
}


class Listener(nextActor: ActorRef, remote: InetSocketAddress) extends Actor {
  import Serialize._

  import context.system
  IO(Udp) ! Udp.Bind(self, remote)

  def receive = {
    case Udp.Bound(local) ⇒
      //#listener
      nextActor forward local
      //#listener
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) ⇒

      val processed = deserialise(data.toArray).toString * 5
      //#listener
      nextActor ! processed
      socket ! Udp.Send(data, remote) // example server echoes back
    case Udp.Unbind  ⇒ socket ! Udp.Unbind
    case Udp.Unbound ⇒ context.stop(self)
  }
}

class Connected(remote: InetSocketAddress, nextActor: ActorRef) extends Actor {
  import context.system
  import Serialize._

  IO(UdpConnected) ! UdpConnected.Connect(self, remote)

  def receive = {
    case UdpConnected.Connected ⇒
      //#connected
      //sender() ! UdpConnected.Send(ByteString(serialise(User("Connect Actor"))))
      //sender() ! UdpConnected.Send(ByteString(serialise(Customer("One", 1))))

      nextActor ! "Connect  " + remote
    //#connected
      context.become(ready(sender()))
  }

  def ready(connection: ActorRef): Receive = {
    case msg: String ⇒
      nextActor !"asdasd"
      connection ! UdpConnected.Send(ByteString(serialise(User(msg))))
    case UdpConnected.Received(data) ⇒
      // process data, send it on, etc.
      //#connected
      nextActor ! deserialise(data.toArray).toString * 2

      //connection ! UdpConnected.Send(ByteString(serialise(User("Received"))))
    //#connected
    case UdpConnected.Disconnect ⇒
      nextActor ! UdpConnected.Disconnect
      connection ! UdpConnected.Disconnect
    case UdpConnected.Disconnected ⇒ context.stop(self)
    case _: Int => 1
  }
}
class Spec extends Actor {
  override def receive: Receive = {
    case any: Any => println(any)
  }
}
object Main extends App {

  val remoteAdr = new InetSocketAddress("127.0.0.1", 60144)
  def listenerProps(next: ActorRef) = Props(new Listener(next, remoteAdr))
  def connectedProps(remote: InetSocketAddress, next: ActorRef) = Props(new Connected(remote, next))

  implicit val system: ActorSystem = ActorSystem("movie-system")
  //implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val probe = system.actorOf(Props(new Spec()))
  val listen = system.actorOf(listenerProps(probe))

  import scala.concurrent.duration._

  val conn =  system.actorOf(connectedProps(remoteAdr, probe))
  implicit val timeout = Timeout(5 seconds)
  val fut = conn.ask(conn, 2).mapTo[Int]

}
