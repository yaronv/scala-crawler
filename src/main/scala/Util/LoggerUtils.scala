package Util

import java.io.{PrintWriter, StringWriter}

/**
  * Created by yaron.vazana on 6/21/16.
  */
object LoggerUtils {

  def getStackTraceAsString(t: Throwable) = {
    val sw = new StringWriter
    t.printStackTrace(new PrintWriter(sw))
    sw.toString
  }

}
