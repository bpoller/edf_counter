package controllers

import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import play.api.Play.{configuration, current}
import play.api.libs.ws._
import play.api.{Logger, Play}
import play.api.libs.ws.WSAuthScheme._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


object Sigfox extends Controller {

  def save(time: Long, data: String) = Action {
    Ok
  }

  def chart(start: Long, end: Long, callback: String) = Action.async {
    getRecords(start, end).map {response =>

      val result = response.json.toString()
      println(result.length/1024/1024)

      Ok(s"$callback($payLoad);")
    }
  }

  def payLoad = "[[1397022948000,419],[1397058138000,440],[1397093330000,810],[1397129123000,397],[1397164318000,524],[1397199510000,499],[1397234700000,429],[1397269893000,551],[1397305086000,467],[1397340279000,518],[1397375472000,589],[1397410665000,1367],[1397445856000,525],[1397481050000,943],[1397516242000,712],[1397551438000,439],[1397586627000,571],[1397621809000,610],[1397656999000,403],[1397692206000,311],[1397727398000,307],[1397762590000,239],[1397797783000,370],[1397832975000,374],[1397868169000,376],[1397903361000,1019],[1397938554000,611],[1397973747000,685],[1398008939000,1529],[1398044133000,1350],[1398079326000,1101],[1398114521000,723],[1398149712000,610],[1398184904000,183],[1398220099000,754],[1398255290000,280],[1398290483000,528],[1398325676000,629],[1398360871000,205],[1398395462000,968],[1398430655000,1275],[1398465848000,838],[1398501041000,1513],[1398536233000,525],[1398570827000,441],[1398606020000,2000],[1398641213000,986],[1398676406000,1334],[1398711603000,969],[1398746793000,1768],[1398781984000,611],[1398817178000,843],[1398852371000,1394],[1398887564000,601],[1398922756000,1138],[1398957949000,757],[1398993143000,804],[1399028336000,1350],[1399063514000,1238],[1399098123000,1534],[1399133315000,883],[1399168507000,498],[1399203702000,855],[1399238892000,894],[1399274085000,540],[1399309278000,186],[1399343871000,542],[1399379065000,380],[1399414857000,362],[1399450049000,385],[1399485242000,1663],[1399520434000,851],[1399555028000,660],[1399590220000,181],[1399625413000,177],[1399660606000,175],[1399695799000,177],[1399730994000,174],[1399767383000,178],[1399802577000,178],[1399837772000,177],[1399872953000,570],[1399908154000,171],[1399943348000,1049],[1399978541000,2303],[1400013733000,821],[1400048927000,1591],[1400084122000,987],[1400121710000,722],[1400156903000,554],[1400192095000,472],[1400226689000,283],[1400261281000,132],[1400304272000,538],[1400339461000,910],[1400374055000,930],[1400409248000,602],[1400444443000,577],[1400479634000,650],[1400514826000,198],[1400550019000,921],[1400585212000,318],[1400620404000,795],[1400655598000,437],[1400690791000,409],[1400725983000,751],[1400761179000,351],[1400796371000,605],[1400831562000,365],[1400866755000,960],[1400901948000,512],[1400937141000,469],[1400972334000,561],[1401006932000,358],[1401042120000,1058],[1401077315000,591],[1401112508000,236],[1401147699000,363],[1401182892000,698],[1401218086000,811],[1401253278000,398],[1401288473000,544],[1401323663000,787],[1401358857000,895],[1401394051000,1142],[1401429242000,1020],[1401464436000,734],[1401499629000,409],[1401534822000,1233],[1401570016000,1566],[1401605209000,433],[1401640400000,736],[1401675594000,765],[1401710788000,253],[1401745981000,472],[1401781172000,512],[1401816366000,274],[1401851558000,943],[1401886753000,280],[1401921945000,332],[1401957138000,484],[1401992332000,389],[1402027522000,403],[1402061637000,193]]"


  def getRecords(start: Long, end: Long): Future[WSResponse] = {

    val query: String = (start, end) match {
      case (-1, -1) => "{}"
      case (-1, _) => "{\"time\":{\"$lte\":" + end + "}}"
      case (_, -1) => "{\"time\":{\"$gte\":" + start + "}}"
      case (a, b) => "{\"time\":{\"$gte\":" + start + ",\"$lte\":" + end + "}}"
    }

    kinveyRequest.withQueryString(("query", query), ("sort", "time")).get()
  }

  def kinveyRequest: WSRequestHolder = {
    val url = configuration.getString("kinvey.dataStoreURL").getOrElse("none")
    val appKey = configuration.getString("kinvey.appKey").getOrElse("none")
    val masterKey = configuration.getString("kinvey.masterKey").getOrElse("none")

    WS.url(url).withAuth(appKey, masterKey, BASIC)
  }
}