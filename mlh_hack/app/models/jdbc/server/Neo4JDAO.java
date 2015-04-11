package models.jdbc.server;

import com.fasterxml.jackson.databind.JsonNode;
import models.jdbc.executor.CypherExecutor;
import models.jdbc.executor.JdbcNeo4JCypherExecutor;
import org.json.JSONObject;
import org.neo4j.helpers.collection.IteratorUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import play.Logger;

import static org.neo4j.helpers.collection.MapUtil.map;

/**
 * Created by joanna on 07/11/14.
 */
public class Neo4JDAO {

    private final CypherExecutor cypher;
    private final String server = "http://localhost:7474/db/data";

    public Neo4JDAO() {
        cypher = createCypherExecutor(server);
    }

    private CypherExecutor createCypherExecutor(String uri) {
        try {
            String auth = new URL(uri).getUserInfo();
            if (auth != null) {
                String[] parts = auth.split(":");
                return new JdbcNeo4JCypherExecutor(uri,parts[0],parts[1]);
            }
            return new JdbcNeo4JCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public Map createEventNode(JsonNode json) {
        String eventName = json.findPath("eventName").textValue();
        String locationName = json.findPath("locationName").textValue();
        Logger.debug(locationName);
        String address = json.findPath("address").textValue();
        String postcode = json.findPath("postcode").textValue();
        String link = json.findPath("link").textValue();
        String startDate = json.findPath("startDate").textValue();
        String endDate = json.findPath("endDate").textValue();
        String startTime = json.findPath("startTime").textValue();
        Logger.debug(startTime);
        String endTime = json.findPath("endTime").textValue();

        Map m = new HashMap();
        m.put("1", eventName);
        m.put("2", locationName);
        m.put("3", address);
        m.put("4", postcode);
        m.put("5", link);
        m.put("6", startDate);
        m.put("7", endDate);
        m.put("8", startTime);
        m.put("9", endTime);

        return IteratorUtil.singleOrNull(cypher.query(
//                "MERGE (e:Event {eventName:{1}," +
//                        " locationName:{2}, " +
//                        " address:{3}," +
//                        " postcode:{4}," +
//                        " link:{5}," +
//                        " startDate:{6}," +
//                        " endDate:{7}," +
//                        " timeStart:{8}," +
//                        " timeEnd:{9}})" +
//                        " RETURN e",
                "MERGE (e:Event {eventName:{1}," +
                        " locationName:{2}," +
                        " address:{3}," +
                        " postcode:{4}," +
                        " link:{5}," +
                        " startDate:{6}," +
                        " endDate:{7}," +
                        " startTime:{8}," +
                        " endTime:{9}" +
                        "})" +
                        " RETURN e",
                m
        ));

    }


}
