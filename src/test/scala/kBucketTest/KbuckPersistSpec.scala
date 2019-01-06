package kBucketTest

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit}
import messeges.StorageEvents._
import org.scalatest._
import GlobalTestValue._
import ident.{GenKadID, KadId}
import kBucketTest.StoragePersistActor
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

class KbuckPersistSpec extends TestKit(ActorSystem("ShoppingCartActorSpec"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  val persistId = "11"
  val storage = system.actorOf(Props(new StoragePersistActor(KadId("testtest"), persistId)))

//  "Persis actor" should {
//
//    "save" in {
//      val event1 = StorageEvent(connection1, AddConnect)
//      storage ! event1
//
//      Thread.sleep(100)
//
//      implicit val timeout = Timeout(5 seconds)
//
//      val res: Array[(KBucket, Int)] = Await.result(ask(storage, event1), 5.second).asInstanceOf[Array[(KBucket, Int)]]
//      res.foreach(println)
//      //storage ! PoisonPill
//      println("===========================")
//      println("===========================")
//      println("===========================")
//      println("===========================")
//
//      assert(false)
//    }
//  }
}
