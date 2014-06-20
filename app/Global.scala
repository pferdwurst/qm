
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    
    Logger.info("Starting main?...")
	 Main.main(Array("sf"))
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}
