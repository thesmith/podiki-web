package controllers

import play.api._
import play.api.mvc._
import java.net.URLDecoder
import com.top10.redis.SingleRedis

object Episode extends Controller {
  val redis = new SingleRedis("localhost", 6379, Some("onerecruit"))
  
  def episode(pid: String, eid: String) = Action {
    val purl = URLDecoder.decode(pid, "UTF-8")
    val eurl = URLDecoder.decode(eid, "UTF-8")
    val results = redis.syncAndReturn[Map[String, String], Map[String, String], Seq[String], Seq[String]](pipeline => {
      pipeline.hgetAll("podcast:"+purl)
      pipeline.hgetAll("episode:"+eurl)
      pipeline.zrange("episode_tracks:"+eurl, 0, -1)
      pipeline.lrange("episode_lines:"+eurl, 0, -1)
    })
    
    Ok(views.html.episode(results._1, results._2, results._3, results._4))
  }
  
  def updateLine(pid: String, eid: String, position: String) = Action { request =>
    val eurl = URLDecoder.decode(eid, "UTF-8")
    val body = request.body.asFormUrlEncoded.get("line").head
    
    redis.lset("episode_lines:"+eurl, position.toLong, body)
    Ok("")
  }

}