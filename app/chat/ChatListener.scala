package chat

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener

import akka.actor.ActorRef
import play.api.Logger

class ChatListener(parent: ActorRef) extends ChatManagerListener {

  override def chatCreated(chat: Chat, createdLocally: Boolean) = {
    val jid: JID = JID(chat.getParticipant(), "", Option(""))
    
    
    if (createdLocally)
      Logger.info("A local chat with %s was created.".format(jid))
    else {
      Logger.info("%s has begun to chat with us.".format(jid))
      
      if (chat.getListeners().isEmpty()) {
        Logger.info("Adding a message listener")
        chat.addMessageListener(new MsgListener(parent))
      }

       parent ! RemoteChatCreated(jid, chat)
  
    }
  }
}

