package chat

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.Roster
import akka.actor.Actor
import akka.actor.ActorRef

sealed abstract class Memo
case class RegisterParent(ref: ActorRef) extends Memo
case class InboundMessage(msg: String) extends Memo
case class OutboundMessage(msg: String) extends Memo

class Chatter(chat: Chat, roster: Roster) extends Actor {
  chat.addMessageListener(new MsgListener(self))
  var parent: Option[ActorRef] = None

  def receive = {
    case RegisterParent(ref) =>
      parent = Some(ref)
    case OutboundMessage(msg) =>
      if (roster.contains(chat.getParticipant))
        chat.sendMessage(msg)
    case msg: String =>
      chat.sendMessage(msg)
    case msg: ReceivedMessage =>
      parent map { _ ! InboundMessage(msg.msg) }
  }
}

