import java.io.{BufferedWriter, File, FileWriter}

import Util.Config
import com.typesafe.scalalogging.LazyLogging
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

/**
  * Created by yaron.vazana on 3/29/17.
  */
object Scraper extends App with Config with LazyLogging {

  logger.info("App started")

  val browser = JsoupBrowser()

  val pages = config.getInt("pages.total")

  val url = config.getString("url.base")

  var houses: Seq[House] = Seq[House]()

  for(i <- 1 to pages){
    logger.info(s"Crawling page #${i}")

    val pageUrl = url.replace("{page}", i.toString)

    val items = browser.get(pageUrl) >?> elementList("article.photo-card")

    items match {
      case Some(items: List[Element]) => {
        items.foreach(item => {
          val house = House()

          val address = item >?> element("[itemprop=address]")

          address match {
            case None => //
            case Some(addressElm) => {
              addressElm >?> element("[itemprop=streetAddress]") match {
                case None => //
                case Some(streetElm) => {
                  house.street = streetElm.text
                }
              }
              addressElm >?> element("[itemprop=postalCode]") match {
                case None => //
                case Some(postalElm) => {
                  house.postalCode = postalElm.text
                }
              }
            }
          }

          val geo = item >?> element("[itemprop=geo]")

          geo match {
            case None => //
            case Some(geoElm) => {
              geoElm >?> element("[itemprop=latitude]") match {
                case None => //
                case Some(latitudeElm) => {
                  house.lat = latitudeElm.attr("content")
                }
              }
              geoElm >?> element("[itemprop=longitude]") match {
                case None => //
                case Some(longElm) => {
                  house.lng = longElm.attr("content")
                }
              }
            }
          }

          val price = item >?> element(".zsg-photo-card-price")

          price match {
            case None => //
            case Some(priceElm) => {
              house.price = priceElm.text.replace(",", "")
            }
          }


          houses = houses :+ house
        })

      }
      case None => // nothing
    }
  }

  val file = new File(config.getString("output.path"))
  if(!file.exists()) {
    new File(file.getParent()).mkdirs()
    file.createNewFile()
  }
  else {
    file.delete()
  }
  val bw = new BufferedWriter(new FileWriter(file))
  bw.write(House.getHeadlines + "\n")

  houses.foreach(h => {bw.write(h + "\n")} )

  bw.close()

  logger.info("App finished")
}
