import play.api._
import com.top10.redis.SingleRedis

object Global extends GlobalSettings {

  val redis = new SingleRedis("localhost", 6379, Some("onerecruit"))

}