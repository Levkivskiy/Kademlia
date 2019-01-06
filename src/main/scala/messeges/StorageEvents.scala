package messeges

import network.Connect

object StorageEvents {

  trait KBucketStorageEvent

  case class StorageEvent(connect: Connect, event: KBucketStorageEvent)

  case object AddConnect extends KBucketStorageEvent

  case object RemoveConnect extends KBucketStorageEvent

  case object UpdateConnect extends KBucketStorageEvent

}