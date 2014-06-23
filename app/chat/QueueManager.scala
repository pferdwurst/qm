package chat

import scala.concurrent.duration.DurationInt
import play.api.Play.current
import play.api.libs.concurrent.Akka
import akka.actor.Props
import play.api.Play
import akka.actor.ActorRef
import play.api.libs.ws.WSRequestHolder
import play.api.libs.ws.WS
import play.api.libs.json._
import scala.concurrent.Future
import play.api.Logger
import play.api.libs.ws.WSResponse
import scala.concurrent.Await

class QueueManager {
  case class Company(
    id: Long,
    name: String,
    industry: String,
    supportsChat: Boolean,
    phone: String,
    jabberID: String)

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def start = {

    val domain = Play.current.configuration.getString("mongooseim.host").getOrElse("localhost")

    for (company <- getCompanyRoster) {
      val jid = JID(company.jabberID)
      val broker = Akka.system.actorOf(Props(new ChatBroker(Option(domain), jid, "password")), name = "chatbroker_" + jid.username)

    }

    //broker ! 'away

  }

  def stop = {
    // broker ! 'exit
  }

  def getCompanyRoster = {
    val url = Play.current.configuration.getString("api.get_companies").getOrElse("http://localhost/")

    implicit val companyReads = Json.reads[Company]

    val holder: Future[WSResponse] = WS.url(url).get

    val futureResult: Future[Seq[Company]] = holder.map {
      response =>
        (response.json).as[Seq[Company]]
    }

    val result = Await.result(futureResult, 5 seconds)
    result

  }

}