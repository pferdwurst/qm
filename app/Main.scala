

import scala.concurrent.duration.DurationInt
import play.api.Play.current
import com.github.sstone.amqp.ConnectionOwner
import com.rabbitmq.client.ConnectionFactory
import play.api.libs.concurrent.Akka
import com.github.sstone.amqp.ChannelOwner
import play.api.Play
import com.github.sstone.amqp.Amqp
import play.api.Logger
import akka.actor.Props
import actors.Listener
import org.jivesoftware.smack.packet.{ Message, Packet, Presence }
import messages.IncomingMessage
import com.github.sstone.amqp.Amqp.QueueParameters
import com.github.sstone.amqp.Amqp.AddConfirmListener
import com.github.sstone.amqp.Amqp.AddReturnListener
import com.github.sstone.amqp.Amqp.DeclareQueue

import com.github.sstone.amqp.Amqp.ConfirmSelect

object Main extends App {

  // create an AMQP connection
  val connFactory = new ConnectionFactory()

  connFactory.setHost(Play.current.configuration.getString("rabbitmq.host").getOrElse("localhost"));
  connFactory.setPassword(Play.current.configuration.getString("rabbitmq.password").getOrElse("password"))

  val conn = Akka.system.actorOf(ConnectionOwner.props(connFactory, 1 second).withDispatcher("my-dispatcher"), name = "connection-owner")

  // time to connect?
  Thread.sleep(100)

  val producer = ConnectionOwner.createChildActor(conn, ChannelOwner.props().withDispatcher("my-dispatcher"), Option("producer"))
  val listener = Akka.system.actorOf(Props(new Listener(producer.path)), name = "listener")


  // wait till everyone is actually connected to the broker
  Amqp.waitForConnection(Akka.system, producer).await()

  
  Logger.info("Okay, sending messages")
  
  listener ! new IncomingMessage("hello")

  listener ! new IncomingMessage("my")

  listener ! new IncomingMessage("name")

  listener ! new IncomingMessage("is")

  listener ! new IncomingMessage("robot")

  // Akka.system.shutdown()

}