package messages

import org.jivesoftware.smack.packet.Message



class IncomingMessage(msg:String) extends Message {
  
  super.setBody(msg)
  
}