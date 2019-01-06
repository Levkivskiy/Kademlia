package kBucketTest

import akka.actor._
import akka.persistence._
import messeges.StorageEvents._
import ident.KadId
import network.Connect
import util.GlobalConfig.snapShotInterval

class StoragePersistActor(kadId: KadId, persistId: String)
  extends PersistentActor with ActorLogging {

  var kBuckCont = new KBucketsContainers(kadId)

  override def persistenceId: String = persistId

  def addConnect(connect: Connect): Boolean = kBuckCont.add(connect)

  def removeConnect(connect: Connect): Boolean = kBuckCont.remove(connect)

  def updateConnect(connect: Connect): Boolean = kBuckCont.update(connect)

  def updateState(event: StorageEvent): Boolean = {
    event.event match {
      case AddConnect => addConnect(event.connect)
      case RemoveConnect => removeConnect(event.connect)
      case UpdateConnect => updateConnect(event.connect)
    }
  }

  override def receiveRecover: Receive = {
    case storageEvent: StorageEvent => updateState(storageEvent)
    case SnapshotOffer(_, snapshot: KBucketsContainers) => kBuckCont = snapshot
  }

  val snapShotInnerv: Int = snapShotInterval

  override def receiveCommand: Receive = {
    case storageEvent: StorageEvent =>
      persist(storageEvent) { event =>
        updateState(event)
        context.system.eventStream.publish(event)

        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(kBuckCont)
      }

      //for testing
      //todo replace
    case "storage" =>
      sender() ! kBuckCont.zipWithIndexNonEmpty
  }
}
