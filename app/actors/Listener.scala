package actors

import scala.concurrent.duration.DurationInt
import com.github.sstone.amqp.Amqp.Publish
import com.github.sstone.amqp.Amqp.ConfirmSelect
import com.github.sstone.amqp.ChannelOwner
import com.github.sstone.amqp.ConnectionOwner
import com.rabbitmq.client.ConnectionFactory
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import play.api.Logger
import play.api.Play
import play.api.Play.current
import com.github.sstone.amqp.Amqp
import play.api.libs.concurrent.Akka
import akka.actor.Actor
import com.github.sstone.amqp.Amqp.AddConfirmListener
import com.github.sstone.amqp.Amqp.AddReturnListener
import com.github.sstone.amqp.Amqp.DeclareQueue
import com.github.sstone.amqp.Amqp.QueueParameters
import akka.actor.ActorPath
import messages.IncomingMessage

class Listener(producerPath: ActorPath) extends Actor with akka.actor.ActorLogging {

  import context._
  

  val producer = Akka.system.actorSelection(producerPath)

  // create a queue
  val queueParams = QueueParameters("my_queue", passive = false, durable = false, exclusive = false, autodelete = true)
  producer ! ConfirmSelect
//  producer ! AddReturnListener(self)
 // producer ! AddConfirmListener(self)
//  producer ! DeclareQueue(queueParams)

  def receive = {

    case 'admin => {
      log.info("No operation for this message ")
    }
    case incoming: IncomingMessage => {
      log.info("Sending " + incoming.getBody() + " to queue")
      /* send a message
  * syntax --> Publish(exchange: String, key: String, body: Array[Byte], properties: Option[BasicProperties] = None, mandatory: Boolean = true, immediate: Boolean = false) extends Request
	*/

      producer ! Publish("amq.direct", "my_queue", incoming.getBody().getBytes, properties = None, mandatory = true, immediate = false)
    }
    case msg => {
      log.info("Error... " + msg)
  
    }
  }

}



