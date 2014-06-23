package chat

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

 import akka.actor.ActorRef
import play.api.Logger

 class MsgListener(parent:ActorRef) extends MessageListener {
  override def processMessage(chat:Chat,msg:Message) = {
    if (msg.getBody != null)
      Logger.info("Doing something with msg body: " + msg.getBody())
      
      parent ! ReceivedMessage(msg.getBody)
  }
}

