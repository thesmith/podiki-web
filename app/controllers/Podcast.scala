package controllers

import play.api._
import play.api.mvc._
import com.top10.redis.SingleRedis
import java.net.URLEncoder
import java.net.URLDecoder

object Podcast extends Controller {
  val redis = new SingleRedis("localhost", 6379, Some("onerecruit"))
  
  def podcast(id: String) = Action {
    val url = URLDecoder.decode(id, "UTF-8")
    val results = redis.syncAndReturn[Map[String, String], Seq[String]](pipeline => {
      pipeline.hgetAll("podcast:"+url)
      pipeline.zrevrange("podcast_episodes:"+url, 0, -1)
    })
    val episodes = redis.syncAndReturnAllAs[Map[String, String]](pipeline => {
      results._2.foreach(episodeUrl => {
        pipeline.hgetAll("episode:"+episodeUrl)
      })
    })
    
    Ok(views.html.podcast(results._1, episodes))
  }

  def podcasts = Action {
    val urls = redis.zrange("podcast_urls", 0, -1)
    val podcasts = redis.syncAndReturnAllAs[Map[String, String]](pipeline => {
      urls.foreach(url => {
        pipeline.hgetAll("podcast:"+url)
      })
    })
    Ok(views.html.podcasts(podcasts))
  }
  
}