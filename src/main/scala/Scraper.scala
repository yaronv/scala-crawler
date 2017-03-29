import Util.Config
import com.typesafe.scalalogging.LazyLogging
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors

/**
  * Created by yaron.vazana on 3/29/17.
  */
object Scraper extends App with Config with LazyLogging {

  logger.info("App started")

  val browser = JsoupBrowser()
  val doc = browser.get(config.getString("url.base"))


  // Go to a news website and extract the hyperlink inside the h1 element if it
  // exists. Follow that link and print both the article title and its short
  // description (inside ".lead")

  val headlines = browser.get("http://observador.pt") >?> elementList("h1 a")

  headlines match {
    case Some(items: List[Element]) => {
      items.foreach(hl => {
        val headlineDesc = browser.get(hl.attr("href")) >?> text(".lead")
        headlineDesc match {
          case None => //
          case _ => {
            println("== " + hl.text + " ==\n" + headlineDesc)
          }
        }

      })

    }
    case None => // nothing
  }

  logger.info("App finished")
}
