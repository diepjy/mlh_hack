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


}
