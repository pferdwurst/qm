
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import chat.QueueManager

object Global extends GlobalSettings {

  
  override def onStart(app: Application) {

    //Logger.info("Starting Queue listener...")
    //Main.main(Array("rabbitmq listener"))
    
    Logger.info("Starting Chat broker..." + app)
    val qm = new QueueManager
    qm.start
    
    
    
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
   // QueueManager.stop
  }

}
