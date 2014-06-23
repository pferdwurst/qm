package chat

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.Roster
import org.jivesoftware.smack.SASLAuthentication
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Presence

import akka.actor.Actor
import akka.actor.ActorRef
import play.api.Logger
import play.api.Play

private sealed abstract class InternalMessage
private case class RemoteChatCreated(jid: JID, chat: Chat) extends InternalMessage
private case class ReceivedMessage(msg: String) extends InternalMessage

class ChatBroker(domain: Option[String] = None, jid: JID, password: String) extends Actor with akka.actor.ActorLogging {

  private val port = Play.current.configuration.getInt("mongooseim.port").getOrElse(5222)

  private val conf = new ConnectionConfiguration("198.23.107.155", 5222, "198.23.107.155")

  //private val conf = new ConnectionConfiguration(domain.getOrElse(jid.domain),
  //port, domain.toString())

  SASLAuthentication.supportSASLMechanism("PLAIN", 0);
  conf.setSASLAuthenticationEnabled(true)
  SmackConfiguration.setPacketReplyTimeout(10000)

  //XMPPConnection.DEBUG_ENABLED = true

  private val conn = new XMPPConnection(conf)

  conn.connect()
  log.info("Logging in with " + JID.jidToString(jid))

  log.info("Is someone logged in? " + conn.getUser())
  conn.login(jid.username, password)

  private val roster: Roster = conn.getRoster()

  log.info("The roster...")
  for (ent <- roster.getEntries().toArray())
    log.info("roster entry: " + ent)

  // roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all)
  //conn.sendPacket(new Presence(Presence.Type.available))

  def setPresence = {
    // Create a new presence. Pass in false to indicate we're unavailable.
    val presence = new Presence(Presence.Type.available);
    presence.setStatus("Gone fishing");
    conn.sendPacket(presence);
  }

  private val chats: collection.mutable.Map[JID, Chat] = new collection.mutable.HashMap

  def receive = {
    case 'away => {
      setPresence
      log.info("We just said we were away")
    }
    case 'stop =>
      {
        conn.disconnect()
      }
    case RemoteChatCreated(jid, chat) => {
      log.info("remote chat created")
    }
    case ReceivedMessage(body) => {
      log.info("Mesg " + body + " came to parent.")
      sender ! body
    }
    case _ => log.info("nothing...")
  }

  // Assume we've created a XMPPConnection name "connection".
  val chatmanager = conn.getChatManager()
  log.info("adding a chat listener")
  chatmanager.addChatListener(new ChatListener(self))

}