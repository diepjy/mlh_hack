package controllers.database;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import models.EventsDB;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by joanna on 11/04/15.
 */
public class DBController extends Controller {

//    @BodyParser.Of(BodyParser.Json.class)
//    public static Result addEvent() {
//        JsonNode json = request().body().asJson();
//        EventsDB e = new EventsDB();
//        e.text = "Testing";
//        EventsDB.create(e);
//        return ok("Record is added");
//    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result addEvent() {
        JsonNode json = request().body().asJson();
        EventsDB e = new EventsDB();
        EventsDB.createNewEvent(e, json);
        return ok("record added");
    }

}
