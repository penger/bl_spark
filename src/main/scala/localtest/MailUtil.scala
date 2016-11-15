package localtest

import org.apache.commons.mail.{DefaultAuthenticator, EmailException, SimpleEmail}

/**
  * Created by GP39 on 2016/8/30.
  */
object MailUtil {
  def main(args: Array[String]): Unit = {

//    val socket = new Socket("mail.bl.com",25)



    val mail = new SimpleEmail
    try{
      mail.setMsg("hello spark ! 中文")
      mail.setHostName("mail.bl.com")
      mail.setSmtpPort(25)
      mail.setAuthenticator(new DefaultAuthenticator("gp39", "syj125?"))
      mail.setFrom("Peng.Gong@bl.com")
      mail.addTo("Peng.Gong@bl.com")
      mail.setSubject("无题")
      mail.send()
    }catch {
      case ex:EmailException => ex.printStackTrace()
    }


  }

}
