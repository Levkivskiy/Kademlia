package events

import node.NodeInform

object ServerEvents {

  @SerialVersionUID(10L)
  sealed trait ServerEvent extends Serializable

  case class SendMsg(msg: ServerEvent)

  case class Bootstrap(nodeInform: NodeInform) extends ServerEvent

}
