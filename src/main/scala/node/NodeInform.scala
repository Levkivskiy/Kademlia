package node

import java.net.InetSocketAddress

import ident.KadId


@SerialVersionUID(10L)
case class NodeInform(inetSocketAddress: InetSocketAddress, kadId: KadId)
  extends Serializable

object NodeInform {

  implicit val ordering: Ordering[NodeInform] = Ordering.by {
    node: NodeInform =>
      node.kadId.decimal
  }
}