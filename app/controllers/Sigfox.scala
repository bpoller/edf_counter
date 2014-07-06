package controllers

import play.api.mvc.{Action, Controller}


object Sigfox extends Controller{

  def save(time:Long, data:String) = Action {Ok}

  def chart(start:Long, end:Long, callback:String) = Action{Ok}
}
