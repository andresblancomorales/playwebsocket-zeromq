package controllers

import actors.ListenerActor
import play.api.mvc._
import play.api.Play.current

object Application extends Controller {

  def socket(account : String) = WebSocket.acceptWithActor[String, String] { request => out =>
    ListenerActor.props(account, out)
  }

}