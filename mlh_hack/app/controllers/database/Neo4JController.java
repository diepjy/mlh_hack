package controllers.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import controllers.Application;
import models.jdbc.server.Neo4JDAO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by joanna on 07/11/14.
 */
public class Neo4JController extends Controller {

    private static Neo4JDAO service = new Neo4JDAO();;

    public Neo4JController(Neo4JDAO service) {
        this.service = service;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result createEvent() throws JSONException {
        JsonNode json = request().body().asJson();
        JSONObject jsonQuery = new JSONObject();
        jsonQuery = new JSONObject(new Gson().toJson(service.createEventNode(json)));
        return ok("created event");
    }

    // TODO: query the db for the event
    public static Result queryEvent(String eventName) throws JSONException {
        JSONObject jsonQuery = new JSONObject();
        Iterable<Map<String,Object>> event = service.queryEventNode(eventName);
//        Map event = service.queryEventNode(eventName);
        Iterator it = event.iterator();
        List a = new ArrayList();
        JSONArray authoredDataArray = new JSONArray();
        while(it.hasNext()) {
            authoredDataArray.put(it.next());
//            Logger.debug(it.next().toString());
        }
        Logger.debug(event.toString());
        String[] elem = authoredDataArray.get(0).toString().split(",");
        Logger.debug(elem[0].replace("{", "").replace("eventName=", ""));
        jsonQuery.append("eventName", elem[0].replace("{", "").replace("eventName=", ""));
        jsonQuery.append("locationName", elem[1].replace("{", "").replace("locationName=", ""));
        jsonQuery.append("address", elem[2].replace("{", "").replace("address=", ""));
        jsonQuery.append("postcode", elem[3].replace("{", "").replace("postcode=", ""));
        jsonQuery.append("link", elem[4].replace("{", "").replace("link=", ""));
        jsonQuery.append("startDate", elem[5].replace("{", "").replace("startDate=", ""));
        jsonQuery.append("endDate", elem[6].replace("{", "").replace("endDate=", ""));
        jsonQuery.append("startTime", elem[7].replace("{", "").replace("startTime=", ""));
        jsonQuery.append("endTime", elem[8].replace("{", "").replace("endTime=", ""));

        Logger.debug(authoredDataArray.toString());

        return ok(jsonQuery.toString());
    }

    //Search old events
    public static Result searchOldEvents() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH.mm");
        Date date = new Date();
        Logger.debug(dateFormat.format(date));
        Logger.debug(timeFormat.format(date));
        Iterable<Map<String,Object>> node = service.searchOldNode(dateFormat.format(date), timeFormat.format(date));
        Iterator it = node.iterator();
        JSONArray a = new JSONArray();
        while (it.hasNext()) {
            a.put(it.next());
        }
        return ok(a.toString());
    }

    //TODO: get rid of old events? Poll every 6 hours
    public static void removeOldEvents() {
        while(true) {
            try {
                Thread.sleep(3600 * 1000);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat timeFormat = new SimpleDateFormat("HH.mm");
                Date date = new Date();
                Logger.debug(dateFormat.format(date));
                Logger.debug(timeFormat.format(date));
                service.removeOldNodeDate(dateFormat.format(date), timeFormat.format(date));
                service.removeOldNodeTime(dateFormat.format(date), timeFormat.format(date));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
//        return ok();
    }

    public static Result allEvents() throws JSONException {
        JSONArray a = new JSONArray();
        Iterable<Map<String,Object>> result = service.getAllNodes();
        Iterator it = result.iterator();
        while(it.hasNext()) {
            a.put(it.next());
        }
        return ok(a.get(0).toString());
    }

}
