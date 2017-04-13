/**
  * Created by yaron on 4/13/17.
  */
case class House() {
  var street : String = ""
  var price: String = ""
  var postalCode: String = ""
  var lat: String = ""
  var lng: String = ""


  override def toString: String = {
    street + "," + price + "," + postalCode + "," + lat + "," + lng
  }
}

object House {

  def getHeadlines = "Street,Price,Postal,Latitude,Longitude"

}
