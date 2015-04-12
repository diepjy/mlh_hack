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
        String startYear = json.findPath("startYear").textValue();
        String endYear = json.findPath("endYear").textValue();
        String startTime = json.findPath("startTime").textValue();
        Logger.debug(startTime);
        String endTime = json.findPath("endTime").textValue();
        String startMonth = json.findPath("startMonth").textValue();
        String startDay = json.findPath("startDay").textValue();
        String endMonth = json.findPath("endMonth").textValue();
        String endDay = json.findPath("endDay").textValue();

        Map m = new HashMap();
        m.put("1", eventName);
        m.put("2", locationName);
        m.put("3", address);
        m.put("4", postcode);
        m.put("5", link);
        m.put("6", startYear);
        m.put("7", startMonth);
        m.put("8", startDay);
        m.put("9", endYear);
        m.put("10", endMonth);
        m.put("11", endDay);
        m.put("12", startTime);
        m.put("13", endTime);

        return IteratorUtil.singleOrNull(cypher.query(
                "MERGE (e:Event {eventName:{1}," +
                        " locationName:{2}," +
                        " address:{3}," +
                        " postcode:{4}," +
                        " link:{5}," +
                        " startYear:{6}," +
                        " startMonth:{7}," +
                        " startDay:{8}," +
                        " endYear:{9}," +
                        " endMonth:{10}," +
                        " endDay:{11}," +
                        " startTime:{12}," +
                        " endTime:{13}" +
                        "})" +
                        " RETURN e",
                m
        ));

    }

    public Iterable<Map<String,Object>> queryEventNode(String eventName) {
        return IteratorUtil.asCollection(cypher.query(
        "MATCH (e:Event {eventName:\"" + eventName + "\"})\n" +
                " RETURN e.eventName as eventName, " +
                "e.locationName as locationName, " +
                "e.address as address, " +
                "e.postcode as postcode, " +
                "e.link as link, " +
                "e.startYear as startYear, " +
                "e.startMonth as startMonth, " +
                "e.startDay as startDay, " +
                "e.endYear as endYear, " +
                "e.endMonth as endMonth, " +
                "e.endYear as endYear, " +
                "e.startTime as startTime, " +
                "e.endTime as endTime"
                ,
                map("1", eventName)));
    }


    /*
    MATCH (e:Event)
WHERE e.startTime <= "10.00"
AND e.startYear <= "2016"
AND e.startMonth <= "04"
AND e.startDay <= "10"
RETURN e
     */
    public Iterable<Map<String,Object>> searchOldNode(String startDate, String startTime) {
        String[] date = startDate.split("/");
        for(int i = 0; i < date.length; i++) {
            Logger.debug(date[i]);
        }
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (e:Event) WHERE e.startTime <= \"" + startTime + "\"" +
                        " AND e.startYear <= \"" + date[0] + "\"" +
                        " AND e.startMonth <= \"" + date[1] + "\"" +
                        " AND e.startDay <= \"" + date[2] + "\"" +
                        " RETURN e"
                ,
                map("1", startTime)
        ));
    }

    //Remove old nodes
    public Iterable<Map<String,Object>> removeOldNodeDate(String startDate, String startTime) {
        String[] date = startDate.split("/");
        for(int i = 0; i < date.length; i++) {
            Logger.debug(date[i]);
        }
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (e:Event) WHERE e.endYear <= \"" + date[0] + "\"" +
                        " AND e.endMonth <= \"" + date[1] + "\"" +
                        " AND e.endDay <= \"" + date[2] + "\"" +
                        " DELETE e"
                ,
                map("1", startTime)
        ));
    }

    public Iterable<Map<String,Object>> removeOldNodeTime(String startDate, String startTime) {
        String[] date = startDate.split("/");
        for(int i = 0; i < date.length; i++) {
            Logger.debug(date[i]);
        }
        return IteratorUtil.asCollection(cypher.query(
                "MATCH (e:Event) WHERE e.endYear = \"" + date[0] + "\"" +
                        " AND e.endMonth = \"" + date[1] + "\"" +
                        " AND e.endDay = \"" + date[2] + "\"" +
                        " AND e.endTime = \"" + startTime + "\"" +
                        " DELETE e"
                ,
                map("1", startTime)
        ));
    }


}
